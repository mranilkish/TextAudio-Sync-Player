/*
 * @(#) MultimediaContainerEventListener.java
 *
 * Created on 27.12.2013 by Anil Kishan
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

import java.util.EventListener;

/**
 * @author Anil Kishan
 * @since 27.12.2013
 */
public interface MultimediaContainerEventListener extends EventListener
{
	/**
	 * Will get fired if an event of interest is raised. This is actually
	 * only once the case: if an mp3 is streamd and the IcyInputStream
	 * gets a new Title
	 * @since 27.12.2013
	 * @param event
	 */
	public void multimediaContainerEventOccured(MultimediaContainerEvent event);
}
