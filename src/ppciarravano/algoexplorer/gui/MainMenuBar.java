package ppciarravano.algoexplorer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import ppciarravano.algoexplorer.CommandManager;
import ppciarravano.algoexplorer.Workspace;
import ppciarravano.algoexplorer.CommandManager.Command;


/**
 * Classe MainMenuBar
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class MainMenuBar extends JMenuBar
{
	private Workspace wrkspc;
	CommandManager commandManager;
	
	public MainMenuBar(Workspace wrkspc)
	{
		super();
		
		this.wrkspc = wrkspc;
		this.commandManager = this.wrkspc.getCommandManager();
		
		//Costruisco il menu File
		JMenu fileMenu = new JMenu("File");
		addMenu(fileMenu, "New Workspace...", "Crea un nuovo workspace", Command.NEW);
		fileMenu.addSeparator();
		addMenu(fileMenu, "Exit", "Uscita dall'applicazione", Command.EXIT);
				
		
		//Costruisco il menu Settings
		//JMenu settingMenu = new JMenu("Settings");
		//addMenu(settingMenu, "Class filters...", "Gestione filtri classi", Command.OPEN_LOG4J_WINDOWS);		
		
		//Costruisco il menu Window
		JMenu windowMenu = new JMenu("Window");
		addMenu(windowMenu, "Open system log", "Apre il log dell'applicazione", Command.OPEN_LOG4J_WINDOWS);
		
		
		//Costruisco il menu Help
		JMenu helpMenu = new JMenu("Help");
		addMenu(helpMenu, "About", "Riguardo all'applicazione", Command.ABOUT);
		
		
		
		
		//Aggiungo i menu al MainMenuBar
		this.add(fileMenu);
		//this.add(settingMenu);
		this.add(windowMenu);
		this.add(helpMenu);
		
	}
	
	private void addMenu(JMenu menu, String labelText, String toolTip, final Command cmd)
	{
		JMenuItem subMenu = new JMenuItem(labelText);
		subMenu.setToolTipText(toolTip);
		
		subMenu.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				commandManager.executeCommand(cmd);
			}
		});
		menu.add(subMenu);
	}
	
}
