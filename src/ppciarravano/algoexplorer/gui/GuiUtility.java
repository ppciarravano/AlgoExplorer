package ppciarravano.algoexplorer.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import ppciarravano.algoexplorer.log.Logger;
import ppciarravano.algoexplorer.util.ConstantManager;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertBlue;


/**
 * Classe di utilita' per gestione interfaccia grafica
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class GuiUtility
{
	private GuiUtility()
	{		
	}
	
	private static Image applicationIcon = null;
	private static Icon lineCodeIcon = null;
	private static Icon breakpointIcon = null;
	private static Icon blankIcon = null;
	
	public static Icon getBlankIcon()
	{
		if (blankIcon==null)
		{
			blankIcon = new ImageIcon(getImage("/ppciarravano/algoexplorer/resources/blank.gif"));
		}
		
		return blankIcon;
	}
	
	public static Icon getBreakpointIcon()
	{
		if (breakpointIcon==null)
		{
			breakpointIcon = new ImageIcon(getImage("/ppciarravano/algoexplorer/resources/breakpoint.gif"));
		}
		
		return breakpointIcon;
	}
	
	public static Icon getLineCodeIcon()
	{
		if (lineCodeIcon==null)
		{
			lineCodeIcon = new ImageIcon(getImage("/ppciarravano/algoexplorer/resources/linecode.gif"));
		}
		
		return lineCodeIcon;
	}
	

	public static Image getApplicationIcon()
	{
		if (applicationIcon==null)
		{
			//Logger.log.debug("Load using getImage");
			applicationIcon = getImage(ConstantManager.APPLICATION_ICON);
		}
		
		return applicationIcon;
	}
	
	
	public static Image getImage(String pathFile)
	{
		InputStream ioStream = GuiUtility.class.getResourceAsStream(pathFile);
		Image result = null;
		try
		{
			result = ImageIO.read(ioStream);
		}
		catch (IOException ioe)
		{
			Logger.log.error("getImage Exception:\n" + Logger.exceptionToString(ioe));
		}
		return result;
	}
	
	
	public static void center(Window wnd)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = wnd.getSize();
		wnd.setLocation(screenSize.width / 2 - (frameSize.width / 2), screenSize.height / 2 - (frameSize.height / 2));
	}
	
	
	public static void setLookAndFeel()
	{
		if (ConstantManager.NOT_STANDARD_LOOK_AND_FEEL)
		{
			PlasticLookAndFeel.setPlasticTheme(new DesertBlue());
			try
			{
				//UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
				//UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
				UIManager.setLookAndFeel(new PlasticLookAndFeel());
			}
			catch (Exception e)
			{
				Logger.log.error("setLookAndFeel Exception:\n" + Logger.exceptionToString(e));
			}
			
		}
	}
	
	/*
	 * 	//TODO per creazione Jcolorchoser semplificato
	 	JColorChooser chooser = new JColorChooser();
		AbstractColorChooserPanel[] oldPanels = chooser.getChooserPanels();
		chooser.removeChooserPanel(oldPanels[0]);
		chooser.removeChooserPanel(oldPanels[2]);
	    chooser.setPreviewPanel(new JPanel());
	 * 
	 */
	
}
