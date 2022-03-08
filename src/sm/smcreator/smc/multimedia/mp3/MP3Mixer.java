/*
 * @(#) MP3Mixer.java
 *
 * Created on 17.10.2007 by Anil Kishan
 * 
 *-----------------------------------------------------------------------
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *----------------------------------------------------------------------
 */
package sm.smcreator.smc.multimedia.mp3;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.sound.sampled.AudioFormat;

import sm.smcreator.smc.io.FileOrPackedInputStream;
import sm.smcreator.smc.mixer.BasicMixer;
import sm.smcreator.smc.multimedia.mp3.streaming.IcyInputStream;
import sm.smcreator.smc.multimedia.mp3.streaming.TagParseListener;
import sm.smcreator.smc.system.Log;
import sm.smcreator.mp3.decoder.Bitstream;
import sm.smcreator.mp3.decoder.BitstreamException;
import sm.smcreator.mp3.decoder.Decoder;
import sm.smcreator.mp3.decoder.Header;
import sm.smcreator.mp3.decoder.SampleBuffer;

/**
 * @author Anil Kishan
 * @since 17.10.2007
 */
public class MP3Mixer extends BasicMixer
{
	private byte [] output;
	
	private InputStream inputStream;
	private Bitstream bitStream;
	private Decoder	decoder; 
	
	private URL mp3FileUrl;
	
	private TagParseListener tagParseListener;
	
	private int played_ms;
	
	private Boolean isStreaming;

	/**
	 * Constructor for MP3Mixer
	 */
	public MP3Mixer(URL mp3FileUrl)
	{
		super();
		this.mp3FileUrl = mp3FileUrl;
	}
	/**
	 * @since 27.12.2013
	 * @param tagParseListener
	 */
	public void setTagParserListener(TagParseListener tagParseListener)
	{
		this.tagParseListener = tagParseListener;
	}
	private void initialize()
	{
		try
		{
			if (bitStream!=null) try { bitStream.close(); bitStream = null; } catch (BitstreamException e) { Log.error("IGNORED", e); }
			if (inputStream!=null) try { inputStream.close(); inputStream = null; } catch (IOException e) { Log.error("IGNORED", e); }
			if (!isStreaming())
			{
				inputStream = new FileOrPackedInputStream(mp3FileUrl);
			}
			else
			{
				URLConnection conn = mp3FileUrl.openConnection();
				conn.setRequestProperty("Icy-Metadata", "1");
				inputStream = new IcyInputStream(new BufferedInputStream(conn.getInputStream()), tagParseListener);
			}
			this.bitStream = new Bitstream(inputStream);
			this.decoder = new Decoder();
			this.played_ms = 0;
			// Setting the AudioFormat is only possible during
			// playback so it is done in startPlayBack
		}
		catch (Exception ex)
		{
			if (inputStream!=null) try { inputStream.close(); inputStream = null; } catch (IOException e) { Log.error("IGNORED", e); }
			Log.error("[MP3Mixer]", ex);
		}
	}
	/**
	 * @since 10.04.2013
	 * @return
	 */
	private boolean isStreaming()
	{
		if (isStreaming==null)
		{
			if (mp3FileUrl.getProtocol().equalsIgnoreCase("file")) 
				isStreaming = Boolean.FALSE;
			else
			{
				try
				{
					URLConnection con = mp3FileUrl.openConnection();
					//String contentType = con.getContentType();
					int length = con.getContentLength();
					if (length==-1) 
						isStreaming = Boolean.TRUE;
					else
						isStreaming = Boolean.FALSE;
				}
				catch (Throwable ex)
				{
					Log.error("[MP3Mixer::isStreamaing]", ex);
				}
			}
		}
		return isStreaming.booleanValue();
	}
	/**
	 * 
	 * @see sm.smcreator.smc.mixer.Mixer#isSeekSupported()
	 */
	@Override
	public boolean isSeekSupported()
	{
		return !isStreaming();
	}
	/**
	 * 
	 * @see sm.smcreator.smc.mixer.Mixer#getMillisecondPosition()
	 */
	@Override
	public long getMillisecondPosition()
	{
		if (!isStreaming())
			return played_ms;
		else
			return 0;
	}
	/**
	 * 
	 * @see sm.smcreator.smc.mixer.Mixer#getLengthInMilliseconds()
	 */
	@Override
	public long getLengthInMilliseconds()
	{
		if (!isStreaming())
		{
			try
			{
				initialize();
				Header h = bitStream.readFrame();
				if (h!=null)  
					return (long)(h.total_ms(inputStream.available()) + 0.5);
			}
			catch (Throwable ex)
			{
				Log.error("IGNORED", ex);
			}
		}
		return 0;
	}
	/**
	 * @param milliseconds
	 * @see sm.smcreator.smc.mixer.BasicMixer#seek(long)
	 * @since 13.02.2013
	 */
	@Override
	protected void seek(long milliseconds)
	{
		try
		{
			if (!isStreaming())
			{
				if (played_ms>milliseconds)
				{
					if (bitStream!=null) try { bitStream.close(); bitStream = null; } catch (BitstreamException e) { Log.error("IGNORED", e); }
					if (inputStream!=null) try { inputStream.close(); inputStream = null; } catch (IOException e) { Log.error("IGNORED", e); }
					
					inputStream = mp3FileUrl.openStream();
					bitStream = new Bitstream(inputStream);
					this.decoder = new Decoder();
					played_ms = 0;
				}
				
				float f_played_ms = (float)played_ms;
				while (f_played_ms < milliseconds)
				{
					Header h = bitStream.readFrame();
					if (h==null) break;
					f_played_ms += h.ms_per_frame(); 
					bitStream.closeFrame();
				}
				played_ms = (int)(f_played_ms + 0.5);
			}
		}
		catch (Throwable ex)
		{
			Log.error("[MP3Mixer]", ex);
		}
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.mixer.Mixer#getChannelCount()
	 */
	@Override
	public int getChannelCount()
	{
		if (decoder != null)
		{
			return decoder.getOutputChannels();
		}
		return 0;
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.mixer.Mixer#getCurrentKBperSecond()
	 */
	@Override
	public int getCurrentKBperSecond()
	{
		if (bitStream!=null)
		{
			Header h = bitStream.getHeader();
			if (h!=null) return h.bitrate_instant()/1000;
		}
		return 0;
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.mixer.Mixer#getCurrentSampleFrequency()
	 */
	@Override
	public int getCurrentSampleFrequency()
	{
		if (decoder!=null)
			return decoder.getOutputFrequency()/1000;
		else
			return 0;
	}
	/**
	 * @since 30.03.2013
	 * @param length
	 * @return
	 */
	private byte[] getOutputBuffer(int length)
	{
		if (output==null || output.length<length)
			output = new byte[length];
		return output;
	}
	/**
	 * 
	 * @see sm.smcreator.smc.mixer.Mixer#startPlayback()
	 */
	@Override
	public void startPlayback()
	{
		initialize();
		setIsPlaying();
		
		if (getSeekPosition()>0) seek(getSeekPosition());

		try
		{
			Header h = bitStream.readFrame();
			if (h==null) return;
			SampleBuffer output = (SampleBuffer)decoder.decodeFrame(h, bitStream);

			// At this point we know our AudioFormat
			setAudioFormat(new AudioFormat(decoder.getOutputFrequency(), 16, decoder.getOutputChannels(), true, false));
			openAudioDevice();
			if (!isInitialized()) return;

			do
			{
				short[] samples = output.getBuffer();
				played_ms += ((samples.length / decoder.getOutputChannels())*1000)/decoder.getOutputFrequency();
				int origLen = output.getBufferLength();
				int len = origLen<<1;
				byte[] b = getOutputBuffer(len);
				
				int idx = 0;
				int pos = 0;
				short s;
				while (origLen-- > 0)
				{
					s = samples[pos++];
					b[idx++] = (byte)s;
					b[idx++] = (byte)(s>>>8);
				}
				writeSampleDataToLine(b, 0, len);
				
				bitStream.closeFrame();

				if (isStopping())
				{
					setIsStopped();
					break;
				}
				if (isPausing())
				{
					setIsPaused();
					while (isPaused())
					{
						try { Thread.sleep(1); } catch (InterruptedException ex) { /*noop*/ }
					}
				}
				if (isInSeeking())
				{
					setIsSeeking();
					while (isInSeeking())
					{
						try { Thread.sleep(1); } catch (InterruptedException ex) { /*noop*/ }
					}
				}

				h = bitStream.readFrame();
				if (h!=null) output = (SampleBuffer)decoder.decodeFrame(h, bitStream);
			}
			while (h!=null);
			if (h==null) setHasFinished(); // piece finished
		}
		catch (Throwable ex)
		{
			throw new RuntimeException(ex);
		}
		finally
		{
			setIsStopped();
			closeAudioDevice();
			if (bitStream!=null) try { bitStream.close(); bitStream = null; } catch (BitstreamException e) { Log.error("IGNORED", e); }
			if (inputStream!=null) try { inputStream.close(); inputStream = null; } catch (IOException e) { Log.error("IGNORED", e); }
		}
	}
}
