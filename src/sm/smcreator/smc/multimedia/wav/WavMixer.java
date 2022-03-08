/*
 * @(#) WavMixer.java
 *
 * Created on 14.10.2007 by Anil Kishan
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
package sm.smcreator.smc.multimedia.wav;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import sm.smcreator.smc.io.FileOrPackedInputStream;
import sm.smcreator.smc.mixer.BasicMixer;
import sm.smcreator.smc.system.Log;

/**
 * @author Anil Kishan
 * @since 14.10.2007
 */
public class WavMixer extends BasicMixer
{
	private int bufferSize;
	private byte [] output;

	private int sampleSizeInBits;
	private int sampleSizeInBytes;
	private int channels;
	private int sampleRate;
	private int lengthInMilliseconds;
	
	private URL waveFileUrl;
	private AudioInputStream audioInputStream;
	
	private long currentSamplesWritten;
	
	/**
	 * Constructor for WavMixer
	 */
	public WavMixer(URL waveFileUrl)
	{
		super();
		this.waveFileUrl = waveFileUrl;
		initialize();
	}
	private void initialize()
	{
		try
		{
			if (audioInputStream!=null) try { audioInputStream.close(); } catch (IOException ex) { Log.error("IGNORED", ex); }
			audioInputStream = AudioSystem.getAudioInputStream(new FileOrPackedInputStream(waveFileUrl));
			AudioFormat audioFormat = audioInputStream.getFormat();
			
			lengthInMilliseconds = 0;
			float frameRate = audioFormat.getFrameRate();
			if (frameRate != AudioSystem.NOT_SPECIFIED)
			{
				lengthInMilliseconds = (int)(((float)audioInputStream.getFrameLength() * 1000f / frameRate)+0.5);
			}
			else
			{
				try
				{
					lengthInMilliseconds = (int)(((long)audioInputStream.available() / ((long)(audioFormat.getSampleSizeInBits()>>3)) / (long)audioFormat.getChannels()) * 1000L / (long)audioFormat.getSampleRate());
				}
				catch (IOException ex)
				{
					Log.error("[WavMixer] No data available!", ex);
				}
			}

			// Check, if conversion is necessary and possible:
			DataLine.Info sourceLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
			if (!AudioSystem.isLineSupported(sourceLineInfo))
			{
				AudioFormat[] possibleFormats = AudioSystem.getTargetFormats(AudioFormat.Encoding.PCM_SIGNED, audioFormat);
				if (possibleFormats!=null && possibleFormats.length!=0)
				{
					audioInputStream = AudioSystem.getAudioInputStream(possibleFormats[0], audioInputStream);
					audioFormat = audioInputStream.getFormat();
					Log.info("Converting input data to " + audioFormat.toString());
				}
			}
			setAudioFormat(audioFormat);

			this.channels = audioFormat.getChannels();
			this.sampleSizeInBits = audioFormat.getSampleSizeInBits();
			this.sampleSizeInBytes = this.sampleSizeInBits>>3;
			this.sampleRate = (int)audioFormat.getSampleRate();
			
			this.bufferSize = 250 * channels * sampleRate / 1000; // 250ms buffer

			// Now for the bits (linebuffer):
			bufferSize *= sampleSizeInBytes;
			output = new byte[bufferSize];
		}
		catch (Throwable ex)
		{
			Log.error("[WavMixer]", ex);
		}
	}
	/**
	 * 
	 * @see sm.smcreator.smc.mixer.Mixer#isSeekSupported()
	 */
	@Override
	public boolean isSeekSupported()
	{
		return true;
	}
	/**
	 * 
	 * @see sm.smcreator.smc.mixer.Mixer#getMillisecondPosition()
	 */
	@Override
	public long getMillisecondPosition()
	{
		if (sampleRate!=0)
			return ((long)currentSamplesWritten * 1000L) / (long)sampleRate;
		else
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
			if (getMillisecondPosition() > milliseconds)
			{
				if (audioInputStream!=null) try { audioInputStream.close(); } catch (IOException ex) { Log.error("IGNORED", ex); }
				audioInputStream = AudioSystem.getAudioInputStream(waveFileUrl);
				currentSamplesWritten = 0;
			}
			long skipSamples = (milliseconds * (long)sampleRate / 1000L) - currentSamplesWritten;
			long skipBytes = skipSamples * sampleSizeInBytes * channels;
			while (skipBytes>0)
			{
				skipBytes -= audioInputStream.skip(skipBytes);
			}
			currentSamplesWritten += skipSamples;
		}
		catch (Exception ex)
		{
			Log.error("[WavMixer]: error while seeking", ex);
		}
	}
	/**
	 * 
	 * @see sm.smcreator.smc.mixer.Mixer#getLengthInMilliseconds()
	 */
	@Override
	public long getLengthInMilliseconds()
	{
		return lengthInMilliseconds;
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.mixer.Mixer#getChannelCount()
	 */
	@Override
	public int getChannelCount()
	{
		return channels;
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.mixer.Mixer#getCurrentKBperSecond()
	 */
	@Override
	public int getCurrentKBperSecond()
	{
		return (sampleSizeInBits*channels*sampleRate) / 1000;
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.mixer.Mixer#getCurrentSampleFrequency()
	 */
	@Override
	public int getCurrentSampleFrequency()
	{
		return sampleRate / 1000;
	}
	/**
	 * 
	 * @see sm.smcreator.smc.mixer.Mixer#startPlayback()
	 */
	@Override
	public void startPlayback()
	{
		initialize();
		currentSamplesWritten = 0; // not in initialize which is also called at freq. changes

		setIsPlaying();

		if (getSeekPosition()>0) seek(getSeekPosition());

		try
		{
			openAudioDevice();
			if (!isInitialized()) return;

			int byteCount = 0;
			
			do
			{
				byteCount = audioInputStream.read(output, 0, bufferSize);
				if (byteCount>0)
				{
					writeSampleDataToLine(output, 0, byteCount);

					currentSamplesWritten += (byteCount / sampleSizeInBytes / channels);

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
				}
			}
			while (byteCount!=-1);
			if (byteCount<=0) setHasFinished(); // Piece finished fully
		}
		catch (Throwable ex)
		{
			throw new RuntimeException(ex);
		}
		finally
		{
			setIsStopped();
			closeAudioDevice();
			if (audioInputStream!=null) try { audioInputStream.close(); audioInputStream = null; } catch (IOException ex) { Log.error("IGNORED", ex); }
		}
	}
}
