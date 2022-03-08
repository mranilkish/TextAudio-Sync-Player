/*
 * @(#) PlayerConfigPanel.java
 *
 * Created on 10.12.2013 by Anil Kishan
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

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import sm.smcreator.smc.multimedia.MultimediaContainer;
import sm.smcreator.smc.multimedia.MultimediaContainerManager;
import sm.smcreator.smc.system.Helpers;

/**
 * @author Anil Kishan
 * @since 10.12.2013
 */
public class PlayerConfigPanel extends JPanel
{
	private static final long serialVersionUID = -923697640128200718L;

    private JTabbedPane tabbedPane = null;

    /**
	 * Constructor for PlayerConfigPanel
	 */
	public PlayerConfigPanel()
	{
		super();
		initialize();
	}
	private void initialize()
	{
		setName("playerSetUpTabbedPane");
		setLayout(new java.awt.GridBagLayout());
		add(getTabbedPane(), Helpers.getGridBagConstraint(0, 0, 1, 0, java.awt.GridBagConstraints.BOTH, java.awt.GridBagConstraints.CENTER, 1.0, 1.0));
	}
	public JTabbedPane getTabbedPane()
	{
		if (tabbedPane==null)
		{
			tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
			tabbedPane.setFont(Helpers.DIALOG_FONT);
			ArrayList<MultimediaContainer> containerList = MultimediaContainerManager.getContainerArray();
			for (int i=0; i<containerList.size(); i++)
			{
				MultimediaContainer container = containerList.get(i);
				JPanel configPanel = container.getConfigPanel();
				if (configPanel != null)
				{
					JScrollPane containerScroller = new JScrollPane();
					containerScroller.setName("scrollPane_Config_" + container.getName());
					containerScroller.setViewportView(configPanel);
					tabbedPane.add(container.getName(), containerScroller);
				}
			}
		}
		return tabbedPane;
	}
	public void selectTabForContainer(MultimediaContainer currentContainer)
	{
		for (int i=0; i<getTabbedPane().getTabCount(); i++)
		{
			if (getTabbedPane().getTitleAt(i).equals(currentContainer.getName()))
			{
				getTabbedPane().setSelectedIndex(i);
				return;
			}
		}
	}
}
