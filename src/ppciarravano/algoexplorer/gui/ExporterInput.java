package ppciarravano.algoexplorer.gui;

import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import ppciarravano.algoexplorer.log.Logger;
import ppciarravano.algoexplorer.util.ConstantManager;

public class ExporterInput
{
	
	private ExporterInput()
	{
		
	}
	
	public static String[] titleDescriptionInput()
	{
		final String[] result = new String[2];; 
		
		final JDialog jDialog = new JDialog((Frame) null, "Insert title and description:", true);
				
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(15, 15, 15, 15));
				
		jDialog.getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gridBagLayout);
		
		JLabel label = new JLabel("Title:");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(label, gbc);
		
		final JTextField textField = new JTextField();
		GridBagConstraints gbc_1 = new GridBagConstraints();
		gbc_1.insets = new Insets(0, 0, 5, 0);
		gbc_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_1.gridx = 1;
		gbc_1.gridy = 0;
		panel.add(textField, gbc_1);
		textField.setColumns(30);
		
		
		JLabel label_1 = new JLabel("Description:");
		GridBagConstraints gbc_2 = new GridBagConstraints();
		gbc_2.anchor = GridBagConstraints.NORTH;
		gbc_2.insets = new Insets(0, 0, 5, 5);
		gbc_2.gridx = 0;
		gbc_2.gridy = 1;
		panel.add(label_1, gbc_2);
		
		final JTextArea textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		GridBagConstraints gbc_4 = new GridBagConstraints();
		gbc_4.insets = new Insets(0, 0, 5, 0);
		gbc_4.fill = GridBagConstraints.BOTH;
		gbc_4.gridx = 1;
		gbc_4.gridy = 1;
		panel.add(scrollPane, gbc_4);
		textArea.setRows(8);
		textArea.setLineWrap(true);

		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_3 = new GridBagConstraints();
		gbc_3.fill = GridBagConstraints.BOTH;
		gbc_3.gridx = 1;
		gbc_3.gridy = 2;
		panel.add(panel_1, gbc_3);
		
		JButton closeButton = new JButton("Cancel");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jDialog.setVisible(false);
			}
		});
		panel_1.add(closeButton);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result[0] = textField.getText().trim();
				result[1] = textArea.getText().trim();
 				jDialog.setVisible(false);
			}
		});
		panel_1.add(okButton);
		
		jDialog.setIconImage(GuiUtility.getApplicationIcon());
		jDialog.setResizable(true);
		jDialog.pack();
		GuiUtility.center(jDialog);
		jDialog.setVisible(true);
		
		return result;
		
	}
	
	
	public static String chooseXmlFileName(Component parent)
	{
		String result = null;
		JFileChooser chooser = new JFileChooser();
		//chooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
		//chooser.setCurrentDirectory(new java.io.File(""));
		chooser.setSelectedFile(new java.io.File(ConstantManager.DEFAULT_XML_FILE_NAME));
		chooser.setDialogTitle("Choose file to export:");
		//chooser.setAcceptAllFileFilterUsed(false);
		int chooserResult = chooser.showSaveDialog(parent);
		if (chooserResult != JFileChooser.CANCEL_OPTION)
		{
			Logger.log.info("chooseXmlFileName: " + chooser.getSelectedFile().toString() );
			result = chooser.getSelectedFile().toString();
		}
		return result;
	}
	
	/*
	public static void main(String[] args)
	{
		String[] result = ExporterInput.titleDescriptionInput();
		System.out.println("-->" + result[0]);
		System.out.println("-->" + result[1]);
		
	}
	*/
}
