package ppciarravano.algoexplorer.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Classe ProgressBarDialog
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010) 
 */
public class ProgressBarDialog extends JDialog implements Runnable
{
	private JProgressBar progressBar;
	private boolean closed = false;
	private int maximum = 100;
	
	public ProgressBarDialog(String title, String subTitle)
	{
		super((Frame) null, title, true);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getContentPane().add(panel, BorderLayout.CENTER);
		
		//Add ProgressBar
		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(this.maximum);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		Border border = BorderFactory.createTitledBorder(subTitle);
		progressBar.setBorder(border);
		panel.add(progressBar, BorderLayout.NORTH);		
		
		//Add Abort Button
		JButton closeButton = new JButton();
		closeButton.setText("Abort");
		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				closed = true;
				dispose();
			}
		});
		//Per prevenire il resizing del JButton nel BorderLayout
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout());
		panelButton.add(closeButton);
		panel.add(panelButton, BorderLayout.SOUTH);
				
		setIconImage(GuiUtility.getApplicationIcon());
		setResizable(false);
		setSize(300,130);
		GuiUtility.center(this);
		
		//Start Thread
		Thread th = new Thread(this);
		th.start();
	}
	
	public void setMaximum(int maxValue)
	{
		this.maximum = maxValue;
		progressBar.setMaximum(this.maximum);
	}
	
	public int getMaximum()
	{
		return maximum;
	}
	
	public void setProgressValue(int value)
	{
		progressBar.setValue(value);
	}
	
	public boolean isClosed()
	{
		return closed;
	}
	
	public void dispose()
	{
		super.dispose();
	}
	
	public void run() 
	{
		setVisible(true);
	}
}
