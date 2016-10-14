package ppciarravano.algoexplorer.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * Classe Led
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class Led extends JComponent
{
	private static Image greenImage = GuiUtility.getImage("/ppciarravano/algoexplorer/resources/green.gif");
	private static Image yellowImage = GuiUtility.getImage("/ppciarravano/algoexplorer/resources/yellow.gif");
	private static Image redImage = GuiUtility.getImage("/ppciarravano/algoexplorer/resources/red.gif");
	
	private final static int BLINK_INTERVAL = 300;
	private enum Status { GREEN, YELLOW, RED }
	private Status status;
	  
	private Timer timer;
	
    public Led()
    {
    	setPreferredSize(new Dimension(30,30));
        //setSize(new Dimension(30,30));
        
        Action updateLedAction = new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (status == Status.GREEN)
				{
					status = Status.YELLOW;
					repaint();
				}
				else
				{
					status = Status.GREEN;
					repaint();
				}
			}
		};
		timer = new Timer(BLINK_INTERVAL, updateLedAction);
		
		setGreen();
    }

    protected void paintComponent(Graphics g)
    {
    	super.paintComponent(g);
    	if (status == Status.GREEN)
		{
    		g.drawImage(greenImage, 0, 0, this);
		}
    	else if (status == Status.YELLOW)
		{
    		g.drawImage(yellowImage, 0, 0, this);
		}
    	else if (status == Status.RED)
		{
    		g.drawImage(redImage, 0, 0, this);
		}    	
    }

    public void setGreen()
    {
    	timer.stop();
    	status = Status.GREEN;
    	repaint();
    }
    
    public void setYellow()
    {
    	timer.stop();
		status = Status.YELLOW;
		repaint();
    }
    
    public void setRed()
    {
    	timer.stop();
		status = Status.RED;
		repaint();
    }
    
    public void setGreenYellowBlink()
    {
    	status = Status.GREEN;
    	timer.start();
	}
    
    /*
    //FOR TEST
    public static void main(String[] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(500, 300);
		JPanel jp = new JPanel(new FlowLayout());
		jf.setContentPane(jp);
		
		final Led led = new Led();
		jp.add(led);
		
		JButton b1 = new JButton();
		b1.setText("Green");
		b1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				led.setGreen();
			}
		});
		jp.add(b1);
				
		JButton b2 = new JButton();
		b2.setText("Blink");
		b2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				led.setGreenYellowBlink();
			}
		});
		jp.add(b2);
		
		JButton b3 = new JButton();
		b3.setText("Yellow");
		b3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				led.setYellow();
			}
		});
		jp.add(b3);
		
		JButton b4 = new JButton();
		b4.setText("Red");
		b4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				led.setRed();
			}
		});
		jp.add(b4);
		
		jf.setVisible(true);
		
	}
	*/
}

