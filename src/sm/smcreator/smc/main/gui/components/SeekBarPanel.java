/*
 * @(#) SeekBarPanel.java
 *
 * Created on 09.09.2013 by Anil Kishan
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
package sm.smcreator.smc.main.gui.components;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import sm.smcreator.smc.mixer.Mixer;
import sm.smcreator.smc.system.Helpers;
import javax.swing.JOptionPane;

/**
 * This panel displays a timeCode and a seekbar to allow seeking
 * @author Anil Kishan
 * @since 09.09.2013
 */
public class SeekBarPanel extends ThreadUpdatePanel
{
	private static final long serialVersionUID = -3570075762823459752L;

	public JTextField timeTextField = null;
	private JLabel timeLabel = null;
	private JTextField KBSField = null;
	private JLabel KBSLabel = null;
	private JTextField KHZField = null;
	private JLabel KHZLabel = null;
	private JTextField activeChannelsTextField = null;
	private JLabel activeChannelsLabel = null;
	private JProgressBar timeBar = null;
	
	private boolean showBarOnly;
	
	private long maxLengthInMillis = 0;
	private int displayWhat = 0;
	
	private Mixer currentMixer;
	
	private ArrayList<SeekBarPanelListener> listeners;
	
	/**
	 * Constructor for SeekBarPanel
	 * @param desiredFPS
	 */
	public SeekBarPanel(int desiredFPS, boolean showBarOnly)
	{
		super(desiredFPS);
		this.showBarOnly = showBarOnly;
		listeners = new  ArrayList<SeekBarPanelListener>();
		initialize();
		startThread();
              //  JOptionPane.showMessageDialog(null,"ggg");
	}
	public void addListener(SeekBarPanelListener newListener)
	{
		listeners.add(newListener);
	}
	public void removeListener(SeekBarPanelListener listener)
	{
		listeners.remove(listener);
	}
	public void fireValuesChanged(long milliseconds)
	{
		final int size = listeners.size();
		for (int i=0; i<size; i++)
		{
			listeners.get(i).valuesChanged(milliseconds);
		}
	}
	/**
	 * Will drop the graphical elements
	 * @since 09.09.2013
	 */
	private void initialize()
	{
		this.setLayout(new java.awt.GridBagLayout());
		if (!showBarOnly)
		{
			this.add(getTimeTextField()	,		Helpers.getGridBagConstraint(0, 0, 1, 1, java.awt.GridBagConstraints.NONE,	java.awt.GridBagConstraints.WEST, 0.0, 0.0));
			this.add(getTimeLabel(),				Helpers.getGridBagConstraint(1, 0, 1, 1, java.awt.GridBagConstraints.NONE, 			java.awt.GridBagConstraints.WEST, 1.0, 0.0));
			this.add(getKBSField(),					Helpers.getGridBagConstraint(2, 0, 1, 1, java.awt.GridBagConstraints.NONE, 	java.awt.GridBagConstraints.WEST, 0.0, 0.0));
			this.add(getKBSLabel(),					Helpers.getGridBagConstraint(3, 0, 1, 1, java.awt.GridBagConstraints.NONE, 			java.awt.GridBagConstraints.WEST, 1.0, 0.0));
			this.add(getKHZField(),					Helpers.getGridBagConstraint(4, 0, 1, 1, java.awt.GridBagConstraints.NONE, 	java.awt.GridBagConstraints.WEST, 0.0, 0.0));
			this.add(getKHZLabel(),					Helpers.getGridBagConstraint(5, 0, 1, 1, java.awt.GridBagConstraints.NONE, 			java.awt.GridBagConstraints.WEST, 1.0, 0.0));
			this.add(getActiveChannelsTextField(),	Helpers.getGridBagConstraint(6, 0, 1, 1, java.awt.GridBagConstraints.NONE,	java.awt.GridBagConstraints.WEST, 0.0, 0.0));
			this.add(getActiveChannelsLabel(), 		Helpers.getGridBagConstraint(7, 0, 1, 0, java.awt.GridBagConstraints.NONE, 			java.awt.GridBagConstraints.WEST, 1.0, 0.0));
			this.add(getTimeBar(),					Helpers.getGridBagConstraint(0, 1, 1, 0, java.awt.GridBagConstraints.HORIZONTAL, 	java.awt.GridBagConstraints.WEST, 1.0, 0.0));
                        
                        
		}
		else
		{
			this.add(getTimeTextField(),			Helpers.getGridBagConstraint(0, 0, 1, 1, java.awt.GridBagConstraints.NONE,			java.awt.GridBagConstraints.WEST, 0.0, 0.0));
			this.add(getTimeBar(),					Helpers.getGridBagConstraint(1, 0, 1, 0, java.awt.GridBagConstraints.HORIZONTAL, 	java.awt.GridBagConstraints.EAST, 1.0, 0.0));
		}
	}
	public JTextField getTimeTextField()
	{
		if (timeTextField==null)
		{
			timeTextField = new JTextField("0.0");//new JTextField("0:00:00");
			timeTextField.setHorizontalAlignment(JTextField.TRAILING);
			timeTextField.setEditable(false);
			timeTextField.setName("timeTextField");
			timeTextField.setFont(Helpers.DIALOG_FONT);
			
			
			// Preserve characters space - not less, not more!
			final FontMetrics metrics = timeTextField.getFontMetrics(Helpers.DIALOG_FONT);
			final Dimension d = new Dimension(18 * metrics.charWidth('0'), metrics.getHeight());
			timeTextField.setSize(d);
			timeTextField.setMinimumSize(d);
			timeTextField.setMaximumSize(d);
			timeTextField.setPreferredSize(d);
			timeTextField.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent pEvent)
				{
					if (pEvent.getButton() == MouseEvent.BUTTON1)
					{
						if (currentMixer!=null)
						{
							displayWhat = 1 - displayWhat;
						}
					}
				}
			});
		}
                Helpers.skk= timeTextField.getText();
       //        JOptionPane.showMessageDialog(null, "ooook");
                
		return timeTextField;
	}
        
	public javax.swing.JLabel getTimeLabel()
	{
		if (timeLabel==null)
		{
			timeLabel = new javax.swing.JLabel();
			timeLabel.setName("timeLabel");
			timeLabel.setText("time");
			timeLabel.setFont(Helpers.DIALOG_FONT);
		}
		return timeLabel;
	}
	public javax.swing.JTextField getKBSField()
	{
		if (KBSField==null)
		{
			KBSField = new javax.swing.JTextField("--");
			KBSField.setHorizontalAlignment(JTextField.TRAILING);
			KBSField.setEditable(false);
			KBSField.setName("KBSField");
			KBSField.setFont(Helpers.DIALOG_FONT);

			// Preserve characters space - not less, not more!
			final FontMetrics metrics = timeTextField.getFontMetrics(Helpers.DIALOG_FONT);
			final Dimension d = new Dimension(6 * metrics.charWidth('0'), metrics.getHeight());
			KBSField.setSize(d);
			KBSField.setMinimumSize(d);
			KBSField.setMaximumSize(d);
			KBSField.setPreferredSize(d);
		}
		return KBSField;
	}
	public javax.swing.JLabel getKBSLabel()
	{
		if (KBSLabel==null)
		{
			KBSLabel = new javax.swing.JLabel();
			KBSLabel.setName("KBSLabel");
			KBSLabel.setText("KB/s");
			KBSLabel.setFont(Helpers.DIALOG_FONT);
		}
		return KBSLabel;
	}
	public javax.swing.JTextField getKHZField()
	{
		if (KHZField==null)
		{
			KHZField = new javax.swing.JTextField("--");
			KHZField.setHorizontalAlignment(JTextField.TRAILING);
			KHZField.setEditable(false);
			KHZField.setName("KHZField");
			KHZField.setFont(Helpers.DIALOG_FONT);

			// Preserve characters space - not less, not more!
			final FontMetrics metrics = timeTextField.getFontMetrics(Helpers.DIALOG_FONT);
			final Dimension d = new Dimension(4 * metrics.charWidth('0'), metrics.getHeight());
			KHZField.setSize(d);
			KHZField.setMinimumSize(d);
			KHZField.setMaximumSize(d);
			KHZField.setPreferredSize(d);
		}
		return KHZField;
	}
	public javax.swing.JLabel getKHZLabel()
	{
		if (KHZLabel==null)
		{
			KHZLabel = new javax.swing.JLabel();
			KHZLabel.setName("KHZLabel");
			KHZLabel.setText("KHZ");
			KHZLabel.setFont(Helpers.DIALOG_FONT);
		}
		return KHZLabel;
	}
	public javax.swing.JTextField getActiveChannelsTextField()
	{
		if (activeChannelsTextField==null)
		{
			activeChannelsTextField = new javax.swing.JTextField("--");
			activeChannelsTextField.setHorizontalAlignment(JTextField.TRAILING);
			activeChannelsTextField.setEditable(false);
			activeChannelsTextField.setName("activeChannelsTextField");
			activeChannelsTextField.setFont(Helpers.DIALOG_FONT);

			// Preserve characters space - not less, not more!
			final FontMetrics metrics = timeTextField.getFontMetrics(Helpers.DIALOG_FONT);
			final Dimension d = new Dimension(4 * metrics.charWidth('0'), metrics.getHeight());
			activeChannelsTextField.setSize(d);
			activeChannelsTextField.setMinimumSize(d);
			activeChannelsTextField.setMaximumSize(d);
			activeChannelsTextField.setPreferredSize(d);
		}
		return activeChannelsTextField;
	}
	public javax.swing.JLabel getActiveChannelsLabel()
	{
		if (activeChannelsLabel==null)
		{
			activeChannelsLabel = new javax.swing.JLabel();
			activeChannelsLabel.setName("activeChannelsLabel");
			activeChannelsLabel.setText("Chn");
			activeChannelsLabel.setFont(Helpers.DIALOG_FONT);
		}
		return activeChannelsLabel;
	}
	public JProgressBar getTimeBar()
	{
		if (timeBar==null)
		{
			timeBar = new JProgressBar(0, 0);
			timeBar.setValue(0);
			timeBar.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
			timeBar.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent pEvent)
				{
					if (pEvent.getButton() == MouseEvent.BUTTON1)
					{
						if (currentMixer!=null)
						{
							Point p = pEvent.getPoint();
							final double x = p.getX();
							final int width = timeBar.getWidth();
							final BoundedRangeModel model = getTimeBar().getModel();
							currentMixer.setMillisecondPosition((long)(model.getMaximum() * x) / width);
						}
					}
				}
			});
		}
		return timeBar;
	}
	public synchronized void setCurrentMixer(Mixer newMixer)
	{
		currentMixer = newMixer;
		getTimeBar().setValue(0);
		getTimeTextField().setText("0.0");//setText("0:00:00");
		getKBSField().setText("--");
		getKHZField().setText("--");
		getActiveChannelsTextField().setText("--");
		if (currentMixer!=null)
		{
			BoundedRangeModel model = getTimeBar().getModel();
			model.setMaximum((int)(maxLengthInMillis = currentMixer.getLengthInMilliseconds()));
		}
	}
	/**
	 * 
	 * @see sm.smcreator.smc.main.gui.components.MeterPanelBase#doThreadUpdate()
	 */
	@Override
	protected synchronized void doThreadUpdate()
	{
		if (currentMixer!=null)
		{
			long timeCode = currentMixer.getMillisecondPosition();
			getTimeBar().setValue((int)timeCode);
			fireValuesChanged(timeCode);
			
			if (!showBarOnly)
			{
				if (displayWhat == 1) timeCode = maxLengthInMillis - timeCode;
				getTimeTextField().setText(Helpers.getTimeStringFromMilliseconds(timeCode));
				getKBSField().setText(Integer.toString(currentMixer.getCurrentKBperSecond()));
				getKHZField().setText(Integer.toString(currentMixer.getCurrentSampleFrequency()));
				getActiveChannelsTextField().setText(Integer.toString(currentMixer.getChannelCount()));
			}
		}
	}
}
