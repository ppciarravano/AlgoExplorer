package ppciarravano.algoexplorer.test;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Vector;

/**
 * Classe Dijkstra<br><br>
 * Per implementare il metodo sssp, ho usato il classico algoritmo di Dijkstra,
 * direttamente usando la matrice di adiacenza data, e non effettuando nessun side-effect su questa;
 * per calcolare il minimo sui nodi ho usato una coda di priorita' indiretta che usa multiway heap e che 
 * restituisce direttamente l'indice dell'elemento minimo dell'array, permettendo di aggiornare le priorita'
 * dello stesso array dinamicamente.
 * La coda di priorita' usata e' desunta dal programma 20.10 di "Algoritmi in Java" di R.Sedgewick. 
 *     
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author		Pier Paolo Ciarravano
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class Dijkstra 
{
	public static void main(String[] args)
	{
		/*
		double[][] graph =  { 
				{ 0.0d, 2.0d, 0.0d, 0.0d, 8.0d, 0.0d, 0.0d},
				{ 2.0d, 0.0d, 6.0d, 2.0d, 0.0d, 0.0d, 0.0d},
				{ 0.0d, 6.0d, 0.0d, 0.0d, 0.0d, 0.0d, 5.0d},
				{ 0.0d, 2.0d, 0.0d, 0.0d, 2.0d, 9.0d, 0.0d},
				{ 8.0d, 0.0d, 0.0d, 2.0d, 0.0d, 3.0d, 0.0d},
				{ 0.0d, 0.0d, 0.0d, 9.0d, 3.0d, 0.0d, 1.0d},
				{ 0.0d, 0.0d, 5.0d, 0.0d, 0.0d, 1.0d, 0.0d}
		
		};
		*/
		
		
		double[][] graph =  { 
				{ 0.0d, 1.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d},
				{ 1.0d, 0.0d, 1.0d, 1.0d, 0.0d, 0.0d, 0.0d},
				{ 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d},
				{ 0.0d, 1.0d, 0.0d, 0.0d, 1.0d, 1.0d, 0.0d},
				{ 1.0d, 0.0d, 0.0d, 1.0d, 0.0d, 1.0d, 0.0d},
				{ 0.0d, 0.0d, 0.0d, 1.0d, 1.0d, 0.0d, 1.0d},
				{ 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 1.0d, 0.0d}
		
		};
		
		/*
		double[][] graph =  { 
				{ 0.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d},
				{ 1.0d, 0.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d},
				{ 1.0d, 1.0d, 0.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d},
				{ 1.0d, 1.0d, 1.0d, 0.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d},
				{ 1.0d, 1.0d, 1.0d, 1.0d, 0.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d},
				{ 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 0.0d, 1.0d, 1.0d, 1.0d, 1.0d},
				{ 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 0.0d, 1.0d, 1.0d, 1.0d},
				{ 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 0.0d, 1.0d, 1.0d},
				{ 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 0.0d, 1.0d},
				{ 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 0.0d}
		
		};
		*/
		/*
		int numNodes = InputUtil.getIntegerFromImput("Num. Nodes", 0, 100);
		int minConnForNode = InputUtil.getIntegerFromImput("Min. Conn. for node", 0, 10);
		int maxConnForNode = InputUtil.getIntegerFromImput("MAX. Conn. for node", 0, 10);
		double[][] graph = TestGraph.getEuclideanRandomGraphMatrix(numNodes, minConnForNode, maxConnForNode, false);
		*/
		
		TestGraph.dumpMatrix(graph);
		GraphNode<String, Double> graphNode = GraphNode.getGraphFromMatrix(graph);
		
		for (int i = 0; i < 5; i++)
		{
			TreeNode<Integer> test = sssp(graph, InputUtil.getIntegerFromImput("Start node", 0, graph.length-1));
		}
		
	}
	
	public Dijkstra()
	{
		
	}
	
	/*
	 * Il metodo sssp riceve in input una matrice double[][] eam che rappresenta 
	 * la matrice di adiacenza estesa di un grafo semplice, connesso e pesato 
	 * sugli spigoli, un intero int i, con 0<=i<n = eam.length, 
	 * che rappresenta l'indice della radice dell’albero dei cammini minimi da costruire 
	 * e restituisce la radice un albero dei cammini minimi a partire dal vertice i. 
	 * La matrice in input è simmetrica rispetto alla diagonale principale, 
	 * ove sono presenti solo zeri, non contiene numeri negativi e descrive 
	 * un grafo sicuramente connesso.
	 */
	public static TreeNode<Integer> sssp(double[][] eam, int i)
	{
		//Salto i controlli su eam
		
		//mi limito ad un controllo su null
		if (eam==null)
		{
			return null;
		}
		//Controllo la validita dell'indice i
		if ((i<0)||(i>=eam.length))
		{
			return null;
		}				
		
		//i = i-1;
		
		//int[] pred = new int[eam.length];
		@SuppressWarnings("unchecked")
		TreeNode<Integer>[] nodes = new TreeNode[eam.length];
		//TreeNode<Integer>[] nodes = (TreeNode<Integer>[])Array.newInstance(TreeNode.class, eam.length);
		
		//boolean[] mark = new boolean[eam.length];
		double[] wt = new double[eam.length];
		int j;
		int u = 0;
		double p = 0.0d;

		//Inizializzazione
		IndirectPQ pQ = new IndirectPQ(eam.length, wt);
		for (j = 0; j < eam.length; j++)
		{
			//mark[i]=false;
			wt[j] = Double.MAX_VALUE;
			pQ.insert(j);
			//pred[j] = 0;
		}
		wt[i] = 0.0d; //Nodo di partenza, distanza 0
		pQ.lower(i);
		//pred[i] = -1; //-1 per radice
		TreeNode<Integer> root = new TreeNode<Integer>(i);
		nodes[i] = root;

		while (!pQ.empty())
		{
			u = pQ.getmin();
			// mark[u] = true;
			for (j = 0; j < eam.length; j++)
			{
				if (eam[u][j] > 0.0d)
				{
					// if (mark[i]) {
					p = wt[u] + eam[u][j];
					if (wt[j] > p)
					{
						wt[j] = p;
						pQ.lower(j);
						// pred[j] = u+1;
						if (nodes[j]==null)
						{
							TreeNode<Integer> node = new TreeNode<Integer>(j);
							
							//Utilizzando parent
							//node.parent = nodes[u];
							
							//Utilizzando childs
							nodes[u].addChild(node);
							
							nodes[j] = node;
						}
						else
						{
							//Utilizzando parent
							//nodes[j].parent = nodes[u];
							
							//Utilizzando childs
							nodes[u].addChild(nodes[j]);
						}
					}
					// }
				}
			}
		}
		
		//return pred;
		return root;

	}
	
}

class TreeNode<T>
{

	
	T element;
	//TreeNode<T> firstChild;
	//TreeNode<T> nextSibling;
	TreeNode<T> parent;
	
	List<TreeNode<T>> childs;
	
	public TreeNode( T theElement )
	{
		element = theElement;
		
		childs = new Vector<TreeNode<T>>(0);
	}
	
	public void addChild(TreeNode<T> child)
	{
		this.childs.add(child);
	}
	
}


/*
 * Algoritmo da: 
 * 
 * "Sedgewick, R. and Schidlowsky, M. 1998 
 * Algorithms in Java, Third Edition, Parts 1-4: 
 * Fundamentals, Data Structures, Sorting, Searching. 3rd. 
 * Addison-Wesley Longman Publishing Co., Inc."
 * 
 * http://www.cs.princeton.edu/~rs/Algs3.java1-4/code.txt
 */
class IndirectPQ {
	private int N;
	private final int d = 3;
	private final int dd = d - 2;
	private double[] a;
	private int[] pq, qp;
	private boolean less(int i, int j) {
		return a[pq[i]] < a[pq[j]];
	}
	private void exch(int i, int j) {
		int t = pq[i];
		pq[i] = pq[j];
		pq[j] = t;
		qp[pq[i]] = i;
		qp[pq[j]] = j;
	}
	private void swim(int k) {
		while (k > 1 && less(k, (k + dd) / d)) {
			exch(k, (k + dd) / d);
			k = (k + dd) / d;
		}
	}
	private void sink(int k, int N) {
		int j;
		while ((j = d * (k - 1) + 2) <= N) {
			for (int i = j + 1; i < j + d && i <= N; i++)
				if (less(i, j))
					j = i;
			if (!(less(j, k)))
				break;
			exch(k, j);
			k = j;
		}
	}
	IndirectPQ(int maxN, double[] a) {
		this.a = a;
		this.N = 0;
		pq = new int[maxN + 1];
		qp = new int[maxN + 1];
		for (int i = 0; i <= maxN; i++) {
			pq[i] = 0;
			qp[i] = 0;
		}
	}
	boolean empty() {
		return N == 0;
	}
	void insert(int v) {
		pq[++N] = v;
		qp[v] = N;
		swim(N);
	}
	int getmin() {
		exch(1, N);
		sink(1, N - 1);
		return pq[N--];
	}
	void lower(int k) {
		swim(qp[k]);
	}
}

