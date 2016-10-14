package ppciarravano.algoexplorer.gui;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D; 
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphSelectionModel;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.PortView;

import ppciarravano.algoexplorer.Workspace;
import ppciarravano.algoexplorer.jgraph.GraphUtility;
import ppciarravano.algoexplorer.log.Logger;
import ppciarravano.algoexplorer.trace.InstanceDescriptor;
import ppciarravano.algoexplorer.trace.InstanceReference;
import ppciarravano.algoexplorer.export.*;

import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.JGraphLayout;
import com.jgraph.layout.demo.JGraphLayoutMorphingManager;
import com.jgraph.layout.organic.JGraphFastOrganicLayout;
import com.jgraph.navigation.GraphNavigator;


/**
 * Classe ExplorerGraph
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class ExplorerGraph
{
	private Workspace wrkspc;
	
	private JGraph graph;
	private GraphNavigator navigator;
	private JGraphLayout layout;
	private GraphModel model;
	private GraphSelectionModel graphSelectionModel;

	//Gestisce animazione del Layout
	protected JGraphLayoutMorphingManager morpher = new JGraphLayoutMorphingManager();

	
	public ExplorerGraph(Workspace wrkspc)
	{
		this.wrkspc = wrkspc;
		this.wrkspc.setExplorerGraph(this);	
		
		initGraph();		
		
	}
	
	private void initGraph()
	{
		
		// Construct Model and Graph
		model = new DefaultGraphModel();
		graph = new JGraph(model);
		graphSelectionModel = graph.getSelectionModel();
		
		//Per selezionare un solo componente
		graphSelectionModel.setSelectionMode(GraphSelectionModel.SINGLE_GRAPH_SELECTION);
		
		//Proprieta' del JGraph
		graph.setCloneable(false);
		graph.setInvokesStopCellEditing(false);
		graph.setJumpToDefaultPort(true);
		graph.setAntiAliased(true);
		graph.setAutoResizeGraph(true); 
		graph.setConnectable(false); 
		graph.setBendable(false);
		graph.setDisconnectable(false);
		graph.setDisconnectOnMove(false);
		graph.setDragEnabled(true);
		graph.setEdgeLabelsMovable(false);
		graph.setEditable(false);
		graph.setMoveable(true);
		graph.setSizeable(false);
		
		//Setto apertura dell'inspect panel quando si clicca su un nodo
		GraphSelectionListener graphSelectionListener = new GraphSelectionListener()
		{
			public void valueChanged(GraphSelectionEvent gse)
			{
				Object selection = gse.getCell();
				if (selection!=null)
				{
					if ((!model.isPort(selection))&&(!model.isEdge(selection)))
					{
						DefaultGraphCell cell = (DefaultGraphCell)selection;
						if (cell!=null)
						{
							InstanceDescriptor instDscr = (InstanceDescriptor)cell.getAttributes().get(GraphUtility.INSTANCE_DESCRIPTOR_OBJECT);
							Logger.log.debug("Cell Selected: " + instDscr.getLabel() + " ID:" + instDscr.getUniqueID() );
							wrkspc.getInspectPanel().createFromInstanceDescriptor(instDscr);
						}
					}
				}
			}
		};
		graph.addGraphSelectionListener(graphSelectionListener);
		
		
		//Se si cambia il layout iniziale cambiare in ExplorerMenuBar la riga: layoutMenu.getItem(nuovo_indice).setSelected(true)
		//layout = new JGraphSimpleLayout(JGraphSimpleLayout.TYPE_CIRCLE);
		layout = new JGraphFastOrganicLayout();
		((JGraphFastOrganicLayout)layout).setForceConstant(200);	
				
		graph.clearSelection();
		executeLayout();
		fitViewport();
		
	}
	
	public JGraph getJGraph()
	{
		return graph;
	}
	
	
//	//For TEST
//	DefaultGraphCell[] cells = null;
//	public void testAdd()
//	{
//		cells = new DefaultGraphCell[2];
//		cells[0] = GraphUtility.createVertex("TARGET");
//		cells[1] = GraphUtility.createEdge(root, cells[0]);
//		graph.getGraphLayoutCache().insert(cells);
//		
//		executeLayout();
//	}
//	
//	public void testDel()
//	{
//		graph.getGraphLayoutCache().remove(cells);
//		/*
//		Iterator it = model.edges(root); 
//		while(it.hasNext())
//		{
//			Object ob = it.next();
//			graph.getGraphLayoutCache().remove(new Object[] {ob});
//		}
//		*/
//		executeLayout();
//	}
	
	
	//Variabili utilizzate per memorizzare lo stato del grafo visualizzato
	//Per Vertici
	HashSet<Long> vertexsInGraph = new HashSet<Long>();
	Hashtable<Long, DefaultGraphCell> hashCellsInGraph = new Hashtable<Long, DefaultGraphCell>();
	//Per Edge
	HashSet<InstanceReference> edgesInGraph = new HashSet<InstanceReference>();
	Hashtable<InstanceReference, DefaultEdge> hashEdgesInGraph = new Hashtable<InstanceReference, DefaultEdge>();
	
	/*
	 * Metodo chiamato nella Classe InspectThread dal metodo updateInstancesReferencesAndLabel
	 */
	public void renderInstances(Hashtable<Long, InstanceDescriptor> instances)
	{
		model.beginUpdate();
		
		//Vertex to add
		HashSet<Long> vertexsToAdd = new HashSet<Long>();
		Enumeration<Long> idEnumeration = instances.keys();
		while(idEnumeration.hasMoreElements())
		{
			Long objId = idEnumeration.nextElement();
			if (!vertexsInGraph.contains(objId))
			{
				vertexsToAdd.add(objId);
			}
		}
		
		//Vertex to remove
		HashSet<Long> vertexsToDel = new HashSet<Long>();
		for (Long objId: vertexsInGraph)
		{
			if (!instances.containsKey(objId))
			{
				vertexsToDel.add(objId);
			}
		}
		
		//Add vertex
		for (Long objId : vertexsToAdd)
		{
			InstanceDescriptor instDscr = instances.get(objId);
			DefaultGraphCell cell = GraphUtility.createVertex(instDscr);
			graph.getGraphLayoutCache().insert(new DefaultGraphCell[]{cell} );
			vertexsInGraph.add(objId);
			hashCellsInGraph.put(objId, cell);
			Logger.log.debug("CREATE VERTEX: " + instDscr.getLabel() + " uniqueID:" + instDscr.getUniqueID());
		}
		
		//Remove vertex
		for (Long objId : vertexsToDel)
		{
			DefaultGraphCell cell = hashCellsInGraph.get(objId);
			graph.getGraphLayoutCache().remove(new DefaultGraphCell[] {cell} );
			vertexsInGraph.remove(objId);
			hashCellsInGraph.remove(objId);
			Logger.log.debug("REMOVE VERTEX: uniqueID:" +objId+ " Cell:" + cell);
		}
		
		
		//Update Label
		for (Long objId: vertexsInGraph)
		{
			if (instances.containsKey(objId))
			{
				String instanceLabel = GraphUtility.getLabel(instances.get(objId));
				//Logger.log.debug("Label: " + instanceLabel);
				DefaultGraphCell cell = hashCellsInGraph.get(objId);
				
				//Non documentato in JGraph: per aggiornare una label anche dopo che si e' trascinata
				// bisogna eseguire questa procedura
				GraphConstants.setValue(cell.getAttributes(), instanceLabel);
				cell.setUserObject(instanceLabel);
				graph.getGraphLayoutCache().valueForCellChanged(cell, instanceLabel);
				model.valueForCellChanged(cell, instanceLabel);
				
			}
		}
				
		
		//Edge to add
		HashSet<InstanceReference> edgesToAdd = new HashSet<InstanceReference>();
		HashSet<InstanceReference> edgesInInstances = new HashSet<InstanceReference>(); //Usato per "edge to remove"
		Enumeration<InstanceDescriptor> instDscrEnumeration = instances.elements();
		while(instDscrEnumeration.hasMoreElements())
		{
			InstanceDescriptor instDscr = instDscrEnumeration.nextElement();
			for (InstanceReference instRef : instDscr.getReferences())
			{
				if (!edgesInGraph.contains(instRef))
				{
					edgesToAdd.add(instRef);
				}
				edgesInInstances.add(instRef); //Usato per "edge to remove"
			}
		}
		
		//Edge to remove
		HashSet<InstanceReference> edgesToDel = new HashSet<InstanceReference>();
		for (InstanceReference instRef: edgesInGraph)
		{
			if (!edgesInInstances.contains(instRef))
			{
				edgesToDel.add(instRef);
			}
		}
		
		//Add edge
		for (InstanceReference instRef : edgesToAdd)
		{
			//Logger.log.debug("CREATING EDGE: " + instRef.getParentID() + "-->" + instRef.getUniqueID());
			DefaultGraphCell cell1 = hashCellsInGraph.get(instRef.getUniqueID());
			DefaultGraphCell cell2 = hashCellsInGraph.get(instRef.getParentID());
			DefaultEdge edge = GraphUtility.createEdge(cell1, cell2, instRef);
			graph.getGraphLayoutCache().insert(new DefaultGraphCell[]{edge} );
			edgesInGraph.add(instRef);
			hashEdgesInGraph.put(instRef, edge);
			Logger.log.debug("CREATE EDGE: " + instRef.getParentID() + "-->" + instRef.getUniqueID());
		}
		
		//Remove edge
		for (InstanceReference instRef : edgesToDel)
		{
			DefaultEdge edge = hashEdgesInGraph.get(instRef);
			graph.getGraphLayoutCache().remove(new DefaultGraphCell[] {edge} );
			edgesInGraph.remove(instRef);
			hashEdgesInGraph.remove(instRef);
			Logger.log.debug("REMOVE EDGE: " + instRef.getParentID() + "-->" + instRef.getUniqueID());
		}
						
		model.endUpdate();
		
		//graph.graphDidChange();

		
		//Update Layout
		executeLayout();
	}
	
	
	/*
	//OLD
	public void renderInstances(Hashtable<Long, InstanceDescriptor> instances)
	{
		Vector<DefaultGraphCell> cells = new Vector<DefaultGraphCell>();
		
		//Create all Vertex
		Enumeration<InstanceDescriptor> instDscrEnumeration = instances.elements();
		while(instDscrEnumeration.hasMoreElements())
		{
			InstanceDescriptor instDscr = instDscrEnumeration.nextElement();
			
			//Creo vertice
			DefaultGraphCell vertex = GraphUtility.createVertex(instDscr);
			instDscr.setjGraphCell(vertex);
			Logger.log.debug("CREATE VERTEX: " + instDscr.getLabel());
			
			cells.addElement(vertex);
		}
		
		
		//Create all Edge
		instDscrEnumeration = instances.elements();
		while(instDscrEnumeration.hasMoreElements())
		{
			InstanceDescriptor instDscr1 = instDscrEnumeration.nextElement();
			DefaultGraphCell vertex1 = instDscr1.getjGraphCell();
			
			HashSet<InstanceReference> references = instDscr1.getReferences();
			for (InstanceReference instRef : references)
			{
				InstanceDescriptor instDscr2 = instances.get(instRef.getUniqueID());
				DefaultGraphCell vertex2 = instDscr2.getjGraphCell();
				
				DefaultEdge edge = GraphUtility.createEdge(vertex2, vertex1, instRef);
				Logger.log.debug("CREATE EDGE: " + instRef.getUniqueID() + "-->" + instDscr2.getUniqueID());
				
				cells.addElement(edge);
			}		
		}
		
		
		//update jgraph
		DefaultGraphCell[] cellsArray = cells.toArray(new DefaultGraphCell[0]);
		graph.getGraphLayoutCache().insert(cellsArray);
		
		//Update Layout
		executeLayout();
		
	}
	*/
	
	//--------------------------------------------------------------------------------
	//Metodi per la gestione dell'esportazione XML
	
	//Variabile utilizzata per memorizzare ExportFrames
	ExportFrames exportFrames = new ExportFrames();
	
	public ExportFrames getExportFrames()
	{
		return exportFrames;
	}
	
	public void captureShot(String title, String description)
	{
		Logger.log.info("CAPTURE SHOT START...");
		
		//build frame
		GraphFrame graphFrame = new GraphFrame(title, description);
			
		//VERTEXS CELL
		for (Long objId: vertexsInGraph)
		{
			DefaultGraphCell cell = hashCellsInGraph.get(objId);
				
			/*
			Hashtable attrMap = cell.getAttributes();
			Enumeration<Object> attrKeys = attrMap.keys();
			while(attrKeys.hasMoreElements())
			{
				Object attrKey = attrKeys.nextElement();
				Object attrObj = attrMap.get(attrKey);
				Logger.log.info("ATTR: " + attrKey + " --> " + attrObj);
			}
			*/
			
			Object userObj = cell.getUserObject();
			//GraphConstants.getValue(cell.getAttributes());
			//Logger.log.debug("CELL: "+objId+" VALUE: " +userObj);	
			//build cell
			GraphCell graphCell = new GraphCell((String)userObj);
			graphCell.setObjId(objId);
			
			Rectangle2D cellBounds = GraphConstants.getBounds(cell.getAttributes());
			//Logger.log.debug("X: " + cellBounds.getX());
			//Logger.log.debug("Y: " + cellBounds.getY());
			//Logger.log.debug("W: " + cellBounds.getWidth());
			//Logger.log.debug("H: " + cellBounds.getHeight());
			//Logger.log.debug("Background: " + GraphConstants.getBackground(cell.getAttributes()));
			//Logger.log.debug("Foreground: " + GraphConstants.getForeground(cell.getAttributes()));
			
			graphCell.setX(cellBounds.getX());
			graphCell.setY(cellBounds.getY());
			graphCell.setWidth(cellBounds.getWidth());
			graphCell.setHeight(cellBounds.getHeight());
			graphCell.setBackground(Integer.toHexString(GraphConstants.getBackground(cell.getAttributes()).getRGB()).substring(2));
			graphCell.setForeground(Integer.toHexString(GraphConstants.getForeground(cell.getAttributes()).getRGB()).substring(2));
						
			//Add cell
			graphFrame.getElements().add(graphCell);
			
		}
		
		//EDGES
		for (InstanceReference instRef : edgesInGraph)
		{
			DefaultEdge edge = hashEdgesInGraph.get(instRef);
			
			Object userObj = edge.getUserObject();
			//Logger.log.debug("EDGE: VALUE: " +edge + " ISDIRECT:"+instRef.isDirect());	
			//build edge
			GraphEdge graphEdge = new GraphEdge((String)userObj);
			graphEdge.setObjId(instRef.getUniqueID());
			graphEdge.setDirect(instRef.isDirect());
					
			CellView ecv = graph.getGraphLayoutCache().getMapping(edge, false);
			if (ecv.getClass().getName().equals("org.jgraph.graph.EdgeView"))
			{
				
				Point2D pFrom = ((EdgeView)ecv).getPoint(0);
				//Logger.log.debug("*PPP: " + " X:" + pFrom.getX() + " Y:"+ pFrom.getY());
				graphEdge.setxFrom(pFrom.getX());
				graphEdge.setyFrom(pFrom.getY());
				
				Point2D pTo = ((EdgeView)ecv).getPoint(1);
				//Logger.log.debug("*PPP: " + " X:" + pTo.getX() + " Y:"+ pTo.getY());
				graphEdge.setxTo(pTo.getX());
				graphEdge.setyTo(pTo.getY());
				
				/*
				//OLD
				Point2D pLabel = ((EdgeView)ecv).getLabelPosition();
				Point2D pLabelVec = ((EdgeView)ecv).getLabelVector();
				//Logger.log.debug("*PLAB: " + " X:" + pLabel.getX() + " Y:"+ pLabel.getY());
				//Logger.log.debug("*PLABVEC: " + " X:" + pLabelVec.getX() + " Y:"+ pLabelVec.getY());
				graphEdge.setxLabel(pLabel.getX()+pLabelVec.getX());
				graphEdge.setyLabel(pLabel.getY()+pLabelVec.getY());
				*/
				
				/*
				List points = ((EdgeView)ecv).getPoints();
				if (points!=null)
				{
					for (Object point : points)
					{
						if (point.getClass().getName().equals("org.jgraph.graph.PortView"))
						{
							Point2D point2d = ((PortView)point).getLocation();
							Logger.log.info("*P: " + point + " X:" + point2d.getX() + " Y:"+ point2d.getY());
							
						}
					}
				}
				*/
				
			}
			
			//Add edge
			graphFrame.getElements().add(graphEdge);
			
		}
				
		//add frame
		exportFrames.getFrames().add(graphFrame);
			
	}
	
	
	
		
	//--------------------------------------------------------------------------------
	//Metodi per la gestione del morphing animato del layout desunti dalla classe
	// di esempio del pacchetto JGraph: com.jgraph.layout.demo.JGraphLayoutPanel
	
	public void fitViewport()
	{
		JGraphLayoutMorphingManager.fitViewport(graph);
	}
	
	public void executeLayout()
	{
		execute(this.layout);
		//graph.clearSelection(); //Puo' lanciare java.util.ConcurrentModificationException
	}
	
	public void setLayout(JGraphLayout layoutParam)
	{
		this.layout = layoutParam;
	}
	
	//TODO: non fare il fitViewport ogni volta che si lancia execute: vedi esempio in com.jgraph.layout
	
	/**
	 * Executes the current layout on the current graph by creating a facade and
	 * progress monitor for the layout and invoking it's run method in a
	 * separate thread so this method call returns immediately. To display the
	 * result of the layout algorithm a JGraphLayoutMorphingManager is
	 * used.
	 */
	private void execute(final JGraphLayout layout)
	{
		if (graph != null && graph.isEnabled() && graph.isMoveable() && layout != null)
		{
			wrkspc.getExplorerStatusBar().setStatusMessage("Performing Layout...");
			
			final JGraphFacade facade = createFacade(graph);

			new Thread()
			{
				public void run()
				{
					synchronized (this)
					{
						try
						{
							// Executes the layout
							layout.run(facade);
							//facade.run(layout, false);
							
							SwingUtilities.invokeLater(new Runnable()
							{
								public void run()
								{
									
									// Processes the result of the layout algorithm
									// by creating a nested map based on the global
									// settings and passing the map to a morpher
									// for the graph that should be changed.
									// The morpher will animate the change and then
									// invoke the edit method on the graph layout cache.
									Map map = facade.createNestedMap(true, true); //TODO ?? il secondo parametro e': flushOriginCheckBox
									morpher.morph(graph, map);
									graph.requestFocus();
									
									wrkspc.getExplorerStatusBar().setStatusMessage("Done");
									wrkspc.getMainStatusBar().updateStatusBar(); //Per aggiornare memory free
								}
							});
						}
						catch (Exception e)
						{
							e.printStackTrace();
							JOptionPane.showMessageDialog(graph, e.getMessage());
						}
					}
				}
			}.start(); // fork
		}
	}
		
	/**
	 * Creates a {@link JGraphFacade} and makes sure it contains a valid set of
	 * root cells if the specified layout is a tree layout. A root cell in this
	 * context is one that has no incoming edges.
	 * 
	 * @param graph
	 *            The graph to use for the facade.
	 * @return Returns a new facade for the specified layout and graph.
	 */
	private JGraphFacade createFacade(JGraph graph)
	{
		// Creates and configures the facade using the global switches
		JGraphFacade facade = new JGraphFacade(graph, graph.getSelectionCells());
		facade.setIgnoresUnconnectedCells(true);
		facade.setIgnoresCellsInGroups(true);
		facade.setIgnoresHiddenCells(true);
		facade.setDirected(true);
		
		// Removes all existing control points from edges
		//facade.resetControlPoints();
		return facade;
	}
		
	
	//--------------------------------------------------------------------------------
	//Metodi per la gestione del GraphNavigator laterale
	
	public GraphNavigator getGraphNavigator()
	{
		navigator = GraphNavigator.createInstance(createGraphForNavigator());
		navigator.setCurrentGraph(this.graph);
		return navigator;
	}
	
	@SuppressWarnings("unchecked")
	private static JGraph createGraphForNavigator()
	{
		// Creates a model that does not allow disconnections
		GraphModel model = new NavigatorGraphModel();
		GraphLayoutCache layoutCache = new GraphLayoutCache(model, new DefaultCellViewFactory(), true);
		Set locals = new HashSet();
		locals.add(GraphConstants.BOUNDS);
		layoutCache.setLocalAttributes(locals);
		return new NavigatorGraph(model, layoutCache);
	}
	
	private static class NavigatorGraph extends JGraph {

		// Construct the Graph using the Model as its Data Source
		public NavigatorGraph(GraphModel model) {
			this(model, null);
		}

		// Construct the Graph using the Model as its Data Source
		public NavigatorGraph(GraphModel model, GraphLayoutCache cache) {
			super(model, cache);
			// Make Ports Visible by Default
			setPortsVisible(true);
			// Use the Grid (but don't make it Visible)
			setGridEnabled(true);
			// Set the Grid Size to 10 Pixel
			setGridSize(6);
			// Set the Tolerance to 2 Pixel
			setTolerance(2);
			// Accept edits if click on background
			setInvokesStopCellEditing(true);
			// Allows control-drag
			setCloneable(false);
			// Jump to default port on connect
			setJumpToDefaultPort(true);
		}

	}
	
	private static class NavigatorGraphModel extends DefaultGraphModel {

		public NavigatorGraphModel() {
			super();
		}

		public NavigatorGraphModel(List roots, AttributeMap attributes) {
			super(roots, attributes);
		}

		public boolean acceptsSource(Object edge, Object port) {
			return (port != null);
		}

		public boolean acceptsTarget(Object edge, Object port) {
			return (port != null);
		}
	}

}
