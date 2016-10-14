package ppciarravano.algoexplorer;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import ppciarravano.algoexplorer.gui.ClassFieldsPanel;
import ppciarravano.algoexplorer.gui.ClassTreeNode;
import ppciarravano.algoexplorer.gui.ClasspathTreePanel;
import ppciarravano.algoexplorer.gui.ExplorerGraph;
import ppciarravano.algoexplorer.gui.ExplorerPanel;
import ppciarravano.algoexplorer.gui.ExplorerToolBar;
import ppciarravano.algoexplorer.gui.InspectPanel;
import ppciarravano.algoexplorer.gui.MainPanel;
import ppciarravano.algoexplorer.gui.MainToolBar;
import ppciarravano.algoexplorer.gui.ProgressBarDialog;
import ppciarravano.algoexplorer.gui.SourcePanel;
import ppciarravano.algoexplorer.gui.StatusBar;
import ppciarravano.algoexplorer.log.Logger;
import ppciarravano.algoexplorer.trace.InspectThread;
import ppciarravano.algoexplorer.trace.Tracer;
import ppciarravano.algoexplorer.util.ConstantManager;

import com.jgraph.navigation.GraphNavigator;


/**
 * Classe Workspace
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010) 
 */
public class Workspace
{
	private CommandManager commandManager;
	private String classpath = "";
	private String sourcepath = "";
	
	private String commandLineMainArguments = "";
	private String commandLineVMOptions = "";
	
	private Hashtable<String, ClassDescriptor> classDescriptors = null;
	private Hashtable<String, SourceDescriptor> sourceDescriptors = null;
	
	private MainPanel mainPanel;
	private MainToolBar mainToolBar;
	private ClasspathTreePanel classpathTreePanel;
	private SourcePanel sourcePanel;
	private ClassFieldsPanel classFieldsPanel;
	private StatusBar mainStatusBar;
	
	private ExplorerPanel explorerPanel;
	private ExplorerToolBar exporerToolBar;
	private ExplorerGraph explorerGraph;
	private GraphNavigator graphNavigator;
	private InspectPanel inspectPanel;
	private StatusBar explorerStatusBar;
	
	private Tracer tracer;
	private InspectThread inspectThread;
	private String[] classExclusionFilter = ConstantManager.CLASS_EXCLUSION_FILTER;
	private String lastRunMainClass;
	
	private ProgressBarDialog progressBar;
	private int countFileClass = 0;
	
	public Workspace()
	{
		commandManager = new CommandManager(this);

	}
	
	
	/*
	 * Metodo usato quanto si vuole reinizializzare il workspace
	 */
	public void clearWorkspace()
	{
		//TODO
	}
	
	public CommandManager getCommandManager()
	{
		return commandManager;
	}
	
	public void terminate()
	{
		terminateVM();
		System.exit(0);
	}
	
	public void terminateVM()
	{
		if ( (getTracer()!=null) &&
				(getTracer().getInspecThread()!=null) &&
				(!getTracer().getInspecThread().isVmDied()) &&
				(getTracer().getInspecThread().isConnected()) )
		{
			Logger.log.debug("terminateVM");
    		getTracer().exitVM();
		}
	}

	
	public void buildClasspathTree()
	{
		Logger.log.info("buildClasspathTree classpath:"+classpath);
		mainStatusBar.setStatusMessage("Reading new classpath...");

		//Init ProgressBarDialog
		progressBar = new ProgressBarDialog("Reading progress", "Reading classpath...");
		
		//Prepara root Node
		ClassTreeNode rootNode = classpathTreePanel.getRootNode();
		rootNode.setFile(new File(classpath));
		this.countFileClass = 0;
		
		//Build tree from file system
		recursiveBuildTree(rootNode, rootNode);
		Logger.log.debug("countFileClass: " + countFileClass);
		
		//Set Maximum in progressBar
		progressBar.setMaximum(countFileClass);
		
		//Read file .class and bytecode intrumentation using Apache Bcel lib
		classDescriptors = new Hashtable<String, ClassDescriptor>();
		sourceDescriptors = new Hashtable<String, SourceDescriptor>();
		recursiveBuildClassDescriptors(rootNode);
		
		/*
			//Dump classDescriptors
			Enumeration<String> keys = classDescriptors.keys();
			while(keys.hasMoreElements())
			{
				String key = keys.nextElement();
				ClassDescriptor cd = classDescriptors.get(key);
				Logger.log.debug(key + " --> " + cd.getSourceFileName());
			}
		*/
			
		//Refresh User Interface
		progressBar.dispose();
		if (!progressBar.isClosed())
		{
			mainStatusBar.setStatusMessage("Done");
		}
		else
		{
			//Clear rootNode
			rootNode.removeAllChildren();
			classDescriptors = null;
			mainStatusBar.setStatusMessage("User abort!!");
		}
		classpathTreePanel.updateTreeUI();
	}
	
	private void recursiveBuildTree(ClassTreeNode nodeToVisit, ClassTreeNode parent)
	{
		if (progressBar.isClosed())
		{
			return;
		}
		
		if (nodeToVisit.getFile().isDirectory())
		{
			nodeToVisit.setDirectory(true);
			
			//list file in dir
			File[] filesInDir = nodeToVisit.getFile().listFiles();
			if ((filesInDir!=null)&&(filesInDir.length!=0))
			{
				ClassTreeNode dirNode = parent;
				//Per evitare di aggiungere la cartella classpath all'albero
				if (!nodeToVisit.isRoot())
				{
					dirNode = nodeToVisit;
					parent.add(dirNode);
				}
				
				for (int i = 0; i < filesInDir.length; i++)
				{
					recursiveBuildTree(new ClassTreeNode(filesInDir[i]), dirNode);
				}
			}
		}
		else
		{
			nodeToVisit.setDirectory(false);
			
			//Aggiungo solo i .class
			//if ( (nodeToVisit.getFile().getName().length()>6) && (nodeToVisit.getFile().getName().toLowerCase().substring(nodeToVisit.getFile().getName().length()-6).equals(".class")))
			if (nodeToVisit.getFile().getName().toLowerCase().endsWith(".class"))
			{
				this.countFileClass++;
				parent.add(nodeToVisit);
			}
		}	
	}
	
	
	@SuppressWarnings("unchecked")
	private void recursiveBuildClassDescriptors(ClassTreeNode parent)
	{
		
		if (progressBar.isClosed())
		{
			return;
		}
		
		Enumeration childs = parent.children();
		while(childs.hasMoreElements())
		{
			ClassTreeNode child = (ClassTreeNode)childs.nextElement();
			//Logger.log.debug("child:" + child.getFile().getName() + " isDir:" + child.isDirectory());
			if (!child.isDirectory())
			{
				//update progressBar
				progressBar.setProgressValue(progressBar.getMaximum()-this.countFileClass+1);
				//Logger.log.debug("progressBar: " + (progressBar.getMaximum()-this.countFileClass+1));
				this.countFileClass--;
				
				ClassDescriptor clsDscr = ClassDescriptor.getClassDescriptorFromFile(child.getFile());
				if (clsDscr!=null)
				{
					//Aggiorno Hashtable
					classDescriptors.put(clsDscr.getClassName(), clsDscr);
					//set reference in ClassTreeNode
					child.setClassDescriptor(clsDscr);
					
					//Verifico se la classe e' una classe main
					// in modo da visualizzarla in modo diverso;
					// per ora aggiunto semplicemente un *,
					//TODO: cambiare colore
					if (clsDscr.isMainClass())
					{
						child.setUserObject(child.getFile().getName() + " *");
					}
					
					//Associo il SourceDescritor
					String sourcePathName = clsDscr.getSourcePathName();
					SourceDescriptor srcDscr = null;
					if (sourceDescriptors.containsKey(sourcePathName))
					{
						//Se gia' esiste un SourceDescriptor
						srcDscr = sourceDescriptors.get(sourcePathName);
					}
					else
					{
						//Se non esiste un SourceDescriptor
						srcDscr = new SourceDescriptor(clsDscr.getSourceFileName(), clsDscr.getPackageName(), sourcePathName);
						sourceDescriptors.put(sourcePathName, srcDscr);
					}
					srcDscr.addClass(clsDscr);
					clsDscr.setSourceDescriptor(srcDscr);
					
				}
				else
				{
					//Se clsDscr e' null allora non rappresenta una classe valida e quindi devo devo rimuovere child dal tree
					child.removeFromParent();
				}
			}
			recursiveBuildClassDescriptors(child);			
		}
	}
	
	public void loadSourceAndFieldsClass(ClassDescriptor clsDscr)
	{
		if (!getSourcePanel().setSourceLines(clsDscr.getSourceDescriptor(), sourcepath))
		{
			Logger.log.debug("loadSourceClass not load source code!!");
		}
		
		getClassFieldsPanel().createFromClassDescriptor(clsDscr);
	}
	
	//Set and Get Methods
	
	public String getClasspath()
	{
		return classpath;
	}

	public void setClasspath(String classpath)
	{
		this.classpath = classpath;
	}

	public String getSourcepath()
	{
		return sourcepath;
	}

	public void setSourcepath(String sourcepath)
	{
		this.sourcepath = sourcepath;
	}
	
	public MainPanel getMainPanel()
	{
		return mainPanel;
	}

	public void setMainPanel(MainPanel mainPanel)
	{
		this.mainPanel = mainPanel;
	}	
	
	public ClasspathTreePanel getClasspathTreePanel()
	{
		return classpathTreePanel;
	}

	public void setClasspathTreePanel(ClasspathTreePanel classpathTreePanel)
	{
		this.classpathTreePanel = classpathTreePanel;
	}

	public SourcePanel getSourcePanel()
	{
		return sourcePanel;
	}

	public void setSourcePanel(SourcePanel sourcePanel)
	{
		this.sourcePanel = sourcePanel;
	}

	public ClassFieldsPanel getClassFieldsPanel()
	{
		return classFieldsPanel;
	}

	public void setClassFieldsPanel(ClassFieldsPanel classChoicePanel)
	{
		this.classFieldsPanel = classChoicePanel;
	}

	public StatusBar getMainStatusBar()
	{
		return mainStatusBar;
	}

	public void setMainStatusBar(StatusBar statusBar)
	{
		this.mainStatusBar = statusBar;
	}

	public String getCommandLineMainArguments()
	{
		return commandLineMainArguments;
	}

	public void setCommandLineMainArguments(String commandLineMainArguments)
	{
		this.commandLineMainArguments = commandLineMainArguments;
	}

	public String getCommandLineVMOptions()
	{
		return commandLineVMOptions;
	}

	public void setCommandLineVMOptions(String commandLineVMOptions)
	{
		this.commandLineVMOptions = commandLineVMOptions;
	}

	public ExplorerPanel getExplorerPanel()
	{
		return explorerPanel;
	}

	public void setExplorerPanel(ExplorerPanel explorerPanel)
	{
		this.explorerPanel = explorerPanel;
	}

	public MainToolBar getMainToolBar()
	{
		return mainToolBar;
	}

	public void setMainToolBar(MainToolBar mainToolBar)
	{
		this.mainToolBar = mainToolBar;
	}

	public ExplorerToolBar getExporerToolBar()
	{
		return exporerToolBar;
	}

	public void setExporerToolBar(ExplorerToolBar exporerToolBar)
	{
		this.exporerToolBar = exporerToolBar;
	}

	public StatusBar getExplorerStatusBar()
	{
		return explorerStatusBar;
	}

	public void setExplorerStatusBar(StatusBar explorerStatusBar)
	{
		this.explorerStatusBar = explorerStatusBar;
	}

	public InspectPanel getInspectPanel()
	{
		return inspectPanel;
	}

	public void setInspectPanel(InspectPanel inspectPanel)
	{
		this.inspectPanel = inspectPanel;
	}

	public GraphNavigator getGraphNavigator()
	{
		return graphNavigator;
	}

	public void setGraphNavigator(GraphNavigator graphNavigator)
	{
		this.graphNavigator = graphNavigator;
	}

	public ExplorerGraph getExplorerGraph()
	{
		return explorerGraph;
	}

	public void setExplorerGraph(ExplorerGraph explorerGraph)
	{
		this.explorerGraph = explorerGraph;
	}

	public String[] getClassExclusionFilter()
	{
		return classExclusionFilter;
	}

	public void setClassExclusionFilter(String[] classExclusionFilter)
	{
		this.classExclusionFilter = classExclusionFilter;
	}

	public Tracer getTracer()
	{
		return tracer;
	}

	public void setTracer(Tracer tracer)
	{
		this.tracer = tracer;
	}

	public InspectThread getInspectThread()
	{
		return inspectThread;
	}

	public void setInspectThread(InspectThread inspectThread)
	{
		this.inspectThread = inspectThread;
	}

	public Hashtable<String, ClassDescriptor> getClassDescriptors()
	{
		return classDescriptors;
	}

	public String getLastRunMainClass()
	{
		return lastRunMainClass;
	}

	public void setLastRunMainClass(String lastRunMainClass)
	{
		this.lastRunMainClass = lastRunMainClass;
	}	
	
}
