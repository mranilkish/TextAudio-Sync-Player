/*
 * @(#) PlayThread.java
 *
 * Created on 18.05.2013 by Anil Kishan
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

import java.io.Serializable;

import sm.smcreator.smc.mixer.Mixer;
import sm.smcreator.smc.system.Log;
import javax.swing.JOptionPane;

/**
 * @author Anil Kishan
 * @since 18.05.2013
 */
public final class PlayThread extends Thread implements Serializable
{
	private static final long serialVersionUID = 3546401588411431506L;

	private transient final Mixer currentMixer;
	private final PlayThreadEventListener listener;
	private volatile boolean isRunning;
	private volatile boolean finishedNormaly;
	
	public PlayThread(Mixer currentMixer, PlayThreadEventListener listener)
	{
		if (currentMixer==null) 
			throw new IllegalArgumentException("Provided Mixer was NULL");
		this.currentMixer = currentMixer;
		this.isRunning = false;
		this.finishedNormaly = false;
		this.listener = listener;
		this.setName("PlayThread");
		this.setDaemon(true);
		this.setPriority(Thread.MAX_PRIORITY);
	}
	private void informListener()
	{
		listener.playThreadEventOccured(this);
	}
	public void stopMod()
	{
		if (isRunning)
		{
                
			this.currentMixer.stopPlayback();
			while (isRunning)
			{
				try { Thread.sleep(1); } catch (InterruptedException ex) { /*NOOP*/ }
			}
		}
	}
	public void pausePlay()
	{
		if (isRunning) 
                {
                this.currentMixer.pausePlayback();
                // JOptionPane.showMessageDialog(null,"pause ");                
                }
		informListener();
	}
	public Mixer getCurrentMixer()
	{
		return currentMixer;
	}
	public boolean isRunning()
	{
		return isRunning;
	}
	public boolean getHasFinishedNormaly()
	{
		return finishedNormaly;
	}
	@Override
	public void run()
	{
		this.isRunning = true;
		this.finishedNormaly = false;
		informListener();
		try
		{
			getCurrentMixer().startPlayback();
		}
		catch (Throwable ex)
		{
			Log.error("[MainForm::run]", ex);
		}
		this.isRunning = false;
		this.finishedNormaly = getCurrentMixer().hasFinished();
		informListener();
	}
}