package ppciarravano.algoexplorer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import ppciarravano.algoexplorer.CommandManager;
import ppciarravano.algoexplorer.Workspace;
import ppciarravano.algoexplorer.CommandManager.Command;


/**
 * Classe MainToolBar
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class MainToolBar extends JToolBar
{
	private Workspace wrkspc;
	CommandManager commandManager;
	
	public MainToolBar(Workspace wrkspc)
	{
		this.wrkspc = wrkspc;
		this.wrkspc.setMainToolBar(this);
		this.commandManager = this.wrkspc.getCommandManager();
		
		addTool("Step", "Step", Command.OPEN_LOG4J_WINDOWS);
		addTool("Terminate", "Terminate", Command.OPEN_LOG4J_WINDOWS);
		//addTool("VIEW LOG", "Visualizza Log", Command.OPEN_LOG4J_WINDOWS);
		addSeparator();
		//addTool("VIEW LOG", "Visualizza Log", Command.OPEN_LOG4J_WINDOWS);
		
	}
	
	
	private void addTool(String labelText, String toolTip, final Command cmd)
	{
		JButton button = new JButton(labelText);
		button.setToolTipText(toolTip);
		
		button.setEnabled(false); //TODO: togliere
		
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				commandManager.executeCommand(cmd);
			}
		});
		this.add(button);
	}
}
