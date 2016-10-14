package ppciarravano.algoexplorer.log;

/**
 * Classe per referenziare il log di Log4j.
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class Logger {

	public static final org.apache.log4j.Logger log;
	
	static
    {
        //System.out.println("Init Logger");
        log = org.apache.log4j.Logger.getRootLogger();
        //log = org.apache.log4j.Logger.getLogger("application");
    
    }
	
	public static String exceptionToString(Throwable ex)
	{
	    java.io.CharArrayWriter cw = new java.io.CharArrayWriter();
	    java.io.PrintWriter pw = new java.io.PrintWriter(cw, true);
	    ex.printStackTrace(pw);
	    return "EXCEPTION --- MESSAGE: " + ex.getMessage() + "\n StackTrace:\n" + cw.toString();
	}
	
	private Logger()
	{
		
	}
}
