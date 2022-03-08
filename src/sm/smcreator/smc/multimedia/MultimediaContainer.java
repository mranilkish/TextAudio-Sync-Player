/*
 * @(#)MultimediaContainer.java
 *
 * Created on 12.10.2007 by Anil Kishan
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
package sm.smcreator.smc.multimedia;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sm.smcreator.smc.mixer.Mixer;

/**
 * @author: Anil Kishan
 * @since: 12.10.2007
 */
public abstract class MultimediaContainer
{
	private ArrayList<MultimediaContainerEventListener> listeners = new ArrayList<MultimediaContainerEventListener>();
	private URL fileURL;
	/**
	 * @since: 12.10.2007
	 */
	public MultimediaContainer()
	{
		super();
	}
	/**
	 * A default implementation. If you need a new instance,
	 * override this and do whatever is needed!
	 * @since 13.10.2007
	 * @return
	 */
	public MultimediaContainer getInstance(URL url)
	{
		this.fileURL = url;
		return this;
	}
	/**
	 * @since 13.10.2007
	 * @return
	 */
	public URL getFileURL()
	{
		return fileURL;
	}
	/**
	 * @since 23.12.2013
	 * @return a printable version of the URL
	 */
	public String getPrintableFileUrl()
	{
		return getPrintableFileUrl(getFileURL());
	}
	public String getPrintableFileUrl(URL urlName)
	{
		try
		{
			java.io.File f = new java.io.File(urlName.toURI());
			try
			{
				return f.getCanonicalPath();
			}
			catch (IOException ex)
			{
				return f.getAbsolutePath();
			}
		}
		catch (URISyntaxException e)
		{
			return urlName.toExternalForm();
		}
	}
	public void updateLookAndFeel() 
	{
		JPanel infoPanel = getInfoPanel();
		JPanel configPanel = getConfigPanel();
		if (infoPanel!=null) SwingUtilities.updateComponentTreeUI(infoPanel); 
		if (configPanel!=null) SwingUtilities.updateComponentTreeUI(configPanel); 
	}
	public void addListener(MultimediaContainerEventListener listener)
	{
		listeners.add(listener);
	}
	public void removeListener(MultimediaContainerEventListener listener)
	{
		listeners.remove(listener);
	}
	protected void fireMultimediaContainerEvent(MultimediaContainerEvent event)
	{
		for (int i=0; i<listeners.size(); i++)
			listeners.get(i).multimediaContainerEventOccured(event);
	}
	/**
	 * Return the name of the song
	 * @since 08.02.2018
	 * @return
	 */
	public String getSongName()
	{
		return MultimediaContainerManager.getSongNameFromURL(fileURL);
	}
	/**
	 * This method will only do (!)localy(!) what is needed to pick up
	 * the song name String at [0] and time in milliseconds as Long at [1]
	 * @since 12.02.2013
	 * @param url
	 * @return
	 */
	public abstract Object [] getSongInfosFor(URL url);
	/**
	 * Returns true if this mixers supports the export function
	 * @since 26.10.2007
	 * @return
	 */
	public abstract boolean canExport();
	/**
	 * Return the info dialog panel
	 * @since 13.10.2007
	 * @return
	 */
	public abstract JPanel getInfoPanel();
	/**
	 * Returns the config panel for this mixer
	 * @since 13.10.2007
	 * @return
	 */
	public abstract JPanel getConfigPanel();
	/**
	 * The file extensions this container is responsible for 
	 * @since: 12.10.2007
	 * @return
	 */
	public abstract String [] getFileExtensionList();
	/**
	 * A describtive Name for e.g. a FileChooser
	 * @since 05.01.2013
	 * @return
	 */
	public abstract String getName();
	/**
	 * @since 13.10.2007
	 * @param newProps
	 */
	public abstract void configurationChanged(Properties newProps);
	/**
	 * @since 14.10.2007
	 * @param props
	 */
	public abstract void configurationSave(Properties props);
	/**
	 * Get the ModMixer of this container
	 * @since: 12.10.2007
	 * @return
	 */
	public abstract Mixer createNewMixer();
}
