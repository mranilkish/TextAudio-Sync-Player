/*
 * @(#) MP3Container.java
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

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.swing.JPanel;

import sm.smcreator.smc.io.RandomAccessInputStreamImpl;
import sm.smcreator.smc.mixer.Mixer;
import sm.smcreator.smc.multimedia.MultimediaContainer;
import sm.smcreator.smc.multimedia.MultimediaContainerEvent;
import sm.smcreator.smc.multimedia.MultimediaContainerManager;
import sm.smcreator.smc.multimedia.mp3.id3.MP3FileID3Controller;
import sm.smcreator.smc.multimedia.mp3.streaming.TagParseEvent;
import sm.smcreator.smc.multimedia.mp3.streaming.TagParseListener;
import sm.smcreator.smc.system.Log;
import sm.smcreator.mp3.decoder.Bitstream;
import sm.smcreator.mp3.decoder.BitstreamException;
import sm.smcreator.mp3.decoder.Header;

/**
 * @author Anil Kishan
 * @since 17.10.2007
 */
public class MP3Container extends MultimediaContainer implements TagParseListener
{
	private static final String[] MP3FILEEXTENSION = new String [] 
  	{
  		"mp1", "mp2", "mp3"
  	};

//	private JPanel mp3ConfigPanel;
	private MP3Mixer currentMixer;
	private MP3InfoPanel mp3InfoPanel;
	private MP3StreamInfoPanel mp3StreamInfoPanel;
	private MP3FileID3Controller mp3FileIDTags = null;
	
	private boolean isStreaming;
	
	/**
	 * Will be executed during class load
	 */
	static
	{
		MultimediaContainerManager.registerContainer(new MP3Container());
	}
	/**
	 * Constructor for MP3Container
	 */
	public MP3Container()
	{
		super();
	}
	/**
	 * @param url
	 * @return
	 * @see sm.smcreator.smc.multimedia.MultimediaContainer#getInstance(java.net.URL)
	 */
	@Override
	public MultimediaContainer getInstance(URL mp3FileUrl)
	{
		MultimediaContainer result = super.getInstance(mp3FileUrl);
		isStreaming = !mp3FileUrl.getProtocol().equalsIgnoreCase("file"); 
		if (!isStreaming)
		{
			mp3FileIDTags = new MP3FileID3Controller(mp3FileUrl);
			((MP3InfoPanel)getInfoPanel()).fillInfoPanelWith(mp3FileIDTags);
		}
		else
		{
			mp3FileIDTags = null;
			((MP3StreamInfoPanel)getInfoPanel()).clearFields();
		}
		return result;
	}
	@Override
	public String getSongName()
	{
		if (mp3FileIDTags!=null)
			return mp3FileIDTags.getShortDescription();
		else
			return super.getSongName();
	}
	/**
	 * @param url
	 * @return
	 * @see sm.smcreator.smc.multimedia.MultimediaContainer#getSongInfosFor(java.net.URL)
	 */
	@Override
	public Object[] getSongInfosFor(URL url)
	{
		String songName = MultimediaContainerManager.getSongNameFromURL(url);
		Long duration = Long.valueOf(-1);
		RandomAccessInputStreamImpl inputStream = null;
		Bitstream bitStream = null;
		try
		{
			if (url.getProtocol().equalsIgnoreCase("file"))
			{
				inputStream = new RandomAccessInputStreamImpl(url);
				bitStream = new Bitstream(inputStream);
				Header h = bitStream.readFrame();
				if (h!=null) duration = Long.valueOf((long)(h.total_ms(inputStream.available()) + 0.5));
				mp3FileIDTags = new MP3FileID3Controller(inputStream);
				if (mp3FileIDTags!=null) songName = mp3FileIDTags.getShortDescription();
			}
		}
		catch (Throwable ex)
		{
		}
		finally
		{
			if (bitStream != null) try { bitStream.close();  } catch (BitstreamException ex) { Log.error("IGNORED", ex); }
			if (inputStream != null) try { inputStream.close(); } catch (IOException ex) { Log.error("IGNORED", ex); }
		}
		return new Object[] { songName, duration };
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.multimedia.MultimediaContainer#canExport()
	 */
	@Override
	public boolean canExport()
	{
		return true;
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.multimedia.MultimediaContainer#getConfigPanel()
	 */
	@Override
	public JPanel getConfigPanel()
	{
		return null;
//		if (mp3ConfigPanel==null)
//		{
//			mp3ConfigPanel = new JPanel();
//		}
//		return mp3ConfigPanel;
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.multimedia.MultimediaContainer#getInfoPanel()
	 */
	@Override
	public JPanel getInfoPanel()
	{
		if (isStreaming)
		{
			if (mp3StreamInfoPanel==null) mp3StreamInfoPanel = new MP3StreamInfoPanel();
			return mp3StreamInfoPanel;
		}
		else
		{
			if (mp3InfoPanel==null) mp3InfoPanel = new MP3InfoPanel();
			return mp3InfoPanel;
		}
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.multimedia.MultimediaContainer#getFileExtensionList()
	 */
	@Override
	public String[] getFileExtensionList()
	{
		return MP3FILEEXTENSION;
	}
	/**
	 * @return the name of the group of files this container knows
	 * @see sm.smcreator.smc.multimedia.MultimediaContainer#getName()
	 */
	@Override
	public String getName()
	{
		return "MP3-File";
	}
	/**
	 * @param newProps
	 * @see sm.smcreator.smc.multimedia.MultimediaContainer#configurationChanged(java.util.Properties)
	 */
	@Override
	public void configurationChanged(Properties newProps)
	{
	}
	/**
	 * @param props
	 * @see sm.smcreator.smc.multimedia.MultimediaContainer#configurationSave(java.util.Properties)
	 */
	@Override
	public void configurationSave(Properties props)
	{
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.multimedia.MultimediaContainer#createNewMixer()
	 */
	@Override
	public Mixer createNewMixer()
	{
		currentMixer = new MP3Mixer(getFileURL());
		currentMixer.setTagParserListener(this);
		return currentMixer;
	}
	/**
	 * @param tpe
	 * @see sm.smcreator.smc.multimedia.mp3.streaming.TagParseListener#tagParsed(sm.smcreator.smc.multimedia.mp3.streaming.TagParseEvent)
	 */
	public void tagParsed(TagParseEvent tpe)
	{
		JPanel panel = getInfoPanel();
		if (panel instanceof MP3StreamInfoPanel)
		{
			((MP3StreamInfoPanel)panel).fillInfoPanelWith(tpe.getIcyTag());
			String currentSongName = ((MP3StreamInfoPanel)panel).getCurrentSongName();
			if (currentSongName!=null && currentSongName.length()!=0)
				fireMultimediaContainerEvent(new MultimediaContainerEvent(this, MultimediaContainerEvent.SONG_NAME_CHANGED, currentSongName));
		}
	}
}
