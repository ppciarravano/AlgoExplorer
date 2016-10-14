package ppciarravano.algoexplorer.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import ppciarravano.algoexplorer.Workspace;


/**
 * Classe WorkspaceNewDialog
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010) 
 */
public class WorkspaceNewDialog extends JDialog
{
	private Workspace wrkspc;
	
	private JTextField textField_1;
	private JTextField textField_2;
	private JButton newButton;
	
	public WorkspaceNewDialog(Workspace wrkspcParam)
	{
		super((Frame) null, "New Workspace:", true);
		this.wrkspc = wrkspcParam;
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(15, 15, 15, 15));
				
		getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gridBagLayout);
		
		JLabel label_1 = new JLabel("Class path dir:");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(label_1, gbc);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setText(this.wrkspc.getClasspath());
		GridBagConstraints gbc_4 = new GridBagConstraints();
		gbc_4.insets = new Insets(0, 0, 5, 5);
		gbc_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_4.gridx = 1;
		gbc_4.gridy = 0;
		panel.add(textField_1, gbc_4);
		textField_1.setColumns(35);
		
		JButton button_1 = new JButton("Browse...");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseClassDir();
			}
		});
		GridBagConstraints gbc_2 = new GridBagConstraints();
		gbc_2.insets = new Insets(0, 0, 5, 0);
		gbc_2.gridx = 2;
		gbc_2.gridy = 0;
		panel.add(button_1, gbc_2);
		
		JLabel label_2 = new JLabel("Source path dir:");
		GridBagConstraints gbc_1 = new GridBagConstraints();
		gbc_1.anchor = GridBagConstraints.EAST;
		gbc_1.insets = new Insets(0, 0, 5, 5);
		gbc_1.gridx = 0;
		gbc_1.gridy = 1;
		panel.add(label_2, gbc_1);
		
		textField_2 = new JTextField();
		textField_2.setEditable(false);
		//textField_2.setText(this.wrkspc.getSourcepath());
		textField_2.setText("");
		GridBagConstraints gbc_5 = new GridBagConstraints();
		gbc_5.insets = new Insets(0, 0, 5, 5);
		gbc_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_5.gridx = 1;
		gbc_5.gridy = 1;
		panel.add(textField_2, gbc_5);
		textField_2.setColumns(35);
		
		JButton button_2 = new JButton("Browse...");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseSourceDir();
			}
		});
		GridBagConstraints gbc_3 = new GridBagConstraints();
		gbc_3.insets = new Insets(0, 0, 5, 0);
		gbc_3.gridx = 2;
		gbc_3.gridy = 1;
		panel.add(button_2, gbc_3);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_6 = new GridBagConstraints();
		gbc_6.insets = new Insets(0, 0, 0, 5);
		gbc_6.fill = GridBagConstraints.BOTH;
		gbc_6.gridx = 1;
		gbc_6.gridy = 2;
		panel.add(panel_1, gbc_6);
		
		JButton closeButton = new JButton("Cancel");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				setVisible(false);
			}
		});
		panel_1.add(closeButton);
		
		newButton = new JButton("New");
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wrkspc.getCommandManager().newWorkspace(textField_1.getText(), textField_2.getText());
				setVisible(false);
			}
		});
		newButton.setEnabled(false);
		panel_1.add(newButton);
		
		enableNewButton();
		
		setIconImage(GuiUtility.getApplicationIcon());
		setResizable(true);
		pack();
		GuiUtility.center(this);
		setVisible(true);
	
		
	}
	
	private void chooseClassDir()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setCurrentDirectory(new java.io.File(textField_1.getText().equals("")? "." : textField_1.getText()));
		chooser.setDialogTitle("Scelta directory classi compilate:");
		chooser.setAcceptAllFileFilterUsed(false);
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION)
		{
			textField_1.setText(chooser.getSelectedFile().toString());
		}
		enableNewButton();
	}
	
	private void chooseSourceDir()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setCurrentDirectory(new java.io.File(textField_2.getText().equals("")? "." : textField_2.getText()));
		chooser.setDialogTitle("Scelta directory sorgenti:");
		chooser.setAcceptAllFileFilterUsed(false);
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION)
		{
			textField_2.setText(chooser.getSelectedFile().toString());
		}
		enableNewButton();
	}
	
	
	
	private void enableNewButton()
	{
		//if ((!textField_1.getText().equals("")) && (!textField_2.getText().equals("")))
		if ( !textField_1.getText().equals("") )
		{
			newButton.setEnabled(true);
		}
	}
	
}
