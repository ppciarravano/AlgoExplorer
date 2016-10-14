package ppciarravano.algoexplorer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ppciarravano.algoexplorer.CommandManager;
import ppciarravano.algoexplorer.Workspace;
import ppciarravano.algoexplorer.CommandManager.Command;
import ppciarravano.algoexplorer.util.ConstantManager;


/**
 * Classe ExplorerToolBar
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010) 
 */
public class ExplorerToolBar extends JToolBar
{
	private Workspace wrkspc;
	CommandManager commandManager;
	
	private Led runLed;
	
	public ExplorerToolBar(Workspace wrkspc)
	{
		this.wrkspc = wrkspc;
		this.wrkspc.setExporerToolBar(this);
		this.commandManager = this.wrkspc.getCommandManager();
		
		addSeparator();
		addTool("Zoom 1:1", "Zoom 1:1", Command.ZOOM_RESET);
		addTool("Zoom In", "Zoom In", Command.ZOOM_IN);
		addTool("Zoom Out", "Zoom Out", Command.ZOOM_OUT);
		
		addSeparator();
		addTool("PLAY", "Play until breakpoint or exit", Command.PLAY);
		addTool("CONTINUOUS PLAY", "Continuous Play", Command.CONTINUOUS_PLAY);
		addTool("SUSPEND", "Suspend at next breakpoint", Command.SUSPEND);
		addTool("EXIT", "Terminate Explorer Virtual Machine", Command.EXIT_VM);
		runLed = new Led();
		runLed.setGreen();
		add(runLed);
		
		//Slider Panel
		final JLabel timeSliderLabel = new JLabel(Math.round(ConstantManager.FRAME_INTERVAL_PLAY/100)/10d + " ms");
		final JSlider timeSlider = new JSlider(JSlider.HORIZONTAL, 0, 10000, ConstantManager.FRAME_INTERVAL_PLAY);
		timeSlider.setValue(ConstantManager.FRAME_INTERVAL_PLAY);
		//timeSlider.setPreferredSize(new Dimension(150, 30));
		//timeSlider.setSize(new Dimension(150, 30));
		timeSlider.setPaintTicks(false);
		timeSlider.setMajorTickSpacing(1000);
		timeSlider.setMinorTickSpacing(250);
		timeSlider.addChangeListener(new ChangeListener()
		//timeSlider.setBorder(new TitledBorder("Frame interval"));
		{
			public void stateChanged(ChangeEvent event)
			{
				commandManager.setIntervalPlay(timeSlider.getValue());
				double valForString =  Math.round(timeSlider.getValue()/100)/10d;
				timeSliderLabel.setText(valForString + " ms");
			}
		});
		addSeparator();
		add(new JLabel("Frame interval:"));
		add(timeSlider);
		add(timeSliderLabel);
		
		addSeparator();
		addTool("CAPTURE", "Capture shot for export", Command.CAPTURE_SHOT);
	}
	
	public Led getRunLed()
	{
		return this.runLed;
	}
	
	private void addTool(String labelText, String toolTip, final Command cmd)
	{
		JButton button = new JButton(labelText);
		button.setToolTipText(toolTip);
		
		button.setEnabled(true);
		
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				commandManager.executeCommand(cmd);
			}
		});
		this.add(button);
	}
}
