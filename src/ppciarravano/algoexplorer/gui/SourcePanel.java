package ppciarravano.algoexplorer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Formatter;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import ppciarravano.algoexplorer.SourceDescriptor;
import ppciarravano.algoexplorer.SourceLine;
import ppciarravano.algoexplorer.Workspace;
import ppciarravano.algoexplorer.log.Logger;


/**
 * Classe SourcePanel
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class SourcePanel extends JPanel
{
	private Workspace wrkspc;
	
	private JList sourceList;
	private JLabel sourcePathNameLabel;
	private JLabel sourceNotAvailableLabel;
	
	public SourcePanel(Workspace wrkspc)
	{
		super(new BorderLayout());
		
		this.wrkspc = wrkspc;
		this.wrkspc.setSourcePanel(this);
		
		
		//Contenuto pannello NORTH
		
		JPanel panelHeader = new JPanel(new BorderLayout());
		add(panelHeader, BorderLayout.NORTH, 0);
		
		sourcePathNameLabel = new JLabel(" ");
		panelHeader.add(sourcePathNameLabel, BorderLayout.WEST);
		sourceNotAvailableLabel = new JLabel("");
		sourceNotAvailableLabel.setForeground(Color.decode("0xBB0000"));
		panelHeader.add(sourceNotAvailableLabel, BorderLayout.EAST);
		
		
		//Contenuto pannello CENTER
		
		setEmpyList();
		
		
		/*
		//OLD: using jTextArea
		//Creo JTextArea
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(Color.WHITE);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textArea.setText("");
		textArea.setBorder(BorderFactory.createLineBorder(Color.BLUE));
						
		//creo uno JScrollPane con dentro la textArea e lo aggiungo
		jScrollPane = new JScrollPane();
		jScrollPane.setViewportView(textArea);
		//add(jScrollPane, BorderLayout.SOUTH);
		setVisible(true);
		*/
	}
	
	/*
	//OLD: using jTextArea
	public void setSourceText(String sourceText)
	{
		textArea.setText(sourceText);
		textArea.select(0,0);
		textArea.requestFocus();
	}
	*/
	
	private void setEmpyList()
	{
		sourceList = new JList();
		sourceList.setFont(new Font("monospaced", Font.PLAIN, 12));
		add(new JScrollPane(sourceList), BorderLayout.CENTER, 1);
	}
	
	/*
	 * Ritorna true se ha avuto successo il caricamente del sorgente
	 */
	public boolean setSourceLines(SourceDescriptor srcDscr, String sourcepath)
	{
		Logger.log.debug("SourcePanel-->setSourceLines");
		setSourcePathNameLabel(srcDscr.getSourcePathName());
		remove(1);
		if (!sourcepath.equals(""))
		{
			//Leggo il file sorgente
			List<SourceLine> sourceLines = srcDscr.loadSource(sourcepath);
			if (sourceLines!=null)
			{
				SourceLineListModel sourceLineListModel = new SourceLineListModel(srcDscr, sourceLines);
				sourceList = new JList(sourceLineListModel);
				sourceList.setFont(new Font("monospaced", Font.PLAIN, 12));
				sourceList.setCellRenderer(new SourceLineRenderer());
				sourceList.setPrototypeCellValue(sourceLineListModel.getPrototypeCellValue());
				sourceList.addMouseListener(new SourceLineMouseListener(srcDscr, sourceLines));
				
				add(new JScrollPane(sourceList), BorderLayout.CENTER, 1);
				validate();
				
				setSourceNotAvailableLabel("");
				return true;
			}
			else
			{
				setEmpyList();
				validate();
				
				setSourceNotAvailableLabel("SORGENTE NON DISPONIBILE");
				return false;
			}
		}
		else
		{
			setEmpyList();
			validate();
			
			setSourceNotAvailableLabel("SORGENTE NON DISPONIBILE");
			return false;
		}
	}
	
	private void setSourcePathNameLabel(String sourcePathName)
	{
		sourcePathNameLabel.setText("  Source: " + sourcePathName);
	}
	
	private void setSourceNotAvailableLabel(String sourceNotAvailable)
	{
		sourceNotAvailableLabel.setText(sourceNotAvailable);
	}
}

class SourceLineListModel extends AbstractListModel
{
	private List<SourceLine> sourceLines;
	private int size;
	
	private int maxCharForLine;
	
	public SourceLineListModel(SourceDescriptor srcDscr, List<SourceLine> sourceLines)
	{
		super();
		this.sourceLines = sourceLines;
		this.size = sourceLines.size();
		
		int charForNumRow = 0;
		int maxForChar = 1;
		for (int i = 1; i<8; i++ )
		{
			maxForChar *= 10;
			if ((this.size+1)<maxForChar)
			{
				charForNumRow = 1*i;
				break;
			}
		}
		String formatterString = "%"+charForNumRow+"d";
		//Logger.log.debug("formatterString :" +formatterString);
		
		int numRow = 0;
		maxCharForLine = 0;
		for (SourceLine sourceLine : sourceLines)
		{
			numRow++;
			sourceLine.setSourceCode(((new Formatter()).format(formatterString,numRow))+ " " + expandTabs(sourceLine.getSourceCode()));
			if (sourceLine.getSourceCode().length()>maxCharForLine)
			{
				maxCharForLine = sourceLine.getSourceCode().length();
			}
			
			//Setto eventuali breakpoint presenti new SourceDescriptor
			if (srcDscr.getBreakpoint().contains(numRow))
			{
				sourceLine.setBreakpoint(true);
			}
		}		
	}
	
	public Object getElementAt(int index)
	{
		return sourceLines.get(index);
	}
	
	public int getSize()
	{
		return this.size;
	}
	
	public SourceLine getPrototypeCellValue()
	{
		StringBuffer sb = new StringBuffer(maxCharForLine);
		for (int i = 0; i <maxCharForLine; i++)
		{
			sb.append(".");
		}
		return new SourceLine(0, true, sb.toString());
	}
		
	private String expandTabs(String s)
	{
		int col = 0;
		int len = s.length();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++)
		{
			char c = s.charAt(i);
			sb.append(c);
			if (c == '\t')
			{
				int pad = (8 - (col % 8));
				for (int j = 0; j < pad; j++)
				{
					sb.append(' ');
				}
				col += pad;
			}
			else
			{
				col++;
			}
		}
		return sb.toString();
	}
	
	/*
	@Override
	public void fireContentsChanged(Object source, int index0, int index1)
	{
		super.fireContentsChanged(source, index0, index1);
	}
	*/
}

class SourceLineRenderer extends DefaultListCellRenderer 
{
	
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		SourceLine sourceLine = (SourceLine)value;

		setText(sourceLine.getSourceCode());
							
		if (sourceLine.isBreakpoint())
		{
			setIcon(GuiUtility.getBreakpointIcon());
		}
		else if (sourceLine.isLineCode())
		{
			setIcon(GuiUtility.getLineCodeIcon());
		}		
		else
		{
			setIcon(GuiUtility.getBlankIcon());
		}
				
		return this;
	}
	
	public Dimension getPreferredSize()
	{
        Dimension dim = super.getPreferredSize();
        return new Dimension(dim.width, dim.height-4);
    }
}

class SourceLineMouseListener extends MouseAdapter implements MouseListener
{
	private SourceDescriptor sourceDescriptor;
	private List<SourceLine> sourceLines;
	//private SourceLineListModel sourceLineListModel;
	
	public SourceLineMouseListener(SourceDescriptor srcDscr, List<SourceLine> sourceLines)
	{
		super();
		this.sourceDescriptor = srcDscr;
		this.sourceLines = sourceLines;
		//this.sourceLineListModel = sourceLineListModel;
	}
	
	public void mousePressed(MouseEvent e)
	{
		if (e.isPopupTrigger())
		{
			showPopupMenu((Component) e.getSource(), e.getX(), e.getY());
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		if (e.isPopupTrigger())
		{
			showPopupMenu((Component) e.getSource(), e.getX(), e.getY());
		}
	}

	private void showPopupMenu(Component invoker, int x, int y)
	{
		JList list = (JList) invoker;
		int ln = list.getSelectedIndex() + 1;
		final SourceLine sourceLine = (SourceLine)list.getSelectedValue();
		JPopupMenu popup = new JPopupMenu();

		if (sourceLine == null)
		{
			popup.add(new JMenuItem("Select a code line"));
		}
		else if (sourceLine.isLineCode())
		{
			if (sourceLine.isBreakpoint())
			{
				JMenuItem item = new JMenuItem("Clear Explorer Breakpoint");
				item.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						sourceLine.setBreakpoint(false);
						sourceDescriptor.removeBreakpoint(sourceLine.getLineNumber());
						//sourceLineListModel.fireContentsChanged(sourceLineListModel, sourceLine.getLineNumber()-1, sourceLine.getLineNumber()-1);
					}
				});
				popup.add(item);
			}
			else
			{
				JMenuItem item = new JMenuItem("Set Explorer Breakpoint");
				item.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						sourceLine.setBreakpoint(true);
						sourceDescriptor.addBreakpoint(sourceLine.getLineNumber());
						//sourceLineListModel.fireContentsChanged(sourceLineListModel, sourceLine.getLineNumber()-1, sourceLine.getLineNumber()-1);
					}
				});
				popup.add(item);
			}
		}
		else
		{
			popup.add(new JMenuItem("Not a esecution line"));
		}

		popup.show(invoker, x + popup.getWidth() / 2, y + popup.getHeight() / 2);
	}

		
}

