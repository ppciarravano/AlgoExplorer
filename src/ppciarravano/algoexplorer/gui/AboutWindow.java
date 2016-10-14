package ppciarravano.algoexplorer.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.jgraph.JGraph;

import ppciarravano.algoexplorer.log.Logger;
import ppciarravano.algoexplorer.util.ConstantManager;



/**
 * Classe per visualizzazione finestra di about.
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010) 
 */
public class AboutWindow
{
	private AboutWindow(){};
	
	private JFrame jFrame = null; 
			
	private AboutWindow(String lblTxt)
	{
		//TODO: trasformarlo in JDialog
		
		if(jFrame==null)
		{
			Logger.log.info("Open About Window");
			
			//Creo JFrame
			JFrame jFrameTemp = new JFrame();
			jFrameTemp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			//jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrameTemp.setSize(450, 650);
			jFrameTemp.setLocation(200,50);
			jFrameTemp.setTitle(ConstantManager.APP_NAME + " - About");
			jFrameTemp.setIconImage(GuiUtility.getApplicationIcon());
			jFrameTemp.setResizable(false);
			
			//Creo Layout
			JPanel jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			
			//Creo e aggiungo pulsante chiusura al JPanel
			JButton closeButton = new JButton();
			closeButton.setText("Chiudi");
			//closeButton.setSize(200, 30);
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jFrame.dispose();
				}
			});
			//Per prevenire il resizing del JButton nel BorderLayout
			JPanel panelButton = new JPanel();
			panelButton.setLayout(new FlowLayout());
			panelButton.add(closeButton);
			jContentPane.add(panelButton, BorderLayout.SOUTH);
			//jContentPane.add(closeButton, BorderLayout.SOUTH);
			
			//Creo JLabel e aggiungo al JPanel
			JLabel jLabel = new JLabel();
			jLabel.setHorizontalAlignment(SwingConstants.CENTER);
			//jLabel.setVerticalAlignment(SwingConstants.CENTER);
			jLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
			
			jLabel.setText(lblTxt);
			jLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			jContentPane.add(jLabel, BorderLayout.CENTER);
			
			//Aggiungo jContentPane al JFrame
			jFrameTemp.setContentPane(jContentPane);
			
			//Center frame in screen
			GuiUtility.center(jFrameTemp);
			
			//Visualizzo il JFrame
			jFrameTemp.setVisible(true);
			
			jFrame = jFrameTemp;
		}
	}
	
	public static void open()
	{
		//Creo il testo per la finestra about
		final StringBuffer lblTxt = new StringBuffer();
		//lblTxt.append("<html><font color=blue><b></b></font><br><b></u></b><br>" +
		//		"<code><font background-color=yellow>&nbsp;&nbsp;<br>&nbsp;&nbsp;test" +
		//		"</font></code></html>");
		lblTxt.append("<html>");
		lblTxt.append("<center>");
		lblTxt.append("<b>\"La Sapienza\"<br>Universit&agrave; di Roma</b><br><br>");
		
		lblTxt.append("Facolt&agrave; di Ingegneria<br>");
		lblTxt.append("Dipartimento di Informatica e Sistemistica<br>");
		lblTxt.append("Corso di Laurea in Ingegneria Informatica<br>");
		lblTxt.append("<font size=-1>Anno Accedemico 2008-2009</font><br><br>");
		
		lblTxt.append("Relatore Stage:<br>");
		lblTxt.append("Prof. Fabrizio d'Amore<br><br>");
		
		lblTxt.append("<font color=blue size=+3><b>"+ConstantManager.APP_NAME+"</b></font><br>");
		lblTxt.append("Applicazione per la visualizzazione didattica<br>e di debug per algoritmi in Java<br><br>");
		
		lblTxt.append("<b>di Pier Paolo Ciarravano</b><br>");
		lblTxt.append("<font size=-1>Matr. 773970</font><br>");
		lblTxt.append("<font size=-1>ppciarravano@gmail.com</font><br><br>");
		
		lblTxt.append("<code>Version: "+ConstantManager.VERSION+"</code><br>");
		lblTxt.append("<code>Using: "+JGraph.VERSION+"</code><br>");
		lblTxt.append("</center>");
		lblTxt.append("</html>");
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new AboutWindow(lblTxt.toString());
			}
		});
		
	}
	
	/*
	public static void main(String[] args)
	{
		GuiUtility.setLookAndFeel();
		AboutWindow.open();
	}
	*/
}
