/*
 * @(#) EffectsPanel.java
 *
 * Created on 15.01.2013 by Anil Kishan
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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import sm.smcreator.smc.mixer.dsp.AudioProcessor;
import sm.smcreator.smc.system.Helpers;

/**
 * @author Anil Kishan
 * @since 15.01.2013
 */
public class EffectsPanel extends JPanel
{
	private static final long serialVersionUID = -3590575857860754245L;

	private JCheckBox passThrough = null;
    private JTabbedPane tabbedPane = null;
    private JPanel[] effectPanels;

    private AudioProcessor audioProcessor;
    
	/**
	 * Constructor for EffectsPanel
	 */
	public EffectsPanel(JPanel[] effectPanels, AudioProcessor audioProcessor)
	{
		this.effectPanels = effectPanels;
		this.audioProcessor = audioProcessor;
		initialize();
	}
	/**
	 * @param audioProcessor the audioProcessor to set
	 */
	public void setAudioProcessor(AudioProcessor newAudioProcessor)
	{
		audioProcessor = newAudioProcessor;
	}
	private void initialize()
	{
		setName("effectsTabbedPane");
		setLayout(new java.awt.GridBagLayout());
		add(getPassThrough(), Helpers.getGridBagConstraint(0, 0, 1, 0, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
		add(getTabbedPane(), Helpers.getGridBagConstraint(0, 1, 1, 0, java.awt.GridBagConstraints.BOTH, java.awt.GridBagConstraints.CENTER, 1.0, 1.0));
	}
	public JCheckBox getPassThrough()
	{
		if (passThrough == null)
		{
			passThrough = new javax.swing.JCheckBox();
			passThrough.setName("passThrough");
			passThrough.setText("activate effects");
			passThrough.setFont(Helpers.DIALOG_FONT);
			if (audioProcessor!=null) passThrough.setSelected(audioProcessor.isDspEnabled());
			passThrough.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED || e.getStateChange()==ItemEvent.DESELECTED)
					{
						if (audioProcessor!=null)
							audioProcessor.setDspEnabled(getPassThrough().isSelected());
					}
				}
			});
		}
		return passThrough;
	}
	public JTabbedPane getTabbedPane()
	{
		if (tabbedPane==null)
		{
			tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
			tabbedPane.setFont(Helpers.DIALOG_FONT);
			for (int i=0; i<effectPanels.length; i++)
			{
				JPanel effectPanel = effectPanels[i];
				if (effectPanel != null)
				{
					JScrollPane containerScroller = new JScrollPane();
					containerScroller.setName("scrollPane_Effect_" + effectPanel.getName());
					containerScroller.setViewportView(effectPanel);
					tabbedPane.add(effectPanel.getName(), containerScroller);
				}
			}
		}
		return tabbedPane;
	}
}
