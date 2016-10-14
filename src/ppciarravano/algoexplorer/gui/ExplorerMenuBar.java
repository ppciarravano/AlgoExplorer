package ppciarravano.algoexplorer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import ppciarravano.algoexplorer.CommandManager;
import ppciarravano.algoexplorer.Workspace;
import ppciarravano.algoexplorer.CommandManager.Command;

import com.jgraph.layout.JGraphLayout;
import com.jgraph.layout.graph.JGraphSimpleLayout;
import com.jgraph.layout.hierarchical.JGraphHierarchicalLayout;
import com.jgraph.layout.organic.JGraphFastOrganicLayout;
import com.jgraph.layout.organic.JGraphOrganicLayout;
import com.jgraph.layout.organic.JGraphSelfOrganizingOrganicLayout;
import com.jgraph.layout.tree.JGraphCompactTreeLayout;
import com.jgraph.layout.tree.JGraphRadialTreeLayout;
import com.jgraph.layout.tree.JGraphTreeLayout;
import com.jgraph.layout.tree.OrganizationalChart;


/**
 * Classe ExplorerMenuBar
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class ExplorerMenuBar extends JMenuBar
{
	private Workspace wrkspc;
	CommandManager commandManager;
	
	public ExplorerMenuBar(Workspace wrkspcParam)
	{
		super();
		
		this.wrkspc = wrkspcParam;
		this.commandManager = this.wrkspc.getCommandManager();
		
		//Costruisco il menu File
		JMenu fileMenu = new JMenu("File");
		addMenu(fileMenu, "Export XML for AlgoExplorer Player...", "Export XML for AlgoExplorer Player...", Command.EXPORT_XML);
			
		//Costruisco il menu Layout
		JMenu layoutMenu = new JMenu("Graph Layout");
		ButtonGroup layoutGroup = new ButtonGroup();
		Action[] layoutActions = createLayoutActions();
		for (int i = 0; i < layoutActions.length; i++) {
			layoutMenu.add(createRadioMenuItem(layoutGroup, layoutActions[i], "Use "+ String.valueOf(layoutActions[i].getValue("title")+ " Layout")));
		}
		layoutMenu.getItem(6).setSelected(true);
		add(layoutMenu);
		layoutMenu.setEnabled(true);
		
		//Costruisco il menu Window
		JMenu traceMenu = new JMenu("Trace");
		addMenu(traceMenu, "Enable Trace Requests", "Enable Trace Requests", Command.ENABLE_REQUEST);
		addMenu(traceMenu, "Disable Trace Requests", "Disable Trace Requests", Command.DISABLE_REQUEST);
	
		//Costruisco il menu Window
		JMenu windowMenu = new JMenu("Window");
		addMenu(windowMenu, "Open system log", "Apre il log dell'applicazione", Command.OPEN_LOG4J_WINDOWS);
		addMenu(windowMenu, "Open VM Output Window", "Apre finestra di Standard Input Output Virtual Machine", Command.OPEN_INOUT_RUNTIME_WINDOWS);
		
		
		//Costruisco il menu Help
		JMenu helpMenu = new JMenu("Help");
		addMenu(helpMenu, "About", "Riguardo all'applicazione", Command.ABOUT);
		
		
		//Aggiungo i menu al MainMenuBar
		this.add(fileMenu);
		this.add(layoutMenu);
		this.add(traceMenu);
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
	
	private static JRadioButtonMenuItem createRadioMenuItem(ButtonGroup group,
			Action action, String toolTip) {
		JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(action);
		menuItem.setToolTipText(toolTip);
		menuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift "
				+ String.valueOf(action.getValue("shortcut")).substring(0, 1)
						.toUpperCase()));
		group.add(menuItem);
		return menuItem;
	}
	
	private Action createLayoutAction(final String title, String shortcut,
			final JGraphLayout layout) {
		Action action = new AbstractAction(title) {
			public void actionPerformed(ActionEvent e) {
				wrkspc.getExplorerGraph().setLayout(layout);
				wrkspc.getExplorerGraph().executeLayout();
			}
		};
		action.putValue("shortcut", shortcut);
		action.putValue("title", title);
		return action;
	}
	
	public Action[] createLayoutActions()
	{
		JGraphFastOrganicLayout fastOrganicLayoutnew = new JGraphFastOrganicLayout();
		fastOrganicLayoutnew.setForceConstant(200);
		
		Action[] layoutActions = new Action[] {
				createLayoutAction("Compact Tree", "m", new JGraphCompactTreeLayout()),
				createLayoutAction("Tree", "t", new JGraphTreeLayout()),
				createLayoutAction("Organisational Chart", "g", new OrganizationalChart()),
				createLayoutAction("Radial Tree", "r", 	new JGraphRadialTreeLayout()),
				createLayoutAction("ISOM", "i", new JGraphSelfOrganizingOrganicLayout()),
				createLayoutAction("Organic", "o", new JGraphOrganicLayout()),
				createLayoutAction("Fast Organic", "f", fastOrganicLayoutnew),
				createLayoutAction("Hierarchical", "h", new JGraphHierarchicalLayout()),
				createLayoutAction("Circle", "c", new JGraphSimpleLayout(JGraphSimpleLayout.TYPE_CIRCLE)),
				createLayoutAction("Tilt", "l", new JGraphSimpleLayout(JGraphSimpleLayout.TYPE_TILT, 100, 100)),
				createLayoutAction("Random", "n", new JGraphSimpleLayout(JGraphSimpleLayout.TYPE_RANDOM, 640, 480)),
				createLayoutAction("No Auto Layout", "z", null) 
				};
		return layoutActions;
	}
	
}
