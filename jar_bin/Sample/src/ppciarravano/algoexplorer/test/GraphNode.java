package ppciarravano.algoexplorer.test;

import java.util.List;
import java.util.Vector;

/**
 * Classe GraphNode
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010) 
 */
public class GraphNode<T,W>
{
	private T element;
	private List<GraphNode<T,W>> adjacencies;
	private List<W> weights;
		
	public GraphNode(T element)
	{
		this.element = element;
		adjacencies = new Vector<GraphNode<T,W>>(0);
		weights = new Vector<W>(0);
	}

	public T getElement()
	{
		return element;
	}

	public void setElement(T element)
	{
		this.element = element;
	}

	public List<GraphNode<T,W>> getAdjacencies()
	{
		return adjacencies;
	}
	
	public List<W> getWeights()
	{
		return weights;
	}
	
	public void addAdjacency(GraphNode<T,W> graphNode)
	{
		addAdjacency(graphNode, null);
	}
	
	public void addAdjacency(GraphNode<T,W> graphNode, W weight)
	{
		this.adjacencies.add(graphNode);
		this.weights.add(weight);		
	}
	
	//Crea un nodo lo connette e lo restituisce
	public GraphNode<T,W> addAdjacency(T element, W weight)
	{
		GraphNode<T,W> graphNode = new GraphNode<T,W>(element);
		this.adjacencies.add(graphNode);
		this.weights.add(weight);
				
		return graphNode;
	}
	
	public static GraphNode<String, Double> getGraphFromMatrix(double[][] matrix)
	{
		GraphNode<String, Double>[] nodi = new GraphNode[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			nodi[i] = new GraphNode<String, Double>("N:" + (i));
		}
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j]!=0)
				{
					nodi[i].addAdjacency(nodi[j], matrix[i][j]);
				}
			}
		}
		
		return nodi[0];
	}
	
		
}
