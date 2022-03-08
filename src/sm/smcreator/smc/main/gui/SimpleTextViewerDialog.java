/*
 * @(#) SimpleTextViewerDialog.java
 *
 * Created on 24.01.2013 by Anil Kishan
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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JFrame;

import sm.smcreator.smc.system.Helpers;
import sm.smcreator.smc.system.Log;

/**
 * @author Anil Kishan
 * @since 24.01.2013
 */
public class SimpleTextViewerDialog extends JDialog
{
	private static final long serialVersionUID = -5666092255473846658L;

	private javax.swing.JButton closeButton;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextArea textArea;
	private URL url;

	/**
	 * Constructor for SimpleTextViewerDialog
	 * @param owner
	 * @throws HeadlessException
	 */
	public SimpleTextViewerDialog(JFrame owner, boolean modal, URL url)
	{
		super(owner, modal);
		this.url=url;
		initialize();
	}
	public SimpleTextViewerDialog(JFrame owner, boolean modal, String url)
	{
		this(owner, modal);
		setDisplayTextFromURL(url);
	}
	public SimpleTextViewerDialog(JFrame owner, boolean modal)
	{
		super(owner, modal);
		url=null;
		initialize();
	}
	private void initialize()
	{
		setTitle("File Viewer");
		setName("SimpleTextFileViewer");
		setSize(new Dimension(640, 480));
		setPreferredSize(getSize());
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(true);
		addWindowListener(new java.awt.event.WindowAdapter()
		{
			@Override
			public void windowClosing(java.awt.event.WindowEvent e)
			{
				doClose();
			}
		});

		setLayout(new java.awt.GridBagLayout());
		Container panel = getContentPane();

		panel.add(getScrollPane(), Helpers.getGridBagConstraint(0, 0, 1, 0, java.awt.GridBagConstraints.BOTH, java.awt.GridBagConstraints.WEST, 1.0, 1.0));
		panel.add(getCloseButton(), Helpers.getGridBagConstraint(0, 1, 1, 0, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));

        if (url != null) fillTextArea();

        pack();
	}
	private javax.swing.JScrollPane getScrollPane()
	{
		if (scrollPane == null)
		{
			scrollPane = new javax.swing.JScrollPane();
			scrollPane.setName("scrollPane_TextField");
			scrollPane.setViewportView(getTextArea());
		}
		return scrollPane;
	}
	private javax.swing.JTextArea getTextArea()
	{
		if (textArea==null)
		{
			textArea = new javax.swing.JTextArea();
			textArea.setName("modInfo_Instruments");
			textArea.setEditable(false);
			textArea.setFont(Helpers.getTextAreaFont());
		}
		return textArea;
	}
	private javax.swing.JButton getCloseButton()
	{
		if (closeButton==null)
		{
	        closeButton = new javax.swing.JButton();
	        closeButton.setMnemonic('C');
	        closeButton.setText("Close");
	        closeButton.setToolTipText("Close");
	        closeButton.setFont(Helpers.DIALOG_FONT);
	        closeButton.addActionListener(new java.awt.event.ActionListener()
	        {
	            public void actionPerformed(java.awt.event.ActionEvent evt)
	            {
	                doClose();
	            }
	        });
		}
		return closeButton;
	}
	public void doClose()
	{
		setVisible(false);
		dispose();
		//if we are alone in the world, exit the vm
		//if (getParent() == null) System.exit(0); // this should not be needed! 
	}
	private void fillTextArea()
	{
		if (url!=null)
		{
			BufferedReader reader = null;
			try
			{
				reader = new BufferedReader(new InputStreamReader(url.openStream()));
				StringBuilder fullText = new StringBuilder();
				String line;
				while ((line=reader.readLine())!=null)
				{
					fullText.append(line).append('\n');
				}
				getTextArea().setText(fullText.toString());
				getTextArea().select(0,0);
			}
			catch (Throwable ex)
			{
				Log.error("reading text failed", ex);
			}
			finally
			{
				if (reader!=null) try { reader.close(); } catch (IOException ex) { Log.error("IGNORED", ex); }
			}
		}
	}
	public void setDisplayTextFromURL(URL url)
	{
		this.url = url;
		fillTextArea();
	}
	public void setDisplayTextFromURL(String url)
	{
		try
		{
			this.url = new URL(url);
			fillTextArea();
		}
		catch (MalformedURLException ex)
		{
			Log.error("url not correct", ex);
		}
	}
}