/*
 * EditPlaylistEntry.
 * 
 * Created on 03.4.2013 by Anil Kishan
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

package sm.smcreator.smc.main.gui.playlist;

import java.awt.GridBagConstraints;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import sm.smcreator.smc.main.gui.tools.FileChooserFilter;
import sm.smcreator.smc.main.gui.tools.FileChooserResult;
import sm.smcreator.smc.multimedia.MultimediaContainerManager;
import sm.smcreator.smc.system.Helpers;

/**
 * EditPlaylistEntry class implements a DialogBox to get an URL.
 */
public class EditPlaylistEntry extends JDialog
{
 	private static final long serialVersionUID = 6551932234216134125L;

    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton openButton;
 	private javax.swing.JButton cancelButton;
 	private javax.swing.JButton searchButton;
    private javax.swing.JTextField textField1;
    private javax.swing.JTextField textField2;
    
    private String value;

    /**
     * Creates new form
     */
    public EditPlaylistEntry(JFrame parent, boolean modal)
    {
        super(parent, modal);
        initialize();
    }
    public String getValue()
    {
    	return value;
    }
    public void setValue(String value)
    {
    	textField1.setText(value);
    	textField2.setText(value);
    }
    /**
     * This method is called from within the constructor to
     * initialize the form.
     */
    private void initialize()
    {
        java.awt.GridBagConstraints gridBagConstraints;
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1 = new javax.swing.JLabel();
        jLabel1.setText("Old:");
        jLabel1.setFont(Helpers.DIALOG_FONT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel1, gridBagConstraints);
        
        textField1 = new javax.swing.JTextField();
        textField1.setFont(Helpers.DIALOG_FONT);
        textField1.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(textField1, gridBagConstraints);

        jLabel2 = new javax.swing.JLabel();
        jLabel2.setText("New:");
        jLabel2.setFont(Helpers.DIALOG_FONT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel2, gridBagConstraints);

        textField2 = new javax.swing.JTextField();
        textField2.setFont(Helpers.DIALOG_FONT);
        textField2.setColumns(80);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(textField2, gridBagConstraints);

        searchButton = new javax.swing.JButton();
        searchButton.setMnemonic('s');
        searchButton.setText("...");
        searchButton.setToolTipText("Search");
        searchButton.setFont(Helpers.DIALOG_FONT);
        searchButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                doSearch();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(searchButton, gridBagConstraints);

        jPanel1 = new javax.swing.JPanel();

        openButton = new javax.swing.JButton();
        openButton.setMnemonic('O');
        openButton.setText("OK");
        openButton.setFont(Helpers.DIALOG_FONT);
        openButton.setToolTipText("OK");
        openButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                doOpen();
            }
        });
        jPanel1.add(openButton);
        
        cancelButton = new javax.swing.JButton();
        cancelButton.setMnemonic('C');
        cancelButton.setText("Cancel");
        cancelButton.setToolTipText("Cancel");
        cancelButton.setFont(Helpers.DIALOG_FONT);
        cancelButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                doCancel();
            }
        });
        jPanel1.add(cancelButton);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel1, gridBagConstraints);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter()
		{
			@Override
			public void windowClosing(java.awt.event.WindowEvent e)
			{
				doClose();
			}
		});

		setName("Edit Playlist entry");
		setTitle("Edit Playlist entry");
		setResizable(false);
        pack();
		setLocation(Helpers.getFrameCenteredLocation(this, getParent()));
    }

	/* EVENT METHODS -------------------------------------------------------- */
	public void doClose()
	{
		setVisible(false);
		dispose();
		//if we are alone in the world, exit the vm
		//if (getParent() == null) System.exit(0); // this should not be needed! 
	}
    private void doOpen()
    {
        value = textField2.getText();
        doClose();
    }
    private void doCancel()
    {
        value = null;
        doClose();
    }
    private void doSearch()
    {
		FileFilter [] filter = new FileFilter[] { new FileChooserFilter(MultimediaContainerManager.getSupportedFileExtensions(), "All playable files") };
		FileChooserResult selectedFile = Helpers.selectFileNameFor(this, textField2.getText(), "Select file", filter, 0, false);
		if (selectedFile!=null)
		{
			File f = selectedFile.getSelectedFile();
			textField2.setText(f.getAbsolutePath());
//			try
//			{
//				URL target = f.toURI().toURL();
//				textField2.setText(target.toString());
//			}
//			catch (Exception ex)
//			{
//				Log.error("IGNORED", ex);
//			}
		}
    }
}
