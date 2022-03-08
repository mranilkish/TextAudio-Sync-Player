/*
 * @(#) MainForm.java
 * 
 * Created on 22.06.2013 by Anil Kishan
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
package sm.smcreator.smc.main.gui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
//import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import sm.smcreator.smc.io.GaplessSoundOutputStreamImpl;
import sm.smcreator.smc.io.SoundOutputStream;
import sm.smcreator.smc.main.SMCreator;
import sm.smcreator.smc.main.gui.components.SMCPanel;
import sm.smcreator.smc.main.gui.components.RoundSlider;
import sm.smcreator.smc.main.gui.components.SeekBarPanel;
import sm.smcreator.smc.main.gui.components.SeekBarPanelListener;
import sm.smcreator.smc.main.gui.playlist.PlaylistGUIChangeListener;
import sm.smcreator.smc.main.gui.tools.FileChooserFilter;
import sm.smcreator.smc.main.gui.tools.FileChooserResult;
import sm.smcreator.smc.main.gui.tools.PlaylistDropListener;
import sm.smcreator.smc.main.gui.tools.PlaylistDropListenerCallBack;
import sm.smcreator.smc.main.playlist.PlayList;
import sm.smcreator.smc.main.playlist.PlayListEntry;
import sm.smcreator.smc.mixer.Mixer;
import sm.smcreator.smc.mixer.dsp.AudioProcessor;
import sm.smcreator.smc.mixer.dsp.DspProcessorCallBack;
import sm.smcreator.smc.mixer.dsp.pitchshift.PitchShift;
import sm.smcreator.smc.mixer.dsp.pitchshift.PitchShiftGUI;
import sm.smcreator.smc.multimedia.MultimediaContainer;
import sm.smcreator.smc.multimedia.MultimediaContainerEvent;
import sm.smcreator.smc.multimedia.MultimediaContainerEventListener;
import sm.smcreator.smc.multimedia.MultimediaContainerManager;
import sm.smcreator.smc.system.Helpers;
import sm.smcreator.smc.system.Log;
import sm.smcreator.smc.system.LogMessageCallBack;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.*;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;
import javax.swing.UIManager;


/**
 * @author Anil Kishan
 * @since 22.06.2013
 */
public class MainForm extends javax.swing.JFrame implements LogMessageCallBack, DspProcessorCallBack, PlayThreadEventListener, MultimediaContainerEventListener, PlaylistDropListenerCallBack
{
	private static final long serialVersionUID = -2737074464335059959L;

	private static final String DEFAULTICONPATH = "/sm/smcreator/smc/main/gui/ressources/smc_icon.gif";
	public static final String BUTTONPLAY_INACTIVE = "/sm/smcreator/smc/main/gui/ressources/play.gif";
	public static final String BUTTONPLAY_ACTIVE = "/sm/smcreator/smc/main/gui/ressources/play_aktiv.gif";
	public static final String BUTTONPLAY_NORMAL = "/sm/smcreator/smc/main/gui/ressources/play_normal.gif";
	public static final String BUTTONPAUSE_INACTIVE = "/sm/smcreator/smc/main/gui/ressources/pause.gif";
	public static final String BUTTONPAUSE_ACTIVE = "/sm/smcreator/smc/main/gui/ressources/pause_aktiv.gif";
	public static final String BUTTONPAUSE_NORMAL = "/sm/smcreator/smc/main/gui/ressources/pause_normal.gif";
	public static final String BUTTONSTOP_INACTIVE = "/sm/smcreator/smc/main/gui/ressources/stop.gif";
	public static final String BUTTONSTOP_ACTIVE = "/sm/smcreator/smc/main/gui/ressources/stop_aktiv.gif";
	public static final String BUTTONSTOP_NORMAL = "/sm/smcreator/smc/main/gui/ressources/stop_normal.gif";
	
	private static final String PROPERTYFILENAME = ".javamod.properties";
	private static final String PROPERTY_SEARCHPATH = "javamod.path.loadpath"; 
	private static final String PROPERTY_EXPORTPATH = "javamod.path.exportpath"; 
	private static final String PROPERTY_LOOKANDFEEL = "javamod.lookandfeel.classname"; 
	private static final String PROPERTY_LASTLOADED = "javamod.path.lastloaded";
	private static final String PROPERTY_SYSTEMTRAY = "javamod.systemtray";
	private static final String PROPERTY_MAINDIALOG_POS = "javamod.dialog.position.main";
	private static final String PROPERTY_SETUPDIALOG_POS = "javamod.dialog.position.setup";	
	private static final String PROPERTY_EFFECTDIALOG_POS = "javamod.dialog.position.equalizer";
	private static final String PROPERTY_MAINDIALOG_SIZE = "javamod.dialog.size.main";
	private static final String PROPERTY_SETUPDIALOG_SIZE = "javamod.dialog.size.setup";	
	private static final String PROPERTY_EFFECTDIALOG_SIZE = "javamod.dialog.size.equalizer";
	private static final String PROPERTY_VOLUME_VALUE = "javamod.dialog.volume.value";
	
	private static final String PROPERTY_SETUPDIALOG_VISABLE = "javamod.dialog.open.setup";
	
	private static final String PROPERTY_EFFECT_VISABLE = "javamod.dialog.open.equalizer";

	private static final String PROPERTY_EFFECTS_PASSTHROUGH = "javamod.player.effects.passthrough";
	private static final String PROPERTY_PITCHSHIFT_ISACTIVE = "javamod.player.pitchshift.isactive";
	private static final String PROPERTY_PITCHSHIFT_PITCH = "javamod.player.pitchshift.pitch";
	private static final String PROPERTY_PITCHSHIFT_SAMPLESCALE = "javamod.player.pitchshift.scale";
	private static final String PROPERTY_PITCHSHIFT_FRAMESIZE = "javamod.player.pitchshift.framesize";
	private static final String PROPERTY_PITCHSHIFT_OVERSAMPLING = "javamod.player.pitchshift.oversampling";
	private static final int PROPERTY_LASTLOADED_MAXENTRIES = 10;
	
	private static final String WINDOW_TITLE = Helpers.FULLVERSION;
	private static final String WINDOW_NAME = "SMCreator";
	
	private static FileFilter fileFilterExport[];
	private static FileFilter fileFilterLoad[];
	
	private javax.swing.ImageIcon buttonPlay_Active = null;
	private javax.swing.ImageIcon buttonPlay_Inactive = null;
	private javax.swing.ImageIcon buttonPlay_normal = null;
	private javax.swing.ImageIcon buttonPause_Active = null;
	private javax.swing.ImageIcon buttonPause_Inactive = null;
	private javax.swing.ImageIcon buttonPause_normal = null;
	private javax.swing.ImageIcon buttonStop_Active = null;
	private javax.swing.ImageIcon buttonStop_Inactive = null;
	private javax.swing.ImageIcon buttonStop_normal = null;
	
	private javax.swing.JButton button_Play = null;
	private javax.swing.JButton button_Pause = null;
	private javax.swing.JButton button_Stop = null;
	
	private RoundSlider volumeSlider = null;
	private javax.swing.JLabel volumeLabel = null;
	
	private javax.swing.JPanel baseContentPane = null;
	private javax.swing.JPanel mainContentPane = null;
	private javax.swing.JPanel musicDataPane = null;
	private javax.swing.JPanel playerControlPane = null;
	private javax.swing.JPanel playerDataPane = null;
	
	private javax.swing.JDialog playerSetUpDialog = null;
	private javax.swing.JDialog equalizerDialog = null;
	private PlayerConfigPanel playerConfigPanel = null; 
	private javax.swing.JPanel effectPane = null;
        
        private String audiofile=null;
        private String xpfile=null;
        boolean justexported=false;
        
	
	private java.awt.Point mainDialogLocation = null;
	private java.awt.Dimension mainDialogSize = null;
	private java.awt.Point playerSetUpDialogLocation = null;
	private java.awt.Dimension playerSetUpDialogSize = null;
	private boolean playerSetUpDialogVisable = false;
	private java.awt.Point effectsDialogLocation = null;
	private java.awt.Dimension effectsDialogSize = null;
	private boolean effectDialogVisable = false;

	
	private SMCPanel d3cPanel = null;
	
	private SeekBarPanel seekBarPanel = null;

	private javax.swing.JTextField messages = null;

	private javax.swing.JMenuBar baseMenuBar = null;
	private javax.swing.JMenu menu_File = null;
	private javax.swing.JMenu menu_Edit = null;
	private javax.swing.JMenu menu_LookAndFeel = null;
	private javax.swing.JMenu menu_Help = null;
	private javax.swing.JMenu menu_File_RecentFiles = null;
	private javax.swing.JMenuItem menu_File_openMod = null;
        private javax.swing.JMenuItem menu_File_newp = null;
        private javax.swing.JMenuItem menu_File_openp = null;
        private javax.swing.JMenuItem menu_File_saveasp = null,menu_File_saveap = null;
        private javax.swing.JMenuItem menu_File_savep = null,menu_File_xpudt=null;
        

	private javax.swing.JMenuItem menu_File_Close = null;
	private javax.swing.JMenuItem menu_View_GraphicEQ = null;
	private javax.swing.JCheckBoxMenuItem menu_View_UseSystemTray = null;
	private javax.swing.JMenuItem menu_Help_About = null;
	private javax.swing.JCheckBoxMenuItem [] menu_LookAndFeel_Items = null;
	
	private MenuItem aboutItem = null;
	private MenuItem playItem = null;
	private MenuItem pauseItem = null;
	private MenuItem stopItem = null;

	private MenuItem closeItem = null;
	
	private TrayIcon dc3TrayIcon = null;
	
	private SMCAbout about = null;

	private SimpleTextViewerDialog simpleTextViewerDialog = null;
	private EffectsPanel effectGUI = null;
	private PitchShiftGUI pitchShiftGUI = null;
	
	private MultimediaContainer currentContainer;
	private PlayThread playerThread;
 //     private Sync syncThread;
	private PlayList currentPlayingFile;
	private PitchShift currentPitchShift;
	
	private ArrayList<DropTarget> dropTargetList;
	private AudioProcessor audioProcessor;
	private transient SoundOutputStream soundOutputStream;

	private String propertyFilePath;
	private String searchPath;
	private String exportPath;
	private String uiClassName;
	private boolean useSystemTray = false;
	private float currentVolume; /* 0.0 - 1.0 */
        
	
	private ArrayList<URL> lastLoaded;
	
	
	
	private final class LookAndFeelChanger implements ActionListener
	{
		private String uiClassName;
		private JCheckBoxMenuItem parent;
		
		public LookAndFeelChanger(JCheckBoxMenuItem parent, String uiClassName)
		{
			this.uiClassName = uiClassName;
			this.parent = parent;
		}
		private void setSelection()
		{
			for (int i=0; i<menu_LookAndFeel_Items.length; i++)
			{
				if (menu_LookAndFeel_Items[i]==parent)
					menu_LookAndFeel_Items[i].setSelected(true);
				else
					menu_LookAndFeel_Items[i].setSelected(false);
			}
		}
		public void actionPerformed(ActionEvent event)
		{
			setSelection();
			MainForm.this.uiClassName = uiClassName;
			MainForm.this.updateLookAndFeel(uiClassName);
		}
	}
	private final class MouseWheelVolumeControl implements MouseWheelListener
	{
		@Override
		public void mouseWheelMoved(MouseWheelEvent e)
		{
			if (!e.isConsumed() && e.getScrollType()==MouseWheelEvent.WHEEL_UNIT_SCROLL)
			{
				final RoundSlider volSlider = getVolumeSlider();
				volSlider.setValue(volSlider.getValue() + ((float)e.getWheelRotation() / 100f));
				e.consume();
			}
		}
	}
	private final class MakeMainWindowVisible implements WindowFocusListener
	{
		public void windowLostFocus(WindowEvent e) { /*NOOP*/ }
		public void windowGainedFocus(WindowEvent e)
		{
			MainForm.this.setFocusableWindowState(false);
			MainForm.this.toFront();
			MainForm.this.setFocusableWindowState(true);
		}
	}

	private transient final MakeMainWindowVisible makeMainWindowVisiable = new MakeMainWindowVisible();
	/**
	 * Constructor for MainForm
	 * @param title
	 * @throws HeadlessException
	 */
	public MainForm() throws HeadlessException
	{
		super();
		propertyFilePath = Helpers.HOMEDIR;
		currentPlayingFile = null;
	//	currentEqualizer = new GraphicEQ();
		currentPitchShift = new PitchShift();
	    audioProcessor = new AudioProcessor(2048, 70);
	    audioProcessor.addListener(this);
	  //  audioProcessor.addEffectListener(currentEqualizer);
	    audioProcessor.addEffectListener(currentPitchShift);
		 setUIFont(new javax.swing.plaf.FontUIResource("Code2000",Font.BOLD,16));
		initialize();
	}
	/**
	 * Read the properties from file. Use default values, if not set or file not available
	 * @since 01.06.2013
	 */
	private void readPropertyFile()
	{
		java.util.Properties props = new java.util.Properties();
	    try
	    {
	        File propertyFile = new File(propertyFilePath + File.separator + PROPERTYFILENAME);
	        if (propertyFile.exists())
	        {
	        	java.io.FileInputStream fis = null;
	        	try
	        	{
			    	fis = new java.io.FileInputStream(propertyFile);
			        props.load(fis);
	        	}
	        	finally
	        	{
	        		if (fis!=null) try { fis.close(); } catch (IOException ex) { Log.error("IGNORED", ex); }
	        	}
	        }

	        searchPath = props.getProperty(PROPERTY_SEARCHPATH, System.getProperty("user.home"));
			exportPath = props.getProperty(PROPERTY_EXPORTPATH, System.getProperty("user.home"));
			uiClassName = props.getProperty(PROPERTY_LOOKANDFEEL, javax.swing.UIManager.getSystemLookAndFeelClassName());
			useSystemTray = Boolean.parseBoolean(props.getProperty(PROPERTY_SYSTEMTRAY, "FALSE"));
			currentVolume = Float.parseFloat(props.getProperty(PROPERTY_VOLUME_VALUE, "1.0"));		
			lastLoaded = new ArrayList<URL>(PROPERTY_LASTLOADED_MAXENTRIES);
			for (int i=0; i<PROPERTY_LASTLOADED_MAXENTRIES; i++)
			{
				String url = props.getProperty(PROPERTY_LASTLOADED+'.'+i, null);
				if (url!=null) lastLoaded.add(new URL(url)); else lastLoaded.add(null);
			}
			setDSPEnabled(Boolean.parseBoolean(props.getProperty(PROPERTY_EFFECTS_PASSTHROUGH, "FALSE")));
			mainDialogLocation = Helpers.getPointFromString(props.getProperty(PROPERTY_MAINDIALOG_POS, "-1x-1"));
			mainDialogSize = Helpers.getDimensionFromString(props.getProperty(PROPERTY_MAINDIALOG_SIZE, "320x410"));
			playerSetUpDialogLocation = Helpers.getPointFromString(props.getProperty(PROPERTY_SETUPDIALOG_POS, "-1x-1"));
			playerSetUpDialogSize = Helpers.getDimensionFromString(props.getProperty(PROPERTY_SETUPDIALOG_SIZE, "720x230"));
			playerSetUpDialogVisable = Boolean.parseBoolean(props.getProperty(PROPERTY_SETUPDIALOG_VISABLE, "false"));
			effectsDialogLocation = Helpers.getPointFromString(props.getProperty(PROPERTY_EFFECTDIALOG_POS, "-1x-1"));
			effectsDialogSize = Helpers.getDimensionFromString(props.getProperty(PROPERTY_EFFECTDIALOG_SIZE, "560x470"));
			effectDialogVisable = Boolean.parseBoolean(props.getProperty(PROPERTY_EFFECT_VISABLE, "false"));
			
			/*if (currentEqualizer!=null)
			{
				boolean isActive = Boolean.parseBoolean(props.getProperty(PROPERTY_EQUALIZER_ISACTIVE, "FALSE"));
				currentEqualizer.setIsActive(isActive);
				float preAmpValueDB = Float.parseFloat(props.getProperty(PROPERTY_EQUALIZER_PREAMP, "0.0"));
				currentEqualizer.setPreAmp(preAmpValueDB);
				for (int i=0; i<currentEqualizer.getBandCount(); i++)
				{
					float bandValueDB = Float.parseFloat(props.getProperty(PROPERTY_EQUALIZER_BAND_PREFIX + Integer.toString(i), "0.0"));
					currentEqualizer.setBand(i, bandValueDB);
				}
			}*/
			if (currentPitchShift!=null)
			{
				boolean isActive = Boolean.parseBoolean(props.getProperty(PROPERTY_PITCHSHIFT_ISACTIVE, "FALSE"));
				currentPitchShift.setIsActive(isActive);
				float pitchValue = Float.parseFloat(props.getProperty(PROPERTY_PITCHSHIFT_PITCH, "1.0"));
				currentPitchShift.setPitchScale(pitchValue);
				float scaleValue = Float.parseFloat(props.getProperty(PROPERTY_PITCHSHIFT_SAMPLESCALE, "1.0"));
				currentPitchShift.setSampleScale(scaleValue);
				int overSampling = Integer.parseInt(props.getProperty(PROPERTY_PITCHSHIFT_OVERSAMPLING, "32"));
				currentPitchShift.setFFTOversampling(overSampling);
				int frameSize = Integer.parseInt(props.getProperty(PROPERTY_PITCHSHIFT_FRAMESIZE, "8192"));
				currentPitchShift.setFFTFrameSize(frameSize);
			}

			MultimediaContainerManager.configureContainer(props);
	    }
	    catch (Throwable ex)
	    {
			Log.error("[MainForm]", ex);
	    }
	}
	/**
	 * Write back to a File
	 * @since 01.06.2013
	 */
	private void writePropertyFile()
	{
	    try
	    {
	    	java.util.Properties props = new java.util.Properties();
			
	    	MultimediaContainerManager.getContainerConfigs(props);
			props.setProperty(PROPERTY_SEARCHPATH, searchPath);
			props.setProperty(PROPERTY_EXPORTPATH, exportPath);
			props.setProperty(PROPERTY_LOOKANDFEEL, uiClassName);
			props.setProperty(PROPERTY_SYSTEMTRAY, Boolean.toString(useSystemTray));
			props.setProperty(PROPERTY_VOLUME_VALUE, Float.toString(currentVolume));		
			for (int i=0; i<PROPERTY_LASTLOADED_MAXENTRIES; i++)
			{
				URL element = lastLoaded.get(i);
				if (element!=null)
					props.setProperty(PROPERTY_LASTLOADED+'.'+i, element.toString());
			}
			props.setProperty(PROPERTY_EFFECTS_PASSTHROUGH, Boolean.toString(isDSPEnabled()));

			props.setProperty(PROPERTY_MAINDIALOG_POS, Helpers.getStringFromPoint(getLocation()));
			props.setProperty(PROPERTY_MAINDIALOG_SIZE, Helpers.getStringFromDimension(getSize()));
			props.setProperty(PROPERTY_SETUPDIALOG_POS, Helpers.getStringFromPoint(getPlayerSetUpDialog().getLocation()));
			props.setProperty(PROPERTY_SETUPDIALOG_SIZE, Helpers.getStringFromDimension(getPlayerSetUpDialog().getSize()));
			props.setProperty(PROPERTY_SETUPDIALOG_VISABLE, Boolean.toString(getPlayerSetUpDialog().isVisible()));
			
			props.setProperty(PROPERTY_EFFECTDIALOG_POS, Helpers.getStringFromPoint(getEffectDialog().getLocation()));
			props.setProperty(PROPERTY_EFFECTDIALOG_SIZE, Helpers.getStringFromDimension(getEffectDialog().getSize()));
			props.setProperty(PROPERTY_EFFECT_VISABLE, Boolean.toString(getEffectDialog().isVisible()));
	/*		if (currentEqualizer!=null)
			{
				props.setProperty(PROPERTY_EQUALIZER_ISACTIVE, Boolean.toString(currentEqualizer.isActive()));
				props.setProperty(PROPERTY_EQUALIZER_PREAMP, Float.toString(currentEqualizer.getPreAmpDB()));
				for (int i=0; i<currentEqualizer.getBandCount(); i++)
				{
					props.setProperty(PROPERTY_EQUALIZER_BAND_PREFIX + Integer.toString(i), Float.toString(currentEqualizer.getBand(i)));
				}
			}*/
			if (currentPitchShift!=null)
			{
				props.setProperty(PROPERTY_PITCHSHIFT_ISACTIVE, Boolean.toString(currentPitchShift.isActive()));
				props.setProperty(PROPERTY_PITCHSHIFT_PITCH, Float.toString(currentPitchShift.getPitchScale()));
				props.setProperty(PROPERTY_PITCHSHIFT_SAMPLESCALE, Float.toString(currentPitchShift.getSampleScale()));
				props.setProperty(PROPERTY_PITCHSHIFT_FRAMESIZE, Integer.toString(currentPitchShift.getFftFrameSize()));
				props.setProperty(PROPERTY_PITCHSHIFT_OVERSAMPLING, Integer.toString(currentPitchShift.getFFTOversampling()));
			}

			File propertyFile = new File(propertyFilePath + File.separator + PROPERTYFILENAME);
	        if (propertyFile.exists())
	        {
	        	boolean ok = propertyFile.delete();
	        	if (ok) ok = propertyFile.createNewFile();
	        	if (!ok) Log.error("Could not create property file: " + propertyFile.getCanonicalPath());
	        }
	        java.io.FileOutputStream fos = null;
	        try
	        {
		    	fos = new java.io.FileOutputStream(propertyFile);
			    props.store(fos, WINDOW_TITLE);
	        }
	        finally
	        {
	        	if (fos!=null) try { fos.close(); } catch (IOException ex) { Log.error("IGNORED", ex); }
	        }
	    }
	    catch (Throwable ex)
	    {
			Log.error("MainForm]", ex);
	    }
	}
	private javax.swing.UIManager.LookAndFeelInfo [] getInstalledLookAndFeels()
	{
//		java.util.ArrayList<UIManager.LookAndFeelInfo> allLAFs = new java.util.ArrayList<UIManager.LookAndFeelInfo>();
//		allLAFs.add(new UIManager.LookAndFeelInfo("Kunststoff", "com.incors.plaf.kunststoff.KunststoffLookAndFeel"));
//		allLAFs.add(new UIManager.LookAndFeelInfo("Oyoaha", "com.oyoaha.swing.plaf.oyoaha.OyoahaLookAndFeel"));
//		allLAFs.add(new UIManager.LookAndFeelInfo("MacOS", "it.unitn.ing.swing.plaf.macos.MacOSLookAndFeel"));
//		allLAFs.add(new UIManager.LookAndFeelInfo("GTK", "org.gtk.java.swing.plaf.gtk.GtkLookAndFeel"));
//		javax.swing.UIManager.LookAndFeelInfo [] installedLAFs = javax.swing.UIManager.getInstalledLookAndFeels();
//		for (int i=0; i<installedLAFs.length; i++)
//		{
//			allLAFs.add(installedLAFs[i]);
//		}
//		return allLAFs.toArray(new javax.swing.UIManager.LookAndFeelInfo[allLAFs.size()]);
		return javax.swing.UIManager.getInstalledLookAndFeels();
	}
	/**
	 * Create the file filters so that we do have them for
	 * the dialogs
	 * @since 05.01.2013
	 */
	private void createFileFilter()
	{
		HashMap<String, String[]> extensionMap = MultimediaContainerManager.getSupportedFileExtensionsPerContainer();
		
		ArrayList<FileFilter> chooserFilterArray = new ArrayList<FileFilter>(extensionMap.size() + 1);

		// Add playlist files to full list of supported file extensions
		String [] containerExtensions = MultimediaContainerManager.getSupportedFileExtensions();
		String [] fullSupportedExtensions = new String[containerExtensions.length + PlayList.SUPPORTEDPLAYLISTS.length];
		System.arraycopy(PlayList.SUPPORTEDPLAYLISTS, 0, fullSupportedExtensions, 0, PlayList.SUPPORTEDPLAYLISTS.length);
		System.arraycopy(containerExtensions, 0, fullSupportedExtensions, PlayList.SUPPORTEDPLAYLISTS.length, containerExtensions.length);
		chooserFilterArray.add(new FileChooserFilter(fullSupportedExtensions, "All playable files"));
		
		// add all single file extensions grouped by container
		Set<String> containerNameSet = extensionMap.keySet();
		Iterator<String> containerNameIterator = containerNameSet.iterator();
		while (containerNameIterator.hasNext())
		{
			String containerName = containerNameIterator.next();
			String [] extensions = extensionMap.get(containerName);
			StringBuilder fileText = new StringBuilder(containerName);
			fileText.append(" (");
			int ende = extensions.length-1;
			for (int i=0; i<=ende; i++)
			{
				fileText.append("*.").append(extensions[i]);
				if (i<ende) fileText.append(", ");
			}
			fileText.append(')');
			chooserFilterArray.add(new FileChooserFilter(extensions, fileText.toString()));
		}
		// now add playlist as group of files
		chooserFilterArray.add(PlayList.PLAYLIST_FILE_FILTER);
		
		fileFilterLoad = new FileFilter[chooserFilterArray.size()];
		chooserFilterArray.toArray(fileFilterLoad);

		fileFilterExport = new FileFilter[1];
		fileFilterExport[0] = new FileChooserFilter(javax.sound.sampled.AudioFileFormat.Type.WAVE.getExtension(), javax.sound.sampled.AudioFileFormat.Type.WAVE.toString());
	}
	/**
	 * Do main initials
	 * @since 22.06.2013
	 */
	private void initialize()
	{
		Log.addLogListener(this);
		
		readPropertyFile();
		
		setSystemTray();

		setName(WINDOW_NAME);
		setTitle(WINDOW_TITLE);
		getTrayIcon().setToolTip(WINDOW_TITLE);

		java.net.URL iconURL = MainForm.class.getResource(DEFAULTICONPATH);
	    if (iconURL!=null) 
	    	setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(iconURL));
		
	    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	    addWindowListener(new java.awt.event.WindowAdapter()
		{
			@Override
			public void windowClosing(java.awt.event.WindowEvent e)
			{
				doClose();
			}
			/**
			 * @param e
			 * @see java.awt.event.WindowAdapter#windowIconified(java.awt.event.WindowEvent)
			 * @since 07.02.2013
			 */
			@Override
			public void windowIconified(WindowEvent e)
			{
				if (useSystemTray) setVisible(false);
			}
			/**
			 * @param e
			 * @see java.awt.event.WindowAdapter#windowDeiconified(java.awt.event.WindowEvent)
			 * @since 07.02.2013
			 */
			@Override
			public void windowDeiconified(WindowEvent e)
			{
				if (useSystemTray) setVisible(true);
			}
		});
	    setSize(mainDialogSize);
		setPreferredSize(mainDialogSize);

		updateLookAndFeel(uiClassName);
		
		setJMenuBar(getBaseMenuBar());
	    setContentPane(getBaseContentPane());
	    setPlayListIcons();
		// Volumecontrol by mousewheel:
	    addMouseWheelListener(new MouseWheelVolumeControl());
	    pack();

		if (mainDialogLocation == null || (mainDialogLocation.getX()==-1 || mainDialogLocation.getY()==-1))
			mainDialogLocation = Helpers.getFrameCenteredLocation(this, null); 
	    setLocation(mainDialogLocation);


		getEffectDialog().setVisible(effectDialogVisable);
		getPlayerSetUpDialog().setVisible(playerSetUpDialogVisable);

		dropTargetList = new ArrayList<DropTarget>();
	    PlaylistDropListener myListener = new PlaylistDropListener(this);
	    Helpers.registerDropListener(dropTargetList, this, myListener);
	    
		MultimediaContainerManager.addMultimediaContainerEventListener(this);

	    createFileFilter();

	    currentContainer = null; //set Back to null!
	    showMessage("ok...");
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
		if (addToLastLoaded!=null) addFileToLastLoaded(addToLastLoaded);
		if (dropResult!=null)
		{
			doStopPlaying();
    	//	getPlaylistGUI().setNewPlaylist(currentPlayingFile = dropResult);
			boolean ok = doNextPlayListEntry();
			if (playerThread==null && ok) doStartPlaying();
		}
	}
	/**
	 * @see sm.smcreator.smc.main.gui.playlist.PlaylistGUIChangeListener#playListChanged()
	 * @since 08.03.2013
	 */
	public void playListChanged(PlayList newPlayList)
	{
		if (newPlayList!=null)
		{
			if (newPlayList!=currentPlayingFile)
			{
				boolean playListWasEmpty = currentPlayingFile == null;
				currentPlayingFile = newPlayList;
				if (playListWasEmpty) doNextPlayListEntry();
			}
			setPlayListIcons();
			//if (playerThread==null) doStartPlaying();
		}
	}
	/**
	 * set the selected look and feel
	 * @since 01.06.2013
	 * @param lookAndFeelClassName
	 * @return
	 */
	private void setLookAndFeel(String lookAndFeelClassName)
	{
		try
		{
	        javax.swing.UIManager.setLookAndFeel(lookAndFeelClassName);
		}
		catch (Throwable e)
		{
			showMessage("The selected Look&Feel is not supported or not reachable through the classpath. Switching to system default...");
	        try
	        {
	        	lookAndFeelClassName = javax.swing.UIManager.getSystemLookAndFeelClassName();
	            javax.swing.UIManager.setLookAndFeel(lookAndFeelClassName);
	        }
	        catch (Throwable e1)
	        {
				Log.error("[MainForm]", e1);
	        }
		}
	}
	/**
	 * Changes the look and feel to the new ClassName
	 * @since 22.06.2013
	 * @param lookAndFeelClassName
	 * @return
	 */
	private void updateLookAndFeel(String lookAndFeelClassName)
	{
	    setLookAndFeel(lookAndFeelClassName);
	    MultimediaContainerManager.updateLookAndFeel();
		javax.swing.SwingUtilities.updateComponentTreeUI(this); pack();
	    javax.swing.SwingUtilities.updateComponentTreeUI(getD3CAbout()); getD3CAbout().pack();
	
	    javax.swing.SwingUtilities.updateComponentTreeUI(getPlayerSetUpDialog()); getPlayerSetUpDialog().pack();
	  
	    javax.swing.SwingUtilities.updateComponentTreeUI(getShowVersion_Text()); getShowVersion_Text().pack();

	    javax.swing.SwingUtilities.updateComponentTreeUI(getEffectDialog()); getEffectDialog().pack();
	}
	
	private void changeConfigPane()
	{
		getPlayerConfigPanel().selectTabForContainer(getCurrentContainer());
//		getPlayerSetUpPane().removeAll();
//		getPlayerSetUpPane().add(getCurrentContainer().getConfigPanel(), java.awt.BorderLayout.CENTER);
//		getPlayerSetUpDialog().pack();
//		getPlayerSetUpDialog().repaint();
	}
	
	/**
	 * @since 15.01.2013
	 * @return
	 */
	public boolean isDSPEnabled()
	{
		if (audioProcessor!=null) return audioProcessor.isDspEnabled();
		return false;
	}
	/**
	 * @since 15.01.2013
	 * @param dspEnabled
	 */
	public void setDSPEnabled(boolean dspEnabled)
	{
		if (audioProcessor!=null) audioProcessor.setDspEnabled(dspEnabled);
	}
	/* Element Getter Methods ---------------------------------------------- */
	public javax.swing.JMenuBar getBaseMenuBar()
	{
		if (baseMenuBar == null)
		{
			baseMenuBar = new javax.swing.JMenuBar();
			baseMenuBar.setName("baseMenuBar");
			baseMenuBar.add(getMenu_File());
			baseMenuBar.add(getMenu_Edit());
			baseMenuBar.add(getMenu_LookAndFeel());
			baseMenuBar.add(getMenu_Help());
		}
		return baseMenuBar;
	}
	public javax.swing.JMenu getMenu_File()
	{
		if (menu_File == null)
		{
			menu_File = new javax.swing.JMenu();
			menu_File.setName("menu_File");
			menu_File.setMnemonic('f');
			menu_File.setText("File");
			menu_File.setFont(Helpers.DIALOG_FONT);                        
                        menu_File.add(getMenu_File_openproject());
                        menu_File.add(getMenu_File_saveproject());                        
                        menu_File.add(getMenu_File_saveasproject());                        
                        menu_File.add(getMenu_File_exportproject());                     
                        menu_File.add(getMenu_File_xpudt());                     
                        
			menu_File.add(getMenu_File_openMod());                        
	
			
			menu_File.add(getMenu_File_RecentFiles());
			menu_File.add(new javax.swing.JSeparator());
                        menu_File.add(getMenu_File_newproject());
			menu_File.add(getMenu_File_Close());
		}
		return menu_File;
	}
	public javax.swing.JMenu getMenu_Edit()
	{
		if (menu_Edit == null)
		{
			menu_Edit = new javax.swing.JMenu();
			menu_Edit.setName("menu_View");
			menu_Edit.setText("Edit");
			menu_Edit.setFont(Helpers.DIALOG_FONT);
	//		menu_View.add(getMenu_View_Info());

			
			menu_Edit.add(getMenu_View_GraphicEQ());
			menu_Edit.add(new javax.swing.JSeparator());
			menu_Edit.add(getMenu_View_UseSystemTray());
		}
		return menu_Edit;
	}
	public javax.swing.JMenu getMenu_LookAndFeel()
	{
		if (menu_LookAndFeel == null)
		{
			menu_LookAndFeel = new javax.swing.JMenu();
			menu_LookAndFeel.setName("menu_LookAndFeel");
			menu_LookAndFeel.setMnemonic('l');
			menu_LookAndFeel.setText("Look&Feel");
			menu_LookAndFeel.setFont(Helpers.DIALOG_FONT);
			
			String currentUIClassName = javax.swing.UIManager.getLookAndFeel().getClass().getName();
			javax.swing.UIManager.LookAndFeelInfo [] lookAndFeels = getInstalledLookAndFeels();
			menu_LookAndFeel_Items = new javax.swing.JCheckBoxMenuItem[lookAndFeels.length];
			for (int i=0; i<lookAndFeels.length; i++)
			{
				menu_LookAndFeel_Items[i] = new javax.swing.JCheckBoxMenuItem();
				menu_LookAndFeel_Items[i].setName("newMenuItem_"+i);
				menu_LookAndFeel_Items[i].setText(lookAndFeels[i].getName());
				menu_LookAndFeel_Items[i].setFont(Helpers.DIALOG_FONT);
				menu_LookAndFeel_Items[i].setToolTipText("Change to " + lookAndFeels[i].getName() + " look and feel");
				String uiClassName = lookAndFeels[i].getClassName();
				if (uiClassName.equals(currentUIClassName)) menu_LookAndFeel_Items[i].setSelected(true);
				menu_LookAndFeel_Items[i].addActionListener(new LookAndFeelChanger(menu_LookAndFeel_Items[i], uiClassName));
				menu_LookAndFeel.add(menu_LookAndFeel_Items[i]);
			}
			
		}
		return menu_LookAndFeel;
	}
	private javax.swing.JMenu getMenu_Help()
	{
		if (menu_Help == null)
		{
			menu_Help = new javax.swing.JMenu();
			menu_Help.setName("menu_Help");
			menu_Help.setMnemonic('h');
			menu_Help.setText("Help");
			menu_Help.setFont(Helpers.DIALOG_FONT);
			menu_Help.add(new javax.swing.JSeparator());
			menu_Help.add(getMenu_Help_About());
		}
		return menu_Help;
	}
        private javax.swing.JMenuItem getMenu_File_newproject()
        {
            if (menu_File_newp == null)
		{
			menu_File_newp = new javax.swing.JMenuItem();
			menu_File_newp.setName("menu_File_new_Project");
			menu_File_newp.setMnemonic('l');
			menu_File_newp.setText("Clear");
			menu_File_newp.setFont(Helpers.DIALOG_FONT);
			menu_File_newp.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{try{
                                    d3cPanel.i=-1;
                                    d3cPanel.tokens=null;
                                    Helpers.filename=null;
                                    justexported=false;
                                    topic=null;
                                    d3cPanel.jTextArea1.setText("");
				//	Helpers.filename=JOptionPane.showInputDialog("Enter New project Name");
                                }catch(Exception ee){}
				}
			});
		}
		return menu_File_newp;
        }
	private javax.swing.JMenuItem getMenu_File_openproject()
        {
            if (menu_File_openp == null)
		{
			menu_File_openp = new javax.swing.JMenuItem();
			menu_File_openp.setName("menu_File_open_Project");
			menu_File_openp.setMnemonic('o');
			menu_File_openp.setText("Open Project");
			menu_File_openp.setFont(Helpers.DIALOG_FONT);
			menu_File_openp.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
                                    try{
                                        d3cPanel.i=-1;
                                        justexported=false;
					String fp=doOpenFile2("Open Project","Open");
                                        Helpers.filename=fp;
                                        FileInputStream fin = new FileInputStream(fp);
                                        ObjectInputStream ois = new ObjectInputStream(fin);
                                        SavedObj sv = (SavedObj) ois.readObject();
                                        d3cPanel.tokens =sv.tkns;
                                        String txt="";
                                        for(int i=0;i<sv.tkns.length;i++)
                                        {
                                            txt+=sv.tkns[i][1]+" ";
                                        }
                                        d3cPanel.jTextArea1.setText(txt);
                                    } catch(Exception erlo){}
                                        
				}
			});
		}
		return menu_File_openp;
        }
        private javax.swing.JMenuItem getMenu_File_xpudt()
        {
            if (menu_File_xpudt == null )
		{
                    menu_File_xpudt = new javax.swing.JMenuItem();
	            menu_File_xpudt.setName("menu_File_export_update");
			menu_File_xpudt.setMnemonic('d');
			menu_File_xpudt.setText("Export Update");
			menu_File_xpudt.setFont(Helpers.DIALOG_FONT);
			menu_File_xpudt.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
                                    
	if(justexported)
        {
            File ff=new File(xpfile+"/index.html");
            try{
            ff.delete();
                    }
            catch(Exception nofile){}
                 parse();
        }
                }});}
            return menu_File_xpudt;
        
        }
        String topic;
        private void parse()
        {
            try{
                 String fname=Helpers.getFileName(xpfile);
                                    //String path=xpfile.substring(0,xpfile.length()-fname.length());                                    
                                     
                                    String tem1="<link rel=\"stylesheet\" href=\"./"+fname+"Files/css/jquery-ui.css\" />\n"
                                              +  "<link rel=\"stylesheet\" type=\"text/css\" href=\"./"+fname+"Files/css/style.css\" /> \n"
                                              +"<script src=\"./"+fname+"Files/jquery-1.9.1.min.js\"></script>\n"
                                              +"<script src=\"./"+fname+"Files/jquery-ui-1.10.3.custom.min.js\"></script>\n"
                                              +"<script type=\"text/javascript\" src=\"./"+fname+"Files/d3/d3.v2.js\"></script>\n";
                                    
    //                                JOptionPane.showMessageDialog(null, xpfile+"/index.html");
                                    
                                    Filecop.apptxt("ki1.txt", tem1, "ki2.txt", xpfile+"/index.html");                                   
                                    if(!justexported)
                                    {
                                    topic=JOptionPane.showInputDialog("Enter Topic");                                    
                                    }
                                    Filecop.apptxt("ki2_1.txt","<h1>"+HtmlEscape.escapeSpecial(topic)+"</h1>", "ki2_2.txt", xpfile+"/index.html");                                    
                                    Filecop.apptxt(null, "d3.xml(\"./"+fname+"files/svgs/s\"+nn+\".svg\", \"image/svg+xml\", function(xml) {", null, xpfile+"/index.html");
                                //    JOptionPane.showMessageDialog(null, "rale");
           //                         Filecop.apptxt("ki3.txt","<audio tabindex=\"0\" controls=true src=\"./"+fname+"Files/"+Helpers.getFileName(audiofile)+"\">" ,"ki4.txt",xpfile+"/index.html");                                    
                                      try{Filecop.apptxt("ki3.txt","<audio tabindex=\"0\" controls=true src=\"./"+fname+"Files/"+Helpers.getFileName(audiofile) +"\">" ,"ki4.txt",xpfile+"/index.html");
                                      }catch(Exception jj){Filecop.apptxt("ki3.txt","<audio tabindex=\"0\" controls=true src=\"./"+fname+"Files/"+ "dummyfile.mp3" +"\">" ,"ki4.txt",xpfile+"/index.html");}
                                      
                                      
                                      String tem2="",tem3="";
                                      for(int ci=0,cj=1;ci<d3cPanel.tokens.length;ci++)
                                      {
                                          if(d3cPanel.tokens[ci][1].compareToIgnoreCase("<bk>")!=0)
                                          tem2+="      <span class=\"A\"  dti=\""+ci+"\" dbn=\""+d3cPanel.tokens[ci][2] +"\" ddr=\""+ d3cPanel.tokens[ci][3] + "\">"+HtmlEscape.escapeSpecial(d3cPanel.tokens[ci][1])+"</span>      \n";
                                          else
                                              tem2+="<br /> \n";
                                          if(d3cPanel.tokens[ci][0].compareToIgnoreCase("1")==0 )
                                          {
                                              if(cj==1)                                                  
                                              tem3+="if(currentTime<="+d3cPanel.tokens[ci][2]+") ex("+cj+"); \n";
                                              else
                                                  tem3+="else if(currentTime <="+d3cPanel.tokens[ci][2]+") ex("+cj+"); \n";
                                              cj++;
                                          }
                                      }
                                    Filecop.apptxt(null, tem2, null,xpfile+"/index.html");    
                                    Filecop.apptxt("ki5.txt", tem3, "ki6.txt",xpfile+"/index.html");  
            }catch(Exception gg){}
                                    
        }
         public static void setUIFont (javax.swing.plaf.FontUIResource f){

    java.util.Enumeration keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
      Object key = keys.nextElement();
      Object value = UIManager.get (key);
      if (value instanceof javax.swing.plaf.FontUIResource)
        UIManager.put (key, f);
      }
    }
        private javax.swing.JMenuItem getMenu_File_exportproject()
        {
            if (menu_File_saveap == null)
		{
			menu_File_saveap = new javax.swing.JMenuItem();
			menu_File_saveap.setName("menu_File_export_Project");
			menu_File_saveap.setMnemonic('E');
			menu_File_saveap.setText("Export to");
			menu_File_saveap.setFont(Helpers.DIALOG_FONT);
			menu_File_saveap.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
                                    try{
			         xpfile=doOpenFile2("Exporting to HTML specify Directory Name","Export");
                                 String temp=Helpers.getFileName(xpfile);
                                 String css,d3,svgs,fz;
                                   //    JOptionPane.showMessageDialog(null, temp);                                       
                                     //  String str = audiofile.substring(0, audiofile.length()-temp.length());
                                       new File(xpfile).mkdir();
                                       new File(fz=xpfile+"/"+temp+"Files").mkdir();
                                       new File(css=xpfile+"/"+temp+"Files"+"/"+"css").mkdir();
                                       new File(svgs=xpfile+"/"+temp+"Files"+"/"+"svgs").mkdir();
                                       new File(d3=xpfile+"/"+temp+"Files"+"/"+"d3").mkdir();
                                       //Files.copy(Paths.get("/d3.v2.js"),Paths.get(d3) ,REPLACE_EXISTING);
                                       copy("d3/d3creator/d3c/main/gui/ressources/d3.v2.js",d3+"/d3.v2.js");
                                       copy("d3/d3creator/d3c/main/gui/ressources/jquery-ui-1.10.3.custom.min.js",fz+"/jquery-ui-1.10.3.custom.min.js");
                                       copy("d3/d3creator/d3c/main/gui/ressources/jquery-1.9.1.min.js",fz+"/jquery-1.9.1.min.js");
                                       copy("d3/d3creator/d3c/main/gui/ressources/style.css",css+"/style.css");
                                       copy("d3/d3creator/d3c/main/gui/ressources/jquery-ui.css",css+"/jquery-ui.css");
                                       String svgsrc=doOpenFile2("Path to SVG","Set");
                                       svgsrc=svgsrc.substring(0, svgsrc.length()-"s1.svg".length());
                                       
                                     //  JOptionPane.showMessageDialog(null, svgsrc);
                                       try{
                                           boolean bb=true;
                                           for(int pk=1;bb ;pk++)
                                           {
                                        //   JOptionPane.showMessageDialog(null,    svgsrc+"s"+pk+".svg");
                                        //   JOptionPane.showMessageDialog(null,   svgs+"/s"+pk+".svg");                                           
                                           bb=   copypa(svgsrc+"s"+pk+".svg",svgs+"/s"+pk+".svg");                                           
                                           }
                                           
                                       }catch(Exception dow){}
                                       try{
                                             String temp1=Helpers.getFileName(audiofile);
                                 //          JOptionPane.showMessageDialog(null, audiofile+" to");
                                 //          JOptionPane.showMessageDialog(null, "->"+fz+"/"+temp1);
                                           copypa(audiofile,fz+"/"+temp1);
                                           
                                       }catch(Exception audiofilenotfound){}
                                 try{
                               
                                    parse();
                                    justexported=true;
                                           
                                    
                                    }catch(Exception err3){ 
                                 }

                                 //      JOptionPane.showConfirmDialog(null, fname);
                                 //      JOptionPane.showConfirmDialog(null, path);
                                    }catch(Exception err){}
                           
				}
			});
		}
		return menu_File_saveap;
        }
        
        static boolean copy(String resource, String destination) {
        InputStream resStreamIn = MainForm.class.getClassLoader().getResourceAsStream(resource);
        File resDestFile = new File(destination);
        try {
            OutputStream resStreamOut = new FileOutputStream(resDestFile);
            int readBytes;
            byte[] buffer = new byte[4096];
            while ((readBytes = resStreamIn.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            resStreamIn.close();
            resStreamOut.close();
            

        } catch (Exception ex) { JOptionPane.showMessageDialog(null, ex);
        return false;
        }
return true;
    }
           static boolean copypa(String resource, String destination) {
        try {
        InputStream resStreamIn = new FileInputStream(resource);
        File resDestFile = new File(destination);
        
            OutputStream resStreamOut = new FileOutputStream(resDestFile);
            int readBytes;
            byte[] buffer = new byte[4096];
            while ((readBytes = resStreamIn.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            resStreamIn.close();
            resStreamOut.close();
            

        } catch (Exception ex) { //JOptionPane.showMessageDialog(null, ex);
        return false;
        }
return true;
    }
        private javax.swing.JMenuItem getMenu_File_saveasproject()
        {
            if (menu_File_saveasp == null)
		{
			menu_File_saveasp = new javax.swing.JMenuItem();
			menu_File_saveasp.setName("menu_File_save_Project");
			menu_File_saveasp.setMnemonic('a');
			menu_File_saveasp.setText("Save as");
			menu_File_saveasp.setFont(Helpers.DIALOG_FONT);
			menu_File_saveasp.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
                                    try{
                                    SavedObj sv=new SavedObj();
                                    sv.set(d3cPanel.tokens);
                                    String fp=doOpenFile2("File to be saved", "save");
                                    fp=fp+".d3c";
                                    Helpers.filename=fp;
                 //                   String fname=Helpers.getFileName(fp);
                   //                 String path=fp.substring(0,fp.length()-fname.length());                                    
                                     try{
                                    FileOutputStream fout = new FileOutputStream(fp);
                                    ObjectOutputStream oos = new ObjectOutputStream(fout);   
                                    oos.writeObject(sv);
                                    oos.close();		 
                                    }catch(Exception ex){
		   //ex.printStackTrace();
                                    }
                                    
             //                       JOptionPane.showMessageDialog(null, path);
               //                     JOptionPane.showMessageDialog(null, fname);
                                    
                                            
                                        
					//JOptionPane.showMessageDialog(null, "saved");
                                    }catch(Exception ekr){}
				}
			});
		}
		return menu_File_saveasp;
        }
        private javax.swing.JMenuItem getMenu_File_saveproject()
        {
            
           
            if (menu_File_savep == null)
		{
			menu_File_savep = new javax.swing.JMenuItem();
			menu_File_savep.setName("menu_File_save_Project");
			menu_File_savep.setMnemonic('s');
			menu_File_savep.setText("Save");
			menu_File_savep.setFont(Helpers.DIALOG_FONT);
			menu_File_savep.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
                                   
                                   //JOptionPane.showMessageDialog(null,Helpers.filename );
                                    try{
                                    if(Helpers.filename==null)                                    
                                        Helpers.filename=doOpenFile2("Please Save","save")+".d3c";
                                    
                                    SavedObj sv=new SavedObj();
                                    sv.set(d3cPanel.tokens);         
                                    String fp=Helpers.filename;
                                    try{
                                    FileOutputStream fout = new FileOutputStream(fp);
                                    ObjectOutputStream oos = new ObjectOutputStream(fout);   
                                    oos.writeObject(sv);
                                    oos.close();		 
                                    }catch(Exception ex){
		   //ex.printStackTrace();
                                    }
                                    
                           //         JOptionPane.showMessageDialog(null, path);
                            //        JOptionPane.showMessageDialog(null, fname);
                                    
                                            
                                        
				//	JOptionPane.showMessageDialog(null, "saved");
                                    }catch(Exception ekr){}
				}
			});
		}
		return menu_File_savep;
        }
        
        
	private javax.swing.JMenuItem getMenu_File_openMod()
	{
		if (menu_File_openMod == null)
		{
			menu_File_openMod = new javax.swing.JMenuItem();
			menu_File_openMod.setName("menu_File_openMod");
			menu_File_openMod.setMnemonic('l');
			menu_File_openMod.setText("Load Sound");
			menu_File_openMod.setFont(Helpers.DIALOG_FONT);
			menu_File_openMod.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doOpenFile();
				}
			});
		}
		return menu_File_openMod;
	}
	
	
	private javax.swing.JMenu getMenu_File_RecentFiles()
	{
		if (menu_File_RecentFiles == null)
		{
			menu_File_RecentFiles = new javax.swing.JMenu();
			menu_File_RecentFiles.setName("menu_File_RecentFiles");
			menu_File_RecentFiles.setMnemonic('r');
			menu_File_RecentFiles.setText("Recent files");
			menu_File_RecentFiles.setFont(Helpers.DIALOG_FONT);
			
			createRecentFileMenuItems();
		}
		return menu_File_RecentFiles;
	}
	private void createRecentFileMenuItems()
	{
		javax.swing.JMenu recent = getMenu_File_RecentFiles();
		recent.removeAll();
		for (int i=0, index=1; i<PROPERTY_LASTLOADED_MAXENTRIES; i++)
		{
			URL element = lastLoaded.get(i);
			if (element!=null)
			{
				String displayName = null;
				// convert to a local filename if possible (that looks better!)
				if (element.getProtocol().equalsIgnoreCase("file"))
				{
					try
					{
						File f = new File(element.toURI());
						displayName = f.getAbsolutePath();
					}
					catch (URISyntaxException ex)
					{
					}
				}
				
				if (displayName==null) displayName = lastLoaded.get(i).toString();
				javax.swing.JMenuItem lastLoadURL = new javax.swing.JMenuItem();
				lastLoadURL.setName("menu_File_RecentFiles_File"+i);
				lastLoadURL.setText(((index<10)?"  ":"") + (index++) + " " + displayName);
				lastLoadURL.setFont(Helpers.DIALOG_FONT);
				lastLoadURL.setToolTipText(element.toString());
				lastLoadURL.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						try
						{
							URL url = new URL(((javax.swing.JMenuItem)e.getSource()).getToolTipText());
							loadMultimediaOrPlayListFile(url);                                                        
                                                        String str=url.toString();
                                                        str=str.replace("%20"," ");
                                                        audiofile= str.substring(6,str.length());
                                                   //   JOptionPane.showMessageDialog(null, audiofile);
						}
						catch (Exception ex)
						{
							Log.error("Load recent error", ex);
						}
					}
				});
				recent.add(lastLoadURL);
			}
		}
	}
	private javax.swing.JMenuItem getMenu_File_Close()
	{
		if (menu_File_Close == null)
		{
			menu_File_Close = new javax.swing.JMenuItem();
			menu_File_Close.setName("menu_File_Close");
			menu_File_Close.setMnemonic('x');
			menu_File_Close.setText("Exit");
			menu_File_Close.setFont(Helpers.DIALOG_FONT);
			menu_File_Close.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doClose();
				}
			});
		}
		return menu_File_Close;
	}
	
	
	private javax.swing.JMenuItem getMenu_View_GraphicEQ()
	{
		if (menu_View_GraphicEQ == null)
		{
			menu_View_GraphicEQ = new javax.swing.JMenuItem();
			menu_View_GraphicEQ.setName("menu_View_GraphicEQ");
			menu_View_GraphicEQ.setMnemonic('e');
			menu_View_GraphicEQ.setText("Effect...");
			menu_View_GraphicEQ.setFont(Helpers.DIALOG_FONT);
			menu_View_GraphicEQ.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					getEffectDialog().setVisible(true);
				}
			});
		}
		return menu_View_GraphicEQ;
	}
	private javax.swing.JCheckBoxMenuItem getMenu_View_UseSystemTray()
	{
		if (menu_View_UseSystemTray == null)
		{
			menu_View_UseSystemTray = new javax.swing.JCheckBoxMenuItem();
			menu_View_UseSystemTray.setName("menu_View_UseSystemTray");
			menu_View_UseSystemTray.setMnemonic('t');
			menu_View_UseSystemTray.setText("Use system tray");
			menu_View_UseSystemTray.setFont(Helpers.DIALOG_FONT);
			menu_View_UseSystemTray.setEnabled(SystemTray.isSupported());
			menu_View_UseSystemTray.setSelected(useSystemTray);
			menu_View_UseSystemTray.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					useSystemTray = getMenu_View_UseSystemTray().isSelected();
					setSystemTray();
				}
			});
		}
		return menu_View_UseSystemTray;
	}
	
	
	private javax.swing.JMenuItem getMenu_Help_About()
	{
		if (menu_Help_About == null)
		{
			menu_Help_About = new javax.swing.JMenuItem();
			menu_Help_About.setName("menu_Help_About");
			menu_Help_About.setMnemonic('a');
			menu_Help_About.setText("About...");
			menu_Help_About.setFont(Helpers.DIALOG_FONT);
			menu_Help_About.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doShowAbout();
				}
			});
		}
		return menu_Help_About;
	}
	private MenuItem getAboutItem()
	{
		if (aboutItem==null)
		{
			aboutItem = new MenuItem("About");
			aboutItem.setFont(Helpers.DIALOG_FONT);
			aboutItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					doShowAbout();
				}
			});
		}
		return aboutItem;
	}
	private MenuItem getPlayItem()
	{
		if (playItem==null)
		{
			playItem = new MenuItem("Play");
			playItem.setFont(Helpers.DIALOG_FONT);
			playItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					doStartPlaying();
				}
			});
		}
		return playItem;
	}
	private MenuItem getPauseItem()
	{
		if (pauseItem==null)
		{
			pauseItem = new MenuItem("Pause");
			pauseItem.setFont(Helpers.DIALOG_FONT);
			pauseItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					doPausePlaying();
				}
			});
		}
		return pauseItem;
	}
	private MenuItem getStopItem()
	{
		if (stopItem==null)
		{
			stopItem = new MenuItem("Stop");
			stopItem.setFont(Helpers.DIALOG_FONT);
			stopItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					doStopPlaying();
				}
			});
		}
		return stopItem;
	}

	private MenuItem getCloseItem()
	{
		if (closeItem==null)
		{
			closeItem = new MenuItem("Close");
			closeItem.setFont(Helpers.DIALOG_FONT);
			closeItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					doClose();
				}
			});
		}
		return closeItem;
	}
	private TrayIcon getTrayIcon()
	{
		if (dc3TrayIcon==null)
		{
			final java.net.URL iconURL = MainForm.class.getResource(DEFAULTICONPATH);
			if (iconURL!=null)
			{
				dc3TrayIcon = new TrayIcon(java.awt.Toolkit.getDefaultToolkit().getImage(iconURL));
				dc3TrayIcon.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent e)
					{
						if (SwingUtilities.isLeftMouseButton(e))
						{
							final MainForm me = MainForm.this;
							me.setVisible(true);
							me.setExtendedState(me.getExtendedState() & ~ICONIFIED);
						}
					}
				});
				// Add components to pop-up menu
				final PopupMenu popUp = new PopupMenu();
				popUp.add(getAboutItem());
				popUp.addSeparator();
				popUp.add(getPlayItem());
				popUp.add(getPauseItem());
				popUp.add(getStopItem());
				
				popUp.addSeparator();
				popUp.add(getCloseItem());
				dc3TrayIcon.setPopupMenu(popUp);
			}
		}
		return dc3TrayIcon;
	}
	/**
	 * 
	 * @since 07.02.2013
	 */
	private void setSystemTray()
	{
		// Check the SystemTray is supported
		if (SystemTray.isSupported())
		{
			final SystemTray tray = SystemTray.getSystemTray();
			try
			{
				tray.remove(getTrayIcon());
				if (useSystemTray)
				{
					tray.add(getTrayIcon());
				}
			}
			catch (AWTException e)
			{
				Log.error("TrayIcon could not be added.", e);
			}
		}
	}
	public javax.swing.JPanel getBaseContentPane()
	{
		if (baseContentPane==null)
		{
			baseContentPane = new javax.swing.JPanel();
			baseContentPane.setName("baseContentPane");
			baseContentPane.setLayout(new java.awt.BorderLayout());                  
			baseContentPane.add(getMessages(), java.awt.BorderLayout.SOUTH);
			baseContentPane.add(getMainContentPane(), java.awt.BorderLayout.CENTER);
		}
		return baseContentPane;
	}
	public javax.swing.JTextField getMessages()
	{
		if (messages==null)
		{
			messages = new javax.swing.JTextField();
			messages.setName("messages");
			messages.setEditable(false);
			messages.setFont(Helpers.DIALOG_FONT);
		}
		return messages;
	}
	public javax.swing.JPanel getMainContentPane()
	{
		if (mainContentPane==null)
		{
			mainContentPane = new javax.swing.JPanel();
			mainContentPane.setName("mainContentPane");
			mainContentPane.setLayout(new java.awt.GridBagLayout());
			mainContentPane.add(getMusicDataPane(),		Helpers.getGridBagConstraint(0, 0, 1, 0, java.awt.GridBagConstraints.BOTH, java.awt.GridBagConstraints.CENTER, 0.0, 1.0));
		//	mainContentPane.add(getPlayerDataPane(),	Helpers.getGridBagConstraint(0, 1, 1, 0, java.awt.GridBagConstraints.BOTH, java.awt.GridBagConstraints.CENTER, 0.0, 1.0));
			mainContentPane.add(getPlayerControlPane(),	Helpers.getGridBagConstraint(0, 2, 1, 0, java.awt.GridBagConstraints.BOTH, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
		}
		return mainContentPane;
	}
	private SMCAbout getD3CAbout()
	{
		if (about == null)
		{
			about = new SMCAbout(this, true);
			about.addWindowFocusListener(makeMainWindowVisiable);
		}
		else
			about.setLocation(Helpers.getFrameCenteredLocation(about, this));
		return about;
	}
	
	public javax.swing.JDialog getEffectDialog()
	{
		if (equalizerDialog==null)
		{
			equalizerDialog = new JDialog(this, "Effect", false);
			equalizerDialog.setName("equalizerDialog");
			equalizerDialog.setSize(effectsDialogSize);
			equalizerDialog.setPreferredSize(effectsDialogSize);
			equalizerDialog.setContentPane(getEffectPane());
			if (effectsDialogLocation == null || (effectsDialogLocation.getX()==-1 || effectsDialogLocation.getY()==-1))
				effectsDialogLocation = Helpers.getFrameCenteredLocation(equalizerDialog, null); 
			equalizerDialog.setLocation(effectsDialogLocation);
			equalizerDialog.addWindowFocusListener(makeMainWindowVisiable);
		}
		return equalizerDialog;
	}
	public javax.swing.JDialog getPlayerSetUpDialog()
	{
		if (playerSetUpDialog==null)
		{
			playerSetUpDialog = new JDialog(this, "Configuration", false);
			playerSetUpDialog.setName("playerSetUpDialog");
			playerSetUpDialog.setSize(playerSetUpDialogSize);
			playerSetUpDialog.setPreferredSize(playerSetUpDialogSize);
		
			if (playerSetUpDialogLocation == null || (playerSetUpDialogLocation.getX()==-1 || playerSetUpDialogLocation.getY()==-1))
				playerSetUpDialogLocation = Helpers.getFrameCenteredLocation(playerSetUpDialog, null); 
			playerSetUpDialog.setLocation(playerSetUpDialogLocation);
			playerSetUpDialog.addWindowFocusListener(makeMainWindowVisiable);
		}
		return playerSetUpDialog;
	}
	
	
	

	
	public javax.swing.JPanel getEffectPane()
	{
		if (effectPane==null)
		{
			effectPane = new javax.swing.JPanel();
			effectPane.setName("effectPane");
			effectPane.setLayout(new java.awt.BorderLayout());
			effectPane.setBorder(new TitledBorder(null, "Effects", TitledBorder.LEADING, TitledBorder.TOP, Helpers.DIALOG_FONT, null));
			effectPane.add(getEffectsPanel());
		}
		return effectPane;
	}

	private PitchShiftGUI getPitchShiftGui()
	{
		if (pitchShiftGUI==null)
		{
			pitchShiftGUI = new PitchShiftGUI(currentPitchShift);
		}
		return pitchShiftGUI;
	}
	public EffectsPanel getEffectsPanel()
	{
		if (effectGUI==null)
		{
			javax.swing.JPanel [] effectPanels = 
			{
			 	null,
			 	getPitchShiftGui()
			};
			effectGUI = new EffectsPanel(effectPanels, audioProcessor);
		}
		return effectGUI;
	}
	
	/**
	 * @since 10.12.2013
	 * @return
	 */
	public PlayerConfigPanel getPlayerConfigPanel()
	{
		if (playerConfigPanel==null)
		{
			playerConfigPanel = new PlayerConfigPanel();
		}
		return playerConfigPanel;
	}
	
	
	public javax.swing.JPanel getMusicDataPane()
	{
		if (musicDataPane==null)
		{
			musicDataPane = new javax.swing.JPanel();
			musicDataPane.setName("musicDataPane");
			musicDataPane.setLayout(new java.awt.GridBagLayout());
			musicDataPane.setBorder(new TitledBorder(null, "Editor ", TitledBorder.LEADING, TitledBorder.TOP, Helpers.DIALOG_FONT, null));
			
			musicDataPane.add(getSMCPanel(), Helpers.getGridBagConstraint(0, 0, 1, 0, java.awt.GridBagConstraints.CENTER, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
		}
		return musicDataPane;
	}
	public SMCPanel getSMCPanel()
	{

		if (d3cPanel==null)
		{
			d3cPanel = new SMCPanel();
			Dimension d = new Dimension(800, 400);
			d3cPanel .setSize(d);
			d3cPanel .setMaximumSize(d);
			d3cPanel .setMinimumSize(d);
			d3cPanel .setPreferredSize(d);
			//d3cPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		}
		return d3cPanel;
	}
	public javax.swing.JPanel getPlayerDataPane()
	{
		if (playerDataPane==null)
		{
			playerDataPane = new javax.swing.JPanel();
			playerDataPane.setName("playerDataPane");
			playerDataPane.setLayout(new java.awt.GridBagLayout());
			playerDataPane.setBorder(new TitledBorder(null, "Player Data", TitledBorder.LEADING, TitledBorder.TOP, Helpers.DIALOG_FONT, null));
			
			
		}
		return playerDataPane;
	}
	public javax.swing.JPanel getPlayerControlPane()
	{
		if (playerControlPane==null)
		{
			playerControlPane = new javax.swing.JPanel();
			playerControlPane.setName("playerControlPane");
			playerControlPane.setLayout(new java.awt.GridBagLayout());
			playerControlPane.setBorder(new TitledBorder(null, "Player Control", TitledBorder.LEADING, TitledBorder.TOP, Helpers.DIALOG_FONT, null));		
			playerControlPane.add(getButton_Play(),		Helpers.getGridBagConstraint(1, 0, 2, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
		
			playerControlPane.add(getButton_Pause(),	Helpers.getGridBagConstraint(3, 0, 2, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
			playerControlPane.add(getButton_Stop(),		Helpers.getGridBagConstraint(4, 0, 2, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
			playerControlPane.add(getVolumeSlider(),	Helpers.getGridBagConstraint(5, 0, 1, 1, java.awt.GridBagConstraints.VERTICAL, java.awt.GridBagConstraints.CENTER, 0.0, 1.0));
			
			playerControlPane.add(getVolumeLabel(),		Helpers.getGridBagConstraint(5, 1, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
			
			playerControlPane.add(getSeekBarPanel(),	Helpers.getGridBagConstraint(0, 2, 1, 0, java.awt.GridBagConstraints.BOTH, java.awt.GridBagConstraints.CENTER, 1.0, 1.0));
		}
		return playerControlPane;
	}
	private SeekBarPanel getSeekBarPanel()
	{
		if (seekBarPanel==null)
		{
			seekBarPanel = new SeekBarPanel(30, false);
			seekBarPanel.setName("SeekBarPanel");
			seekBarPanel.addListener(new SeekBarPanelListener()
			{
				@Override
				public void valuesChanged(long milliseconds)
				{
					if (currentPlayingFile!=null && playerThread!=null && playerThread.isRunning()) 
						currentPlayingFile.setCurrentElementByTimeIndex(milliseconds);
				}
			});
		}
                d3cPanel.initSBP(seekBarPanel);
		return seekBarPanel;
	}
	private javax.swing.JButton getButton_Play()
	{
		if (button_Play == null)
		{
			buttonPlay_normal = new javax.swing.ImageIcon(getClass().getResource(BUTTONPLAY_NORMAL));
			buttonPlay_Inactive = new javax.swing.ImageIcon(getClass().getResource(BUTTONPLAY_INACTIVE));
			buttonPlay_Active = new javax.swing.ImageIcon(getClass().getResource(BUTTONPLAY_ACTIVE));

			button_Play = new javax.swing.JButton();
			button_Play.setName("button_Play");
			button_Play.setText("");
			button_Play.setToolTipText("play");
			button_Play.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			button_Play.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			button_Play.setIcon(buttonPlay_normal);
			button_Play.setDisabledIcon(buttonPlay_Inactive);
			button_Play.setPressedIcon(buttonPlay_Active);
			button_Play.setMargin(new java.awt.Insets(10, 10, 10, 10));
			button_Play.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doStartPlaying();
				}
			});
		}
		return button_Play;
	}
	private javax.swing.JButton getButton_Pause()
	{
		if (button_Pause == null)
		{
			buttonPause_normal = new javax.swing.ImageIcon(getClass().getResource(BUTTONPAUSE_NORMAL));
			buttonPause_Inactive = new javax.swing.ImageIcon(getClass().getResource(BUTTONPAUSE_INACTIVE));
			buttonPause_Active = new javax.swing.ImageIcon(getClass().getResource(BUTTONPAUSE_ACTIVE));

			button_Pause = new javax.swing.JButton();
			button_Pause.setName("button_Pause");
			button_Pause.setText("");
			button_Pause.setToolTipText("pause");
			button_Pause.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			button_Pause.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			button_Pause.setIcon(buttonPause_normal);
			button_Pause.setDisabledIcon(buttonPause_Inactive);
			button_Pause.setPressedIcon(buttonPause_Active);
			button_Pause.setMargin(new java.awt.Insets(10, 10, 10, 10));
			button_Pause.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doPausePlaying();
				}
			});
		}
		return button_Pause;
	}
	private javax.swing.JButton getButton_Stop()
	{
		if (button_Stop == null)
		{
			buttonStop_normal = new javax.swing.ImageIcon(getClass().getResource(BUTTONSTOP_NORMAL));
			buttonStop_Inactive = new javax.swing.ImageIcon(getClass().getResource(BUTTONSTOP_INACTIVE));
			buttonStop_Active = new javax.swing.ImageIcon(getClass().getResource(BUTTONSTOP_ACTIVE));

			button_Stop = new javax.swing.JButton();
			button_Stop.setName("button_Stop");
			button_Stop.setText("");
			button_Stop.setToolTipText("stop");
			button_Stop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			button_Stop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			button_Stop.setIcon(buttonStop_normal);
			button_Stop.setDisabledIcon(buttonStop_Inactive);
			button_Stop.setPressedIcon(buttonStop_Active);
			button_Stop.setMargin(new java.awt.Insets(10, 10, 10, 10));
			button_Stop.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doStopPlaying();
				}
			});
		}
		return button_Stop;
	}
	
	public javax.swing.JLabel getVolumeLabel()
	{
		if (volumeLabel==null)
		{
			volumeLabel = new JLabel("Volume");
			volumeLabel.setFont(Helpers.DIALOG_FONT);
		}
		return volumeLabel;
	}
	public RoundSlider getVolumeSlider()
	{
		if (volumeSlider==null)
		{
			volumeSlider = new RoundSlider();
			volumeSlider.setSize(new Dimension(50,50));
			volumeSlider.setMinimumSize(new Dimension(20,20));
			volumeSlider.setMaximumSize(new Dimension(50,50));
			volumeSlider.setPreferredSize(new Dimension(50,50));
			volumeSlider.setValue(currentVolume);
			volumeSlider.setToolTipText(Float.toString(currentVolume*100f) + '%');
			volumeSlider.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					RoundSlider slider = (RoundSlider) e.getSource();
					if (e.getClickCount()==2)
					{
						slider.setValue(0.5f);
					}
				}
			});
			volumeSlider.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent e)
				{
					RoundSlider slider = (RoundSlider) e.getSource();
					currentVolume = slider.getValue();
					if (currentVolume<0) currentVolume=0;
					else
					if (currentVolume>1) currentVolume=1;
					slider.setToolTipText(Float.toString(currentVolume*100f) + '%');
					doSetVoumeValue();
				}
			});
		}
		return volumeSlider;
	}
		/* DspAudioProcessor CallBack -------------------------------------------*/
	public void currentSampleChanged(float [] leftSample, float [] rightSample)
	{
	
	}
	public void multimediaContainerEventOccured(MultimediaContainerEvent event)
	{
	////	if (event.getType() == MultimediaContainerEvent.SONG_NAME_CHANGED)
	//		getLEDScrollPanel().addScrollText(event.getEvent().toString() + Helpers.SCROLLY_BLANKS);
//		else
//		if (event.getType() == MultimediaContainerEvent.SONG_NAME_CHANGED_OLD_INVALID)
//			getLEDScrollPanel().setScrollTextTo(event.getEvent().toString() + Helpers.SCROLLY_BLANKS);
//		getTrayIcon().setToolTip(event.getEvent().toString());
	}
	/**
	 * @param thread
	 * @see sm.smcreator.smc.main.gui.PlayThreadEventListener#playThreadEventOccured(sm.smcreator.smc.main.gui.PlayThread)
	 */
	public void playThreadEventOccured(PlayThread thread)
	{
		if (thread.isRunning())
		{
			getButton_Play().setIcon(buttonPlay_Active);
		}
		else // Signaling: not running-->Piece finished...
		{
			getButton_Play().setIcon(buttonPlay_normal);
			if (thread.getHasFinishedNormaly())
			{
			 doStopPlaying();
			}
		}
		
		Mixer mixer = thread.getCurrentMixer();
		if (mixer!=null)
		{
			if (mixer.isPaused())
				getButton_Pause().setIcon(buttonPause_Active);
			else
				getButton_Pause().setIcon(buttonPause_normal);
		}
	}
	private void setPlayListIcons()
	{
		
	}
	/* EVENT METHODS --------------------------------------------------------*/
	/**
	 * Default Close Operation
	 * @since 22.06.2013
	 */
	private void doClose()
	{
		// set visible, if system tray active and frame is iconified
		if (useSystemTray && (getExtendedState()&ICONIFIED)!=0) setVisible(true);
		
		doStopPlaying();
		getSeekBarPanel().pauseThread();
	
	//	getSMCPanel().pauseThread();
		writePropertyFile();
		if (audioProcessor!=null) audioProcessor.removeListener(this);
		
		MultimediaContainerManager.removeMultimediaContainerEventListener(this);

		useSystemTray = false; setSystemTray();

		getD3CAbout().setVisible(false);
	
		getPlayerSetUpDialog().setVisible(false);
	
		getShowVersion_Text().setVisible(false);

		getEffectDialog().setVisible(false);
		setVisible(false);

		Log.removeLogListener(this);
		
		getD3CAbout().dispose();
	
		getPlayerSetUpDialog().dispose();

		getShowVersion_Text().dispose();

		getEffectDialog().dispose();
		dispose();
		
		//System.exit(0); // this should not be needed! 
	}
	/**
	 * Open a new ModFile
	 * @since 22.06.2013
	 */
	private void doOpenFile()
	{
		FileChooserResult selectedFile = Helpers.selectFileNameFor(this, searchPath, "Load a Sound-File", fileFilterLoad, 0, true);
		if (selectedFile!=null) 
			doOpenFile(selectedFile.getSelectedFiles());
                audiofile= selectedFile.getSelectedFile().toString();
              // JOptionPane.showMessageDialog(null, audiofile);
	}
	private String doOpenFile2(String s1,String s2)
	{
            final JFileChooser fc = new JFileChooser();
            fc.setDialogTitle(s1);
            fc.setApproveButtonText(s2);
            int returnVal=fc.showOpenDialog(about);
            String str=null;
             if (returnVal == JFileChooser.APPROVE_OPTION)
             { 
             str = fc.getSelectedFile().toString();
          //   JOptionPane.showMessageDialog(null, str);
             }
          //   else
            //     JOptionPane.showMessageDialog(null,"Error in this Operation");  
           return str;             
	}
        public void doOpenFile(File[] files)
	{
	    if (files!=null)
	    {
	    	if (files.length==1)
	    	{
	    		File f = files[0];
		    	if (f.isFile())
		    	{
			    	String modFileName = f.getAbsolutePath();
			    	int i = modFileName.lastIndexOf(File.separatorChar);
			    	searchPath = modFileName.substring(0, i);
		    		loadMultimediaOrPlayListFile(Helpers.createURLfromFile(f)); 
		    	}
		    	else
		    	if (f.isDirectory())
		    	{
			    	searchPath = f.getAbsolutePath();
		    	}
	    	}
	    	else
	    	{
	    		playlistRecieved(null, PlayList.createNewListWithFiles(files, false, false), null);
	    	}
	    }
	}
	/**
	 * Open a new File
	 * @since 22.06.2013
	 */

	/**
	 * Open a new ModFile
	 * @since 17.10.2007
	 */
	
	/**
	 * Open a new File
	 * @since 22.06.2013
	 */
	
	/**
	 * Exports to a Wavefile
	 * @since 01.06.2013
	 */
	
	private SimpleTextViewerDialog getShowVersion_Text()
	{
		if (simpleTextViewerDialog==null)
		{
			simpleTextViewerDialog = new SimpleTextViewerDialog(this, true);
		}
		simpleTextViewerDialog.setLocation(Helpers.getFrameCenteredLocation(simpleTextViewerDialog, this));
		return simpleTextViewerDialog;
	}
	
	private void doShowAbout()
	{
		getD3CAbout().setVisible(true);
	}
	/**
	 * start playback of a audio file
	 * @since 01.06.2013
	 */
	public void doStartPlaying()
	{
		doStartPlaying(false, 0);
	}
	/**
	 * @param initialSeek
	 * @since 13.02.2013
	 */
        public SeekBarPanel getSBP()
        {
            return seekBarPanel;
        }
	public void doStartPlaying(boolean reuseMixer, long initialSeek)
	{
           // JOptionPane.showMessageDialog(null, "hai"+seekBarPanel.timeTextField.getText());
            d3cPanel.jTextArea1.requestFocusInWindow();
		if (currentContainer!=null)
		{
			if (playerThread!=null && !reuseMixer)
			{
				playerThread.stopMod();
				removeMixer();
				playerThread = null;
			}
			
			if (playerThread == null)
			{
				Mixer mixer = createNewMixer();
				if (mixer!=null)
				{
					if (initialSeek>0) mixer.setMillisecondPosition(initialSeek);
					playerThread = new PlayThread(mixer, this);
                                        //syncThread =new Sync();
                                        //syncThread.start();
					playerThread.start();
                            //            JOptionPane.showMessageDialog(null, "started");
				}
			}
			else
			{
                            
				playerThread.getCurrentMixer().setMillisecondPosition(initialSeek);
                                
			}
		}
	}
	/**
	 * stop playback of a mod
	 * @since 01.06.2013
	 */
	private void doStopPlaying()
	{
		if (playerThread!=null)
		{
			playerThread.stopMod();
                      //SMCPanel.jTextArea1.removeKeyListener(syncThread.l);
                    //  JOptionPane.showMessageDialog(null, "aaaa");
			getSoundOutputStream().closeAllDevices();
			playerThread = null;
			removeMixer();
		}
	}
	/**
	 * pause the playing of a mod
	 * @since 01.06.2013
	 */
	private void doPausePlaying()
	{
           //JOptionPane.showMessageDialog(null, "paused at"+seekBarPanel.timeTextField.getText()); 
		if (playerThread!=null)
		{
			playerThread.pausePlay();
		}
	}
	private boolean doNextPlayListEntry()
	{
		boolean ok = false;
		while (currentPlayingFile!=null && currentPlayingFile.hasNext() && !ok)
		{
			currentPlayingFile.next();
			ok = loadMultimediaFile(currentPlayingFile.getCurrentEntry());
		}
		return ok;
	}

	/**
	 * 
	 * @see sm.smcreator.smc.main.gui.playlist.PlaylistGUIChangeListener#userSelectedPlaylistEntry()
	 * @since 13.02.2013
	 */

	private void doSetVoumeValue()
	{
		if (playerThread!=null)
		{
			Mixer currentMixer = playerThread.getCurrentMixer();
			currentMixer.setVolume(currentVolume);
		}
	}
	
	private SoundOutputStream getSoundOutputStream()
	{
		if (soundOutputStream==null)
		{
			soundOutputStream = new GaplessSoundOutputStreamImpl();
		}
		return soundOutputStream;
	}
	/**
	 * Creates a new Mixer for playback
	 * @since 01.06.2013
	 * @return
	 */
	private Mixer createNewMixer()
	{
		Mixer mixer = getCurrentContainer().createNewMixer();
		if (mixer!=null)
		{
			mixer.setAudioProcessor(audioProcessor);
			mixer.setVolume(currentVolume);		
			mixer.setSoundOutputStream(getSoundOutputStream());
			getSeekBarPanel().setCurrentMixer(mixer);
		}
		return mixer;
	}
	private void removeMixer()
	{
		getSeekBarPanel().setCurrentMixer(null);
	}
	/**
	 * @since 14.09.2013
	 * @param mediaPLSFileURL
	 */
	private boolean loadMultimediaOrPlayListFile(URL mediaPLSFileURL)
	{
		Log.info("");
		addFileToLastLoaded(mediaPLSFileURL);
		
    	try
    	{
   			currentPlayingFile = PlayList.createFromFile(mediaPLSFileURL, false, false);//imp
   			if (currentPlayingFile!=null) //important
   			{	
   				//getPlaylistGUI().setNewPlaylist(currentPlayingFile);
   				return doNextPlayListEntry();
   			}
    	}
    	catch (Throwable ex)
    	{
			Log.error("[MainForm::loadMultimediaOrPlayListFile]", ex);
			currentPlayingFile = null;
    	}
    	return false;
	}
	/**
	 * load a mod file and display it
	 * @since 01.06.2013
	 * @param modFileName
	 * @return boolean if loading succeeded
	 */
	private boolean loadMultimediaFile(PlayListEntry playListEntry)
	{
		final URL mediaFileURL = playListEntry.getFile();
		final boolean reuseMixer = (currentContainer!=null &&
									Helpers.isEqualURL(currentContainer.getFileURL(), mediaFileURL) &&
									playerThread!=null && playerThread.isRunning());
		if (!reuseMixer)
		{
	    	try
	    	{
	    		if (mediaFileURL!=null)
	    		{
	    			MultimediaContainer newContainer = MultimediaContainerManager.getMultimediaContainer(mediaFileURL);
	    			if (newContainer!=null)
	    			{
	    				currentContainer = newContainer;
	//        			getLEDScrollPanel().setScrollTextTo(currentContainer.getSongName() + Helpers.SCROLLY_BLANKS);
	        			getTrayIcon().setToolTip(currentContainer.getSongName());
	    			}
	    		}
	    	}
	    	catch (Throwable ex)
	    	{
				Log.error("[MainForm::loadMultimediaFile] Loading of " + mediaFileURL + " failed!", ex);
				return false;
	    	}
			
			changeConfigPane();
		}
		setPlayListIcons();
		// if we are currently playing, start the current piece:
		if (playerThread!=null) doStartPlaying(reuseMixer, playListEntry.getTimeIndex());
		return true;
	}
	/**
	 * @since 14.09.2013
	 * @param url
	 */
	private void addFileToLastLoaded(URL url)
	{
		if (lastLoaded.contains(url)) lastLoaded.remove(url);
		lastLoaded.add(0, url);
		createRecentFileMenuItems();
	}
	/**
	 * @since 14.09.2013
	 * @return
	 */
	private MultimediaContainer getCurrentContainer()
	{
		if (currentContainer == null)
		{
			try
			{
//				currentContainer = MultimediaContainerManager.getMultimediaContainerForType("mod");
			}
			catch (Exception ex)
			{
				Log.error("getCurrentContainer()", ex);
			}
		}
		return currentContainer;
	}
	/**
	 * Shows the given Message
	 * @since 22.06.2013
	 * @param msg
	 */
	private synchronized void showMessage(final String msg)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				getMessages().setText(msg);
			}
		});
	}
	/**
	 * Shows the errormessage
	 * 
	 * @since 22.06.2013
	 * @param error
	 */
//	private void showMessage(Throwable ex)
//	{
//		showMessage(ex.toString());
//		ex.printStackTrace(System.err);
//	}
	/**
	 * @param message
	 * @param ex
	 * @see sm.smcreator.smc.system.LogMessageCallBack#error(java.lang.String, java.lang.Throwable)
	 */
	public void error(String message, Throwable ex)
	{
		if (ex!=null)
		{
			showMessage(message+'|'+ex.toString());
			ex.printStackTrace(System.err);
		}
		else
			showMessage(message);
	}
	/**
	 * @param message
	 * @see sm.smcreator.smc.system.LogMessageCallBack#info(java.lang.String)
	 */
	public void info(String message)
	{
		showMessage(message);
	}
}