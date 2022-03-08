/*
 * @(#) Log.java
 * 
 * Created on 21.04.2006 by Anil Kishan (quippy@quippy.de)
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
package sm.smcreator.smc.system;

import java.util.ArrayList;

/**
 * A simple Logger
 * @author Anil Kishan
 * @since 21.04.2006
 */
public class Log
{
	private static ArrayList<LogMessageCallBack> logReceiver = new ArrayList<LogMessageCallBack>();

	private Log()
	{
		super();
	}
	public static void addLogListener(LogMessageCallBack receiver)
	{
		logReceiver.add(receiver);
	}
	public static void removeLogListener(LogMessageCallBack receiver)
	{
		logReceiver.remove(receiver);
	}
	public static void error(String message)
	{
		error(message, null);
	}
	public static void error(String message, Throwable ex)
	{
		for (int i=0; i<logReceiver.size(); i++) logReceiver.get(i).error(message, ex);
		System.err.println(message);
		if (ex!=null) 
		{
			ex.printStackTrace(System.err);
			System.err.print('\n');
		}
	}
	public static void info(String message)
	{
		for (int i=0; i<logReceiver.size(); i++) logReceiver.get(i).info(message);
		System.out.println(message);
	}
}
