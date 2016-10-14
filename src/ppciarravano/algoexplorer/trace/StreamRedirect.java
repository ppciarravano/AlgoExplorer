package ppciarravano.algoexplorer.trace;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ppciarravano.algoexplorer.log.Logger;


/**
 * Classe StreamRedirect
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class StreamRedirect extends Thread
{

	private final InputStream in;
	private final OutputStream out;
	
	private static final int BUFFER_SIZE = 2048;

	/**
	 * Set up for copy.
	 * 
	 * @param name
	 *            Name of the thread
	 * @param in
	 *            Stream to copy from
	 * @param out
	 *            Stream to copy to
	 */
	StreamRedirect(String name, InputStream in, OutputStream out)
	{
		super(name);
		this.in = in;
		this.out = out;
		setPriority(Thread.MAX_PRIORITY - 1);
		
	}

	/**
	 * Copy.
	 */
	public void run()
	{
		try
		{
			byte[] cbuf = new byte[BUFFER_SIZE];
			int count;
			while ((count = in.read(cbuf, 0, BUFFER_SIZE)) >= 0)
			{
				//Logger.log.debug("StreamRedirect Main Out:" + new String(cbuf, 0, count));
				out.write(cbuf, 0, count);
			}
			out.flush();

		}
		catch (IOException exc)
		{
			Logger.log.error("Child I/O Transfer - " + Logger.exceptionToString(exc));
		}
	}
}
