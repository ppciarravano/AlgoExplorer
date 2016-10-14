package ppciarravano.algoexplorer.gui;

import java.io.OutputStream;

import javax.swing.JTextArea;

/**
 * Classe JTextAreaOutputStream
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class JTextAreaOutputStream extends OutputStream
{

	JTextArea textArea;

	public JTextAreaOutputStream(JTextArea textAreaParam)
	{
		super();
		textArea = textAreaParam;
	}

	public void write(int i)
	{
		/*
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textArea.append(Character.toString((char) i));
			}
		});
		*/
		textArea.append(Character.toString((char) i));
	}

	public void write(char[] buf, int off, int len)
	{
		String s = new String(buf, off, len);
		/*
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textArea.append(s);
			}
		});
		*/
		textArea.append(s);
		
		//Per spostare la scrool bar automaticamente: non funziona
		//textArea.select(textArea.getCaretPosition(),textArea.getCaretPosition());
		//textArea.requestFocus();
	}

}
