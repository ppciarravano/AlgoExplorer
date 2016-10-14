package ppciarravano.algoexplorer;

import javax.swing.JOptionPane;

import org.jgraph.JGraph;

import ppciarravano.algoexplorer.export.ExportFrames;
import ppciarravano.algoexplorer.export.XMLExporter;
import ppciarravano.algoexplorer.gui.AboutWindow;
import ppciarravano.algoexplorer.gui.ExplorerPanel;
import ppciarravano.algoexplorer.gui.ExporterInput;
import ppciarravano.algoexplorer.gui.RunMainClassDialog;
import ppciarravano.algoexplorer.gui.StandardInOutWindow;
import ppciarravano.algoexplorer.gui.WorkspaceNewDialog;
import ppciarravano.algoexplorer.log.Logger;
import ppciarravano.algoexplorer.log.LoggerWindow;
import ppciarravano.algoexplorer.trace.Tracer;


/**
 * Classe CommandManager
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class CommandManager
{
	private Workspace wrkspc;
	
	public enum Command {
		NEW,
		//OPEN,
		//SAVE,
		EXIT,
		OPEN_LOG4J_WINDOWS,
		OPEN_INOUT_RUNTIME_WINDOWS,
		HELP,
		ABOUT,
		ZOOM_RESET,
		ZOOM_IN,
		ZOOM_OUT,
		//TEST_ADD,
		//TEST_DEL,
		PLAY,
		CONTINUOUS_PLAY,
		SUSPEND,
		EXIT_VM,
		ENABLE_REQUEST,
		DISABLE_REQUEST,
		
		CAPTURE_SHOT,
		EXPORT_XML
				
	}
	
	public CommandManager(Workspace wrkspc)
	{
		this.wrkspc = wrkspc;
	}
	
	public void executeCommand(Command command)
	{
		Logger.log.info("CommandManager executeCommand:"+command);
		
		switch (command) {
		
			case ABOUT:
				AboutWindow.open();
				break;	
				
			case OPEN_LOG4J_WINDOWS:
				LoggerWindow.show();
				break;	
				
			case OPEN_INOUT_RUNTIME_WINDOWS:
				StandardInOutWindow.show();
				break;	
			
			case NEW:
			{
				//TODO: per ora disabilito la possibilita' di fare "new workspace",
				// se gia' un workspace e' stato inizializzato,
				// ma in versioni future gestire questa possibilita'
				if (this.wrkspc.getClasspath().equals(""))
				{
					new WorkspaceNewDialog(this.wrkspc);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "In questa versione non è possibile inizializzare un nuovo Workspace!", "Attenzione!!", JOptionPane.ERROR_MESSAGE);
				}
				break;	
			}
			
			case ZOOM_RESET:
			{
				JGraph graph = this.wrkspc.getExplorerGraph().getJGraph();
				graph.setScale(1.0);
				break;
			}
			
			case ZOOM_IN:
			{
				JGraph graph = this.wrkspc.getExplorerGraph().getJGraph();
				graph.setScale(2 * graph.getScale());
				break;
			}
			
			case ZOOM_OUT:
			{
				JGraph graph = this.wrkspc.getExplorerGraph().getJGraph();
				graph.setScale(graph.getScale() / 2);
				break;
			}
			
			case PLAY:
			{
				if ( (this.wrkspc.getTracer()!=null) &&
						(this.wrkspc.getTracer().getInspecThread()!=null) &&
						(!this.wrkspc.getTracer().getInspecThread().isVmDied()) &&
						(this.wrkspc.getTracer().getInspecThread().isConnected()) )
				{
					this.wrkspc.getTracer().resumeVM();
					if (this.wrkspc.getExporerToolBar()!=null)
					{
						this.wrkspc.getExporerToolBar().getRunLed().setYellow();
					}
				}
				break;	
			}	
			
			case CONTINUOUS_PLAY:
			{
				if ( (this.wrkspc.getTracer()!=null) &&
						(this.wrkspc.getTracer().getInspecThread()!=null) &&
						(!this.wrkspc.getTracer().getInspecThread().isVmDied()) &&
						(this.wrkspc.getTracer().getInspecThread().isConnected()) )
				{
					this.wrkspc.getTracer().getInspecThread().setContinuousPlay(true);
					this.wrkspc.getTracer().resumeVM();
					if (this.wrkspc.getExporerToolBar()!=null)
					{
						this.wrkspc.getExporerToolBar().getRunLed().setYellow();
					}
				}
				break;	
			}	
			
			case SUSPEND:
			{
				if ( (this.wrkspc.getTracer()!=null) &&
						(this.wrkspc.getTracer().getInspecThread()!=null) &&
						(!this.wrkspc.getTracer().getInspecThread().isVmDied()) &&
						(this.wrkspc.getTracer().getInspecThread().isConnected()) )
				{
					this.wrkspc.getTracer().getInspecThread().setContinuousPlay(false);
				}
				break;	
			}	
			
			case ENABLE_REQUEST:
			{
				if ( (this.wrkspc.getTracer()!=null) &&
						(this.wrkspc.getTracer().getInspecThread()!=null) &&
						(!this.wrkspc.getTracer().getInspecThread().isVmDied()) &&
						(this.wrkspc.getTracer().getInspecThread().isConnected()) )
				{
					this.wrkspc.getTracer().getInspecThread().enableRequest();
				}
				break;	
			}
			
			case DISABLE_REQUEST:
			{
				if ( (this.wrkspc.getTracer()!=null) &&
						(this.wrkspc.getTracer().getInspecThread()!=null) &&
						(!this.wrkspc.getTracer().getInspecThread().isVmDied()) &&
						(this.wrkspc.getTracer().getInspecThread().isConnected()) )
				{
					this.wrkspc.getTracer().getInspecThread().disableRequest();
				}
				break;	
			}
			
			case EXIT_VM:
			{
				this.wrkspc.terminateVM();
				break;	
			}	
			
			case EXIT:
				this.wrkspc.terminate();
				break;
				
			//FOR TEST
			//case TEST_ADD:
			//{
			//	this.wrkspc.getExplorerGraph().testAdd();
			//	break;
			//}
			
			//FOR TEST
			//case TEST_DEL:
			//{
			//	this.wrkspc.getExplorerGraph().testDel();
			//	break;
			//}
				
			case CAPTURE_SHOT:
			{
				String[] titleDescription = ExporterInput.titleDescriptionInput();
				if (titleDescription[0]!=null)
				{
					this.wrkspc.getExplorerGraph().captureShot(titleDescription[0], titleDescription[1]);
				}
				else
				{
					Logger.log.info("CANCEL CAPTURE_SHOT!");
				}
				break;	
			}	
			
			case EXPORT_XML:
			{
				String[] titleDescription = ExporterInput.titleDescriptionInput();
				if (titleDescription[0]!=null)
				{
					ExportFrames exportFrames = this.wrkspc.getExplorerGraph().getExportFrames();
					exportFrames.setTitle(titleDescription[0]);
					exportFrames.setDescription(titleDescription[1]);
					String xmlFileName = ExporterInput.chooseXmlFileName(this.wrkspc.getExplorerPanel());
					if (xmlFileName!=null)
					{
						XMLExporter.export(exportFrames, xmlFileName);
					}
					else
					{
						Logger.log.info("CANCEL EXPORT XML ON FILE INPUT!");
					}
				}
				else
				{
					Logger.log.info("CANCEL EXPORT XML ON TITLE AND DESCRIPTION INPUT!");
				}
				break;	
			}	
										
			default:
				
				break;
				
		}
		
	}
	
	public void setIntervalPlay(int intervalPlay)
	{
		//Logger.log.debug("Change intervalPlay: " + intervalPlay);
		if ( (this.wrkspc.getTracer()!=null) &&
				(this.wrkspc.getTracer().getInspecThread()!=null) )
		{
			this.wrkspc.getTracer().getInspecThread().setIntervalPlay(intervalPlay);
		}
	}
	
	public void executeRunMainClass(ClassDescriptor classDescriptor)
	{
		new RunMainClassDialog(this.wrkspc, classDescriptor);
	}
	
	public void runMainClass(final ClassDescriptor classDescriptor, final String commandLineMainArguments, final String commandLineVMOptions)
	{
		Logger.log.info("runMainClass:"+classDescriptor.getClassName()+" commandLineMainArguments:"+commandLineMainArguments+" commandLineVMOptions:"+commandLineVMOptions);
		this.wrkspc.setLastRunMainClass(classDescriptor.getClassName());
		this.wrkspc.setCommandLineMainArguments(commandLineMainArguments);
		this.wrkspc.setCommandLineVMOptions(commandLineVMOptions);
		
		if (this.wrkspc.getTracer()==null)
		{
			//lanciare VM
			try
			{
				//new Tracer(this.wrkspc, classDescriptor.getClassName(), commandLineMainArguments, commandLineVMOptions);
				ExplorerPanel.createExplorerFrame(this.wrkspc);
				
				Thread runner = new Thread() {
					public void run() {
						try
						{
							new Tracer(wrkspc, classDescriptor.getClassName(), commandLineMainArguments, commandLineVMOptions);
						}
						catch (Throwable t)
						{
							Logger.log.error("Tracer Exception : " + t.getMessage() + " " + Logger.exceptionToString(t));
						}
					}
				};
				runner.start();
				
				//TODO disabilito il main panel
				//wrkspc.getMainPanel().setEnabled(false); //NON FUNZIONA!!
				
			}
			catch (Throwable e)
			{
				Logger.log.error("Error Running Trace: "+ e.getMessage() + " Stack:" + Logger.exceptionToString(e));
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Un processo e' gia' in esecuzione, non e' possibile eseguire altri processi!", "Attenzione!!", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	public void newWorkspace(String classpath, String sourcepath)
	{
		Logger.log.info("newWorkspace: classpath:"+classpath+" sourcepath:"+sourcepath);
		this.wrkspc.setClasspath(classpath);
		this.wrkspc.setSourcepath(sourcepath);
		//this.wrkspc.buildClasspathTree();
		
		Thread runner = new Thread() {
			public void run() {
				wrkspc.buildClasspathTree();
			}
		};
		runner.start();
		
	}
	
			
}
