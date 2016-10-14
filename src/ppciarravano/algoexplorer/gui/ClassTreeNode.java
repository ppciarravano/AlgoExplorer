package ppciarravano.algoexplorer.gui;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

import ppciarravano.algoexplorer.ClassDescriptor;


/**
 * Classe ClassTreeNode
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class ClassTreeNode extends DefaultMutableTreeNode
{
	private File file = null;
	private ClassDescriptor classDescriptor = null;
	private boolean directory = false;
	private boolean root = false;
	
	/*
	 * Costruttore utilizzato per il solo nodo root
	 */
	public ClassTreeNode(String rootName)
	{
		super(rootName);
		file = null;
		this.directory = true;
		root = true;
		classDescriptor = null;
	}
	
	public ClassTreeNode(File file)
	{
		super(file.getName());
		this.file = file;
		root = false;
	}

	public ClassDescriptor getClassDescriptor()
	{
		return classDescriptor;
	}

	public void setClassDescriptor(ClassDescriptor classDescriptor)
	{
		this.classDescriptor = classDescriptor;
	}

	public File getFile()
	{
		return file;
	}
	
	public void setFile(File file)
	{
		this.file = file;
	}
	
	public boolean isDirectory()
	{
		return directory;
	}
	
	public void setDirectory(boolean directory)
	{
		this.directory = directory;
	}

	public boolean isRoot()
	{
		return root;
	}	
	
}
