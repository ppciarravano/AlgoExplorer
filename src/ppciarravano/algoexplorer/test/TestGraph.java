package ppciarravano.algoexplorer.test;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Classe TestGraph
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010) 
 */
public class TestGraph
{
		
	public static GraphNode<String, Integer> getGraphSample()
	{
		GraphNode<String, Integer> n1 = new GraphNode<String, Integer>("N1");
		GraphNode<String, Integer> n2 = new GraphNode<String, Integer>("N2");
		GraphNode<String, Integer> n3 = new GraphNode<String, Integer>("N3");
		n1.addAdjacency(n2, 1);
		n1.addAdjacency(n3, 2);
		GraphNode<String, Integer> n4 = new GraphNode<String, Integer>("N4");
		n2.addAdjacency(n4, 3);
		GraphNode<String, Integer> n5 = new GraphNode<String, Integer>("N5");
		n2.addAdjacency(n5, 4);
		n5.addAdjacency(n1, 5);
		GraphNode<String, Integer> n6 = new GraphNode<String, Integer>("N6");
		n5.addAdjacency(n6, 6);
		n4.addAdjacency(n6, 7);
		GraphNode<String, Integer> n7 = new GraphNode<String, Integer>("N7");
		n6.addAdjacency(n7, 8);
		n7.addAdjacency(n3, 9);
		n6.addAdjacency(n2, 10);
		GraphNode<String, Integer> n8 = n7.addAdjacency("N8", 9);
		n8.addAdjacency(n3, 11);
		n7.addAdjacency(n8, 12);
		n8.addAdjacency(n1, 13);
				
		return n1;
	}
	
	
	
	public static void dumpMatrix(double[][] matrix)
	{
		DecimalFormat df = new DecimalFormat("000.00"); 
		for (int i = 0; i < matrix.length; i++) {
			System.out.println();
			for (int j = 0; j < matrix.length; j++) {
				
				String number = df.format(matrix[j][i]);

				System.out.print( number + " " );
			}
		}
		System.out.println();
	}
	
	public static double[][] getRandomGraphMatrix(int numNodes, int minConnForNode, int maxConnForNode, boolean randomWeight )
	{
		Random rnd = new Random();
		
		double[][] graph = new double[numNodes][numNodes];
		
				
		for (int i = 0; i < graph.length; i++)
		{
			graph[i][i] = 0.0d;
			for (int j = i+1; j < graph.length; j++)
			{
				graph[i][j] = 1.0d;
				graph[j][i] = graph[i][j];
								
			}
		}
				
		return graph;
	}
	
	
	
	public static double[][] getEuclideanRandomGraphMatrix(int numNodes, int minConnForNode, int maxConnForNode, boolean randomWeight )
	{
		Random rnd = new Random();
		
		//Costruisco matrice da un piano cartesiano (100x100) con punti casuali
		double[][] graph = new double[numNodes][numNodes];
		
		//genero punti
		double[][] punti = new double[numNodes][2];
		for (int i = 0; i < punti.length; i++) {
			punti[i][0] = Math.random()*100; //Coordinata X
			punti[i][1] = Math.random()*100; //Coordinata Y
		}
		
		//intanto azzero la matrice
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph.length; j++) {
				graph[j][i] = 0.0d;
			}
		}
				
		//ciclo sui punti e costruisco le connessioni
		for (int i = 0; i < punti.length; i++) {
			
			//Scelgo numero di nodi da connettere al nodo attuale
			int numConnessioni = rnd.nextInt(maxConnForNode-minConnForNode+1)+minConnForNode;
			//System.out.println("Connessioni "+i+":" + numConnessioni);
			int[] puntiConnessi = new int[numConnessioni];
			for(int m=0; m<puntiConnessi.length; m++) {
				puntiConnessi[m] = -1;
			}
			for(int m=0; m<puntiConnessi.length; m++) {
				
				//scelgo un punto casuale che non sia il punto i
				boolean trovato = true; 
				int numPunto = -1;
				do {
					numPunto = rnd.nextInt(punti.length);
					if (numPunto==i)
					{
						trovato = false; 
					}
					else
					{
						trovato = true; 
					}
					//Ciclo per i punti precedenti per vedere se gia' l'ho scelto	
					for (int j = 0; j < m; j++) {
						//System.out.println("***" + m); 
						if ((puntiConnessi[j] == numPunto) || (numPunto==i) )
						{
							trovato = false;
						}
					}	
				} while (!trovato);
				puntiConnessi[m] = numPunto;
					
				//System.out.println("-------------> " + numPunto);
			}
						
			//puntiConnessi contiene i punti da connettere
			for(int m=0; m<puntiConnessi.length; m++) {
				//System.out.println("-------------> " + puntiConnessi[m]);
				int j = puntiConnessi[m];
				if (randomWeight)
				{
					//Distanza dal nodo i al nodo h
					double distance = Math.sqrt((punti[i][0]-punti[j][0])*(punti[i][0]-punti[j][0])
							+(punti[i][1]-punti[j][1])*(punti[i][1]-punti[j][1]));
					//System.out.println("-------------> " + h + ": " + distance);
					graph[i][j] = distance;
				}
				else
				{
					graph[i][j] = 1.0d;
				}
				graph[j][i] = graph[i][j];
			}
			
		}
		
		
//		//Restituisco int[][]
//		int[][] intGraph = new int[NUM_NODI][NUM_NODI];
//		for (int i = 0; i < graph.length; i++) {
//			for (int j = 0; j < graph.length; j++) {
//				intGraph[j][i] = new Double( graph[j][i] ).intValue();
//			}
//		}
//		return intGraph;
		
		return graph;
	}
	
	
	
}
