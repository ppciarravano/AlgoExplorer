package ppciarravano.algoexplorer.test;

public class TestClassInit
{
	private int id = 0;
	private TestClassInit pointer;
	
	public TestClassInit(int idParam)
	{
		this.id = idParam;
	}
	
	
	public static void main(String[] args)
	{
		long startTime = System.currentTimeMillis();
		
		
		TestClassInit[] testArray = new TestClassInit[1000];
		TestClassInit tciOld = null;
		for (int i = 0; i < testArray.length; i++)
		{
			TestClassInit tci = new TestClassInit(i);
			tci.pointer = tciOld;
			tciOld = tci;
			
			testArray[i] = tci;
		}
				
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("Total millisec: " + (endTime - startTime));
	}

}
