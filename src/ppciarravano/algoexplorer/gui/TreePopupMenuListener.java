package ppciarravano.algoexplorer.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import ppciarravano.algoexplorer.Workspace;
import ppciarravano.algoexplorer.log.Logger;


/**
 * Classe TreePopupMenuListener
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class TreePopupMenuListener extends MouseAdapter implements ActionListener {

	private Workspace wrkspc;
	
	private JPopupMenu popup;
	private JTree tree;

	public TreePopupMenuListener(Workspace wrkspc, JTree tree)
	{
		super();
		this.wrkspc = wrkspc;
		
		this.tree = tree;
		JMenuItem menu;
		popup = new JPopupMenu();
		
		menu = new JMenuItem("Clear set Fields and open");
		menu.addActionListener(this);
		menu.setActionCommand("clear");
		popup.add(menu);
		
		menu = new JMenuItem("Run...");
		menu.addActionListener(this);
		menu.setActionCommand("run");
		popup.add(menu);
		
		
		popup.setOpaque(true);
		popup.setLightWeightPopupEnabled(true);

	}

	// @Override
	public void mousePressed(MouseEvent e) {
		//Logger.log.debug(e.getClickCount());
		verifyDoubleClickTrigger(e);
		verifyPopupTrigger(e);
	}

	// @Override
	public void mouseReleased(MouseEvent e) {
		verifyPopupTrigger(e);
	}
	
	private void verifyDoubleClickTrigger(MouseEvent e)
	{
		if (e.getClickCount()==2)
		{
			Point point = e.getPoint();
			TreePath path = tree.getClosestPathForLocation(point.x, point.y);
			if (path != null)
			{
				ClassTreeNode ctn = (ClassTreeNode)path.getLastPathComponent();
				if (ctn.getClassDescriptor()!=null) 
				{
					Logger.log.debug("Open Class:" + ctn.getClassDescriptor().getClassName());
					//Apro il pannello di edit per variabili scelta visualizzazione
					wrkspc.loadSourceAndFieldsClass(ctn.getClassDescriptor());
				}
			}
		}
	}
	
	private void verifyPopupTrigger(MouseEvent e)
	{
		if (popup != null && e.isPopupTrigger())
		{
			Point point = e.getPoint();
			TreePath path = tree.getClosestPathForLocation(point.x, point.y);
			if (path != null)
			{
				ClassTreeNode ctn = (ClassTreeNode)path.getLastPathComponent();
				if (ctn.getClassDescriptor()!=null) 
				{
					tree.getSelectionModel().setSelectionPath(path);
					//TreeNode node = (TreeNode)path.getLastPathComponent();
					JPopupMenu menu = popup;
					if (menu != null)
					{
						menu.show(tree, point.x, point.y);
					}
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		TreePath path = tree.getSelectionPath();
		//DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)path.getLastPathComponent();
		
		ClassTreeNode ctn = (ClassTreeNode)path.getLastPathComponent();
		if (ctn.getClassDescriptor()!=null)
		{
			if (e.getActionCommand().equals("clear"))
			{
				int option = JOptionPane.showOptionDialog(  
						wrkspc.getMainPanel(),  
	                    "Sei sicuro che vuoi pulire i settaggi di visualizzazione della classe?",  
	                    "Clear Fields", JOptionPane.YES_NO_OPTION,  
	                    JOptionPane.WARNING_MESSAGE, null, null, null );  
	            if( option == JOptionPane.YES_OPTION )
	            {  
					//Logger.log.debug("CLEAR:" + e.getActionCommand() + ": " + dmtn.getUserObject());
					Logger.log.debug("CLEAR:" + e.getActionCommand() + ": " + ctn.getClassDescriptor().getClassName());
					//Pulisco le variabili che settano le scelta di visualizzazione
					ctn.getClassDescriptor().clearViewChoices();
					//Pulisco i breakpoint eventualmente inseriti
					ctn.getClassDescriptor().getSourceDescriptor().clearBreakpoint();
					//Apro il pannello di edit per variabili scelta visualizzazione
					wrkspc.loadSourceAndFieldsClass(ctn.getClassDescriptor());
	            }
			}
			else if (e.getActionCommand().equals("run"))
			{
				//Logger.log.debug("RUN:" + e.getActionCommand() + ": " + dmtn.getUserObject());
				Logger.log.debug("RUN:" + e.getActionCommand() + ": " + ctn.getClassDescriptor().getClassName());
				//ctn.getClassDescriptor().dumpFieldsAndMethods();
				Logger.log.debug("IS MAIN CLASS:" + ctn.getClassDescriptor().isMainClass() );
				Logger.log.debug("getSourcePathName:" + ctn.getClassDescriptor().getSourcePathName());
				if (ctn.getClassDescriptor().isMainClass())
				{
					Logger.log.debug("Class is a main class");
					this.wrkspc.getCommandManager().executeRunMainClass(ctn.getClassDescriptor());
				}
				else
				{
					JOptionPane.showMessageDialog(null, "La classe selezionata non e' una main class!", "Attenzione!!", JOptionPane.ERROR_MESSAGE); 
				}
				
			}
		}
	}
		
}
