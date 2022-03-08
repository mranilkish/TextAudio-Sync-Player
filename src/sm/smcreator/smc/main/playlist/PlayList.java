/*
 * @(#) PlayList.java
 *
 * Created on 03.12.2006 by Anil Kishan
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
package sm.smcreator.smc.main.playlist;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import sm.smcreator.smc.main.gui.tools.FileChooserFilter;
import sm.smcreator.smc.multimedia.MultimediaContainerManager;
import sm.smcreator.smc.system.Helpers;
import sm.smcreator.smc.system.Log;

/**
 * @author Anil Kishan
 * @since 03.12.2006
 */
public class PlayList
{
	public static String [] SUPPORTEDPLAYLISTS = { "pls", "m3u", "cue", "zip" };
	public static FileChooserFilter PLAYLIST_FILE_FILTER = new FileChooserFilter(PlayList.SUPPORTEDPLAYLISTS, PlayList.getFileChooserDescription());
	
	public static String [] SUPPORTEDSAVELISTS = { "pls", "m3u" };
	public static FileChooserFilter PLAYLIST_SAVE_FILE_FILTER = new FileChooserFilter(PlayList.SUPPORTEDSAVELISTS, PlayList.getFileChooserDescription());
	
	private static String INDEX_STRING = "  index ";

	private URL loadedFromURL;
	private ArrayList<PlayListEntry> entries;
	private int current;
	private boolean repeat;
	
	private ArrayList<PlaylistChangedListener> listeners = new ArrayList<PlaylistChangedListener>();
	
	/**
	 * Constructor for PlayList
	 */
	public PlayList(boolean shuffle, boolean repeat)
	{
		this.entries = new ArrayList<PlayListEntry>();
		this.current = -1;
		this.repeat = repeat;
		if (shuffle) doShuffle();
	}
	/**
	 * @param entries
	 * @param shuffle
	 * @since 23.03.2013
	 */
	public PlayList(ArrayList<PlayListEntry> entries, boolean shuffle, boolean repeat)
	{
		this.entries = entries;
		for (int i=0; i<entries.size(); i++) entries.get(i).setSavedInPlaylist(this);
		this.current = -1;
		this.repeat = repeat;
		if (shuffle) doShuffle();
	}
	/**
	 * Constructor for PlayList
	 */
	public PlayList(File [] files, boolean shuffle, boolean repeat)
	{
		this(generateURLListFromFiles(files), shuffle, repeat);
	}
	/**
	 * Constructor for PlayList
	 */
	public PlayList(String [] fileNames, boolean shuffle, boolean repeat)
	{
		this(generateURLListFromFileNames(fileNames), shuffle, repeat);
	}
	/**
	 * Constructor for PlayList
	 */
	public PlayList(URL [] urls, boolean shuffle, boolean repeat)
	{
		this.entries = new ArrayList<PlayListEntry>(urls.length);
		for (int i=0; i<urls.length; i++)
		{
			URL url = urls[i];
			// "Expand" playlist files
			if (PlayList.isPlaylistFile(url))
			{
				try
				{
					PlayList newPlayList = PlayList.createFromFile(url, false, false);
					if (this.getLoadedFromURL()==null) this.setLoadedFromURL(url);
					Iterator<PlayListEntry> elementIter = newPlayList.getIterator();
					while (elementIter.hasNext())
					{
						PlayListEntry entry = elementIter.next();
						entry.setSavedInPlaylist(this);
						entries.add(entry);
					}
				}
				catch (IOException ex)
				{
					Log.error("PlayList", ex);
				}
			}
			else
			{
				entries.add(new PlayListEntry(urls[i], this));
			}
		}
		this.current = -1;
		this.repeat = repeat;
		if (shuffle) doShuffle();
	}
	public synchronized void addPlaylistChangedListener(PlaylistChangedListener listener)
	{
		listeners.add(listener);
	}
	public synchronized void removePlaylistChangedListener(PlaylistChangedListener listener)
	{
		listeners.remove(listener);
	}
	public synchronized void fireActiveElementChanged(PlayListEntry oldElement, PlayListEntry newEntry)
	{
		for (int i=0; i<listeners.size(); i++)
		{
			listeners.get(i).activeElementChanged(oldElement, newEntry);
		}
	}
	public synchronized void fireSelectedElementChanged(PlayListEntry oldElement, PlayListEntry newEntry)
	{
		for (int i=0; i<listeners.size(); i++)
		{
			listeners.get(i).selectedElementChanged(oldElement, newEntry);
		}
	}
	/**
	 * @return the repeat
	 * @since 22.11.2013
	 */
	public synchronized boolean isRepeat()
	{
		return repeat;
	}
	/**
	 * @param repeat the repeat to set
	 * @since 22.11.2013
	 */
	public synchronized void setRepeat(boolean repeat)
	{
		this.repeat = repeat;
	}
	/**
	 * @since 08.11.2013
	 */
	public synchronized void doShuffle()
	{
		Random rnd = new Random(System.currentTimeMillis());
		ArrayList<PlayListEntry> newEntries = new ArrayList<PlayListEntry>(size());
		while (!entries.isEmpty())
		{
			newEntries.add(entries.remove(rnd.nextInt(entries.size())));
		}
		entries = newEntries;
		current = -1;
		for (int i=0; i<entries.size(); i++)
		{
			PlayListEntry entry = entries.get(i);
			if (entry.isActive())
			{
				if (current==-1) 
					current = i; 
				else 
					entry.setActive(false);
			}
		}
	}
	private synchronized ArrayList<PlayListEntry> getEntries()
	{
		return entries;
	}
	public synchronized PlayListEntry[] getAllEntries()
	{
		return entries.toArray(new PlayListEntry[entries.size()]);
	}
	/**
	 * Retrieve the current playlist entry
	 * if no (more) entries are set this will return null 
	 * @since 03.12.2006
	 * @return
	 */
	public synchronized PlayListEntry getCurrentEntry()
	{
		if (current>=0 && current<size()) return entries.get(current);
		return null;
	}
	/**
	 * @param index
	 * @return
	 * @since 23.03.2013
	 */
	public synchronized PlayListEntry getEntry(int index)
	{
		if (index>=0 && index<size()) return entries.get(index);
		return null;
	}
	/**
	 * @since 30.01.2013
	 */
	private synchronized void activateCurrentEntry()
	{
		PlayListEntry current = getCurrentEntry();
		if (current!=null) current.setActive(true);
	}
	/**
	 * @since 30.01.2013
	 */
	private synchronized void deactivateCurrentEntry()
	{
		PlayListEntry current = getCurrentEntry();
		if (current!=null) current.setActive(false);
	}
	/**
	 * @since 12.02.2013
	 * @param index
	 * @return
	 */
	public synchronized boolean setCurrentElement(int index)
	{
		if (index>=0 && index<size() && index!=current)
		{
			PlayListEntry oldEntry = getCurrentEntry();
			deactivateCurrentEntry();
			current = index;
			activateCurrentEntry();
			fireActiveElementChanged(oldEntry, getCurrentEntry());
			return true;
		}
		return false;
	}
	/**
	 * set the current Element by timeIndex
	 * This Method refers to the starttimeIndex and Duration of each entry
	 * The index is searched in the current entrie's file
	 * @param timeIndex
	 * @since 15.02.2013
	 */
	public synchronized void setCurrentElementByTimeIndex(final long timeIndex)
	{
		if (current==-1 || entries==null) return;
		int currentIndex = current;
		final int end = entries.size()-1;
		URL file = null;
		do
		{
			final PlayListEntry currentEntry = entries.get(currentIndex);
			if (file!=null && !Helpers.isEqualURL(file, currentEntry.getFile())) return;
			file = currentEntry.getFile();
			final long startIndex = currentEntry.getTimeIndex();
			if (startIndex > timeIndex)
			{
				currentIndex--;
			}
			else
			{
				final long endIndex = startIndex + currentEntry.getDuration();
				if (endIndex < timeIndex)
				{
					currentIndex++;
				}
				else
					break;
			}
		}
		while (currentIndex>=0 && currentIndex<=end);
		if (currentIndex != current) 
			setCurrentElement(currentIndex);
	}
	/**
	 * @since 03.04.2013
	 * @return
	 */
	public synchronized PlayListEntry [] getSelectedEntries()
	{
		if (entries.size()>0)
		{
			ArrayList<PlayListEntry> selected = new ArrayList<PlayListEntry>(entries.size());
			for (int i=0; i<entries.size(); i++)
			{
				PlayListEntry entry = entries.get(i);
				if (entry.isSelected()) selected.add(entry);
			}
			if (selected.size()>0)
				return selected.toArray(new PlayListEntry[selected.size()]);
		}
		return null;
	}
	/**
	 * @since 03.04.2013
	 * @param index
	 */
	public synchronized void addSelectedElement(int index)
	{
		PlayListEntry entry = entries.get(index);
		entry.setSelected(true);
		fireSelectedElementChanged(null, entry);
	}
	/**
	 * @since 03.04.2013
	 * @param index
	 */
	public synchronized void toggleSelectedElement(int index)
	{
		PlayListEntry entry = entries.get(index);
		entry.setSelected(!entry.isSelected());
		fireSelectedElementChanged(null, entry);
	}
	/**
	 * @since 12.02.2013
	 * @param index (-1 means deselect any!)
	 * @return
	 */
	public synchronized void setSelectedElement(int index)
	{
		setSelectedElements(index, index);
	}
	/**
	 * @since 03.04.2013
	 * @param fromIndex
	 * @param toIndex
	 */
	public synchronized void setSelectedElements(int fromIndex, int toIndex)
	{
		if (fromIndex>toIndex)
		{
			int swap = fromIndex; fromIndex = toIndex; toIndex = swap;
		}
		for (int i=0; i<entries.size(); i++)
		{
			PlayListEntry entry = entries.get(i);
			if (entry.isSelected() && (i<fromIndex || i>toIndex))
			{
				entry.setSelected(false);
				fireSelectedElementChanged(entry, null);
			}
			else
			if (!entry.isSelected() && (i>=fromIndex && i<=toIndex))
			{
				entry.setSelected(true);
				fireSelectedElementChanged(null, entry);
			}
		}
	}
	/**
	 * set index to next element or return false, if
	 * end is reached.
	 * The first call of "next" steps to the first element
	 * @since 03.12.2006
	 */
	public synchronized boolean next()
	{
		if (hasNext())
		{
			PlayListEntry oldEntry = getCurrentEntry();
			deactivateCurrentEntry();
			if (current >= size()-1) current = 0; else current ++;
			activateCurrentEntry();
			fireActiveElementChanged(oldEntry, getCurrentEntry());
			return true;
		}
		else 
			return false;
	}
	/**
	 * set index to prev element and wrap around
	 * @since 03.12.2006
	 */
	public synchronized boolean previous()
	{
		if (hasPrevious())
		{
			PlayListEntry oldEntry = getCurrentEntry();
			deactivateCurrentEntry();
			current--;
			activateCurrentEntry();
			fireActiveElementChanged(oldEntry, getCurrentEntry());
			return true;
		}
		else
			return false;
	}
	/**
	 * @since 14.09.2013
	 * @return
	 */
	public synchronized boolean hasNext()
	{
		if ((current >= size()-1) && !repeat) 
			return false;
		else
			return true;
	}
	/**
	 * @since 14.09.2013
	 * @return
	 */
	public synchronized boolean hasPrevious()
	{
		if (current <= 0) 
			return false;
		else
			return true;
	}
	/**
	 * @return
	 * @since 08.03.2013
	 */
	public synchronized int size()
	{
		return entries.size(); 
	}
	public synchronized int indexOf(PlayListEntry entry)
	{
		return entries.indexOf(entry);
	}
	public synchronized void addAllAt(int indexAt, PlayList newPlaylist)
	{
		ArrayList<PlayListEntry> newEntries = newPlaylist.getEntries();
		int size = newEntries.size();
		for (int i=0; i<size; i++) newEntries.get(i).setSavedInPlaylist(this);
		if (indexAt>entries.size()) indexAt = entries.size();
		entries.addAll(indexAt, newEntries);
		if (current >= indexAt) current += size;
	}
	public synchronized void addEntry(PlayListEntry newPlaylistEntry)
	{
		newPlaylistEntry.setSavedInPlaylist(this);
		entries.add(newPlaylistEntry);
	}
	public synchronized void move(int fromIndex, int toIndex)
	{
		PlayListEntry mover = entries.remove(fromIndex);
		entries.add(toIndex, mover);
		
		if (current == fromIndex) current = toIndex;
		else
		if (current == toIndex) current++;
	}
	public synchronized void remove(int fromIndex)
	{
		/*PlayListEntry mover = */entries.remove(fromIndex);
		if (current>fromIndex && current>-1) current--;
		else
		if (current==fromIndex) current=-1;
	}
	/**
	 * @return the loadedFromURL
	 */
	public URL getLoadedFromURL()
	{
		return loadedFromURL;
	}
	/**
	 * @param loadedFromURL the loadedFromURL to set
	 */
	private void setLoadedFromURL(URL loadedFromURL)
	{
		this.loadedFromURL = loadedFromURL;
	}
	/**
	 * Saves the current playlist to a File
	 * @since 03.12.2006
	 * @param f
	 * @throws IOException
	 */
	public synchronized void savePlayListTo(File f) throws IOException
	{
		PrintStream ps = null;

		try
		{
			String prefix = f.getAbsolutePath();
			String lowCasePrefix = prefix.toLowerCase();
			boolean writePLSFile = (lowCasePrefix.endsWith("pls")); 
			boolean writeCueSheet = (lowCasePrefix.endsWith("cue"));
			// if none of the above, write m3u (and add extension if missing)
			if (!writePLSFile && !writeCueSheet && !lowCasePrefix.endsWith("m3u")) 
				f = new File(prefix+=".M3U");
			
			if (f.exists())
			{
				boolean ok = f.delete();
				if (ok) ok = f.createNewFile();
				if (!ok) throw new IOException("Could not overwrite file " + prefix);
			}
			lowCasePrefix = (prefix = prefix.substring(0, prefix.lastIndexOf(File.separatorChar)+1)).toLowerCase();
		
		
			
			if (!writeCueSheet)
			{
				ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(f)));
				ps.println((writePLSFile)?"[playlist]":"#EXTM3U");
			}
			
			
					
					
		}
		finally
		{
			if (ps!=null) ps.close();
		}
	}
	/**
	 * Saves the current playlist to a File
	 * @since 03.12.2006
	 * @param fileName
	 * @throws IOException
	 */
	public synchronized void savePlayListTo(String fileName) throws IOException
	{
		savePlayListTo(new File(fileName));
	}
	/**
	 * @param fileNames
	 * @return
	 * @since 23.03.2013
	 */
	private static URL[] generateURLListFromFileNames(String [] fileNames)
	{
		ArrayList<File> files = new ArrayList<File>(fileNames.length);
		for (int i=0; i<fileNames.length; i++)
		{
			files.add(new File(fileNames[i]));
		}
		return generateURLListFromFiles(files.toArray(new File[files.size()]));
	}
	/**
	 * @param files
	 * @return
	 * @since 23.03.2013
	 */
	private static URL[] generateURLListFromFiles(File [] files)
	{
		ArrayList<URL> urls = new ArrayList<URL>(files.length);
		for (int i=0; i<files.length; i++)
		{
			URL url = Helpers.createURLfromFile(files[i]);
			if (url!=null) urls.add(url);
		}
		return urls.toArray(new URL[urls.size()]);
	}
	/**
	 * Checks first the File exists method - will try URL than
	 * @since 01.05.2013
	 * @param url
	 * @return
	 */
	/**
	 * @since 14.09.2013
	 * @param playListURL
	 * @param br
	 * @return
	 * @throws IOException
	 */
	private static PlayList readPLSFile(final URL playListURL, final BufferedReader br, final boolean shuffle, final boolean repeat) throws IOException
	{
		String line;
		String [] songName = null;
		String [] duration = null;
		URL [] file = null;
		while ((line=br.readLine())!=null)
		{
			if (line.length()!=0)
			{
				line = line.toLowerCase();
				int equalOp = line.indexOf('=');
				String value = line.substring(equalOp+1);
				if (line.startsWith("numberofentries"))
				{
					int numOfEntries = Integer.parseInt(value);
					songName = new String[numOfEntries];
					duration = new String[numOfEntries];
					file = new URL[numOfEntries];
				}
				else
				if (line.startsWith("file") && file!=null)
				{
					int index = Integer.parseInt(line.substring(4, equalOp)) - 1;
					file[index] = Helpers.createAbsolutePathForFile(playListURL, value);
				}
				else
				if (line.startsWith("title") && songName!=null)
				{
					int index = Integer.parseInt(line.substring(5, equalOp)) - 1;
					songName[index] = value;
				}	
				else
				if (line.startsWith("length") && duration!=null)
				{
					if (!value.equals("-1"))
					{
						int index = Integer.parseInt(line.substring(6, equalOp)) - 1;
						duration[index] = value;
					}
				}	
			}
		}
		
		ArrayList<PlayListEntry> entries = new ArrayList<PlayListEntry>();
		if (file!=null && songName!=null && duration!=null)
		{
			for (int i=0; i<file.length; i++)
			{
				PlayListEntry entry = new PlayListEntry(file[i], null);
				if (songName[i]!=null && songName[i].length()!=0) entry.setSongName(songName[i]);
				if (duration[i]!=null && duration[i].length()!=0)
				{
					int seconds = Integer.parseInt(duration[i]);
					entry.setDuration(seconds*1000L);
				}
				entries.add(entry);
			}
		}
		
		if (entries.size()>0)
		{
			PlayList playList = new PlayList(entries, shuffle, repeat);
			playList.setLoadedFromURL(playListURL);
			return playList;
		}
		else
			return null;
	}
	/**
	 * @since 14.09.2013
	 * @param playListURL
	 * @param br
	 * @return
	 * @throws IOException
	 */
	private static PlayList readM3UFile(final URL playListURL, final BufferedReader br, final boolean shuffle, final boolean repeat) throws IOException
	{
		ArrayList<PlayListEntry> entries = new ArrayList<PlayListEntry>();
		String line;
		String songName = null;
		String duration = null;
		while ((line=br.readLine())!=null)
		{
			if (line.length()!=0)
			{
				if (line.startsWith("#EXTM3U")) continue; // should be consumed!
				if (line.startsWith("#EXTINF:"))
				{
					int comma = line.indexOf(',');
					if (comma>-1)
					{
						duration = line.substring(8, comma);
						songName = line.substring(comma+1);
					}
				}
				else
				{
					PlayListEntry entry;
					URL normalizedEntry = Helpers.createAbsolutePathForFile(playListURL, line);
					if (normalizedEntry==null) normalizedEntry = Helpers.createURLfromString(line);
					entries.add(entry = new PlayListEntry(normalizedEntry, null));
					
					if (songName!=null && songName.length()!=0) entry.setSongName(songName);
					if (duration!=null && duration.length()!=0)
					{
						int seconds = Integer.parseInt(duration);
						entry.setDuration(seconds*1000L);
					}
					songName = duration = null;
				}
			}
		}
		
		if (entries.size()>0)
		{
			PlayList playList = new PlayList(entries, shuffle, repeat);
			playList.setLoadedFromURL(playListURL);
			return playList;
		}
		else
			return null;
	}
	/**
	 * @since 02.01.2013
	 * @param playListURL
	 * @param shuffle
	 * @return
	 * @throws IOException
	 */
	private static PlayList readZIPFile(final URL playListURL, final boolean shuffle, final boolean repeat) throws IOException
	{
		ArrayList<File> entries = new ArrayList<File>();
		ZipInputStream input = null;
		try
		{
			File zipFile = new File(playListURL.toURI());
			input = new ZipInputStream(playListURL.openStream());
			ZipEntry entry;
			while ((entry = input.getNextEntry())!=null)
			{
				if (entry.isDirectory()) continue;
				entries.add(new File(zipFile.getCanonicalPath() + File.separatorChar + entry.getName()));
			}
		}
		catch (Throwable ex)
		{
		}
		finally
		{
			if (input!=null) try { input.close(); } catch (IOException e) { Log.error("IGNORED", e); }
		}
		if (entries.size()>0)
			return new PlayList(entries.toArray(new File[entries.size()]), shuffle, repeat);
		else
			return null;
	}
	/**
	 * @since 04.03.2013
	 * @param playListURL
	 * @param shuffle
	 * @param repeat
	 * @return
	 * @throws IOException
	 */
	private static PlayList readCUEFile(final URL playListURL, final boolean shuffle, final boolean repeat) throws IOException
	{
				return null;
	}
	/**
	 * will create a playlists with the given files
	 * if a file represents a playlist, it will get expanded
	 * @param url
	 * @param shuffle
	 * @return
	 * @since 28.04.2013
	 */
	public static PlayList createNewListWithFiles(URL[] url, boolean shuffle, boolean repeat)
	{
		return new PlayList(url, shuffle, repeat);
	}
	/**
	 * will create a playlists with the given file
	 * if the file represents a playlist, it will get expanded
	 * @param url
	 * @param shuffle
	 * @return
	 * @since 28.04.2013
	 */
	public static PlayList createNewListWithFile(URL url, boolean shuffle, boolean repeat)
	{
		return createNewListWithFiles(new URL[] { url }, shuffle, repeat);
	}
	/**
	 * will create a playlists with the given files
	 * if a file represents a playlist, it will get expanded
	 * @param file
	 * @param shuffle
	 * @return
	 * @since 28.04.2013
	 */
	public static PlayList createNewListWithFiles(File[] file, boolean shuffle, boolean repeat)
	{
		return new PlayList(file, shuffle, repeat);
	}
	/**
	 * will create a playlists with the given file
	 * if the file represents a playlist, it will get expanded
	 * @param file
	 * @param shuffle
	 * @return
	 * @since 28.04.2013
	 */
	public static PlayList createNewListWithFile(File file, boolean shuffle, boolean repeat)
	{
		return createNewListWithFiles(new File[] { file }, shuffle, repeat);
	}
	/**
	 * reads a playlist from a file
	 * @since 03.12.2006
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static PlayList createFromFile(URL url, boolean shuffle, boolean repeat) throws IOException
	{
		PlayList result = null;

		final String fileName = url.getPath().toLowerCase();
		if (fileName.endsWith(".zip"))
		{
			result = readZIPFile(url, shuffle, repeat);
		}
		else
		if (fileName.endsWith(".cue"))
		{
			result = readCUEFile(url, shuffle, repeat);
		}
		else
		if (isPlaylistFile(url))
		{
			BufferedReader br = null;
			try
			{
				br = new BufferedReader(new InputStreamReader(url.openStream()));
				String line=br.readLine();
				if (line!=null)
				{
					line = line.trim();
					if (line.equalsIgnoreCase("[playlist]"))
						result = readPLSFile(url, br, shuffle, repeat);
					else
					if (line.equalsIgnoreCase("#EXTM3U"))
						result = readM3UFile(url, br, shuffle, repeat);
					else
						throw new IllegalArgumentException("Unsupported Playlist File: " + url.toString());
				}
			}
			finally
			{
				if (br!=null) try { br.close(); } catch (IOException ex) { Log.error("IGNORED", ex); }
			}
		}
		else
		{
			result = createNewListWithFile(url, shuffle, repeat);
		}
		return result;
	}
	/**
	 * reads a Playlist from a file
	 * @since 03.12.2006
	 * @param f
	 * @param shuffle
	 * @return
	 * @throws IOException
	 */
	public static PlayList createFromFile(File f, boolean shuffle, boolean repeat) throws IOException
	{
		return createFromFile(f.toURI().toURL(), shuffle, repeat);
	}
	/**
	 * reads a Playlist from a file
	 * @since 03.12.2006
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static PlayList createFromFile(String fileName, boolean shuffle, boolean repeat) throws IOException
	{
		return PlayList.createFromFile(new File(fileName), shuffle, repeat);
	}
	/**
	 * @since 02.01.2013
	 * @param fileURL
	 * @return
	 */
	public static boolean isPlaylistFile(URL fileURL)
	{
		return isPlaylistFile(fileURL.getPath().toLowerCase());
	}
	/**
	 * @since 02.01.2013
	 * @param fileName
	 * @return
	 */
	public static boolean isPlaylistFile(String fileName)
	{
		for (int i=0; i<SUPPORTEDPLAYLISTS.length; i++)
		{
			if (fileName.endsWith('.'+SUPPORTEDPLAYLISTS[i])) return true;
		}
		return false;
	}
	/**
	 * @since 02.01.2013
	 * @return
	 */
	public static String getFileChooserDescription()
	{
		StringBuilder sb = new StringBuilder("Playlist (");
		for (int i=0; i<SUPPORTEDPLAYLISTS.length; i++)
		{
			sb.append("*.").append(SUPPORTEDPLAYLISTS[i]);
			if (i<SUPPORTEDPLAYLISTS.length-1) sb.append(", ");
		}
		sb.append(')');
		return sb.toString();
	}
	/**
	 * @since 03.04.2013
	 * @return
	 */
	public static String getFileChooserSaveDescription()
	{
		StringBuilder sb = new StringBuilder("Playlist (");
		for (int i=0; i<SUPPORTEDSAVELISTS.length; i++)
		{
			sb.append("*.").append(SUPPORTEDSAVELISTS[i]);
			if (i<SUPPORTEDSAVELISTS.length-1) sb.append(", ");
		}
		sb.append(')');
		return sb.toString();
	}
	/**
	 * @since 02.01.2013
	 * @return
	 */
	public synchronized Iterator<PlayListEntry> getIterator()
	{
		return entries.iterator();
	}
	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		int lastIndex = size()-1;
		for (int i=0; i<=lastIndex; i++)
		{
			result.append('[').append(entries.get(i)).append(']');
			if (i<lastIndex) result.append(',');
		}
		return result.toString();
	}
}
