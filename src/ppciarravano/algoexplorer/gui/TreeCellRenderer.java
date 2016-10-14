package ppciarravano.algoexplorer.gui;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * Classe TreeCellRenderer
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010) 
 */
public class TreeCellRenderer extends DefaultTreeCellRenderer
{

	public Component getTreeCellRendererComponent(JTree tree, Object obj, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		super.getTreeCellRendererComponent(tree, obj, selected, expanded, leaf, row, hasFocus);
		
		//if (((ClassTreeNode) obj).getFile()!=null)
		//	Logger.log.debug("obj: " + ((ClassTreeNode) obj).getFile().getName() + "-->" + ((ClassTreeNode) obj).isDirectory());
		
		if (((ClassTreeNode) obj).isDirectory())
		{
			setIcon(closedIcon);
		}

		if (expanded)
		{
			setIcon(openIcon);
		}

		return this;
	}
}
