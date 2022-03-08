/*
 * @(#) SoundOutputStreamImpl.java
 *
 * Created on 30.12.2007 by Anil Kishan
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
package sm.smcreator.smc.io;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

import sm.smcreator.smc.io.wav.WaveFile;
import sm.smcreator.smc.mixer.dsp.AudioProcessor;
import sm.smcreator.smc.system.Helpers;
import sm.smcreator.smc.system.Log;

/**
 * This outputstream will wrap audiolines and file-exports
 * so that the mixers do not have to think about it.
 * @author Anil Kishan
 * @since 30.12.2007
 */
public class SoundOutputStreamImpl implements SoundOutputStream
{
	protected AudioProcessor audioProcessor;
	protected AudioFormat audioFormat;
	protected File exportFile;
	
	protected float currentVolume;
	protected float currentBalance;

	protected SourceDataLine sourceLine;
	protected WaveFile waveExportFile;
	protected boolean playDuringExport;
	protected boolean keepSilent;
	
	protected SoundOutputStreamImpl()
	{
		super();
	}
	/**
	 * Constructor for SoundOutputStreamImpl
	 * @param audioFormat		the Format of delivered Audio
	 * @param audioProcessor	the class of the audioProcessor - if any
	 * @param exportFile		exportFile - the File to write to
	 * @param playDuringExport	if true, data will be send to line and file
	 * @param keepSilent		if true, 0 bytes will be send to the line
	 */
	public SoundOutputStreamImpl(AudioFormat audioFormat, AudioProcessor audioProcessor, File exportFile, boolean playDuringExport, boolean keepSilent)
	{
		this();
		this.audioFormat = audioFormat;
		this.audioProcessor = audioProcessor;
		this.exportFile = exportFile;
		this.playDuringExport = playDuringExport;
		this.keepSilent = keepSilent;
	}
	/**
	 * @since 30.12.2007
	 */
	protected synchronized void openSourceLine()
	{
		if (audioFormat!=null)
		{
			try
			{
				closeSourceLine();
				DataLine.Info sourceLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
				if (AudioSystem.isLineSupported(sourceLineInfo))
				{
					sourceLine = (SourceDataLine) AudioSystem.getLine(sourceLineInfo);
					sourceLine.open();
					sourceLine.start();
					setVolume(currentVolume);
					setBalance(currentBalance);
				}
				else
					Log.info("Audioformat is not supported");
			}
			catch (Exception ex)
			{
				sourceLine = null;
				Log.error("Error occured when opening audio device", ex);
			}
		}
	}
	/**
	 * @since 30.12.2007
	 */
	protected synchronized void openAudioProcessor()
	{
		if (audioProcessor!=null)
		{
			if (sourceLine!=null)
			{
				audioProcessor.initializeProcessor(sourceLine);
				audioProcessor.setUseInternalCounter(keepSilent);
			}
			else
			{
				audioProcessor.initializeProcessor(audioFormat);
				audioProcessor.setUseInternalCounter(true);
			}
		}
	}
	protected synchronized void openExportFile()
	{
		if (exportFile!=null)
		{
			waveExportFile = new WaveFile();
			if (waveExportFile.openForWrite(exportFile, audioFormat)!=WaveFile.DDC_SUCCESS)
			{
				waveExportFile = null;
				Log.error("Creation of exportfile was NOT successfull!");
			}
		}
	}
	/**
	 * @since 30.12.2007
	 */
	protected synchronized void closeSourceLine()
	{
		if (sourceLine!=null)
		{
			stopLine();
			sourceLine.close();
			sourceLine = null;
		}
	}
	/**
	 * @since 30.12.2007
	 */
	protected synchronized void closeAudioProcessor()
	{
		if (audioProcessor!=null) audioProcessor.stop();
	}
	/**
	 * @since 30.12.2007
	 */
	protected synchronized void closeExportFile()
	{
		if (waveExportFile!=null) waveExportFile.close();
	}
	/**
	 * @since 30.12.2007
	 */
	public synchronized void open()
	{
		close();
		if (playDuringExport || exportFile==null) openSourceLine();
		openAudioProcessor();
		openExportFile();
	}
	/**
	 * @since 30.12.2007
	 */
	public synchronized void close()
	{
		closeSourceLine();
		closeAudioProcessor();
		closeExportFile();
	}
	public synchronized void closeAllDevices()
	{
		close();
	}
	/**
	 * @since 30.12.2007
	 * @return
	 */
	public synchronized boolean isInitialized()
	{
		return (sourceLine!=null && sourceLine.isOpen()) || exportFile!=null;
	}
	/**
	 * @since 30.12.2007
	 */
	public synchronized void startLine()
	{
		if (sourceLine!=null)
		{
			sourceLine.flush();
			sourceLine.start();
		}
	}
	/**
	 * @since 30.12.2007
	 */
	public synchronized void stopLine()
	{
		if (sourceLine!=null)
		{
			if (sourceLine.isRunning()) sourceLine.drain();
			sourceLine.stop();
		}
	}
	/**
	 * @since 27.12.2013
	 * @param samples
	 * @param start
	 * @param length
	 */
	protected synchronized void writeSampleDataInternally(byte[] samples, int start, int length)
	{
		if (sourceLine!=null && !keepSilent) sourceLine.write(samples, start, length);
		if (waveExportFile!=null) waveExportFile.writeSamples(samples, start, length);
	}
	/**
	 * @since 30.12.2007
	 * @param samples
	 * @param start
	 * @param length
	 */
	public synchronized void writeSampleData(byte[] samples, int start, int length)
	{
		if (audioProcessor!=null)
		{
			int anzSamples = audioProcessor.writeSampleData(samples, start, length);
			writeSampleDataInternally(audioProcessor.getResultSampleBuffer(), 0, anzSamples);
		}
		else
			writeSampleDataInternally(samples, start, length);
	}
	/**
	 * @since 27.11.2013
	 * @param newFramePosition
	 * @see sm.smcreator.smc.io.SoundOutputStream#setInternalFramePosition(long)
	 */
	public synchronized void setInternalFramePosition(long newFramePosition)
	{
		if (audioProcessor!=null) audioProcessor.setInternalFramePosition(newFramePosition);
	}
	/**
	 * Set the Gain of the sourceLine
	 * @since 01.11.2013
	 * @param gain
	 */
	public synchronized void setVolume(float gain)
	{
		currentVolume = gain;
	    if (sourceLine!=null && sourceLine.isControlSupported(FloatControl.Type.MASTER_GAIN))
	    {
	    	FloatControl gainControl = (FloatControl)sourceLine.getControl(FloatControl.Type.MASTER_GAIN);
	        float dB = (float)(Helpers.getDBValueFrom(gain));
	        if (dB > gainControl.getMaximum()) dB = gainControl.getMaximum();
	        else
	        if (dB < gainControl.getMinimum()) dB = gainControl.getMinimum();
        	gainControl.setValue(dB);
	    }
	}
	/**
	 * Set the Balance of the sourceLine
	 * @since 01.11.2013
	 * @param gain
	 */
	public synchronized void setBalance(float balance)
	{
		currentBalance = balance;
	    if (sourceLine!=null && sourceLine.isControlSupported(FloatControl.Type.BALANCE))
	    {
	    	FloatControl balanceControl = (FloatControl)sourceLine.getControl(FloatControl.Type.BALANCE);
	    	if (balance <= balanceControl.getMaximum() && balance >= balanceControl.getMinimum())
	    		balanceControl.setValue(balance);
	    }
	}
	/**
	 * @param audioProcessor the audioProcessor to set
	 * @since 25.02.2013
	 */
	public synchronized void setAudioProcessor(AudioProcessor audioProcessor)
	{
		this.audioProcessor = audioProcessor;
	}
	/**
	 * @param exportFile the exportFile to set
	 * @since 25.02.2013
	 */
	public synchronized void setExportFile(File exportFile)
	{
		this.exportFile = exportFile;
	}
	/**
	 * @param waveExportFile the waveExportFile to set
	 * @since 25.02.2013
	 */
	public synchronized void setWaveExportFile(WaveFile waveExportFile)
	{
		this.waveExportFile = waveExportFile;
	}
	/**
	 * @param playDuringExport the playDuringExport to set
	 * @since 25.02.2013
	 */
	public synchronized void setPlayDuringExport(boolean playDuringExport)
	{
		this.playDuringExport = playDuringExport;
	}
	/**
	 * @param keepSilent the keepSilent to set
	 * @since 25.02.2013
	 */
	public synchronized void setKeepSilent(boolean keepSilent)
	{
		this.keepSilent = keepSilent;
	}
	public boolean matches(SoundOutputStream otherStream)
	{
		return getAudioFormat().matches(otherStream.getAudioFormat());
	}
	/** 
	 * @see sm.smcreator.smc.io.SoundOutputStream#getAudioFormat()
	 */
	public synchronized AudioFormat getAudioFormat()
	{
		return audioFormat;
	}
	/**
	 * @see sm.smcreator.smc.io.SoundOutputStream#changeAudioFormatTo(javax.sound.sampled.AudioFormat)
	 */
	public synchronized void changeAudioFormatTo(AudioFormat newAudioFormat)
	{
		boolean reOpen = sourceLine!=null && sourceLine.isOpen();
		close();
		audioFormat = newAudioFormat;
		if (reOpen) open();
	}
}
