package ppciarravano.algoexplorer.trace;

import java.util.HashSet;
import java.util.Hashtable;

import ppciarravano.algoexplorer.ClassDescriptor;

import com.sun.jdi.ObjectReference;


/**
 * Classe InstanceDescriptor
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class InstanceDescriptor
{
	private ObjectReference objectReference;
	private long uniqueID;
	private String label;
	private ClassDescriptor classDescriptor;
	private HashSet<InstanceReference> references;
	private Hashtable<String, String> fieldsValue;
	private boolean collected;
	
	public InstanceDescriptor(ObjectReference objectReference, ClassDescriptor classDescriptor)
	{
		this.objectReference = objectReference;
		this.uniqueID = objectReference.uniqueID();
		this.classDescriptor = classDescriptor;
		
		label = "";
		references = new HashSet<InstanceReference>();
		fieldsValue = new Hashtable<String, String>();
		collected = false;
	}
	
	public void addFieldValue(String fieldName, String value)
	{
		fieldsValue.put(fieldName, value);
	}
	
	public void addReference(InstanceReference reference)
	{
		//TODO: Lanciare errore se reference.parentId!=this.uniqueId
		references.add(reference);
	}
	
	public void clearAllReferences()
	{
		references.clear();
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public HashSet<InstanceReference> getReferences()
	{
		return references;
	}

	public void setReferences(HashSet<InstanceReference> references)
	{
		this.references = references;
	}

	public long getUniqueID()
	{
		return uniqueID;
	}

	public ClassDescriptor getClassDescriptor()
	{
		return classDescriptor;
	}

	public ObjectReference getObjectReference()
	{
		return objectReference;
	}
	
	/*
	 * Determines if the objectReference in InstanceDescriptor has been garbage collected in the target VM. 
	 */
	public boolean isCollected()
	{
		return objectReference.isCollected();
	}
	
	public void setCollected(boolean collected)
	{
		this.collected = collected;
	}

	public Hashtable<String, String> getFieldsValue()
	{
		return fieldsValue;
	}
	
}
