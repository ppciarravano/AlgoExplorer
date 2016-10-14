package ppciarravano.algoexplorer.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.jgraph.JGraph;

import ppciarravano.algoexplorer.Workspace;
import ppciarravano.algoexplorer.log.Logger;
import ppciarravano.algoexplorer.util.ConstantManager;

import com.jgraph.navigation.GraphNavigator;


/**
 * Classe ExplorerPanel
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010) 
 */
public class ExplorerPanel extends JPanel
{
	private Workspace wrkspc;
	
	public ExplorerPanel(Workspace wrkspcParam)
	{
		this.wrkspc = wrkspcParam;
		this.wrkspc.setExplorerPanel(this);
		
		setLayout(new BorderLayout());
		
		ExplorerToolBar explorerToolBar = new ExplorerToolBar(this.wrkspc);
		add(explorerToolBar, BorderLayout.NORTH);
		
		StatusBar statusBar = new StatusBar(wrkspc);
		wrkspc.setExplorerStatusBar(statusBar);
		add(statusBar, BorderLayout.SOUTH);
		
		ExplorerGraph explorerGraph = new ExplorerGraph(wrkspc);
		JGraph graph = explorerGraph.getJGraph();
		
		GraphNavigator navigator = explorerGraph.getGraphNavigator();
		wrkspc.setGraphNavigator(navigator);
		
		InspectPanel inspectPanel = new InspectPanel(wrkspc);
				
		JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, navigator, inspectPanel);
		leftSplitPane.setDividerLocation(320);
		leftSplitPane.setContinuousLayout(true);
		
		JSplitPane centerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplitPane, new JScrollPane(graph));
		centerSplitPane.setDividerLocation(270);
		centerSplitPane.setContinuousLayout(true);
		centerSplitPane.setOneTouchExpandable(true);
		centerSplitPane.setDividerSize(8);
		
		add(centerSplitPane, BorderLayout.CENTER);
		
	}	
	
	public static void createExplorerFrame(Workspace wrkspcParam)
	{
		try
		{
						
			//Construct MainPanel
			final ExplorerPanel explorerPanel = new ExplorerPanel(wrkspcParam);
						
			//Construct Frame
			JFrame frame = new JFrame(ConstantManager.APP_NAME + " - Explorer Window" );
			
			//frame.setBackground(Color.lightGray);
			
			//Set Close Operation to Exit
			//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.addWindowListener(new WindowAdapter() {
			    public void windowClosing(WindowEvent e) {
			    	explorerPanel.wrkspc.terminateVM();
			    }
			});			
			
			//Set icon
			frame.setIconImage(GuiUtility.getApplicationIcon());
			//Set Default Size
			frame.setSize(ConstantManager.INITIAL_APPLICATION_WIDTH, ConstantManager.INITIAL_APPLICATION_HEIGHT);
			frame.setResizable(true);
				
			//Set Menu
			frame.setJMenuBar(new ExplorerMenuBar(wrkspcParam));
			//Set ContentPane
			frame.setContentPane(explorerPanel);
			
			//Center frame in screen
			//GuiUtility.center(frame);
			
			//Show Frame
			frame.setVisible(true);
			frame.requestFocus();
			
			
		}
		catch (Exception e)
		{
			Logger.log.error("AlgoExplorer Exception:" + Logger.exceptionToString(e));
		}
	}
}
