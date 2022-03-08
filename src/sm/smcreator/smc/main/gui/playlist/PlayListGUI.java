/*
 * @(#) PlayListGUI.java
 *
 * Created on 30.01.2013 by Anil Kishan
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

package sm.smcreator.smc.main.gui.playlist;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLDocument;

import sm.smcreator.smc.main.gui.tools.FileChooserResult;
import sm.smcreator.smc.main.gui.tools.PlaylistDropListener;
import sm.smcreator.smc.main.gui.tools.PlaylistDropListenerCallBack;
import sm.smcreator.smc.main.playlist.PlayList;
import sm.smcreator.smc.main.playlist.PlayListEntry;
import sm.smcreator.smc.main.playlist.PlaylistChangedListener;
import sm.smcreator.smc.multimedia.MultimediaContainerManager;
import sm.smcreator.smc.system.Helpers;
import sm.smcreator.smc.system.Log;


public class PlayListGUI extends JPanel implements PlaylistChangedListener, PlaylistDropListenerCallBack
{
	private static final long serialVersionUID = -7914306014081401144L;
	
	// lines to show more when scrolling 
	private static final int PLUS_LINES_VISABLE = 2;

    private JFrame parentFrame = null;
	private PlayList playList;
	private PlayListEntry lastClickedEntry; // This entry is set if the mouse is pressed in an selected Entry
    
	private JCheckBox repeatCheckBox = null;
	private JScrollPane scrollPane = null;
    private JTextPane textArea = null;
    private JPopupMenu playListPopUp = null;
 	private JMenuItem popUpEntryDeleteFromList = null;
 	private JMenuItem popUpEntryCropFromList = null;
 	private JMenuItem popUpEntryRefreshEntry = null;
 	private JMenuItem popUpEntryEditEntry = null;
 	private JMenuItem popUpEntrySaveList = null;
 	private JMenuItem popUpEntryShuffleList = null;
 	
 	private EditPlaylistEntry editPlayListEntryDialog = null;
  	
    private ArrayList<DropTarget> dropTargetList;

    private PlayListUpdateThread playlistUpdateThread;
    
    private String unmarkColorBackground;
    private String unmarkColorForeground;
    private String markColorBackground;
    private String markColorForeground;
    
	private ArrayList<PlaylistGUIChangeListener> listeners = new ArrayList<PlaylistGUIChangeListener>();

	private final static class InvisiableCaret implements Caret
	{
		private Point magicCaretPosition;
		private int dot;
		private int rate;
		
		public InvisiableCaret()
		{
			super();
		}
		public void setVisible(boolean v) { /*NOOP*/ }
		public void setSelectionVisible(boolean v) { /*NOOP*/ }
		public boolean isVisible() { return false; }
		public boolean isSelectionVisible() {  return false; }
		public void setMagicCaretPosition(Point p) { magicCaretPosition = p; }
		public Point getMagicCaretPosition() { return magicCaretPosition; }
		public void setDot(int dot) { this.dot = dot; }
		public int getDot() { return dot; }
		public void setBlinkRate(int rate) { this.rate = rate; }
		public int getBlinkRate() { return rate; }
		public void removeChangeListener(ChangeListener l) { /*NOOP*/ }
		public void addChangeListener(ChangeListener l) { /*NOOP*/ }
		public void paint(Graphics g) { /*NOOP*/ }
		public void moveDot(int dot) { /*NOOP*/ }
		public void install(JTextComponent c) { /*NOOP*/ }
		public void deinstall(JTextComponent c) { /*NOOP*/ }
		public int getMark() { return dot; }
	}
	private final static class PlayListUpdateThread extends Thread implements Serializable
	{
		private static final long serialVersionUID = -8105723830268691249L;
		
		private PlayListGUI parent;
		private volatile boolean stopIt;
		private volatile boolean isStopped;
		private volatile boolean finished;
		
		public PlayListUpdateThread(PlayListGUI parent)
		{
			super();
			this.setName("PlayListUpdateThread::" + this.getClass().getName());
			this.setDaemon(true);
			this.setPriority(Thread.MIN_PRIORITY);
			this.parent = parent;
			this.isStopped = true;
			this.stopIt = false;
			this.finished = false;
		}
		public boolean isStopped()
		{
			return isStopped;
		}
		public void halt()
		{
			if (!isStopped)
			{
				stopIt = true;
				this.interrupt();
				while (!isStopped()) try { PlayListUpdateThread.sleep(1L); } catch (InterruptedException ex) { /*NOOP*/ }
				stopIt = false;
			}
		}
//		public void finish()
//		{
//			halt();
//			finished = true;
//		}
		public void restart()
		{
			stopIt = false;
			isStopped = false;
		}
		/**
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run()
		{
			while (!finished)
			{
				while (isStopped() && !finished) try { PlayListUpdateThread.sleep(1000L); } catch (InterruptedException ex) { /*NOOP*/ }
				try
				{
					if (parent.playList!=null)
					{
						int size = parent.playList.size();
						for (int index = 0; (index<size && !stopIt); index++) 
						{
							parent.updateLine(index);
							try { PlayListUpdateThread.sleep(250L); } catch (InterruptedException ex) { /*NOOP*/ }
						}
					}
				}
				finally
				{
					this.stopIt = false;
					this.isStopped = true;
				}
			}
		}
	}
	/**
	 * Constructor for PlayListGUI
	 */
	public PlayListGUI(JFrame parentFrame)
	{
		super();
		this.parentFrame = parentFrame;
		initialize();
	}
	private static String getHTMLColorString(Color color)
	{
		String htmlColor = Integer.toHexString(color.getRGB());
		if (htmlColor.length()>6) htmlColor = htmlColor.substring(htmlColor.length() - 6);
		return htmlColor;
	}
	private void initialize()
	{
		setName("PlayList");
		setLayout(new java.awt.GridBagLayout());
		add(getScrollPane()		, Helpers.getGridBagConstraint(0, 0, 1, 0, java.awt.GridBagConstraints.BOTH, java.awt.GridBagConstraints.WEST, 1.0, 1.0));
		add(getRepeatCheckBox()	, Helpers.getGridBagConstraint(0, 1, 1, 0, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.EAST, 0.0, 0.0));
		
		dropTargetList = new ArrayList<DropTarget>();
	    PlaylistDropListener myListener = new PlaylistDropListener(this);
	    Helpers.registerDropListener(dropTargetList, getPlaylistTextArea(), myListener);
		
		unmarkColorBackground = getHTMLColorString(getPlaylistTextArea().getBackground());
		unmarkColorForeground = getHTMLColorString(getPlaylistTextArea().getForeground());
		markColorBackground = getHTMLColorString(getPlaylistTextArea().getSelectionColor());
		markColorForeground = getHTMLColorString(getPlaylistTextArea().getSelectedTextColor());
		playlistUpdateThread = new PlayListUpdateThread(this);
		playlistUpdateThread.start();
	}
	/**
	 * @return
	 * @since 22.11.2013
	 */
	private JCheckBox getRepeatCheckBox()
	{
		if (repeatCheckBox == null)
		{
			repeatCheckBox = new JCheckBox();
			repeatCheckBox.setName("repeatCombobox");
			repeatCheckBox.setText("repeat playlist");
			repeatCheckBox.setFont(Helpers.DIALOG_FONT);
			repeatCheckBox.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED || e.getStateChange()==ItemEvent.DESELECTED)
					{
						if (playList!=null)
							playList.setRepeat(repeatCheckBox.isSelected()); 
						else 
							repeatCheckBox.setSelected(false);
						firePlaylistChanged();
					}
				}
			});
		}
		return repeatCheckBox;
	}
	private javax.swing.JScrollPane getScrollPane()
	{
		if (scrollPane == null)
		{
			scrollPane = new javax.swing.JScrollPane();
			scrollPane.setName("scrollPane_TextField");
			scrollPane.setViewportView(getPlaylistTextArea());
		}
		return scrollPane;
	}
	private JTextPane getPlaylistTextArea()
	{
		if (textArea==null)
		{
			textArea = new JTextPane(new HTMLDocument());
			textArea.setContentType("text/html");
			textArea.setName("textArea");
			textArea.setEditable(false);
			textArea.setCaret(new InvisiableCaret());
			textArea.addKeyListener(new KeyListener()
			{
				public void keyTyped(KeyEvent e) {}
				public void keyReleased(KeyEvent e) {}
				public void keyPressed(KeyEvent e)
				{
					if (e.isConsumed() || playList==null) return;
					
					if (e.isControlDown()) // All CTRL-Combinations:
					{
						switch (e.getKeyCode())
						{
							case KeyEvent.VK_S: doSavePlayList(); e.consume(); break;
							case KeyEvent.VK_E: doEditSelectedEntry(); e.consume(); break;
							case KeyEvent.VK_U: doUpdateSelectedEntryFromList(); e.consume(); break;
							case KeyEvent.VK_R: doShufflePlayList(); e.consume(); break;
							case KeyEvent.VK_A: doSelectAll(); e.consume(); break;
							case KeyEvent.VK_DELETE: doCropSelectedEntryFromList(); e.consume(); break;
						}
					}
					else
					if (e.isShiftDown()) // All SHIFT-Combinations
					{
						switch (e.getKeyCode())
						{
							case KeyEvent.VK_UP: doChangeSelectionInList(-1); e.consume(); break;
							case KeyEvent.VK_DOWN: doChangeSelectionInList(+1); e.consume(); break;
						}
					}
					else
					if (e.isAltDown()) // All ALT-Combinations
					{
						switch (e.getKeyCode())
						{
							case KeyEvent.VK_UP: doMoveSelectedEntriesInList(-1); e.consume(); break;
							case KeyEvent.VK_DOWN: doMoveSelectedEntriesInList(+1); e.consume(); break;

						}
					}
					else
					if (!e.isAltGraphDown() && !e.isMetaDown())
					{
						switch (e.getKeyCode())
						{
							case KeyEvent.VK_DELETE: doDeleteSelectedEntryFromList(); e.consume(); break;
							case KeyEvent.VK_ESCAPE: playList.setSelectedElement(-1); e.consume(); break;
							case KeyEvent.VK_HOME: playList.setSelectedElement(0); doMakeIndexVisible(0); e.consume(); break;
							case KeyEvent.VK_END: int ende = playList.size() - 1; playList.setSelectedElement(ende); doMakeIndexVisible(ende); e.consume(); break;
							case KeyEvent.VK_UP: doMoveSelectionInList(-1); e.consume(); break;
							case KeyEvent.VK_DOWN: doMoveSelectionInList(+1); e.consume(); break;
							case KeyEvent.VK_PAGE_UP: doMoveSelectionInList(- (getMaxVisableRows()-1)); e.consume(); break;
							case KeyEvent.VK_PAGE_DOWN: doMoveSelectionInList(getMaxVisableRows()-1); e.consume(); break;
							case KeyEvent.VK_ENTER: doPlaySelectedPiece(); e.consume(); break;
						}
					}
				}
			});
			textArea.addMouseMotionListener(new MouseMotionListener()
			{
				public void mouseMoved(MouseEvent e) {}
				public void mouseDragged(MouseEvent e)
				{
					if (e.isConsumed() || playList==null) return;
					
					if (SwingUtilities.isLeftMouseButton(e))
					{
						int index = getSelectedIndexFromPoint(e.getPoint(), false);
						if (index==-1) return;
						
						PlayListEntry entry = playList.getEntry(index);
						if (entry.isSelected() && lastClickedEntry==null) 
							lastClickedEntry = entry;
						else
						{	
							if (lastClickedEntry!=null)
							{
								int moveBy = index - lastClickedEntry.getIndexInPlaylist();
								if (moveBy == 0) return;
								
								doMoveSelectedEntriesInList(moveBy);
							}
						}
						e.consume();
					}
				}
			});
			textArea.addMouseListener(new MouseAdapter()
			{
				public void mousePressed(MouseEvent e) 
				{
					if (e.isConsumed() || playList==null) return;

					lastClickedEntry = null;
					int index = getSelectedIndexFromPoint(e.getPoint(), false);
					if (index!=-1)
					{
						PlayListEntry entry = playList.getEntry(index);
						if (e.isShiftDown()) // Select multiple (area)
						{
							PlayListEntry [] alreadySelectedEntries = playList.getSelectedEntries();
							if (alreadySelectedEntries == null) 
								playList.setSelectedElement(index);
							else
							{
								int fromIndex = alreadySelectedEntries[0].getIndexInPlaylist();
								playList.setSelectedElements(fromIndex, index);
							}
							lastClickedEntry = entry;
						}
						else
						if (e.isControlDown()) // select multiple (individual)
						{
							playList.toggleSelectedElement(index);
							lastClickedEntry = entry;
						}
						else
						if (!entry.isSelected())
						{
							playList.setSelectedElement(index);
							lastClickedEntry = entry;
						}
					}
					else
					{
						playList.setSelectedElement(index);
					}

					if (SwingUtilities.isRightMouseButton(e))
			        {
						getPopup().show(textArea, e.getX(), e.getY());
			        }
					e.consume();
				}
				public void mouseClicked(MouseEvent e)
				{
					if (e.isConsumed() || playList==null) return;
					
					int index = getSelectedIndexFromPoint(e.getPoint(), false);
					if (index!=-1)
					{
						if (SwingUtilities.isLeftMouseButton(e))
						{
							PlayListEntry entry = playList.getEntry(index);
							if (e.getClickCount()>1)
							{
								doPlaySelectedPiece();
							}
							else
							{
								if (entry!=null && lastClickedEntry==null)
									playList.setSelectedElement(index);
							}
							e.consume();
						}
					}
				}
			});
		}
		return textArea;
	}
    private JPopupMenu getPopup()
    {
    	if (playListPopUp==null)
    	{
	        playListPopUp = new javax.swing.JPopupMenu();
	        playListPopUp.setName("playListPopUp");
	        playListPopUp.add(getPopUpEntryDeleteFromList());
	        playListPopUp.add(getPopUpEntryCropFromList());
	        playListPopUp.add(new javax.swing.JSeparator());
	        playListPopUp.add(getPopUpEntryRefreshEntry());
	        playListPopUp.add(getPopUpEntryEditEntry());
	        playListPopUp.add(getPopUpEntryShuffleList());
	        playListPopUp.add(new javax.swing.JSeparator());
	        playListPopUp.add(getPopUpEntrySaveList());
    	}
    	boolean noEmptyList = (playList!=null && playList.size()>0);
    	PlayListEntry [] selectedEntries = (noEmptyList)?playList.getSelectedEntries():null;
    	boolean elementSpecificEntriesEnabled = (noEmptyList && selectedEntries!=null);
   		getPopUpEntryDeleteFromList().setEnabled(elementSpecificEntriesEnabled);
   		getPopUpEntryRefreshEntry().setEnabled(elementSpecificEntriesEnabled);
   		getPopUpEntryEditEntry().setEnabled(elementSpecificEntriesEnabled && selectedEntries!=null && selectedEntries.length==1);
    	getPopUpEntrySaveList().setEnabled(noEmptyList);
    	getPopUpEntryShuffleList().setEnabled(noEmptyList);
        return playListPopUp;
    }
    private JMenuItem getPopUpEntryDeleteFromList()
    {
        if (popUpEntryDeleteFromList == null)
        {
        	popUpEntryDeleteFromList = new javax.swing.JMenuItem();
        	popUpEntryDeleteFromList.setName("JPopUpMenu_DeleteFromList");
        	popUpEntryDeleteFromList.setText("<del> delete entry from list");
        	popUpEntryDeleteFromList.addActionListener(
                new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						doDeleteSelectedEntryFromList();
					}
				});
        }
        return popUpEntryDeleteFromList;
    }
    private JMenuItem getPopUpEntryCropFromList()
    {
        if (popUpEntryCropFromList == null)
        {
        	popUpEntryCropFromList = new javax.swing.JMenuItem();
        	popUpEntryCropFromList.setName("JPopUpMenu_CropFromList");
        	popUpEntryCropFromList.setText("<ctrl-del> crop entry from list");
        	popUpEntryCropFromList.addActionListener(
                new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						doCropSelectedEntryFromList();
					}
				});
        }
        return popUpEntryCropFromList;
    }
    private JMenuItem getPopUpEntryRefreshEntry()
    {
        if (popUpEntryRefreshEntry == null)
        {
        	popUpEntryRefreshEntry = new javax.swing.JMenuItem();
        	popUpEntryRefreshEntry.setName("JPopUpMenu_RefreshEntry");
        	popUpEntryRefreshEntry.setText("<ctrl-u> Refresh entry");
        	popUpEntryRefreshEntry.addActionListener(
                new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						doUpdateSelectedEntryFromList();
					}
				});
        }
        return popUpEntryRefreshEntry;
    }
    private JMenuItem getPopUpEntryEditEntry()
    {
        if (popUpEntryEditEntry == null)
        {
        	popUpEntryEditEntry = new javax.swing.JMenuItem();
        	popUpEntryEditEntry.setName("JPopUpMenu_EditEntry");
        	popUpEntryEditEntry.setText("<ctrl-e> Edit entry");
        	popUpEntryEditEntry.addActionListener(
                new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						doEditSelectedEntry();
					}
				});
        }
        return popUpEntryEditEntry;
    }
    private JMenuItem getPopUpEntrySaveList()
    {
        if (popUpEntrySaveList == null)
        {
        	popUpEntrySaveList = new javax.swing.JMenuItem();
        	popUpEntrySaveList.setName("JPopUpMenu_SaveList");
        	popUpEntrySaveList.setText("<ctrl-s> save playlist to");
        	popUpEntrySaveList.addActionListener(
                new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						doSavePlayList();
					}
				});
        }
        return popUpEntrySaveList;
    }
    private JMenuItem getPopUpEntryShuffleList()
    {
        if (popUpEntryShuffleList == null)
        {
        	popUpEntryShuffleList = new javax.swing.JMenuItem();
        	popUpEntryShuffleList.setName("JPopUpMenu_ShuffleList");
        	popUpEntryShuffleList.setText("<ctrl-r> shuffle list");
        	popUpEntryShuffleList.addActionListener(
                new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						doShufflePlayList();
					}
				});
        }
        return popUpEntryShuffleList;
    }
	private EditPlaylistEntry getEditDialog()
	{
		if (editPlayListEntryDialog == null)
		{
			editPlayListEntryDialog = new EditPlaylistEntry(parentFrame, true);
		}
		else
			editPlayListEntryDialog.setLocation(Helpers.getFrameCenteredLocation(editPlayListEntryDialog, parentFrame));
		return editPlayListEntryDialog;
	}
    private void doDeleteSelectedEntryFromList()
    {
		PlayListEntry [] selectedEntries = playList.getSelectedEntries();
		if (selectedEntries!=null)
		{
			playlistUpdateThread.halt();
			try
			{
				for (int i=0; i<selectedEntries.length; i++)
				{
					playList.remove(selectedEntries[i].getIndexInPlaylist());
				}
				createList(getFirstVisableIndex());
				firePlaylistChanged();
			}
			finally
			{
				playlistUpdateThread.restart();
			}
		}
    }
    private void doCropSelectedEntryFromList()
    {
		PlayListEntry [] allEntries = playList.getAllEntries();
		if (allEntries!=null)
		{
			playlistUpdateThread.halt();
			try
			{
				for (int i=0; i<allEntries.length; i++)
				{
					if (!allEntries[i].isSelected())
						playList.remove(allEntries[i].getIndexInPlaylist());
				}
				createList(getFirstVisableIndex());
				firePlaylistChanged();
			}
			finally
			{
				playlistUpdateThread.restart();
			}
		}
    }
    private void doUpdateSelectedEntryFromList()
    {
		PlayListEntry [] selectedEntries = playList.getSelectedEntries();
		if (selectedEntries!=null)
		{
			playlistUpdateThread.halt();
			try
			{
				for (int i=0; i<selectedEntries.length; i++)
				{
					selectedEntries[i].setSongName(null);
					selectedEntries[i].setDuration(null);
					updateLine(selectedEntries[i].getIndexInPlaylist());
				}
			}
			finally
			{
				playlistUpdateThread.restart();
			}
		}
    }
	private void doEditSelectedEntry()
	{
		PlayListEntry [] selectedEntries = playList.getSelectedEntries();
		if (selectedEntries!=null)
		{
			playlistUpdateThread.halt();
			try
			{
				PlayListEntry entry = selectedEntries[0];
				EditPlaylistEntry editor = getEditDialog();
				editor.setValue(Helpers.createLocalFileStringFromURL(entry.getFile(), false));
				editor.setVisible(true);
				String newValue = editor.getValue();
				if (newValue!=null)
				{
					try
					{
						URL url = Helpers.createURLfromString(newValue);
						if (url!=null)
						{
							entry.setFile(url);
							entry.setSongName(null);
							entry.setDuration(null);
							updateLine(entry.getIndexInPlaylist());
						}
					}
					catch (Throwable ex)
					{
						JOptionPane.showMessageDialog(PlayListGUI.this, "Changing entry failed", "Failed", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			finally
			{
				playlistUpdateThread.restart();
			}
		}
	}
	private void doSavePlayList()
	{
		if (playList!=null)
		{
			do
			{
				String suggestedPath = Helpers.createLocalFileStringFromURL(playList.getLoadedFromURL(), true);
				FileChooserResult selectedFile = Helpers.selectFileNameFor(PlayListGUI.this, suggestedPath, "Save playlist to", new FileFilter[] { PlayList.PLAYLIST_FILE_FILTER }, 1, false);
				if (selectedFile!=null)
				{
					File f = selectedFile.getSelectedFile();
					if (f!=null)
				    {
						final String filename = f.getAbsolutePath();
						final String fileNameLow = filename.toLowerCase();
						if (!fileNameLow.endsWith("pls") && !fileNameLow.endsWith("m3u") && !fileNameLow.endsWith("cue")) 
							f = new File(filename+".M3U");

				    	if (f.exists())
				    	{
				    		int result = JOptionPane.showConfirmDialog(PlayListGUI.this, "File already exists! Overwrite?", "Overwrite confirmation", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				    		if (result==JOptionPane.CANCEL_OPTION) return;
				    		if (result==JOptionPane.NO_OPTION) continue; // Reselect
				    		boolean ok = f.delete();
				    		if (!ok)
				    		{
		        		    	JOptionPane.showMessageDialog(PlayListGUI.this, "Overwrite failed. Is file write protected or in use?", "Failed", JOptionPane.ERROR_MESSAGE);
		        		    	return;
				    		}
				    	}
				    	try
				    	{
				    		playList.savePlayListTo(f);
				    	}
				    	catch (Exception ex)
				    	{
				    		Log.error("Save playlist", ex);
				    	}
				    }
				}
		    	return;
			}
			while (true);
		}
	}
	private void doShufflePlayList()
	{
		playlistUpdateThread.halt();
		try
		{
			playList.doShuffle();
			createList(getFirstVisableIndex());
		}
		finally
		{
			playlistUpdateThread.restart();
		}
	}
	/**
	 * @since 04.09.2013
	 */
	private void doSelectAll()
	{
		playList.setSelectedElements(0, playList.size() - 1);
	}
	/**
	 * Expands the selection (moving cursor with shift)
	 * @param moveBy
	 * @since 15.12.2013
	 */
	private void doChangeSelectionInList(int moveBy)
	{
		PlayListEntry [] selected = playList.getSelectedEntries();
		if (selected==null || selected.length == 0)
		{
			playList.setSelectedElement(0);
			doMakeIndexVisible(0);
		}
		else
		{
			int firstIndex = selected[0].getIndexInPlaylist(); 
			int lastIndex = selected[selected.length - 1].getIndexInPlaylist();
			if (moveBy < 0)
			{
				firstIndex += moveBy;
				if (firstIndex<0) firstIndex = 0;
			}
			else
			{
				lastIndex += moveBy;
				if (lastIndex >= playList.size()) lastIndex = playList.size() - 1; 
			}
			playList.setSelectedElements(firstIndex, lastIndex);
			if (moveBy < 0) doMakeIndexVisible(firstIndex); else doMakeIndexVisible(lastIndex);
		}
	}
	/**
	 * Moves the selection (cursor)
	 * @param moveBy
	 * @since 15.12.2013
	 */
	private void doMoveSelectionInList(int moveBy)
	{
		PlayListEntry [] selected = playList.getSelectedEntries();
		if (selected==null || selected.length == 0)
		{
			playList.setSelectedElement(0);
			doMakeIndexVisible(0);
		}
		else
		{
			int index = selected[0].getIndexInPlaylist();
			index += moveBy;
			if (index<0) index = 0;
			else
			if (index>=playList.size()) index = playList.size() - 1;
			playList.setSelectedElement(index);
			if (moveBy <0) index-=PLUS_LINES_VISABLE; else index+=PLUS_LINES_VISABLE;
			doMakeIndexVisible(index);
		}
	}
	/**
	 * Move the selected entries (alt cursor)
	 * @param moveBy
	 * @since 15.12.2013
	 */
	private void doMoveSelectedEntriesInList(int moveBy)
	{
		PlayListEntry [] selected = playList.getSelectedEntries();
		if (selected!=null)
		{
			playlistUpdateThread.halt();
			try
			{
				if (moveBy < 0)
				{
					// Moveup --> Still possible?
					if ((selected[0].getIndexInPlaylist() + moveBy) < 0)
						moveBy = selected[0].getIndexInPlaylist();
					if (moveBy == 0) return;
					
					for (int i=0; i<selected.length; i++)
					{
						int fromIndex = selected[i].getIndexInPlaylist();
						playList.move(fromIndex, fromIndex + moveBy);
					}
				}
				else
				{
					// Movedown --> Still possible?
					int lastIndex = selected.length - 1;
					if ((selected[lastIndex].getIndexInPlaylist() + moveBy) >= playList.size())
						moveBy = playList.size() - 1 - selected[lastIndex].getIndexInPlaylist();
					if (moveBy == 0) return;
					
					for (int i=lastIndex; i>=0; i--)
					{
						int fromIndex = selected[i].getIndexInPlaylist();
						playList.move(fromIndex, fromIndex + moveBy);
					}
				}
				int showIndex = (moveBy<0)?selected[0].getIndexInPlaylist()-PLUS_LINES_VISABLE:selected[selected.length-1].getIndexInPlaylist()+PLUS_LINES_VISABLE;   
				createList(showIndex);
				firePlaylistChanged();
			}
			finally
			{
				playlistUpdateThread.restart();
			}
		}
	}
	private void doPlaySelectedPiece()
	{
		final PlayListEntry [] selected = playList.getSelectedEntries();
		if (selected!=null)
		{
			final int index = selected[0].getIndexInPlaylist();
			playList.setSelectedElement(index);
			playList.setCurrentElement(index); 
			fireActiveElementChanged();
		}
	}
	private Element getDocumentRootElement()
	{
		final Document doc = getPlaylistTextArea().getDocument();
		if (doc!=null)
		{
			final Element map = doc.getDefaultRootElement();
			if (map!=null)
			{
				final Element e1 = map.getElement(1);
				if (e1!=null) 
				{
					return e1.getElement(0);
				}
			}
		}
		return null;
	}
	private Element getDocumentElementForIndex(int index)
	{
		if (index>-1)
		{
			final Element element = getDocumentRootElement();
			if (element!=null) return element.getElement(index);
		}
		return null;
	}
	private int getMaxVisableRows()
	{
		Rectangle size = getScrollPane().getVisibleRect();
		Point p = new Point(0, (int)size.getHeight());
		return getSelectedIndexFromPoint(p, true) - 1;
	}
	private int getFirstVisableIndex()
	{
		final Element table = getDocumentRootElement();
		if (table!=null)
		{
			for (int i=0; i<table.getElementCount(); i++)
			{
				final Element element = table.getElement(i);
				try
				{
					final Rectangle r1 = getPlaylistTextArea().modelToView(element.getStartOffset());
					final Rectangle r2 = getPlaylistTextArea().modelToView(element.getEndOffset()-1);
					final Rectangle r = new Rectangle((int)r1.getX(), (int)r1.getY(), (int)(r2.getX()-r1.getX()), (int)r1.getHeight());
					final Rectangle intersect = r.intersection(getPlaylistTextArea().getVisibleRect());
					if (!intersect.isEmpty() && intersect.height==r.height) return i;
				}
				catch (BadLocationException ex)
				{
				}
			}
		}
		return -1;
	}
	/**
	 * @param position
	 * @param returnNearest 
	 * @return -1, if unselectable Index, otherwise index of clicked Element
	 * @since 23.03.2013
	 */
	private int getSelectedIndexFromPoint(Point position, boolean returnNearest)
	{
		final int modelPos = getPlaylistTextArea().viewToModel(position);
		final Element table = getDocumentRootElement();
		if (table!=null)
		{
			// get the index of the *nearest* element
			int index = table.getElementIndex(modelPos);
			// now lets check, if the user really hit the element or somewhere outside
			final Element selectedElement = table.getElement(index);
			try
			{
				final Rectangle r1 = getPlaylistTextArea().modelToView(selectedElement.getStartOffset());
				final Rectangle r2 = getPlaylistTextArea().modelToView(selectedElement.getEndOffset()-1);
				final Rectangle r = new Rectangle((int)r1.getX(), (int)r1.getY(), (int)(r2.getX()-r1.getX()), (int)r1.getHeight());
				if (!returnNearest && !r.contains(position)) 
					return -1;
				else
				if (returnNearest)
				{
					if (position.getY() < r1.getY()) index--;
					else
					if (position.getY() > r1.getY() + r1.getHeight()) index++;
				}
			}
			catch (BadLocationException ex)
			{
			}
			return index;
		}
		return -1;
	}
	/**
	 * @since 04.09.2013
	 * @param entry
	 */
	private void doMakeIndexVisible(PlayListEntry entry)
	{
		if (entry!=null)
		{
			try
			{
				final Element element = getDocumentElementForIndex(entry.getIndexInPlaylist());
				if (element!=null)
				{
					final Rectangle r1 = getPlaylistTextArea().modelToView(element.getStartOffset());
					final Rectangle r2 = getPlaylistTextArea().modelToView(element.getEndOffset()-1);
					if (r1!=null && r2!=null)
					{
						final Rectangle r = new Rectangle((int)r1.getX(), (int)r1.getY(), (int)(r2.getX()-r1.getX()), (int)r1.getHeight());
						getPlaylistTextArea().scrollRectToVisible(r);
					}
				}
			}
			catch (Throwable ex)
			{
				Log.error("PlayListGui::doMakeIndexVisible", ex);
			}
		}
	}
	/**
	 * @since 04.09.2013
	 * @param index
	 */
	private void doMakeIndexVisible(int index)
	{
		if (playList!=null)
		{
			if (index<0) index = 0;
			else
			if (index>=playList.size()) index = playList.size() - 1;
			doMakeIndexVisible(playList.getEntry(index));
		}
	}
	/**
	 * @param dtde
	 * @param dropResult
	 * @param addToLastLoaded
	 * @see sm.smcreator.smc.main.gui.tools.PlaylistDropListenerCallBack#playlistRecieved(java.awt.dnd.DropTargetDropEvent, sm.smcreator.smc.main.playlist.PlayList, java.net.URL)
	 * @since 08.03.2013
	 */
	public void playlistRecieved(DropTargetDropEvent dtde, PlayList dropResult, URL addToLastLoaded)
	{
		if (playList==null)
		{
			setNewPlaylist(dropResult);
			firePlaylistChanged();
		}
		else
		{
			playlistUpdateThread.halt();
			try
			{
				int index = getSelectedIndexFromPoint(dtde.getLocation(), true);
				if (index==-1) index = 0; // more Top than top is not OK ;)
				if (index > playList.size()) index = playList.size();
				playList.addAllAt(index, dropResult);
				createList(getFirstVisableIndex());
				firePlaylistChanged();
			}
			finally
			{
				playlistUpdateThread.restart();
			}
		}
	}
	public void addPlaylistGUIChangeListener(PlaylistGUIChangeListener listener)
	{
		listeners.add(listener);
	}
	public void removePlaylistGUIChangeListener(PlaylistGUIChangeListener listener)
	{
		listeners.remove(listener);
	}
	private synchronized void fireActiveElementChanged()
	{
		for (int i=0; i<listeners.size(); i++)
		{
			listeners.get(i).userSelectedPlaylistEntry();
		}
	}
	private void firePlaylistChanged()
	{
		for (int i=0; i<listeners.size(); i++)
		{
			listeners.get(i).playListChanged(playList);
		}
	}
	private String getHTMLString(PlayListEntry entry, String text, String duration)
	{
		StringBuilder html = new StringBuilder("<TR style=\"")
			.append("background:#").append((entry.isSelected())?markColorBackground:unmarkColorBackground).append("; ")
			.append("color:#").append((entry.isSelected())?markColorForeground:unmarkColorForeground).append("; ")
			.append("font-family:").append(Helpers.TEXTAREA_FONT.getFamily()).append("; ")
			.append("font-size:").append(Helpers.TEXTAREA_FONT.getSize()).append(';')
			.append("font-weight:").append(entry.isActive()?"bold":"normal").append(';')
			.append("\"><TD align=\"left\" nowrap>").append(text.replace(" ", "&nbsp;"))
			.append("</TD><TD align=\"right\" nowrap>").append(duration.replace(" ", "&nbsp;")).append("</TD></TR>");
		return html.toString();
	}
	private void createList(int makeThisIndexVisable)
	{
		if (playList!=null)
		{
			StringBuilder fullText = new StringBuilder("<HTML><HEAD><TITLE>PlayList</TITLE></HEAD><BODY><TABLE WIDTH=\"100%\" BORDER=\"0\" CELLPADDING=\"0\" CELLSPACING=\"0\">");
			Iterator<PlayListEntry> iter = playList.getIterator();
			int lineIndex = 0;
			int digits = (int)(Math.log10(playList.size()) + 1);
			String formatString = String.format("%%%dd. ", Integer.valueOf(digits));
			while (iter.hasNext())
			{
				PlayListEntry entry = iter.next();
				fullText.append(getHTMLString(entry, String.format(formatString, Integer.valueOf(lineIndex+1)) + entry.getQuickSongName(), entry.getQuickDuration()));
				lineIndex ++;
			}
			fullText.append("</TABLE></FONT></BODY></HTML>");
			getPlaylistTextArea().setText(fullText.toString());
			getPlaylistTextArea().select(0, 0);
			doMakeIndexVisible((makeThisIndexVisable<0)?0:makeThisIndexVisable);
			getRepeatCheckBox().setSelected(playList.isRepeat());
		}
		else
			getRepeatCheckBox().setSelected(false);
	}
	public void setNewPlaylist(final PlayList playList)
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				PlayListGUI me = PlayListGUI.this;
				playlistUpdateThread.halt();
				try
				{
					me.playList = playList;
					createList(0);
					me.playList.addPlaylistChangedListener(me);
				}
				finally
				{
					playlistUpdateThread.restart();
				}
			}
		});
	}
	/**
	 * @since 03.04.2013
	 * @param newName
	 * @param time
	 * @param lineIndex
	 */
	private void replaceLine(String newName, String time, int lineIndex)
	{
        try
        {
	        Element element = getDocumentElementForIndex(lineIndex);
	        if (element!=null)
	        {
		        Element TextElement = element.getElement(0).getElement(0).getElement(0);
		        Element TimeElement = element.getElement(1).getElement(0).getElement(0);
		        getPlaylistTextArea().getDocument().remove(TextElement.getStartOffset(), TextElement.getEndOffset() - TextElement.getStartOffset());
		        getPlaylistTextArea().getDocument().insertString(TextElement.getStartOffset(), newName, TextElement.getAttributes());
		        getPlaylistTextArea().getDocument().remove(TimeElement.getStartOffset(), TimeElement.getEndOffset() - TimeElement.getStartOffset());
		        getPlaylistTextArea().getDocument().insertString(TimeElement.getStartOffset(), time, TimeElement.getAttributes());
	        }
        }
        catch (BadLocationException ex)
        {
        	Log.error("PlayListGUI::replaceLine", ex);
        }
	}
	/**
	 * @since 03.04.2013
	 * @param index
	 */
	private void updateLine(int index)
	{
		int digits = (int)(Math.log10(playList.size()) + 1);
		String formatString = String.format("%%%dd. %%s", Integer.valueOf(digits));
		PlayListEntry entry = playList.getEntry(index);
		String name = MultimediaContainerManager.getSongNameFromURL(entry.getFile());
		String duration = "0:00";
		try
		{
			name = entry.getFormattedName();
			duration = entry.getDurationString();
		}
		catch (RuntimeException ex)
		{
			name += " [" + ex.getMessage() + "]";
		}
		String songLine = String.format(formatString, Integer.valueOf(index+1), name);
		replaceLine(songLine, duration, index);
	}
	/**
	 * @since 18.02.2013
	 * @param element
	 * @return
	 * @throws BadLocationException
	 */
	private static String getText(Element element) throws BadLocationException
	{
		String text = element.getDocument().getText(element.getStartOffset(), element.getEndOffset() - element.getStartOffset());
		if (text.endsWith("\n"))
			return text.substring(0, text.length() - 1);
		else
			return text;
	}
	/**
	 * @since 18.02.2013
	 * @param element
	 * @param newColor
	 */
	private void setTextDecorationAndColorsFor(PlayListEntry entry)
	{
		try
		{
	        Element element =getDocumentElementForIndex(entry.getIndexInPlaylist());
	        if (element!=null)
	        {
				String text = getText(element.getElement(0).getElement(0).getElement(0));
				String duration = getText(element.getElement(1).getElement(0).getElement(0));
	
				HTMLDocument doc = (HTMLDocument) element.getDocument();
				doc.setOuterHTML(element, getHTMLString(entry, text, duration));
	        }
		}
		catch (Throwable ex)
		{
			Log.error("PlayListGui::setTextDecorationAndColorsFor", ex);
		}
	}
	/**
	 * @param oldActiveElement
	 * @param newActiveElement
	 * @see sm.smcreator.smc.main.playlist.PlaylistChangedListener#activeElementChanged(sm.smcreator.smc.main.playlist.PlayListEntry, sm.smcreator.smc.main.playlist.PlayListEntry)
	 */
	public void activeElementChanged(final PlayListEntry oldActiveElement, final PlayListEntry newActiveElement)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				if (oldActiveElement!=null) setTextDecorationAndColorsFor(oldActiveElement);
				if (newActiveElement!=null)
				{
					setTextDecorationAndColorsFor(newActiveElement);
					doMakeIndexVisible(newActiveElement);
				}
			}
		});
	}
	/**
	 * @param oldSelectedElement
	 * @param newSelectedElement
	 * @see sm.smcreator.smc.main.playlist.PlaylistChangedListener#selectedElementChanged(sm.smcreator.smc.main.playlist.PlayListEntry, sm.smcreator.smc.main.playlist.PlayListEntry)
	 * @since 23.03.2013
	 */
	public void selectedElementChanged(final PlayListEntry oldSelectedElement, final PlayListEntry newSelectedElement)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				if (oldSelectedElement!=null) setTextDecorationAndColorsFor(oldSelectedElement);
				if (newSelectedElement!=null)
				{
					setTextDecorationAndColorsFor(newSelectedElement);
					doMakeIndexVisible(newSelectedElement);
				}
				//doMakeIndexVisable(getFirstVisableIndex());
			}
		});
	}
}
