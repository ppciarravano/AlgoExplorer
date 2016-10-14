package ppciarravano.algoexplorer;

import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ppciarravano.algoexplorer.gui.GuiUtility;
import ppciarravano.algoexplorer.gui.MainMenuBar;
import ppciarravano.algoexplorer.gui.MainPanel;
import ppciarravano.algoexplorer.log.Logger;
import ppciarravano.algoexplorer.log.LoggerWindow;
import ppciarravano.algoexplorer.util.ConstantManager;


/**
 * Classe main per AlgoExplorer.
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010) 
 */
public class AlgoExplorer
{

	public AlgoExplorer()
	{

		/*
		 * Per Caricare la SplashScreen usare i seguenti metodi:
		 * 
		 * Manifest-Version: 1.0
 		 * Main-Class: AlgoExplorer
 		 * SplashScreen-Image: SplashScreen.png
 		 * 
 		 * oppure
 		 * 
 		 * java -splash:SplashScreen.png mainclass
 		 */
		final SplashScreen splash = SplashScreen.getSplashScreen();
		if (splash == null)
		{
			System.out.println("SplashScreen.getSplashScreen() returned null!");
		}
		else
		{
			Graphics2D g = splash.createGraphics();
			if (g == null)
			{
				System.out.println("Graphics2D is null!");
			}
			else
			{
				try
				{
					Thread.sleep(ConstantManager.SPLASH_SCREEN_TIME);
				}
				catch (InterruptedException e) {}
			}
		}
				
		
		try
		{
			//Switch off D3D because of Sun XOR painting bug
			//See http://www.jgraph.com/forum/viewtopic.php?t=4066
			System.setProperty("sun.java2d.d3d", "false");
			
			//Set LookAndFeel
			GuiUtility.setLookAndFeel();
			
			//Init LoggerWindow
			LoggerWindow.init();
			
			//Init Workspace
			final Workspace wrkspc = new Workspace();
			
			//Construct MainPanel
			JPanel mainPanel = new MainPanel(wrkspc);
						
			//Construct Frame
			JFrame frame = new JFrame(ConstantManager.APP_NAME);
			
			//frame.setBackground(Color.lightGray);
			
			//Set Close Operation to Exit
			//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.addWindowListener(new WindowAdapter() {
			    public void windowClosing(WindowEvent e) {
			    	wrkspc.terminate();
			    }
			});			
			
			//Set icon
			frame.setIconImage(GuiUtility.getApplicationIcon());
			//Set Default Size
			frame.setSize(ConstantManager.INITIAL_APPLICATION_WIDTH, ConstantManager.INITIAL_APPLICATION_HEIGHT);
			frame.setResizable(true);
			
			//Set Menu
			frame.setJMenuBar(new MainMenuBar(wrkspc));
			//Set ContentPane
			frame.setContentPane(mainPanel);
			
			//Center frame in screen
			GuiUtility.center(frame);
			
			//Show Frame
			frame.setVisible(true);
			frame.requestFocus();
			
			
		}
		catch (Exception e)
		{
			Logger.log.error("AlgoExplorer Exception:" + Logger.exceptionToString(e));
		}
				
		
	}
	
	public static void main(String[] args)
	{
		new AlgoExplorer();	
	}

}
