package ppciarravano.algoexplorer;

/**
 * Classe SourceLine
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class SourceLine
{
	private int lineNumber;
	private boolean lineCode;
	private boolean breakpoint;
	private String sourceCode;
	
	public SourceLine(int lineNumber, boolean lineCode, String sourceCode)
	{
		this.lineNumber = lineNumber;
		this.lineCode = lineCode;
		this.sourceCode = sourceCode;
	}

	public boolean isBreakpoint()
	{
		return breakpoint;
	}

	public void setBreakpoint(boolean breakpoint)
	{
		this.breakpoint = breakpoint;
	}

	public int getLineNumber()
	{
		return lineNumber;
	}

	public boolean isLineCode()
	{
		return lineCode;
	}

	public String getSourceCode()
	{
		return sourceCode;
	}
	
	public void setSourceCode(String sourceCode)
	{
		this.sourceCode = sourceCode;
	}

	
}
