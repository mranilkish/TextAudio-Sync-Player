/*
 * @(#) PlaylistDropListener.java
 *
 * Created on 08.03.2013 by Anil Kishan
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
package sm.smcreator.smc.main.gui.tools;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import sm.smcreator.smc.main.playlist.PlayList;
import sm.smcreator.smc.multimedia.MultimediaContainerManager;
import sm.smcreator.smc.system.Log;

/**
 * @author Anil Kishan
 *
 */
public class PlaylistDropListener extends DropTargetAdapter
{
	private PlaylistDropListenerCallBack callBack;

	/**
	 * 
	 * @since 08.03.2013
	 */
	public PlaylistDropListener(PlaylistDropListenerCallBack callBack)
	{
		this.callBack = callBack;
	}

	private void fillWithPlayableFiles(ArrayList<URL> urls, File startDir)
	{
		String [] files = startDir.list(new FilenameFilter()
		{
			public boolean accept(File dir, String name)
			{
				File fullFileName = new File(dir.getAbsolutePath() + File.separatorChar + name);
				if (fullFileName.isDirectory()) return true;
				try
				{
					return MultimediaContainerManager.getMultimediaContainerSingleton(fullFileName.toURI().toURL()) != null;
				}
				catch (Exception ex)
				{
					//NOOP;
				}
				return false;
			}
		});
		for (int i=0; i<files.length; i++)
		{
			File fullFileName = new File(startDir.getAbsolutePath() + File.separatorChar + files[i]);
			if (fullFileName.isDirectory())
				fillWithPlayableFiles(urls, fullFileName);
			else
			{
				try
				{
					urls.add(fullFileName.toURI().toURL());
				}
				catch (Exception ex)
				{
					//NOOP;
				}
			}
		}
	}
	/**
	 * @param dtde
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 * @since 08.03.2013
	 */
	public void drop(DropTargetDropEvent dtde)
	{
		try
		{
			URL addToLastLoaded = null;
			Transferable t = dtde.getTransferable();
			if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
			{
				dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
				Object userObject = t.getTransferData(DataFlavor.javaFileListFlavor);
				if (userObject instanceof List<?>)
				{
					List<?> files = ((List<?>)userObject);
					ArrayList<URL> urls = new ArrayList<URL>(files.size());
					for (int i=0; i<files.size(); i++)
					{
						String fileName = files.get(i).toString();
						File f = new File(fileName);
						if (f.isDirectory())
						{
							fillWithPlayableFiles(urls, f);
						}
						else
						{
							URL url = f.toURI().toURL();
							if (files.size()==1) addToLastLoaded = url;
							urls.add(url);
						}
					}
	    			PlayList playList = PlayList.createNewListWithFiles(urls.toArray(new URL[urls.size()]), false, false);
					callBack.playlistRecieved(dtde, playList, addToLastLoaded);
				}
			}
		}
		catch (Exception ex)
		{
			Log.error("[MainForm::DropListener]", ex);
		}
		finally
		{
			dtde.dropComplete(true);
		}
	}

}
