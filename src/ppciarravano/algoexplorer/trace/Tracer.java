package ppciarravano.algoexplorer.trace;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ppciarravano.algoexplorer.Workspace;
import ppciarravano.algoexplorer.gui.JTextAreaOutputStream;
import ppciarravano.algoexplorer.gui.StandardInOutWindow;
import ppciarravano.algoexplorer.log.Logger;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;


/**
 * Classe Tracer
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010) 
 */
public class Tracer
{
		
	private final VirtualMachine vm;
	private InspectThread inspecThread;
	
	private Thread errThread;
	private Thread outThread;
	
	private Workspace wrkspc;	
	
	private String mainClassName;
	private String commandLineMainArguments;
	private String commandLineVMOptions;
	
	/*
	 * 
	 * java -classpath CLASSPATH VM_OPTIONS -Xdebug -Xrunjdwp:transport=dt_shmem,address=javadebug50603,suspend=y MAIN_CLASS MAIN_ARGS
	 * TEST: VM_OPTIONS = -Xms256M -Xmx512M
	 */
	public Tracer(Workspace wrkspcParam, String mainClassNameParam, String commandLineMainArgumentsParam, String commandLineVMOptionsParam)
	{
		this.wrkspc = wrkspcParam;
		this.wrkspc.setTracer(this);
		
		this.mainClassName = mainClassNameParam;
		this.commandLineMainArguments = commandLineMainArgumentsParam;
		this.commandLineVMOptions = commandLineVMOptionsParam;
				
		vm = prepareLaunchTarget();
		
		Logger.log.info("VM Description:" + vm.description());
		vm.setDebugTraceMode(VirtualMachine.TRACE_NONE);
		
		StandardInOutWindow.init();
		PrintStream textAreaPrintStream = new PrintStream(new JTextAreaOutputStream(StandardInOutWindow.getTextArea()), true);
		
		inspecThread = new InspectThread(this.wrkspc, vm, textAreaPrintStream);
		inspecThread.start();
		redirectOutput(textAreaPrintStream);
		//vm.resume(); //Il resume e' chiamato dal CommandManager
		vm.suspend(); //Blocco inizialmente la VM altrimenti parte automaticamente
		
		// Shutdown begins when event thread terminates
		try
		{
			inspecThread.join();
			Logger.log.debug("inspecThread.join()");
			errThread.join(); // Make sure output is forwarded
			Logger.log.debug("errThread.join()");
			outThread.join(); // before we exit
			Logger.log.debug("outThread.join()");
		}
		catch (InterruptedException exc)
		{
			Logger.log.error("InterruptedException: " + exc.getMessage() + " " + Logger.exceptionToString(exc));
			//exc.printStackTrace();
		}
		
				
		//Chiamare la seguente riga solo dopo aver utilizzato tutti i valori generati dal tracer
		this.wrkspc.setTracer(null);
				
	}
	
	/**
	 * Prepare Launch target VM. Forward target's output and error.
	 */
	@SuppressWarnings("unchecked")
	VirtualMachine prepareLaunchTarget()
	{
		LaunchingConnector connector = findLaunchingConnector();
		Map arguments = connectorArguments(connector);
		try
		{
			return connector.launch(arguments);
		}
		catch (IOException exc)
		{
			Logger.log.error("IOException: " + exc.getMessage() + " " + Logger.exceptionToString(exc));
			throw new Error("Unable to launch target VM: " + exc);
		}
		catch (IllegalConnectorArgumentsException exc)
		{
			Logger.log.error("IllegalConnectorArgumentsException: " + exc.getMessage() + " " + Logger.exceptionToString(exc));
			throw new Error("Internal error: " + exc);
		}
		catch (VMStartException exc)
		{
			Logger.log.error("VMStartException: " + exc.getMessage() + " " + Logger.exceptionToString(exc));
			throw new Error("Target VM failed to initialize: " + exc.getMessage());
		}
	}

	/**
	 * Find a com.sun.jdi.CommandLineLaunch connector
	 */
	@SuppressWarnings("unchecked")
	LaunchingConnector findLaunchingConnector()
	{
		List connectors = Bootstrap.virtualMachineManager().allConnectors();
		Iterator iter = connectors.iterator();
		while (iter.hasNext())
		{
			Connector connector = (Connector) iter.next();
			if (connector.name().equals("com.sun.jdi.CommandLineLaunch"))
			{
				return (LaunchingConnector) connector;
			}
		}
		throw new Error("No launching connector");
	}

	/**
	 * Return the launching connector's arguments.
	 */
	@SuppressWarnings("unchecked")
	Map connectorArguments(LaunchingConnector connector)
	{
		Map arguments = connector.defaultArguments();
		
		//Set Main class and program arguments
		Connector.Argument mainArg = (Connector.Argument) arguments.get("main");
		if (mainArg == null)
		{
			throw new Error("Bad launching connector");
		}
		String mainArgs = this.mainClassName;
		if (!this.commandLineMainArguments.trim().equals(""))
		{
			mainArgs += " " + this.commandLineMainArguments;
		}
		mainArg.setValue(mainArgs);
		
		//Set VM options
		Connector.Argument optionArg = (Connector.Argument) arguments.get("options");
		if (optionArg == null)
		{
			throw new Error("Bad launching connector");
		}
		//String vmOptions = "-classic -classpath "+this.wrkspc.getClasspath(); //Dump: "Warning: classic VM not supported; client VM will be used"
		String vmOptions = "-classpath "+this.wrkspc.getClasspath();
		if (!this.commandLineVMOptions.trim().equals(""))
		{
			vmOptions += " " + this.commandLineVMOptions;
		}
		optionArg.setValue(vmOptions);
		
		//Dump DEBUG for test		
		Set key = arguments.keySet();
		Iterator it = key.iterator();
		while(it.hasNext())
		{
			Object obj = it.next();
			Logger.log.debug("VM Argument: " + obj + "=" + arguments.get(obj));
		}
		
		return arguments;
	}

	
	void redirectOutput(PrintStream outStream)
	{
		Process process = vm.process();

		errThread = new StreamRedirect("error reader", process.getErrorStream(), outStream);
		outThread = new StreamRedirect("output reader", process.getInputStream(), outStream);
		errThread.start();
		outThread.start();
		
	}

	private VirtualMachine getVm()
	{
		return vm;
	}
	
	public void resumeVM()
	{
		vm.resume();
	}
	
	public void exitVM()
	{
		vm.exit(-1);
	}
	
	//Non utilizzato per il pulsante suspend nella ExplorerToolBar, in quanto quel pulsante
	// setta semplicemente InspectThread.continuousPlay = false
	//private void suspendVM()
	//{
	//	vm.suspend();
	//}

	public InspectThread getInspecThread()
	{
		return inspecThread;
	}
	

}
