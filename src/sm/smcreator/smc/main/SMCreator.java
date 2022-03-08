/* aug 1
 * @(#) SMCreator.java
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
package sm.smcreator.smc.main;

import java.io.File;

import sm.smcreator.smc.main.gui.MainForm;
import sm.smcreator.smc.system.Helpers;

/**
 * @author Anil Kishan
 * @since 22.06.2013
 */
public class SMCreator extends SMCreatorMainBase
{
	/**
	 * Constructor for SMCreator
	 */
	public SMCreator()
	{
		super(true);
	}

	
	
	public static void main(final String[] args)
	{
		java.awt.EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				MainForm mainForm = new MainForm();
				Helpers.setCoding(true);
				mainForm.setVisible(true);				
			}
		});
              /* java.awt.EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				MainForm mainForm = new MainForm();
				Helpers.setCoding(true);
				mainForm.setVisible(true);				
			}
		}); */
	}
}
