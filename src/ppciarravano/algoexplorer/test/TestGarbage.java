package ppciarravano.algoexplorer.test;

public class TestGarbage
{

	private String nome;
	
	public TestGarbage(String n)
	{
		nome=n;
	}
	
	public static void main(String[] args)
	{
		for (int i = 0; i < 100; i++)
		{
			
			System.out.println("Test " + i);
			TestGarbage tg = new TestGarbage(i + "");
			System.out.println("OK");
			
			System.gc();
		}

	}

	
}
