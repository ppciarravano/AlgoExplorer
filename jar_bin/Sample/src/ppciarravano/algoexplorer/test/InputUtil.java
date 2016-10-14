package ppciarravano.algoexplorer.test;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * Classe InputUtil
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010) 
 */
public class InputUtil
{
	
	public static int getIntegerFromImput(String message, int min, int max)
	{
		int num = 0;
		
		JOptionPane optPane = new JOptionPane(message + " - Insert integer value: ["+min+","+max+"]", JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
	    optPane.setWantsInput(true);
	    //JDialog d = optPane.createDialog(null, "Input Value");
        //d.setVisible(true);
		
		boolean isOk = false;
		while(!isOk)
		{
			try
			{
				JDialog d = optPane.createDialog(null, "Input Value");
		        d.setVisible(true);
		        d.requestFocus();
				Object o = optPane.getInputValue();
		        String strValue = "";
				if (o != null)
					strValue = (String) o;
					
				num = Integer.parseInt(strValue);
				isOk = true;
				d.dispose();
			}
			catch (NumberFormatException nfe)
			{
				isOk = false;
				continue;
			}
			if ((num<min)||(num>max))
			{
				isOk = false;
			}
		}
        
		System.out.println("Value for input ("+message+") is: " + num);
		return num;
	}
	
	/*
	public static int getIntegerFromImput(String message, int min, int max)
	{
		int num = 0;
		boolean isOk = false;
		while(!isOk)
		{
			try
			{
				String str = JOptionPane.showInputDialog(null, message + " - Insert integer value: ["+min+","+max+"]", "Input value", 1);
				num = Integer.parseInt(str);
				isOk = true;
			}
			catch (NumberFormatException nfe)
			{
				isOk = false;
				continue;
			}
			if ((num<min)||(num>max))
			{
				isOk = false;
			}
		}
		System.out.println("Value for input ("+message+") is: " + num);
		return num;
	}
	*/
	
	/*	
	public static void main(String[] args)
	{
		getIntegerFromImput("Value test", 3, 12);
	}
	*/
}
