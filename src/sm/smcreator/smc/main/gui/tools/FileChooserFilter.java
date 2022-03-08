/*
 * @(#) FileChooserFilter.java
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
package sm.smcreator.smc.main.gui.tools;

import javax.swing.filechooser.FileFilter;

/**
 * @author Anil Kishan
 * @since 22.06.2013
 * Defines a file chooser filter for the <CODE>JFileChooser</CODE> component
 */
public class FileChooserFilter extends FileFilter
{
	private java.util.ArrayList<String> mExtensions = new java.util.ArrayList<String>();
	private java.lang.String mDescription;

	/**
	 * FileChooserFilter - Konstruktorkommentar.
	 */
	public FileChooserFilter()
	{
		super();
	}
	/** 
	 * FileChooserFilter - Konstruktorkommentar.
	 * @param extension java.lang.String
	 * @param description java.lang.String
	 */
	public FileChooserFilter(String extension, String description)
	{
		this();
		mExtensions.add(extension.toLowerCase());
		mDescription = (description == null ? extension + " files" : description);
	}
	/** 
	 * FileChooserFilter - Konstruktorkommentar.
	 * @param extension java.lang.String
	 * @param description java.lang.String
	 */
	public FileChooserFilter(String extension)
	{
		this(extension, null);
	}
	/** 
	 * FileChooserFilter - Konstruktorkommentar.
	 * @param extension java.lang.String
	 * @param description java.lang.String
	 */
	public FileChooserFilter(String[] extensions, String description)
	{
		this();
		if (description != null)
			mDescription = description;
		else
			mDescription = "";
		for (int i = 0; i < extensions.length; i++)
		{
			String suffix = extensions[i].toLowerCase();
			if (description == null) mDescription += "*." + suffix + " ";
			mExtensions.add(extensions[i].toLowerCase());
		}
	}
	/** 
	 * FileChooserFilter - Konstruktorkommentar.
	 * @param extension java.lang.String
	 * @param description java.lang.String
	 */
	public FileChooserFilter(String[] extensions)
	{
		this(extensions, null);
	}
	/** 
	 * Whether the given file is accepted by this filter.
	 * @param f java.io.File
	 * @return boolean
	 */
	@Override
	public boolean accept(java.io.File f)
	{
		if (f.isDirectory()) return true;
		int len = mExtensions.size();
		if (len == 0) return true;
		for (int i=0; i<len; i++)
		{
			String suffix = mExtensions.get(i);
			if (f.getName().toLowerCase().endsWith('.' + suffix)) return true;
			if (f.getName().toLowerCase().startsWith(suffix + '.')) return true; // Amiga Mods *start* with suffix, e.g. "mod.songname"
		}
		return false;
	}
	/**
	 * The description of this filter. For example: "JPG and GIF Images"
	 * @return java.lang.String
	 */
	@Override
	public String getDescription()
	{
		return mDescription;
	}
}
