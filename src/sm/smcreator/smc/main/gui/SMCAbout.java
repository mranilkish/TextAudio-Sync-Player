/*
 * @(#) JavaModAbout.java
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
package sm.smcreator.smc.main.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import sm.smcreator.smc.system.Helpers;

/**
 * @author Anil Kishan
 * @since 22.06.2013
 */
public class SMCAbout extends JDialog
{
	private static final long serialVersionUID = 134421312687144510L;

	private static final String DEFAULTICONPATH = "ressources/about.gif";
	
	private JPanel baseContentPane = null;
	private javax.swing.JButton button_GIF = null;
	private javax.swing.JLabel textLine1 = null;
	private javax.swing.JLabel textLine2 = null;
	private javax.swing.JLabel textLine3 = null;
	private javax.swing.JLabel textLine5 = null;
	private javax.swing.JLabel textLine8 = null;
	private javax.swing.JLabel textLine9 = null;
	private javax.swing.JLabel textLine10 = null;
	private JButton btn_OK = null;
	private Frame parent;

	/**
	 * Constructor for JavaModAbout
	 */
	public SMCAbout(Frame parent, boolean modal)
	{
		super(parent, modal);
		this.parent = parent;
		initialize();
	}

	private void initialize()
	{
		setContentPane(getBaseContentPane());
		setName("Player About");
		setTitle("About SMCreator");
		java.awt.Insets insets = getInsets();
		setSize(690 + insets.left + insets.right, 400 + insets.top + insets.bottom);
		setPreferredSize(getSize());
		setMinimumSize(getSize());
		setResizable(false);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter()
		{
			@Override
			public void windowClosing(java.awt.event.WindowEvent e)
			{
				doClose();
			}
		});

		pack();
		setLocation(Helpers.getFrameCenteredLocation(this, parent));
	}

	/* Element Getter Methods ---------------------------------------------- */
	private javax.swing.JPanel getBaseContentPane()
	{
		if (baseContentPane == null)
		{
			baseContentPane = new javax.swing.JPanel();
			baseContentPane.setName("baseContentPane");
			baseContentPane.setLayout(new java.awt.GridBagLayout());
			baseContentPane.setBounds(0, 0, 0, 0);
			baseContentPane.setMinimumSize(new java.awt.Dimension(0, 0));

			java.awt.GridBagConstraints constraintsButton_GIF = new java.awt.GridBagConstraints();
			constraintsButton_GIF.gridx = 0;
			constraintsButton_GIF.gridy = 0;
			constraintsButton_GIF.gridheight = 11;
			constraintsButton_GIF.fill = java.awt.GridBagConstraints.VERTICAL;
			constraintsButton_GIF.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsButton_GIF.weightx = 1.0;
			constraintsButton_GIF.weighty = 1.0;
			constraintsButton_GIF.insets = new java.awt.Insets(4, 4, 4, 4);
			baseContentPane.add(getButton_GIF(), constraintsButton_GIF);

			java.awt.GridBagConstraints constraintsl_TextLine1 = new java.awt.GridBagConstraints();
			constraintsl_TextLine1.gridx = 1;
			constraintsl_TextLine1.gridy = 0;
			constraintsl_TextLine1.gridwidth = 0;
			constraintsl_TextLine1.anchor = java.awt.GridBagConstraints.CENTER;
			constraintsl_TextLine1.fill = java.awt.GridBagConstraints.NONE;
			constraintsl_TextLine1.weightx = 1.0;
			constraintsl_TextLine1.weighty = 0.0;
			constraintsl_TextLine1.insets = new java.awt.Insets(4, 4, 4, 4);
			baseContentPane.add(getl_TextLine1(), constraintsl_TextLine1);

			java.awt.GridBagConstraints constraintsl_TextLine2 = new java.awt.GridBagConstraints();
			constraintsl_TextLine2.gridx = 1;
			constraintsl_TextLine2.gridy = 1;
			constraintsl_TextLine2.gridwidth = 0;
			constraintsl_TextLine2.anchor = java.awt.GridBagConstraints.NORTH;
			constraintsl_TextLine2.fill = java.awt.GridBagConstraints.NONE;
			constraintsl_TextLine2.weightx = 1.0;
			constraintsl_TextLine2.weighty = 1.0;
			constraintsl_TextLine2.insets = new java.awt.Insets(4, 4, 4, 4);
			baseContentPane.add(getl_TextLine2(), constraintsl_TextLine2);

			java.awt.GridBagConstraints constraintsl_TextLine3 = new java.awt.GridBagConstraints();
			constraintsl_TextLine3.gridx = 1;
			constraintsl_TextLine3.gridy = 2;
			constraintsl_TextLine3.gridwidth = 0;
			constraintsl_TextLine3.anchor = java.awt.GridBagConstraints.CENTER;
			constraintsl_TextLine3.fill = java.awt.GridBagConstraints.NONE;
			constraintsl_TextLine3.weightx = 1.0;
			constraintsl_TextLine3.weighty = 0.0;
			constraintsl_TextLine3.insets = new java.awt.Insets(4, 4, 4, 4);
			baseContentPane.add(getl_TextLine3(), constraintsl_TextLine3);

			java.awt.GridBagConstraints constraintsl_TextLine4 = new java.awt.GridBagConstraints();
			constraintsl_TextLine4.gridx = 1;
			constraintsl_TextLine4.gridy = 3;
			constraintsl_TextLine4.gridwidth = 0;
			constraintsl_TextLine4.anchor = java.awt.GridBagConstraints.CENTER;
			constraintsl_TextLine4.fill = java.awt.GridBagConstraints.NONE;
			constraintsl_TextLine4.weightx = 1.0;
			constraintsl_TextLine4.weighty = 0.0;
			constraintsl_TextLine4.insets = new java.awt.Insets(4, 4, 4, 4);
			baseContentPane.add(getl_TextLine5(), constraintsl_TextLine4);

			java.awt.GridBagConstraints constraintsl_TextLine5 = new java.awt.GridBagConstraints();
			constraintsl_TextLine5.gridx = 1;
			constraintsl_TextLine5.gridy = 4;
			constraintsl_TextLine5.gridwidth = 0;
			constraintsl_TextLine5.anchor = java.awt.GridBagConstraints.CENTER;
			constraintsl_TextLine5.fill = java.awt.GridBagConstraints.NONE;
			constraintsl_TextLine5.weightx = 1.0;
			constraintsl_TextLine5.weighty = 0.0;
			constraintsl_TextLine5.insets = new java.awt.Insets(4, 4, 4, 4);
			baseContentPane.add(getl_TextLine8(), constraintsl_TextLine5);

			java.awt.GridBagConstraints constraintsl_TextLine6 = new java.awt.GridBagConstraints();
			constraintsl_TextLine6.gridx = 1;
			constraintsl_TextLine6.gridy = 5;
			constraintsl_TextLine6.gridwidth = 0;
			constraintsl_TextLine6.anchor = java.awt.GridBagConstraints.CENTER;
			constraintsl_TextLine6.fill = java.awt.GridBagConstraints.NONE;
			constraintsl_TextLine6.weightx = 1.0;
			constraintsl_TextLine6.weighty = 0.0;
			constraintsl_TextLine6.insets = new java.awt.Insets(4, 4, 4, 4);
			baseContentPane.add(getl_TextLine9(), constraintsl_TextLine6);

			

			java.awt.GridBagConstraints constraintsl_TextLine10 = new java.awt.GridBagConstraints();
			constraintsl_TextLine10.gridx = 1;
			constraintsl_TextLine10.gridy = 9;
			constraintsl_TextLine10.gridwidth = 0;
			constraintsl_TextLine10.anchor = java.awt.GridBagConstraints.SOUTH;
			constraintsl_TextLine10.fill = java.awt.GridBagConstraints.NONE;
			constraintsl_TextLine10.weightx = 1.0;
			constraintsl_TextLine10.weighty = 0.0;
			constraintsl_TextLine10.insets = new java.awt.Insets(4, 4, 4, 4);
			baseContentPane.add(getl_TextLine10(), constraintsl_TextLine10);

			java.awt.GridBagConstraints constraintsBtn_OK = new java.awt.GridBagConstraints();
			constraintsBtn_OK.gridx = 1;
			constraintsBtn_OK.gridy = 10;
			constraintsBtn_OK.gridwidth = 0;
			constraintsBtn_OK.anchor = java.awt.GridBagConstraints.SOUTH;
			constraintsBtn_OK.fill = java.awt.GridBagConstraints.NONE;
			constraintsBtn_OK.weightx = 1.0;
			constraintsBtn_OK.weighty = 0.0;
			constraintsBtn_OK.insets = new java.awt.Insets(4, 4, 4, 4);
			baseContentPane.add(getBtn_OK(), constraintsBtn_OK);
		}
		return baseContentPane;
	}

	private javax.swing.JButton getButton_GIF()
	{
		if (button_GIF == null)
		{
			button_GIF = new javax.swing.JButton();
			button_GIF.setName("button_GIF");
			button_GIF.setText("");
			button_GIF.setFocusPainted(false);
			button_GIF.setIcon(new javax.swing.ImageIcon(getClass().getResource(DEFAULTICONPATH)));
			button_GIF.setBorderPainted(false);
		}
		return button_GIF;
	}

	private javax.swing.JLabel getl_TextLine1()
	{
		if (textLine1 == null)
		{
			textLine1 = new javax.swing.JLabel();
			textLine1.setName("textLine1");
			textLine1.setText(Helpers.FULLVERSION);
			textLine1.setFont(Helpers.DIALOG_FONT);
		}
		return textLine1;
	}

	private javax.swing.JLabel getl_TextLine2()
	{
		if (textLine2 == null)
		{
			textLine2 = new javax.swing.JLabel();
			textLine2.setName("textLine2");
			textLine2.setText(Helpers.COPYRIGHT);
			textLine2.setFont(Helpers.DIALOG_FONT);
		}
		return textLine2;
	}

	private javax.swing.JLabel getl_TextLine3()
	{
		if (textLine3 == null)
		{
			textLine3 = new javax.swing.JLabel();
			textLine3.setName("textLine3");
			textLine3.setText("MP3 decoding with JLayer V1.0.1");
			textLine3.setFont(Helpers.DIALOG_FONT);
		}
		return textLine3;
	}

	

	private javax.swing.JLabel getl_TextLine5()
	{
		if (textLine5 == null)
		{
			textLine5 = new javax.swing.JLabel();
			textLine5.setName("textLine5");
			textLine5.setText("OGG decoding with the JOrbis Lib V0.0.17");
			textLine5.setFont(Helpers.DIALOG_FONT);
		}
		return textLine5;
	}

	
	
	private javax.swing.JLabel getl_TextLine8()
	{
		if (textLine8 == null)
		{
			textLine8 = new javax.swing.JLabel();
			textLine8.setName("textLine8");
			textLine8.setText(System.getProperty("java.runtime.name"));
			textLine8.setFont(Helpers.DIALOG_FONT);
		}
		return textLine8;
	}

	private javax.swing.JLabel getl_TextLine9()
	{
		if (textLine9 == null)
		{
			textLine9 = new javax.swing.JLabel();
			textLine9.setName("textLine9");
			textLine9.setText(System.getProperty("java.runtime.version") + " (" + System.getProperty("java.vm.info") + ")");
			textLine9.setFont(Helpers.DIALOG_FONT);
		}
		return textLine9;
	}

	private javax.swing.JLabel getl_TextLine10()
	{
		if (textLine10 == null)
		{
			textLine10 = new javax.swing.JLabel();
			textLine10.setName("textLine10");
			textLine10.setText(System.getProperty("java.vm.name"));
			textLine10.setFont(Helpers.DIALOG_FONT);
		}
		return textLine10;
	}

	private javax.swing.JButton getBtn_OK()
	{
		if (btn_OK == null)
		{
			btn_OK = new javax.swing.JButton();
			btn_OK.setName("jbtn_OK");
			btn_OK.setMnemonic('c');
			btn_OK.setText("Close");
			btn_OK.setActionCommand("Ende");
			btn_OK.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doClose();
				}
			});
		}
		return btn_OK;
	}

	/* EVENT METHODS -------------------------------------------------------- */
	public void doClose()
	{
		setVisible(false);
		dispose();
		//if we are alone in the world, exit the vm
		//if (getParent() == null) System.exit(0); // this should not be needed! 
	}
}
