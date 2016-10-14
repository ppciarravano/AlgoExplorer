package ppciarravano.algoexplorer.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ppciarravano.algoexplorer.Workspace;
import ppciarravano.algoexplorer.util.ConstantManager;


/**
 * Classe StatusBar
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010) 
 */
public class StatusBar extends JPanel
{
	
	protected JLabel leftSideStatus;
	protected JLabel rightSideStatus;

	private Workspace wrkspc;
	
	public StatusBar(Workspace wrkspc) {
		super();
		
		this.wrkspc = wrkspc;
		
		// Add this as graph model change listener
		setLayout(new BorderLayout());
		leftSideStatus = new JLabel(ConstantManager.VERSION);
		rightSideStatus = new JLabel("0 of 0Mb");
		leftSideStatus.setBorder(BorderFactory.createLoweredBevelBorder());
		rightSideStatus.setBorder(BorderFactory.createLoweredBevelBorder());
		add(leftSideStatus, BorderLayout.CENTER);
		add(rightSideStatus, BorderLayout.EAST);
		
		updateStatusBar();
	}

	synchronized public void updateStatusBar()
	{
		Runtime runtime = Runtime.getRuntime();
		int freeMemory = (int) (runtime.freeMemory() / 1024);
		int totalMemory = (int) (runtime.totalMemory() / 1024);
		int usedMemory = (totalMemory - freeMemory);
		String str = (usedMemory / 1024) + " of " + (totalMemory / 1024) + "Mb";
		rightSideStatus.setText(str);
	}

	synchronized public void setStatusMessage(final String message)
	{
		leftSideStatus.setText(message);
		updateStatusBar();
	}
	
	/*
	private JLabel getLeftSideStatus() {
		return leftSideStatus;
	}

	private void setLeftSideStatus(JLabel leftSideStatus) {
		this.leftSideStatus = leftSideStatus;
	}

	private JLabel getRightSideStatus() {
		return rightSideStatus;
	}

	private void setRightSideStatus(JLabel rightSideStatus) {
		this.rightSideStatus = rightSideStatus;
	}
	*/
	
}
