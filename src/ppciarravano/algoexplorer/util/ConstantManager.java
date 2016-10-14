package ppciarravano.algoexplorer.util;

import java.util.Properties;

import ppciarravano.algoexplorer.log.Logger;


/**
 * Classe per la gestione delle costanti. Legge le costanti in un file di
 * properties e le popola in modo finale, attraverso il codice presente in un
 * blocco statico.
 * 
 * <br>
 * <br>
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author Pier Paolo Ciarravano
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class ConstantManager
{

	// Costanti non caricate da /constant.properties
	public static final String VERSION = "AlgoExplorer 0.9 (11/01/2010)";
	public static final String APP_NAME = "AlgoExplorer 0.9";

	private static Properties constantProps = null;

	// Costanti dell'applicazione caricate dal file /constant.properties
	public static final String APPLICATION_ICON;
	public static final boolean NOT_STANDARD_LOOK_AND_FEEL;
	public static final String CLASSPATH_TREE_ROOT_NAME;
	public static final String STANDARD_BACKGROUND_COLOR_CELL;
	public static final String STANDARD_FOREGROUND_COLOR_CELL;
	public static final int INITIAL_APPLICATION_WIDTH;
	public static final int INITIAL_APPLICATION_HEIGHT;
	public static final int SPLASH_SCREEN_TIME;
	public static final String[] CLASS_EXCLUSION_FILTER;
	public static final int MAX_REFERENCE_RECURSION_LEVEL;
	public static final boolean DISABLE_GARBAGE_COLLECTION;
	public static final int FRAME_INTERVAL_PLAY;
	public static final String DEFAULT_XML_FILE_NAME;
	
	static
	{

		// Caricamento dei valori da file
		try
		{
			Logger.log.info("Loading Constants...");
			constantProps = PropFileLoader.loadProperties("/algoexplorer.properties");
		}
		catch (Exception ie)
		{
			Logger.log.error("Impossibile procedere al caricamento delle proprieta'", ie);
		}

		APPLICATION_ICON = constantProps.getProperty("APPLICATION_ICON");
		NOT_STANDARD_LOOK_AND_FEEL = Boolean.parseBoolean(constantProps.getProperty("NOT_STANDARD_LOOK_AND_FEEL"));
		CLASSPATH_TREE_ROOT_NAME = constantProps.getProperty("CLASSPATH_TREE_ROOT_NAME");
		STANDARD_BACKGROUND_COLOR_CELL = constantProps.getProperty("STANDARD_BACKGROUND_COLOR_CELL");
		STANDARD_FOREGROUND_COLOR_CELL = constantProps.getProperty("STANDARD_FOREGROUND_COLOR_CELL");
		INITIAL_APPLICATION_WIDTH = Integer.parseInt(constantProps.getProperty("INITIAL_APPLICATION_WIDTH"));
		INITIAL_APPLICATION_HEIGHT = Integer.parseInt(constantProps.getProperty("INITIAL_APPLICATION_HEIGHT"));
		SPLASH_SCREEN_TIME = Integer.parseInt(constantProps.getProperty("SPLASH_SCREEN_TIME"));
		CLASS_EXCLUSION_FILTER = constantProps.getProperty("CLASS_EXCLUSION_FILTER").split(","); 
		MAX_REFERENCE_RECURSION_LEVEL = Integer.parseInt(constantProps.getProperty("MAX_REFERENCE_RECURSION_LEVEL"));
		DISABLE_GARBAGE_COLLECTION = Boolean.parseBoolean(constantProps.getProperty("DISABLE_GARBAGE_COLLECTION"));
		FRAME_INTERVAL_PLAY = Integer.parseInt(constantProps.getProperty("FRAME_INTERVAL_PLAY"));
		// LONG_CONST = Long.parseLong(constantProps.getProperty("LONG_CONST"));
		DEFAULT_XML_FILE_NAME = constantProps.getProperty("DEFAULT_XML_FILE_NAME");

	}

}
