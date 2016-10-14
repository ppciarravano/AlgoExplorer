package ppciarravano.algoexplorer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import ppciarravano.algoexplorer.Workspace;
import ppciarravano.algoexplorer.log.Logger;
import ppciarravano.algoexplorer.trace.InstanceDescriptor;


/**
 * Classe InspectPanel
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class InspectPanel extends JPanel
{
	private Workspace wrkspc;
	
	private InstanceDescriptor instanceDescriptor;
				
	public InspectPanel(Workspace wrkspcParam)
	{
		super(new BorderLayout());
		
		this.wrkspc = wrkspcParam;
		this.wrkspc.setInspectPanel(this);
		
		add(new JLabel("INSPECT PANEL"), BorderLayout.NORTH);
		
		setVisible(true);
	}
	
	public void createFromInstanceDescriptor(InstanceDescriptor instDscr)
	{
		this.instanceDescriptor = instDscr;
		Logger.log.debug("createFromInstanceDescriptor:" + this.instanceDescriptor.getClassDescriptor().getClassName() + " Label:"+this.instanceDescriptor.getLabel());
		
		removeAll();
		
		
		//Contenuto pannello NORTH
		
		JPanel panelHeader = new JPanel(new BorderLayout());
		JPanel panelHWest = new JPanel(new FlowLayout());
		panelHeader.add(panelHWest, BorderLayout.WEST);
		add(panelHeader, BorderLayout.NORTH);
		
		JLabel className = new JLabel("Class: "+this.instanceDescriptor.getClassDescriptor().getClassName()+ " : " + this.instanceDescriptor.getLabel());
		panelHWest.add(className);
		    
		
		//Contenuto tabella centrale CENTER
		
		InstanceFieldsTableModel instanceFieldsTableModel = new InstanceFieldsTableModel(this.instanceDescriptor);
	    JTable table = new JTable(instanceFieldsTableModel);
	    table.setSelectionBackground(Color.WHITE);
	    table.setSelectionForeground(Color.BLACK);
	    /*
	    table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(0).setMinWidth(10);
        table.getColumnModel().getColumn(0).setMaxWidth(1000);
        table.getColumnModel().getColumn(1).setPreferredWidth(1000);
        table.getColumnModel().getColumn(1).setMinWidth(10);
        table.getColumnModel().getColumn(1).setMaxWidth(1000);
        */      
        JScrollPane scroll = new JScrollPane(table);
        //scroll.setViewportView(table);
        add(scroll, BorderLayout.CENTER);
	    
        updateUI();
	}
	
}

class InstanceFieldsTableModel extends AbstractTableModel
{
	private String[] columnNames = { "Field Name", "Value" };
	private Object[][] data = null;
	
	public InstanceFieldsTableModel(InstanceDescriptor instDscr)
	{
		super();
		
		Hashtable<String, String> fieldsValue = instDscr.getFieldsValue();
		data = new Object[fieldsValue.size()][2];
		
		Enumeration<String> fieldNameEnumeration = fieldsValue.keys();
		int row = 0;
		while(fieldNameEnumeration.hasMoreElements())
		{
			String fieldName = fieldNameEnumeration.nextElement();
			String fieldValue = fieldsValue.get(fieldName);
			data[row][0] = fieldName;
			data[row][1] = fieldValue;
			row++;
		}
				
	}
	
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
		return false;
	}

	
}
