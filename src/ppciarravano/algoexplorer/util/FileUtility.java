package ppciarravano.algoexplorer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import ppciarravano.algoexplorer.log.Logger;

/**
 * Classe FileUtility
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class FileUtility
{
	
	public static String readTextFile(String absolutePath)
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			FileReader fr = new FileReader(absolutePath);
			BufferedReader reader = new BufferedReader(fr);
			String line;
		    while ((line = reader.readLine()) != null)
		    {
		    	sb.append(line).append("\r\n");
		    }
		    fr.close();
		    reader.close();
		}
		catch(FileNotFoundException fnfe)
		{
			Logger.log.info("FileNotFoundException" + Logger.exceptionToString(fnfe));
		}
		catch (IOException ioe)
		{
			Logger.log.info("IOException" + Logger.exceptionToString(ioe));
		}
	    return sb.toString();
	}
		
	public static List<String> readTextFileInLines(String absolutePath)
	{
		Vector<String> result = new Vector<String>(100); 
		try
		{
			FileReader fr = new FileReader(absolutePath);
			BufferedReader reader = new BufferedReader(fr);
			String line;
		    while ((line = reader.readLine()) != null)
		    {
		    	result.addElement(line);
		    }
		    fr.close();
		    reader.close();
		}
		catch(FileNotFoundException fnfe)
		{
			Logger.log.info("FileNotFoundException" + Logger.exceptionToString(fnfe));
		}
		catch (IOException ioe)
		{
			Logger.log.info("IOException" + Logger.exceptionToString(ioe));
		}
	    return result;
	}
	
	
	public static boolean writeTextFile(String content, String absolutePath)
	{
		boolean result = false;
		try
		{
			File outputFile = new File(absolutePath);
	        FileWriter out = new FileWriter(outputFile);
	        out.write(content);
	        out.close();
	        result = true;
		}
		catch (IOException ioe)
		{
			Logger.log.info("IOException" + Logger.exceptionToString(ioe));
		}
	    return result;
	}
	
	
}
