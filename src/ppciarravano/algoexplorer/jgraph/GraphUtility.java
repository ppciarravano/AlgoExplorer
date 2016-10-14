package ppciarravano.algoexplorer.jgraph;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import ppciarravano.algoexplorer.trace.InstanceDescriptor;
import ppciarravano.algoexplorer.trace.InstanceReference;


/**
 * Classe GraphUtility
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class GraphUtility
{
	private static final int MAX_X_GRID = 500;
	private static final int MAX_Y_GRID = 500;
	
	public static final String INSTANCE_DESCRIPTOR_OBJECT = "INSTANCE_DESCRIPTOR_OBJECT";	
	public static final String INSTANCE_REFERENCE_OBJECT = "INSTANCE_REFERENCE_OBJECT";	
	
	private static final Random randomGenerator = new Random();
	
	@SuppressWarnings("unchecked")
	public static DefaultEdge createEdge(DefaultGraphCell vertex1, DefaultGraphCell vertex2, InstanceReference instRef)
	{
		// Create Edge with the given name
		DefaultEdge edge = new DefaultEdge(instRef.getName());
		
		AttributeMap attributeMap = edge.getAttributes();
		
		GraphConstants.setLabelAlongEdge(edge.getAttributes(), true);
		
		if (!instRef.isDirect())
		{
			GraphConstants.setDashPattern(edge.getAttributes(), new float[] {10f,10f});
			//GraphConstants.setDashOffset(edge.getAttributes(), 10);
		}
		
		// Fetch the ports from the new vertices, and connect them with the edge
		edge.setSource(vertex1.getChildAt(0));
		edge.setTarget(vertex2.getChildAt(0));
				
		// Set Arrow Style for edge
		int arrow = GraphConstants.ARROW_TECHNICAL;
		GraphConstants.setLineEnd(edge.getAttributes(), arrow);
		GraphConstants.setEndFill(edge.getAttributes(), true);
		
		//Set Object reference
		// Memorizzata per scopi futuri, attualmente non utilizzata
		attributeMap.put(INSTANCE_REFERENCE_OBJECT, instRef);
		
		return edge;
	}
	
	
	@SuppressWarnings("unchecked")
	public static DefaultGraphCell createVertex(InstanceDescriptor instDscr)
	{
		String label = getLabel(instDscr);
		
		// Create vertex with the given name
		DefaultGraphCell cell = new DefaultGraphCell(label);

		AttributeMap attributeMap = cell.getAttributes();
		
		//Setto le proprieta' del vertice
		GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(randomGenerator.nextInt(MAX_X_GRID),randomGenerator.nextInt(MAX_Y_GRID), 10d, 20d));
		GraphConstants.setAutoSize(attributeMap, true);
		//GraphConstants.setGradientColor(attributeMap, instDscr.getClassDescriptor().getBackgroundColorCell());
		GraphConstants.setBackground(attributeMap, instDscr.getClassDescriptor().getBackgroundColorCell());
		GraphConstants.setForeground(attributeMap, instDscr.getClassDescriptor().getForegroundColorCell());
		GraphConstants.setOpaque(attributeMap, true);
		//GraphConstants.setBorder(attributeMap, BorderFactory.createRaisedBevelBorder());
		GraphConstants.setBorderColor(attributeMap, Color.black);
		
		//Set Object reference
		// referenza utilizzata per gestire il click sul vertice e la visualizzazione del pannello di inspect
		attributeMap.put(INSTANCE_DESCRIPTOR_OBJECT, instDscr);
		
		// Add a Floating Port
		cell.addPort();

		return cell;
	}

	public static String getLabel(InstanceDescriptor instDscr)
	{
		String label = " " + instDscr.getLabel() + " (ID:"+instDscr.getUniqueID()+(instDscr.isCollected() ? "*" : "")+") ";
		return label;
	}

}
