package ppciarravano.algoexplorer.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import ppciarravano.algoexplorer.Workspace;


/**
 * Classe MainPanel
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class MainPanel extends JPanel
{
	private Workspace wrkspc;
	
	public MainPanel(Workspace wrkspcParam)
	{
		this.wrkspc = wrkspcParam;
		this.wrkspc.setMainPanel(this);
		
		setLayout(new BorderLayout());
		
		//TODO: MainToolBar usata per versioni future
		//MainToolBar mainToolBar = new MainToolBar(this.wrkspc);
		//add(mainToolBar, BorderLayout.NORTH);
		
		StatusBar statusBar = new StatusBar(wrkspc);
		wrkspc.setMainStatusBar(statusBar);
		add(statusBar, BorderLayout.SOUTH);
		
		ClasspathTreePanel classpathTreePanel = new ClasspathTreePanel(wrkspc);
		SourcePanel sourcePanel = new SourcePanel(wrkspc);
		ClassFieldsPanel classFieldsPanel = new ClassFieldsPanel(wrkspc);
		
		JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sourcePanel, classFieldsPanel);
		rightSplitPane.setDividerLocation(320);
		rightSplitPane.setContinuousLayout(true);
		
		JSplitPane centerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, classpathTreePanel, rightSplitPane);
		centerSplitPane.setDividerLocation(270);
		centerSplitPane.setContinuousLayout(true);
		centerSplitPane.setOneTouchExpandable(true);
		centerSplitPane.setDividerSize(8);
		
		add(centerSplitPane, BorderLayout.CENTER);
				
		
	}	
	
	
}
