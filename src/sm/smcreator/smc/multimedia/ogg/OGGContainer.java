/*
 * @(#) OGGContainer.java
 *
 * Created on 01.11.2013 by Anil Kishan
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
package sm.smcreator.smc.multimedia.ogg;

import java.net.URL;
import java.util.Properties;

import javax.swing.JPanel;

import sm.smcreator.smc.mixer.Mixer;
import sm.smcreator.smc.multimedia.MultimediaContainer;
import sm.smcreator.smc.multimedia.MultimediaContainerManager;
import sm.smcreator.smc.multimedia.ogg.metadata.OggMetaData;

/**
 * @author Anil Kishan
 * @since 01.11.2013
 */
public class OGGContainer extends MultimediaContainer
{
	private static final String[] OGGFILEEXTENSION = new String [] 
	{
		"ogg", "oga"
	};
//	private JPanel oggConfigPanel;
	private JPanel oggInfoPanel;
	private OggMetaData oggMetaData = null;
	/**
	 * Will be executed during class load
	 */
	static
	{
		MultimediaContainerManager.registerContainer(new OGGContainer());
	}
	/**
	 * Constructor for OGGContainer
	 */
	public OGGContainer()
	{
		super();
	}
	/**
	 * @param url
	 * @return
	 * @see sm.smcreator.smc.multimedia.MultimediaContainer#getInstance(java.net.URL)
	 */
	@Override
	public MultimediaContainer getInstance(URL url)
	{
		MultimediaContainer result = super.getInstance(url);
		oggMetaData = new OggMetaData(url);
		((OGGInfoPanel)getInfoPanel()).fillInfoPanelWith(oggMetaData, getPrintableFileUrl());
		return result;
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.multimedia.MultimediaContainer#getSongName()
	 */
	@Override
	public String getSongName()
	{
		if (oggMetaData!=null) 
			return oggMetaData.getShortDescription();
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
		try
		{
			OggMetaData metaData = new OggMetaData(url);
			songName = metaData.getShortDescription();
			duration = Long.valueOf(metaData.getLengthInMilliseconds());
		}
		catch (Throwable ex)
		{
		}
		return new Object[] { songName, duration };
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.multimedia.MultimediaContainer#createNewMixer()
	 */
	@Override
	public Mixer createNewMixer()
	{
		return new OGGMixer(getFileURL(), oggMetaData.getLengthInMilliseconds());
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
	 * @see sm.smcreator.smc.multimedia.MultimediaContainer#getFileExtensionList()
	 */
	@Override
	public String[] getFileExtensionList()
	{
		return OGGFILEEXTENSION;
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.multimedia.MultimediaContainer#getName()
	 */
	@Override
	public String getName()
	{
		return "ogg/vorbis-File";
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.multimedia.MultimediaContainer#getConfigPanel()
	 */
	@Override
	public JPanel getConfigPanel()
	{
		return null;
//		if (oggConfigPanel==null)
//		{
//			oggConfigPanel = new JPanel();
//		}
//		return oggConfigPanel;
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.multimedia.MultimediaContainer#getInfoPanel()
	 */
	@Override
	public JPanel getInfoPanel()
	{
		if (oggInfoPanel==null)
		{
			oggInfoPanel = new OGGInfoPanel();
		}
		return oggInfoPanel;
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
}
