package ppciarravano.algoexplorer.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import ppciarravano.algoexplorer.Workspace;
import ppciarravano.algoexplorer.util.ConstantManager;


/**
 * Classe ClasspathTreePanel
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class ClasspathTreePanel extends JPanel
{
	private Workspace wrkspc;
	
	private ClassTreeNode rootNode = null;
	private JTree tree = null;
	
	public ClasspathTreePanel(Workspace wrkspc)
	{
		super();
		
		this.wrkspc = wrkspc;
		this.wrkspc.setClasspathTreePanel(this);
		
		setLayout(new BorderLayout());
		
		rootNode = new ClassTreeNode(ConstantManager.CLASSPATH_TREE_ROOT_NAME);
				
		//Render tree
		tree = new JTree(rootNode);
		tree.setCellRenderer(new TreeCellRenderer()); //Usato per il render come cartellina vuota delle directory vuote
		tree.addMouseListener(new TreePopupMenuListener(this.wrkspc, tree)); //Gestisce l'interazione con l'albero e il menu popup contestuale
		JScrollPane jScrollPane = new JScrollPane(tree);
		add(jScrollPane, BorderLayout.CENTER);
				
		setVisible(true);
		
	}
	
	public ClassTreeNode getRootNode()
	{
		return this.rootNode;
	}
	
	public void updateTreeUI()
	{
		this.updateUI();
		tree.updateUI();
	}
	
}
