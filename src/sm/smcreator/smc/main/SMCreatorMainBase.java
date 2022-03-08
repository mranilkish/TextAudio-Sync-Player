/*
 * @(#) SMCreatorMainBase.java
 * 
 * Created on 08.02.2018 by Anil Kishan
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

import sm.smcreator.smc.system.Helpers;
import sm.smcreator.smc.system.Log;


/**
 * @author Anil Kishan
 * @since 08.02.2018
 */
public class SMCreatorMainBase
{
	static
	{
		// Now load and initialize all classes, that should not be
		// initialized during play!
		try
		{
			Helpers.registerAllClasses();
		}
		catch (ClassNotFoundException ex)
		{
			Log.error("SMCreatorMainBase: a class moved?!", ex);
			System.exit(3);
		}
	}
	/**
	 * Constructor for SMCreatorMainBase
	 */
	public SMCreatorMainBase(boolean gui)
	{
		super();
		Helpers.setCoding(gui);
	}
}
