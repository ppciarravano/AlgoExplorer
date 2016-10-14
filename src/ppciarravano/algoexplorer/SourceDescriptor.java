package ppciarravano.algoexplorer;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import ppciarravano.algoexplorer.log.Logger;
import ppciarravano.algoexplorer.util.FileUtility;


/**
 * Classe SourceDescriptor
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class SourceDescriptor
{
	private String sourceFileName = null;
	private String packageName = null;
	private String sourcePathName = null;
	private Set<ClassDescriptor> classesInSource = null;
	private Set<Integer> breakpoints = null;
	
	public SourceDescriptor(String sourceFileName, String packageName, String sourcePathName)
	{
		this.sourceFileName = sourceFileName;
		this.packageName = packageName;
		this.sourcePathName = sourcePathName;
		classesInSource = new HashSet<ClassDescriptor>();
		breakpoints = new HashSet<Integer>();
		Logger.log.debug("SourceDescriptor: sourcePathName:" + sourcePathName + " sourceFileName:" + sourceFileName + " packageName:" + packageName);
				
	}
	
	public int[] getLineNumbers()
	{
		//Ciclo su tutte le classesInSource aggiungendo classesInSource.lineNumbers
		Vector<Integer> lineNumbersVector = new Vector<Integer>(100);
		for (ClassDescriptor clsDscr : classesInSource)
		{
			int[] classLineNumber = clsDscr.getLineNumbers();
			for (int i = 0; i < classLineNumber.length; i++)
			{
				//If for Patch package line code setting:
				// la prima linea di codice contentente il package e' erroneamente riportata da bcel come linea di codice eseguibile!
				if (classLineNumber[i]!=1)
				{
					lineNumbersVector.addElement(classLineNumber[i]);
				}
			}
		}
		
		//Populate result array 
		int[] result = new int[lineNumbersVector.size()];
		for (int i = 0; i < lineNumbersVector.size(); i++)
		{
			result[i] = lineNumbersVector.elementAt(i);
		}
		
		return result;
	}
	
	public Set<Integer> getLineNumbersSet()
	{
		Set<Integer> result = new HashSet<Integer>();
		int[] lineNumbers = getLineNumbers();
		for (int i = 0; i < lineNumbers.length; i++)
		{
			result.add(lineNumbers[i]);
		}
		
		return result;
	}
	
	public List<SourceLine> loadSource(String sourcepath)
	{
		String sourcepathFile = sourcepath;
		if (!sourcepathFile.endsWith(String.valueOf(File.separatorChar)))
		{
			sourcepathFile += File.separatorChar;
		}
		sourcepathFile += getSourcePathName();
		
		//Load Class from file name sourcepathFile
		Logger.log.debug("Loading java source from path: " + sourcepathFile);

		List<String> stringLines = FileUtility.readTextFileInLines(sourcepathFile);
		if (stringLines==null)
		{
			return null;
		}
		
		//get LineNumbers
		Set<Integer> lineNumbers = getLineNumbersSet();
		
		Vector<SourceLine> result = new Vector<SourceLine>(100);
		for (int i = 0; i < stringLines.size(); i++)
		{
			boolean isLineCode = lineNumbers.contains(new Integer(i+1));
			SourceLine sourceLine = new SourceLine(i+1, isLineCode, stringLines.get(i));
			result.addElement(sourceLine);
		}
		
		return result;
	}
	
	
	public void addClass(ClassDescriptor clsDscr)
	{
		classesInSource.add(clsDscr);
	}
	
	public void clearBreakpoint()
	{
		breakpoints.clear();
	}
	
	public Set<Integer> getBreakpoint()
	{
		return breakpoints;
	}
	
	public void addBreakpoint(int lineNumber)
	{
		breakpoints.add(lineNumber);
	}
	
	public void removeBreakpoint(int lineNumber)
	{
		breakpoints.remove(lineNumber);
	}

	/*
	public void setBreakpoint(Set<Integer> breakpoint)
	{
		this.breakpoint = breakpoint;
	}
	*/

	public String getSourceFileName()
	{
		return sourceFileName;
	}

	public String getPackageName()
	{
		return packageName;
	}

	public Set<ClassDescriptor> getClassesInSource()
	{
		return classesInSource;
	}

	public String getSourcePathName()
	{
		return sourcePathName;
	}
	
	
}
