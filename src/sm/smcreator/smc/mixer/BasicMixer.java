/*
 * @(#) BasicMixer.java
 *
 * Created on 30.12.2007 by Anil Kishan
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
package sm.smcreator.smc.mixer;

import sm.smcreator.smc.system.Log;


/**
 * @author Anil Kishan
 * @since 30.12.2007
 */
public abstract class BasicMixer extends Mixer
{
	private static final int ISNOTHING = 0;
	private static final int ISDOING = 1;
	private static final int ISDONE = 2;
	
	private int paused;
	private int stopped;
	private int seeking;
	private long seekPosition;
	private boolean hasFinished;

	/**
	 * Constructor for BasicMixer
	 */
	public BasicMixer()
	{
		super();
		setIsStopped();
	}

	/**
	 * @return
	 * @see sm.smcreator.smc.mixer.Mixer#isPaused()
	 */
	@Override
	public boolean isPaused()
	{
		return paused==ISDONE;
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.mixer.Mixer#isPausing()
	 */
	@Override
	public boolean isPausing()
	{
		return paused==ISDOING;
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.mixer.Mixer#isStopped()
	 */
	@Override
	public boolean isStopped()
	{
		return stopped==ISDONE;
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.mixer.Mixer#isStopping()
	 */
	@Override
	public boolean isStopping()
	{
		return stopped==ISDOING;
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.mixer.Mixer#isNotPausingNorPaused()
	 */
	@Override
	public boolean isNotPausingNorPaused()
	{
		return paused==ISNOTHING;
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.mixer.Mixer#isNotStoppingNorStopped()
	 */
	@Override
	public boolean isNotStoppingNorStopped()
	{
		return stopped==ISNOTHING;
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.mixer.Mixer#isPlaying()
	 */
	@Override
	public boolean isPlaying()
	{
		return (!isStopped());
	}
	/**
	 * @return
	 * @see sm.smcreator.smc.mixer.Mixer#isFinished()
	 */
	@Override
	public boolean hasFinished()
	{
		return hasFinished;
	}
	protected void setIsPausing()
	{
		paused = ISDOING; 
		stopped = ISNOTHING;
		seeking = ISNOTHING;
	}
	protected void setIsPaused()
	{
		paused = ISDONE; 
		stopped = ISNOTHING;
		seeking = ISNOTHING;
	}
	protected void setIsStopping()
	{
		paused = ISNOTHING; 
		stopped = ISDOING;
		seeking = ISNOTHING;
	}
	protected void setIsStopped()
	{
		paused = ISNOTHING; 
		stopped = ISDONE;
		seeking = ISNOTHING;
	}
	protected void setIsPlaying()
	{
		paused = ISNOTHING; 
		stopped = ISNOTHING;
		seeking = ISNOTHING;
	}
	protected void setHasFinished()
	{
		hasFinished = true;
	}
	/**
	 * @param milliseconds
	 * @since 13.02.2013
	 */
	protected abstract void seek(long milliseconds);
	/**
	 * @return
	 * @since 13.02.2013
	 */
	protected long getSeekPosition()
	{
		return seekPosition;
	}
	public boolean isNotSeeking()
	{
		return seeking == ISNOTHING;
	}
	public boolean isInSeeking()
	{
		return seeking != ISNOTHING;
	}
	public boolean isSeeking()
	{
		return seeking == ISDONE;
	}
	public void setIsSeeking()
	{
		seeking = ISDONE;
	}
	/**
	 * @param milliseconds
	 * @see sm.smcreator.smc.mixer.Mixer#setMillisecondPosition(long)
	 */
	@Override
	public void setMillisecondPosition(long milliseconds)
	{
		if (!isPlaying())
			seekPosition = milliseconds;
		else
		if (isNotSeeking())
		{
			try
			{
				seeking = ISDOING;
				while (seeking==ISDOING) try { Thread.sleep(1); } catch (InterruptedException ex) { /*NOOP */ }
				seek(milliseconds);
			}
			catch (Exception ex)
			{
				Log.error("[BasicMixer]", ex);
			}
			finally
			{
				seeking = ISNOTHING;
			}
		}
	}
	/**
	 * Stopps the playback.
	 * Will wait until stopp is done
	 * @since 22.06.2013
	 */
	@Override
	public void stopPlayback()
	{
		if (isNotStoppingNorStopped())
		{
			setIsStopping();
			while (!isStopped())
			{
				try { Thread.sleep(1); } catch (InterruptedException ex) { /*noop*/ }
			}
			stopLine();
		}
	}
	/**
	 * Halts the playback
	 * Will wait until playback halted
	 * @since 22.06.2013
	 */
	@Override
	public void pausePlayback()
	{
		if (isNotPausingNorPaused() && isNotStoppingNorStopped())
		{
			setIsPausing();
			while (!isPaused() && !isStopped())
			{
				try { Thread.sleep(1); } catch (InterruptedException ex) { /*noop*/ }
			}
			stopLine();
		}
		else
		if (isPaused())
		{
			startLine();
			setIsPlaying();
		}
	}
}
