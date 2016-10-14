package ppciarravano.algoexplorer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.bcel.classfile.Field;

import ppciarravano.algoexplorer.ClassDescriptor;
import ppciarravano.algoexplorer.Workspace;
import ppciarravano.algoexplorer.log.Logger;


/**
 * Classe ClassFieldsPanel
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class ClassFieldsPanel extends JPanel
{
	private Workspace wrkspc;
	
	private ClassDescriptor classDescriptor;
	private JCheckBox classSelect;
	private JLabel colorSample;
	private Color backgroundColorCell;
	private Color foregroundColorCell;
	private ClassFieldsTableModel classFieldsTableModel;
	
	public ClassFieldsPanel(Workspace wrkspcParam)
	{
		super(new BorderLayout());
		
		this.wrkspc = wrkspcParam;
		this.wrkspc.setClassFieldsPanel(this);
		  
		setVisible(true);
	}
	
	public void createFromClassDescriptor(ClassDescriptor clsDscr)
	{
		this.classDescriptor = clsDscr;
		Logger.log.debug("createFromClassDescriptor:" + this.classDescriptor.getClassName());
		
		removeAll();
		
		
		//Contenuto pannello NORTH
		
		JPanel panelHeader = new JPanel(new BorderLayout());
		JPanel panelHWest = new JPanel(new FlowLayout());
		JPanel panelHEast = new JPanel(new FlowLayout());
		panelHeader.add(panelHWest, BorderLayout.WEST);
		panelHeader.add(panelHEast, BorderLayout.EAST);
		add(panelHeader, BorderLayout.NORTH);
		
		JLabel className = new JLabel("Class: "+this.classDescriptor.getClassName()+" :");
		panelHWest.add(className);
		
		classSelect = new JCheckBox();
		classSelect.setSelected(this.classDescriptor.isClassSelect());
		panelHWest.add(classSelect);
				
		JButton choseColorButton = new JButton();
		choseColorButton.setText("Scegli colore cella");
		choseColorButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				Color newBackground = javax.swing.JColorChooser.showDialog(
						wrkspc.getMainPanel(),
		                "Choose Background Color",
		                backgroundColorCell);
				Color newForeground = javax.swing.JColorChooser.showDialog(
						wrkspc.getMainPanel(),
		                "Choose Foreground Color",
		                foregroundColorCell);
				
				if (newBackground!=null)
				{
					colorSample.setBackground(newBackground);
					backgroundColorCell = newBackground;
				}
				
				if (newForeground!=null)
				{
					colorSample.setForeground(newForeground);
					foregroundColorCell = newForeground;
				}
			}
		});
		panelHEast.add(choseColorButton);
		
		colorSample = new JLabel(" ESEMPIO COLORE ");
		colorSample.setOpaque(true);
		colorSample.setBackground(this.classDescriptor.getBackgroundColorCell());
		colorSample.setForeground(this.classDescriptor.getForegroundColorCell());
		backgroundColorCell = this.classDescriptor.getBackgroundColorCell();
		foregroundColorCell = this.classDescriptor.getForegroundColorCell();
		colorSample.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panelHEast.add(colorSample);

		
		//Contenuto Pulsanti SOUTH
		
		JPanel panelButtons = new JPanel(new FlowLayout());
		add(panelButtons, BorderLayout.SOUTH);
		
		/*
		//TODO: da implementare successivamente, dato che la funzione e' gia' disponibile dal menu popup contestuale sull'albero delle classi
		JButton clearButton = new JButton();
		clearButton.setText("Clear");
		clearButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				
			}
		});
		panelButtons.add(clearButton);
		*/
		
		JButton resetButton = new JButton();
		resetButton.setText("Cancel");
		resetButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				createFromClassDescriptor(classDescriptor);
			}
		});
		panelButtons.add(resetButton);
		
		JButton applyButton = new JButton();
		applyButton.setText("Apply");
		applyButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				classDescriptor.setClassSelect(classSelect.isSelected());
				classDescriptor.setBackgroundColorCell(backgroundColorCell);
				classDescriptor.setForegroundColorCell(foregroundColorCell);
				classDescriptor.setFieldUseForLabelCell(classFieldsTableModel.getFieldUseForLabelCell());
				classDescriptor.setFieldInspect(classFieldsTableModel.getFieldInspect());
			}
		});
		panelButtons.add(applyButton);
			    
		
		//Contenuto tabella centrale CENTER
		
		classFieldsTableModel = new ClassFieldsTableModel(this.classDescriptor);
	    JTable table = new JTable(classFieldsTableModel);
	    table.setSelectionBackground(Color.WHITE);
	    table.setSelectionForeground(Color.BLACK);
	    
	    TableColumn column = table.getColumnModel().getColumn(2);
	    column.setCellRenderer(new RadioButtonRenderer()); 
        column.setCellEditor(new RadioButtonEditor()); 
	    
        table.getColumnModel().getColumn(0).setPreferredWidth(1000);
        table.getColumnModel().getColumn(0).setMinWidth(70);
        table.getColumnModel().getColumn(0).setMaxWidth(1000);
        table.getColumnModel().getColumn(1).setPreferredWidth(1000);
        table.getColumnModel().getColumn(1).setMinWidth(50);
        table.getColumnModel().getColumn(1).setMaxWidth(1000);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setMinWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.getColumnModel().getColumn(3).setMinWidth(50);
	    //TODO:center colonna 2
         
        JScrollPane scroll = new JScrollPane(table);
        //scroll.setViewportView(table);
        add(scroll, BorderLayout.CENTER);
	    
        updateUI();
	}
	
}

class ClassFieldsTableModel extends AbstractTableModel
{
	private String[] columnNames = { "Field Name", "Type (Access)", "Use for label Cell", "Inspect" };
	private Object[][] data = null;
	
	public ClassFieldsTableModel(ClassDescriptor clsDscr)
	{
		super();
		
		Field[] fields = clsDscr.getFields();
		data = new Object[fields.length][4];
		for (int i = 0; i < fields.length; i++)
		{
			data[i][0] = fields[i].getName();
			data[i][1] = fields[i].getType() + " " + ClassDescriptor.getFieldAccessMode(fields[i]);
			data[i][2] = clsDscr.getFieldUseForLabelCell()[i];
			data[i][3] = clsDscr.getFieldInspect()[i];
		}
	}
	
	public boolean[] getFieldUseForLabelCell()
	{
		boolean[] result = new boolean[data.length];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = (Boolean)data[i][2];
		}
		return result;
	}
	
	public boolean[] getFieldInspect()
	{
		boolean[] result = new boolean[data.length];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = (Boolean)data[i][3];
		}
		return result;
	}
		
	/*	// For Test
	private Object[][] data = {
			{"aaa","bbb",  new Boolean(false), new Boolean(false)},
			{"aaa","bbb", new Boolean(false), new Boolean(true)},
			{"aaa","bbb",  new Boolean(true), new Boolean(false)}
			};
	*/
	
	public int getColumnCount()
	{
		return columnNames.length;
	}

	public int getRowCount()
	{
		return data.length;
	}

	public String getColumnName(int col)
	{
		return columnNames[col];
	}

	public Object getValueAt(int row, int col)
	{
		return data[row][col];
	}

	/*
	 * JTable uses this method to determine the default renderer/ editor for
	 * each cell. If we didn't implement this method, then the last column would
	 * contain text ("true"/"false"), rather than a check box.
	 */
	public Class getColumnClass(int c)
	{
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col)
	{
		if (col <= 1)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public void setValueAt(Object value, int row, int col)
	{
		//Logger.log.debug("value:"+ value + " row:"+row+ " col:"+col);
		//Patch per gestire group radiobutton
		if (col==2)
		{
			for (int i = 0; i < getRowCount(); i++)
			{
				data[i][2] = false;
				fireTableCellUpdated(i, 2);
			}
		}
		
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

}

class RadioButtonEditor extends DefaultCellEditor implements ItemListener
{
	public JRadioButton btn = new JRadioButton();

	public RadioButtonEditor()
	{
		super(new JCheckBox());
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{

		if (value == null)
			return null;
		btn.addItemListener(this);
		if (((Boolean) value).booleanValue())
			btn.setSelected(true);
		else
			btn.setSelected(false);

		return btn;
	}

	public Object getCellEditorValue()
	{
		if (btn.isSelected() == true)
			return new Boolean(true);
		else
			return new Boolean(false);
	}

	public void itemStateChanged(ItemEvent e)
	{
		super.fireEditingStopped();
	}
}

class RadioButtonRenderer implements TableCellRenderer //extends DefaultTableCellRenderer 
{
	public JRadioButton btn = new JRadioButton();

	//public RadioButtonRenderer()
	//{
	//	//super();
	//	setHorizontalAlignment(SwingConstants.CENTER);
    //    setOpaque(true);
	//}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		if (value == null)
			return null;

		if (((Boolean) value).booleanValue())
			btn.setSelected(true);
		else
			btn.setSelected(false);

		if (isSelected)
		{
			btn.setForeground(table.getSelectionForeground());
			btn.setBackground(table.getSelectionBackground());
		}
		else
		{
			btn.setForeground(table.getForeground());
			btn.setBackground(table.getBackground());
		}
		return btn;
	}
}
