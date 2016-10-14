package ppciarravano.algoexplorer.trace;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import ppciarravano.algoexplorer.ClassDescriptor;
import ppciarravano.algoexplorer.SourceDescriptor;
import ppciarravano.algoexplorer.Workspace;
import ppciarravano.algoexplorer.log.Logger;
import ppciarravano.algoexplorer.util.ConstantManager;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ClassType;
import com.sun.jdi.Field;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.Value;
import com.sun.jdi.ObjectCollectedException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.ModificationWatchpointEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.ThreadDeathEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodExitRequest;
import com.sun.jdi.request.ModificationWatchpointRequest;


/**
 * Classe InspectThread
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class InspectThread extends Thread
{
	private Workspace wrkspc;	
	
	private final VirtualMachine vm; // Running VM
	private MethodExitRequest methodExitRequest; //dichiarata come field perche' usata da getValueToString, enableRequest, disableRequest
	private ClassPrepareRequest cpr; //dichiarata come field perche' usata da enableRequest, disableRequest 	
	
	private boolean connected; // Connected to VM
	private boolean vmDied; // VMDeath occurred
	private String[] classExclusionFilter;
	private PrintStream outStream;
	
	//Variabili per controllare l'interazione con l'utente
	private boolean continuousPlay;
	private int intervalPlay;
	
	//Variabile da Workspace.class
	private Hashtable<String, ClassDescriptor> classDescriptors = null;
	
	//Variabile per memorizzare i nodi del grafo che poi sara' visualizzato
	private Hashtable<Long, InstanceDescriptor> instances = null;
	
	InspectThread(Workspace wrkspcParam, VirtualMachine vmParam, PrintStream outStreamParam)
	{
		this.wrkspc = wrkspcParam;
		this.wrkspc.setInspectThread(this);
		
		this.vm = vmParam;
		this.connected = true;
		this.vmDied = false;
		this.classExclusionFilter = this.wrkspc.getClassExclusionFilter();
		this.outStream = outStreamParam;
		
		this.classDescriptors = this.wrkspc.getClassDescriptors();
		this.instances = new Hashtable<Long, InstanceDescriptor>();
		
		this.continuousPlay = false;
		this.intervalPlay = ConstantManager.FRAME_INTERVAL_PLAY;
		
		setEventRequests();
	}

	public Hashtable<Long, InstanceDescriptor> getInstances()
	{
		return instances;
	}
	
	/**
	 * Run the event handling thread. As long as we are connected, get event
	 * sets off the queue and dispatch the events within them.
	 */
	public void run()
	{
		EventQueue queue = vm.eventQueue();
		while (connected)
		{
			try
			{
				EventSet eventSet = queue.remove();
				EventIterator it = eventSet.eventIterator();
				while (it.hasNext())
				{
					handleEvent(it.nextEvent());
				}
				eventSet.resume();
			}
			catch (InterruptedException exc)
			{
				Logger.log.error("InterruptedException:" + Logger.exceptionToString(exc));
			}
			catch (VMDisconnectedException discExc)
			{
				Logger.log.error("VMDisconnectedException: " + discExc.getMessage()); 
				handleDisconnectedException();
				break;
			}
		}
	}
	
	
	private void setEventRequests()
	{
		EventRequestManager mgr = vm.eventRequestManager();
		
/*
		//Event on exceptions
		ExceptionRequest excReq = mgr.createExceptionRequest(null, true, true);
		// suspend so we can step
		excReq.setSuspendPolicy(EventRequest.SUSPEND_ALL);
		excReq.enable();
*/
		
/*
		//Event on Entry Method
		MethodEntryRequest menr = mgr.createMethodEntryRequest();
		for (int i = 0; i < classExclusionFilter.length; ++i)
		{
			menr.addClassExclusionFilter(classExclusionFilter[i]);
		}
		menr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
		menr.enable();
*/
	
		//Event on Exit Method
		methodExitRequest = mgr.createMethodExitRequest();
		for (int i = 0; i < classExclusionFilter.length; ++i)
		{
			methodExitRequest.addClassExclusionFilter(classExclusionFilter[i]);
		}
		//if (getClassSelected().isEmpty())
		//{
		//	Logger.log.info("MethodExitRequest->addClassFilter: *");
		//	methodExitRequest.addClassExclusionFilter("*");
		//}
		//else
		//{
		//	for (ClassDescriptor clsDscr : getClassSelected())
		//	{
		//		//Questo metodo di filtro non funziona perche' si puo' specificare solo per una classe! 
		//		Logger.log.info("MethodExitRequest->addClassFilter: "+clsDscr.getClassName());
		//		methodExitRequest.addClassFilter(clsDscr.getClassName());
		//	}
		//}
		methodExitRequest.setSuspendPolicy(EventRequest.SUSPEND_ALL);
		methodExitRequest.enable();

		
/*
		//Event on death Thread
		ThreadDeathRequest tdr = mgr.createThreadDeathRequest();
		// Make sure we sync on thread death
		tdr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
		tdr.enable();
*/


		//Event on Class Prepare request
		cpr = mgr.createClassPrepareRequest();
		for (int i = 0; i < classExclusionFilter.length; ++i)
		{
			cpr.addClassExclusionFilter(classExclusionFilter[i]);
		}
		//if (getClassSelected().isEmpty())
		//{
		//	Logger.log.info("ClassPrepareRequest->addClassFilter: *");
		//	cpr.addClassExclusionFilter("*");
		//}
		//else
		//{
		//	for (ClassDescriptor clsDscr : getClassSelected())
		//	{
		//		//Questo metodo di filtro non funziona perche' si puo' specificare solo per una classe! 
		//		Logger.log.info("ClassPrepareRequest->addClassFilter: "+clsDscr.getClassName());
		//		cpr.addClassFilter(clsDscr.getClassName());
		//	}
		//}
		cpr.addClassFilter("*");
		cpr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
		cpr.enable();

		
	}

	/**
	 * Dispatch incoming events
	 */
	private void handleEvent(Event event)
	{
		if (event instanceof ExceptionEvent)
		{
			exceptionEvent((ExceptionEvent) event);
		}
		else if (event instanceof ModificationWatchpointEvent)
		{
			fieldWatchEvent((ModificationWatchpointEvent) event);
		}
		else if (event instanceof MethodEntryEvent)
		{
			methodEntryEvent((MethodEntryEvent) event);
		}
		else if (event instanceof MethodExitEvent)
		{
			methodExitEvent((MethodExitEvent) event);
		}
		else if (event instanceof StepEvent)
		{
			stepEvent((StepEvent) event);
		}
		else if (event instanceof ThreadDeathEvent)
		{
			threadDeathEvent((ThreadDeathEvent) event);
		}
		else if (event instanceof ClassPrepareEvent)
		{
			classPrepareEvent((ClassPrepareEvent) event);
		}
		else if (event instanceof VMStartEvent)
		{
			vmStartEvent((VMStartEvent) event);
		}
		else if (event instanceof VMDeathEvent)
		{
			vmDeathEvent((VMDeathEvent) event);
		}
		else if (event instanceof VMDisconnectEvent)
		{
			vmDisconnectEvent((VMDisconnectEvent) event);
		}
		else if (event instanceof BreakpointEvent)
		{
			breakpointEvent((BreakpointEvent) event);
		}
		else
		{
			throw new Error("Unexpected event type");
		}

	}

	/***
	 * A VMDisconnectedException has happened while dealing with another event.
	 * We need to flush the event queue, dealing only with exit events (VMDeath,
	 * VMDisconnect) so that we terminate correctly.
	 */
	synchronized void handleDisconnectedException()
	{
		EventQueue queue = vm.eventQueue();
		while (connected)
		{
			try
			{
				EventSet eventSet = queue.remove();
				EventIterator iter = eventSet.eventIterator();
				while (iter.hasNext())
				{
					Event event = iter.nextEvent();
					if (event instanceof VMDeathEvent)
					{
						vmDeathEvent((VMDeathEvent) event);
					}
					else if (event instanceof VMDisconnectEvent)
					{
						vmDisconnectEvent((VMDisconnectEvent) event);
					}
				}
				eventSet.resume(); // Resume the VM
			}
			catch (InterruptedException exc)
			{
				Logger.log.error("InterruptedException:" + Logger.exceptionToString(exc));
			}
		}
	}

	/*
	 * Ritorna true se il field e' un attributo selezionato come inspezionabile per la classe
	 */
	private boolean isInspectedField(String className, String fieldName)
	{
		boolean result = false;
		ClassDescriptor clsDscr = classDescriptors.get(className);
		if (clsDscr!=null)
		{
			result = clsDscr.isInspectedField(fieldName);
		}
		return result;
	}
	
	/*
	 * Ritorna true se il field e' un attributo selezionato come inspezionabile per la classe
	 */
	private boolean isInspectedField(ClassDescriptor clsDscr, String fieldName)
	{
		boolean result = false;
		if (clsDscr!=null)
		{
			result = clsDscr.isInspectedField(fieldName);
		}
		return result;
	}
		
	/*
	 * Ritorna il ClassDescriptor corrispondente se il className e' tra le classi selezionate per la visualizzazione
	 */
	private ClassDescriptor getClassDescriptorSelectedFromName(String className)
	{
		ClassDescriptor result = null;
		for (ClassDescriptor clsDscr : getClassSelected())
		{
			if (className.equals(clsDscr.getClassName()))
			{
				result = clsDscr;
				break;
			}
		}
		return result;
	}
	
	/*
	 *	Ritorna un Set delle ClassDescriptor selezionate per la visualizzazione 
	 */
	private Set<ClassDescriptor> getClassSelected()
	{
		Set<ClassDescriptor> result = new HashSet<ClassDescriptor>();
		
		/*
		//Ciclando sulle chiavi e poi estraendo
		Enumeration<String> classNames = classDescriptors.keys();
		while(classNames.hasMoreElements())
		{
			String className = classNames.nextElement();
			ClassDescriptor clsDscr = classDescriptors.get(className);
			if (clsDscr.isClassSelect())
			{
				result.add(clsDscr);
			}
		}
		*/
		
		Enumeration<ClassDescriptor> clsDscrEnumeration = classDescriptors.elements();
		while(clsDscrEnumeration.hasMoreElements())
		{
			ClassDescriptor clsDscr = clsDscrEnumeration.nextElement();
			if (clsDscr.isClassSelect())
			{
				result.add(clsDscr);
			}
		}
		
		return result;
	}
	
	public boolean isContinuousPlay()
	{
		return continuousPlay;
	}
	
	public void setContinuousPlay(boolean continuousPlay)
	{
		this.continuousPlay = continuousPlay;
	}
	
	public void setIntervalPlay(int intervalPlay)
	{
		this.intervalPlay = intervalPlay;
	}
	
	public boolean isConnected()
	{
		return connected;
	}

	public boolean isVmDied()
	{
		return vmDied;
	}

	public void enableRequest()
	{
		Logger.log.info("ENABLE_REQUEST!!");
		methodExitRequest.enable();
		cpr.enable();
	}
	
	public void disableRequest()
	{
		Logger.log.info("DISABLE_REQUEST!!");
		methodExitRequest.disable();
		cpr.disable();
	}

	
	//----------------------------------------------------------------------------------
	// Implement Event Methods
	
	private void methodExitEvent(MethodExitEvent event)
	{
		
		Logger.log.debug("methodExitEvent: " +event.method().declaringType().name() );
		
		//Eseguo il corpo del metodo solo se il metodo appena eseguito e' un costruttore
		if ( (!event.method().isConstructor()) &&
				(!event.method().name().equals("main")) )
		{
			return;
		}
				
		//printMessage("MethodExitEvent: "+event.method().name() + " "+event.method().declaringType().name());
		ThreadReference currentThread = null;
		
		try
		{
			currentThread = event.thread();
			if (event.thread().frame(0).thisObject() != null)
			{
				ObjectReference objRef = event.thread().frame(0).thisObject();
				long objId = objRef.uniqueID();
				Logger.log.debug("--->" + objRef.toString() + " --->" + objRef.referenceType().name() + " ID--->"+objId + " Num.Ref.:"+objRef.referringObjects(0l).size() + " - Method Name:"+event.method().name());
				
				ClassDescriptor clsDscr = null;
				if ((clsDscr = getClassDescriptorSelectedFromName(objRef.referenceType().name())) != null )
				{
					//TODO: checkbox su interfaccia grafica che permette di disabilitare o abilitare questo parametro
					if (ConstantManager.DISABLE_GARBAGE_COLLECTION)
					{
						objRef.disableCollection(); //Prevents garbage collection for this object.
					}
					InstanceDescriptor instDscr = null;
					if (!instances.containsKey(objId))
					{
						instDscr = new InstanceDescriptor(objRef, clsDscr);
						instances.put(objId, instDscr);
						Logger.log.debug("ADD in instances objId:" + objId);
					}
					else
					{
						//Assegnazione superflua non utilizzata e commentata
						//instDscr = instances.get(objId);
						//Logger.log.debug("ALREADY PRESENT instances objId:" + objId);
					}
					
					//In questo punto instDscr e' comunque inizializzato
					// l'aggiornamento della label, collected, references e field e'
					// delegato al metodo updateInstancesReferencesAndLabel
					
				}
			}
		}
		catch (com.sun.jdi.IncompatibleThreadStateException itex)
		{
			//Logger.log.error("IncompatibleThreadStateException:" + Logger.exceptionToString(itex));
			Logger.log.error("IncompatibleThreadStateException:" + itex.getMessage());
		}

		
		//Se ho terminato il main della main class allora chiamo updateInstancesReferencesAndLabel
		if ((event.method().declaringType().name().equals(this.wrkspc.getLastRunMainClass()))
				&& (event.method().name().equals("main")) 
				&& (event.method().argumentTypeNames()!=null)
				&& (event.method().argumentTypeNames().size()==1)
				&& (event.method().argumentTypeNames().contains("java.lang.String[]")) )
		{
			/*
			//For test
			List<String> lis = event.method().argumentTypeNames();
			for (String string : lis)
			{
				Logger.log.debug("--->" + string); //"java.lang.String[]"
			}
			*/
			
			updateInstancesReferencesAndLabel(currentThread, event, true);
			Logger.log.info("Exiting from Main!!");
		}
		
		//updateInstancesReferencesAndLabel(currentThread, event);
				
	}
	
	
	private void updateInstancesReferencesAndLabel(ThreadReference currentThread, Event eventOccurred, boolean isLastCall)
	{
		Logger.log.debug("Call updateInstancesReferencesAndLabel...");
		
		//Ciclo su tutte le istanze e aggiorno:
		// collected, label, references e fields
		Enumeration<InstanceDescriptor> instancesEnumeration = instances.elements();
		while(instancesEnumeration.hasMoreElements())
		{
			InstanceDescriptor instDscr = instancesEnumeration.nextElement();
			//long objId = instDscr.getUniqueID();
			ObjectReference objRef = instDscr.getObjectReference();
			ClassDescriptor clsDscr = instDscr.getClassDescriptor();
			ReferenceType refType = objRef.referenceType();
			
			Logger.log.debug("Update Instance ID:" + instDscr.getUniqueID());
			
			//Aggiorno collected: se sull'istanza e' stata operata la garbage collection
			instDscr.setCollected(objRef.isCollected());
			
			//Aggiorno la label
			String fieldNameUseForLabelCell = clsDscr.getFieldNameUseForLabelCell();
			try
			{
				String instanceLabel = "";
				if (fieldNameUseForLabelCell != null)
				{
					//e' stato scelto un field come label
					Field field = refType.fieldByName(fieldNameUseForLabelCell);
					Value value = objRef.getValue(field);
					instanceLabel = getValueToString(value, currentThread, eventOccurred);
				}
				else
				{
					//non e' stato scelto nessun field come label, pertanto si utilizza il valore toString dell'istanza
					instanceLabel = getValueToString(objRef, currentThread, eventOccurred);
				}
				
				//Logger.log.debug("Label: " + instanceLabel);
				instDscr.setLabel(instanceLabel);
			}
			catch (ObjectCollectedException oce)
			{
				Logger.log.debug("ObjectCollectedException on get instanceLabel from field!!");
			}
						
			//Aggiorno le references a questa instanza
			instDscr.clearAllReferences();
			try
			{
				findReferences(objRef, instDscr);
			}
			catch (ObjectCollectedException oce)
			{
				Logger.log.debug("ObjectCollectedException on findReferences!!");
			}
			
			//i field in stato inspect vengono aggiornati da fieldWatchEvent
			// potrebbero essere aggiornati chiamando getValueToString in questo punto			
			
		}
		
		if ((!continuousPlay)&&(!isLastCall))
		{
			vm.suspend();
			if (this.wrkspc.getExporerToolBar()!=null)
			{
				this.wrkspc.getExporerToolBar().getRunLed().setGreenYellowBlink();
			}
		}
		
		//visualizzo grafo
		this.wrkspc.getExplorerGraph().renderInstances(getInstances());
		
		if ((continuousPlay)&&(!isLastCall))
		{
			try
			{
				Thread.sleep(intervalPlay);
			}
			catch (InterruptedException e)
			{
				Logger.log.error("InterruptedException in continuousPlay SLEEP THREAD:" + Logger.exceptionToString(e));
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void classPrepareEvent(ClassPrepareEvent event)
	{
		ClassDescriptor clsDscr = null;
		EventRequestManager mgr = vm.eventRequestManager();
		
		//Set ModificationWatchpointRequest per le classi con la spunta classSelect
		if ((clsDscr = getClassDescriptorSelectedFromName(event.referenceType().name())) != null )
		{
			Logger.log.info("classPrepareEvent: "+event.referenceType().name());
			
			//Set ModificationWatchpointRequest for variables in inspect state
			List fields = event.referenceType().visibleFields();
			for (Iterator it = fields.iterator(); it.hasNext();)
			{
				Field field = (Field) it.next();
				Logger.log.debug("classPrepareEvent field name:" + field.name());
				//Setto ModificationWatchpointRequest solo se field e' tra i gli attributi con richiesta inspect
				//if (isInspectedField(event.referenceType().name(), field.name()))
				if (isInspectedField(clsDscr, field.name()))
				{
					Logger.log.debug("set ModificationWatchpointRequest: "+field+ " Name:"+field.name());
					ModificationWatchpointRequest req = mgr.createModificationWatchpointRequest(field);
					for (int i = 0; i < classExclusionFilter.length; ++i)
					{
						req.addClassExclusionFilter(classExclusionFilter[i]);
					}
					req.setSuspendPolicy(EventRequest.SUSPEND_ALL);
					req.enable();
				}
			}
		}
		else
		{
			if ( ! this.wrkspc.getLastRunMainClass().equals( event.referenceType().name() ))
			{
				//Se la classe non e' tra le classi con la spunta classSelect, allora filtro tutti i MethodExitRequest
				Logger.log.info("MethodExitRequest->addClassExclusionFilter: "+event.referenceType().name());
				methodExitRequest.disable();
				methodExitRequest.addClassExclusionFilter(event.referenceType().name());
				methodExitRequest.enable();
			}
			else
			{
				Logger.log.info("NOT MethodExitRequest->addClassExclusionFilter FOR MAIN CLASS!");
			}
		}
		
		
		//Set Breakpoints
		// I breakpoint vengono settati anche per le classi che non hanno la spunta classSelect
		clsDscr = classDescriptors.get(event.referenceType().name());
		if (clsDscr!=null)
		{
			/*
			//Dump for test
			try
			{
				List<Location> allLocs = event.referenceType().allLineLocations();
				
				for (Location location : allLocs)
				{
					Logger.log.debug("ALL LOCATION: " + location);
				}
			}
			catch (AbsentInformationException e1)
			{
				e1.printStackTrace();
			}			
			*/
			SourceDescriptor srcDscr = clsDscr.getSourceDescriptor();
			Set<Integer> breakpoints = srcDscr.getBreakpoint();
			for (Integer lineNumber : breakpoints)
			{
				try
				{
					List<Location> locs = event.referenceType().locationsOfLine(lineNumber);
					/*		
					//Dump for test
					for (Location location : locs)
					{
						Logger.log.debug("LOCATION: " + location);
					}
					*/
					if (locs.size() == 0) 
					{
						Logger.log.error("Breakpoint set: Line not found for:"+event.referenceType()+" lineNumber:"+lineNumber);
		            }
					else
					{
			            //TODO: handle multiple locations ???
						Location location = locs.get(0);
			            if (location.method() == null) 
			            {
			            	Logger.log.error("Breakpoint set: Not in Method body: for:"+event.referenceType()+" lineNumber:"+lineNumber);
			            }
			            else
			            {
			            	Logger.log.info("Breakpoint set: find for:"+event.referenceType()+" lineNumber:"+lineNumber + "["+location+"]");
			            	//Setto il breakpoint
			            	BreakpointRequest req = mgr.createBreakpointRequest(location);
							//for (int i = 0; i < classExclusionFilter.length; ++i)
							//{
							//	req.addClassExclusionFilter(classExclusionFilter[i]);
							//}
							req.setSuspendPolicy(EventRequest.SUSPEND_ALL);
							req.enable();		            	
			            
			            }
					}
				}
				catch (AbsentInformationException e)
				{
					Logger.log.error("Breakpoint set: AbsentInformationException: for:"+event.referenceType()+" lineNumber:"+lineNumber);
					//e.printStackTrace();
				}
			}
		}
		
		
	}
	
			
	private void breakpointEvent(BreakpointEvent event)
	{
		Logger.log.debug("BreakpointEvent: "+event);
		this.wrkspc.getExplorerStatusBar().setStatusMessage(event.toString());
	
		updateInstancesReferencesAndLabel(event.thread(), event, false);
		
		/*
		try
		{
			StackFrame sf = event.thread().frame(0);
			List<LocalVariable> listLocal = sf.visibleVariables();

			for (int j = 0; j < listLocal.size(); j++)
			{
				LocalVariable lv = listLocal.get(j);
				String gs = lv.typeName() + " " + lv.name() + " = " + sf.getValue(lv);
				Logger.log.debug("     Var:" + gs);
			}

		}
		catch (IncompatibleThreadStateException e)
		{
			e.printStackTrace();
		}
		catch (AbsentInformationException e)
		{
			e.printStackTrace();
		}
		*/
	}

	private void fieldWatchEvent(ModificationWatchpointEvent event)
	{
		String className = event.object().referenceType().name();
		Field field = event.field();
		Value value = event.valueToBe();
		ObjectReference objRef = event.object();
		long objId = objRef.uniqueID();
		
		String valueToString = "null";
		if (value!=null)
		{
			valueToString = value.toString();
		}
		
		//TODO: Per utilizzare getValueToString bisogna modificare getValueToString in modo da disabilitare eventi MethodExitRequest
		//String valueToString = getValueToString(value, event.thread(), event);
		//Logger.log.debug("fieldWatchEvent: (ID:"+objId+") "+className+" ("+field.name()+") = " + valueToString);
				
		InstanceDescriptor instDscr = null;
		if (!instances.containsKey(objId))
		{
			Logger.log.debug("NOT FIND InstanceDescriptor for fieldWatchEvent (ID:"+objId+")");
		
			//L'istanza a cui fa riferimento il campo e' appena creata 
			// pertanto non e' presente in instances, la vado ad aggiungere
			
			ClassDescriptor clsDscr = getClassDescriptorSelectedFromName(className);
			if (clsDscr!=null)
			{
				instDscr = new InstanceDescriptor(objRef, clsDscr);
				instances.put(objId, instDscr);
			}
			else
			{
				Logger.log.error("Evento fieldWatchEvent per una classe non selezionata!!");
				throw new Error("Evento fieldWatchEvent per una classe non selezionata!!");
			}
		}
		else
		{
			instDscr = instances.get(objId);
		}
		
		Logger.log.debug("fieldWatchEvent: (ID:"+objId+") "+className+" ("+field.name()+") = " + valueToString);
		instDscr.addFieldValue(field.name(), valueToString);
				
	}
	
	private void methodEntryEvent(MethodEntryEvent event)
	{
		printMessage("MethodEntryEvent: "+event.method().name());
	}
	
	private void stepEvent(StepEvent event)
	{
		printMessage("StepEvent: " + event);
	}
	
	private void threadDeathEvent(ThreadDeathEvent event)
	{
		printMessage("ThreadDeathEvent: " + event);
	}
	
	private void exceptionEvent(ExceptionEvent event)
	{
		printMessage("ExceptionEvent: " + event.exception() + " catch: " + event.catchLocation());
	}

	private void vmStartEvent(VMStartEvent event)
	{
		printMessage("VMStartEvent: -- VM Started --");
	}
	
	private void vmDeathEvent(VMDeathEvent event)
	{
		vmDied = true;
		printMessage("VMDeathEvent: -- The application exited --");
		if (this.wrkspc.getExporerToolBar()!=null)
		{
			this.wrkspc.getExporerToolBar().getRunLed().setRed();
		}
	}

	private void vmDisconnectEvent(VMDisconnectEvent event)
	{
		connected = false;
		if (!vmDied)
		{
			printMessage("VMDisconnectEvent: -- The application has been disconnected --");
		}
		if (this.wrkspc.getExporerToolBar()!=null)
		{
			this.wrkspc.getExporerToolBar().getRunLed().setRed();
		}
	}
	
	private void printMessage(String message)
	{
		outStream.println("VM--->" + message);
		Logger.log.info("VM--->" + message);
	}
	
	
	//----------------------------------------------------------------
	
	private String getValueToString(Value value, ThreadReference currentThread, Event eventOccurred)
	{
		if ( (value instanceof com.sun.jdi.ObjectReference) && (value.type() instanceof com.sun.jdi.ClassType))
		{
			ObjectReference valueObj = (ObjectReference)value;
			ClassType classType = (ClassType)value.type();
			
			//Cerco il metodo toString()
			Method toStringMethod = null;
			for (Method m : classType.methodsByName("toString")) 
			{
	            if (m.argumentTypeNames().size()==0)
	            {
	            	toStringMethod = m;
	            	break;
	            }
			}
			
			if (toStringMethod!=null)
			{
				//Logger.log.debug("Trovato metodo toString():"+toStringMethod);
				try
				{
					List<Value> arguments = new Vector<Value>();
					//Logger.log.debug("currentThread: " + currentThread);
					
					//Devo disabilitare la richiesta eventi sull'evento che e' in corso,
					// altrimenti si blocca l' esecuzione alla chiamata di invokeMethod
					EventRequest eventRequest = eventOccurred.request();
					methodExitRequest.disable();//Fermo l'evento methodExitRequest
					//Logger.log.debug("1");
					eventRequest.disable(); //TODO: verificare se necessario
					//Logger.log.debug("2");
					Value valueToString = null;
					try
					{
						valueToString = valueObj.invokeMethod(currentThread, toStringMethod, arguments, ClassType.INVOKE_SINGLE_THREADED);
					}
					catch (Throwable t)
					{
						return "null";
					}
					//Logger.log.debug("3");
					methodExitRequest.enable();//Fermo l'evento methodExitRequest
					eventRequest.enable(); //TODO: verificare se necessario
					//Logger.log.debug("4");
					
					return valueToString.toString();
				}
				catch (Exception e)
				{
					Logger.log.error("getElementToString Exception:" + Logger.exceptionToString(e));
				}				
			}
		}
		
		
		if (value!=null)
		{
			return "*"+value.toString(); //"*" indica un tipo primitivo
		}
		return "null";
				 
	}
			
	private void findReferences(ObjectReference objRef, InstanceDescriptor instDscr)
	{
		Logger.log.debug("Running findReferencesRecursive");
		findReferencesRecursive(objRef, instDscr, true, new HashSet<Long>(), 0);
	}
	
	private void findReferencesRecursive(ObjectReference objRef, InstanceDescriptor instDscr, boolean isDirectReference, HashSet<Long> objectsVisited, int recursionLevel)
	{
		recursionLevel++;
		//Logger.log.debug("RECURSION LEVEL: " + recursionLevel);
		if (recursionLevel>ConstantManager.MAX_REFERENCE_RECURSION_LEVEL)
		{
			return;
		}
		
		//Aggiorno gli id degli oggetti visitati
		objectsVisited.add(objRef.uniqueID());
		
		//Cerco chi referenzia questa istanza tra le classi classSelect
		List<ObjectReference> referrObjs = objRef.referringObjects(0l);
		Logger.log.debug("TOT REF: " + referrObjs.size());
		for (ObjectReference objectReference : referrObjs)
		{
			if(getClassDescriptorSelectedFromName(objectReference.referenceType().name()) != null)
			{
				Logger.log.debug("findReferences FINDCLASS ID:" + objRef.uniqueID()+"-->"+objectReference.uniqueID()+ "["+objectReference.referenceType().name()+"]");
				
				//Cerco quale field di objectReference punta a objRef
				List<Field> fields = objectReference.referenceType().visibleFields();
				for (Iterator<Field> it = fields.iterator(); it.hasNext();)
				{
					Field field = (Field)it.next();
					Value value = objectReference.getValue(field);
					if ( (value instanceof com.sun.jdi.ObjectReference) && (value.type() instanceof com.sun.jdi.ClassType))
					{
						ObjectReference valueObj = (ObjectReference)value;
						long idRef = valueObj.uniqueID();
						if(idRef==objRef.uniqueID())
						{
							String referenceName = field.name();
							//String referenceName = field.name() + " ["+field.typeName()+"]";
							Logger.log.debug("REFERENCE NAME: " + referenceName);
							InstanceReference instRef = new InstanceReference(objectReference.uniqueID(), referenceName, isDirectReference, instDscr.getUniqueID());
							instDscr.addReference(instRef);
						}
					}
				}
								
				/*
				//Vecchia parte di codice che assieme al metodo findReferenceName
				// Non era capace di individuare piu' field che puntavano alla stessa instanza
				String referenceName = findReferenceName(objRef, objectReference);
				if (referenceName==null)
				{
					referenceName = "*";
				}		
				
				InstanceReference instRef = new InstanceReference(objectReference.uniqueID(), referenceName, isDirectReference);
				references.add(instRef);
				*/
			}
			else
			{
				if (!objectsVisited.contains(objectReference.uniqueID()))
				{
					Logger.log.debug("Recursive findReferences on ID:" + objRef.uniqueID()+"-->"+objectReference.uniqueID() + "["+objectReference.referenceType().name()+"]");
					findReferencesRecursive(objectReference, instDscr, false, objectsVisited, recursionLevel);
				}
			}
		}	
		
		recursionLevel--;
		//Logger.log.debug("RECURSION LEVEL: " + recursionLevel);
	}

	/*
	 * Cerco quale field di parentReference punta a childReference
	 */
	/*
	//Vecchio metodo usato in findReferencesRecursive
	private String findReferenceName(ObjectReference objRef, ObjectReference objectReference)
	{
		String referenceName = null;
		List<Field> fields = objectReference.referenceType().visibleFields();
		for (Iterator<Field> it = fields.iterator(); it.hasNext();)
		{
			Field field = (Field)it.next();
			Value value = objectReference.getValue(field);
			if ( (value instanceof com.sun.jdi.ObjectReference) && (value.type() instanceof com.sun.jdi.ClassType))
			{
				ObjectReference valueObj = (ObjectReference)value;
				long idRef = valueObj.uniqueID();
				if(idRef==objRef.uniqueID())
				{
					referenceName = field.name();
					break;
				}
			}
		}
		return referenceName;
	}
	*/
	
	
}
