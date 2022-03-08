/*
 * @(#) SoundOutputStream.java
 *
 * Created on 02.10.2013 by Anil Kishan
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

import sm.smcreator.smc.io.wav.WaveFile;
import sm.smcreator.smc.mixer.dsp.AudioProcessor;


/**
 * @author Anil Kishan
 * This Interface describes a soundoutput stream for playback
 * @since 02.10.2013
 */
public interface SoundOutputStream
{
	public void open();
	public void close();
	public void closeAllDevices();
	public boolean isInitialized();
	public void startLine();
	public void stopLine();
	public void writeSampleData(byte[] samples, int start, int length);
	public void setInternalFramePosition(long newPosition);
	public void setVolume(float gain);
	public void setBalance(float balance);
	public void setAudioProcessor(AudioProcessor audioProcessor);
	public void setExportFile(File exportFile);
	public void setWaveExportFile(WaveFile waveExportFile);
	public void setPlayDuringExport(boolean playDuringExport);
	public void setKeepSilent(boolean keepSilent);
	public void changeAudioFormatTo(AudioFormat newFormat);
	public AudioFormat getAudioFormat();
	public boolean matches(SoundOutputStream otherStream);
}
