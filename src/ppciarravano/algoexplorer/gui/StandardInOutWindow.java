package ppciarravano.algoexplorer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ppciarravano.algoexplorer.util.ConstantManager;


/**
 * Classe per gestire lo standard Input/Output dell'applicazione lanciata
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class StandardInOutWindow
{
	private static JFrame jFrame = null; 
	private static JTextArea textArea = null;
	
	public static JTextArea getTextArea()
	{
		return textArea;
	}
	
	public static void show()
	{
		if(jFrame!=null)
		{
			jFrame.setVisible(true);
		}
	}
	
	public static void hide()
	{
		if(jFrame!=null)
		{
			jFrame.setVisible(false);
		}
	}
	
	private StandardInOutWindow()
	{
		if(jFrame==null)
		{
			
			//Creo JFrame
			JFrame jFrameTemp = new JFrame();
			jFrameTemp.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			//jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrameTemp.setSize(800, 450);
			jFrameTemp.setLocation(100,100);
			jFrameTemp.setTitle(ConstantManager.APP_NAME + " - Standard Input Output Window");
			jFrameTemp.setIconImage(GuiUtility.getApplicationIcon());
			
			//Creo Layout
			JPanel jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			
			//Creo e aggiungo pulsante chiusura al JPanel
			JButton closeButton = new JButton();
			closeButton.setText("Chiudi");
			//closeButton.setSize(200, 30);
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					hide();
				}
			});
			//Per prevenire il resizing del JButton nel BorderLayout
			JPanel panelButton = new JPanel();
			panelButton.setLayout(new FlowLayout());
			panelButton.add(closeButton);
			jContentPane.add(panelButton, BorderLayout.SOUTH);
			//jContentPane.add(closeButton, BorderLayout.SOUTH);
			
			//Creo JTextArea
			textArea = new JTextArea();
			textArea.setEditable(true);
			textArea.setBackground(Color.WHITE);
			textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
			textArea.setText("");
			textArea.setBorder(BorderFactory.createLineBorder(Color.BLUE));
			
			//creo uno JScrollPane con dentro la textArea e lo aggiungo
			JScrollPane jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(textArea);
			jContentPane.add(jScrollPane, BorderLayout.CENTER);
			
			//Aggiungo jContentPane al JFrame
			jFrameTemp.setContentPane(jContentPane);
			
			//Visualizzo il JFrame
			jFrameTemp.setVisible(true);
			
			jFrame = jFrameTemp;
		}
	}
	
	public static void init()
	{
		new StandardInOutWindow();
	}
	
	/*
	public void append(final String message) {
		if (textArea!=null)
		{
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					textArea.append(message);
				}
			});
		}
	}
	*/
	
}
