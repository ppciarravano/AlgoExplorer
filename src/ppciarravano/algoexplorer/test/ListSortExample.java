package ppciarravano.algoexplorer.test;

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
class ListSortExample
{
	
	static class Node
	{
		int val;
		Node next;

		Node(int v, Node t)
		{
			val = v;
			next = t;
		}
	}

	/* CODICE ORIGINALE
	static Node create()
	{
		Node a = new Node(0, null);
		for (In.init(); !In.empty();)
			a.next = new Node(In.getInt(), a.next);
		return a;
	}
	*/
	
	static Node sort(Node a)
	{
		Node t, u, x, b = new Node(0, null);
		while (a.next != null)
		{
			t = a.next;
			u = t.next;
			a.next = u;
			for (x = b; x.next != null; x = x.next)
				if (x.next.val > t.val)
					break;
			t.next = x.next;
			x.next = t;
		}
		return b;
	}

	static void print(Node h)
	{
		for (Node t = h.next; t != null; t = t.next)
			System.out.println(t.val + "");
	}

	public static void main(String[] args)
	{
		/*
		 * CODICE ORIGINALE print(sort(create()));
		 */

		int numNodes = InputUtil.getIntegerFromImput("Num. Nodes", 0, 100);
		Node start = null;
		Node prec = null;
		for (int i = 0; i < numNodes; i++)
		{
			int value = InputUtil.getIntegerFromImput("Node Value", 0, 100);
			Node a = new Node(value, null);
			if (prec != null)
			{
				prec.next = a;
			}
			else
			{
				start = a;
			}
			prec = a;
		}
		
		sort(start);
		print(start);
		
	}
}
