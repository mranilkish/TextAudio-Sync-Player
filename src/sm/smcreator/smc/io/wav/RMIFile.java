/*
 * @(#) RMIFile.java
 *
 * Created on 07.11.2013 by Anil Kishan
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
package sm.smcreator.smc.io.wav;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.sampled.UnsupportedAudioFileException;

import sm.smcreator.smc.io.FileOrPackedInputStream;
import sm.smcreator.smc.system.Log;

/**
 * @author Anil Kishan
 * @since 07.11.2013
 */
public class RMIFile extends RiffFile
{
	/**
	 * Constructor for RMIFile
	 */
	public RMIFile()
	{
		super();
	}

	private static int fourCC(byte br[])
	{
		return ((br[0] << 24) & 0xFF000000) | ((br[1] << 16) & 0x00FF0000) | ((br[2] << 8) & 0x0000FF00) | (br[3] & 0x000000FF);
	}
	public static Sequence open(URL url) throws UnsupportedAudioFileException
	{
		InputStream rmiInput = null;
		try
		{
			rmiInput = new FileOrPackedInputStream(url);
			byte[] br = new byte[8];
			
			rmiInput.read(br, 0, 8);
			int chkID = ((br[0] << 24) & 0xFF000000) | ((br[1] << 16) & 0x00FF0000) | ((br[2] << 8) & 0x0000FF00) | (br[3] & 0x000000FF);
			//int chkSize = ((br[7] << 24) & 0xFF000000) | ((br[6] << 16) & 0x00FF0000) | ((br[5] << 8) & 0x0000FF00) | (br[4] & 0x000000FF);
			if (chkID != fourCC("RIFF")) throw new UnsupportedAudioFileException("File is not a RMI RIFF file");

			rmiInput.read(br, 0, 4);
			if (fourCC(br) != fourCC("RMID")) throw new UnsupportedAudioFileException("File is not a RMI RIFF file");
			
			rmiInput.read(br, 0, 8);
			int dataID = ((br[0] << 24) & 0xFF000000) | ((br[1] << 16) & 0x00FF0000) | ((br[2] << 8) & 0x0000FF00) | (br[3] & 0x000000FF);
			int dataSize = ((br[7] << 24) & 0xFF000000) | ((br[6] << 16) & 0x00FF0000) | ((br[5] << 8) & 0x0000FF00) | (br[4] & 0x000000FF);
			if (dataID != fourCC("data")) throw new UnsupportedAudioFileException("File is not a RMI RIFF file");

			byte [] buffer = new byte[dataSize];
			int fullSize = 0;
			while (fullSize<dataSize)
			{
				int readLength = rmiInput.read(buffer, fullSize, dataSize - fullSize);
				if (readLength==-1) break;
				fullSize += readLength;
			}
			
			ByteArrayInputStream input = null;
			try
			{
				input = new ByteArrayInputStream(buffer);
				Sequence result = MidiSystem.getSequence(input);
				return result;
			}
			catch (Exception ex)
			{
				Log.error("[RMIFile]", ex);
			}
			finally
			{
				if (input!=null) try { input.close(); } catch (Exception ex) { Log.error("IGNORED", ex); }
			}
		}
		catch (IOException ex)
		{
			Log.error("[RMIFile]", ex);
		}
		finally
		{
			if (rmiInput!=null) try { rmiInput.close(); } catch (Exception ex) { Log.error("IGNORED", ex); }
		}
		return null;		
	}
}
