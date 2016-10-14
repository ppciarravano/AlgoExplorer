package ppciarravano.algoexplorer;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.Type;

import ppciarravano.algoexplorer.log.Logger;
import ppciarravano.algoexplorer.util.ConstantManager;


/**
 * Classe ClassDescriptor
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class ClassDescriptor
{
	private String className = "";
	private String sourceFileName = "";
	//private boolean innerClass = false;
	private Method[] methods = null;
	private Field[] fields = null;
	private String packageName = null;
	private int[] lineNumbers = null;
	
	private SourceDescriptor sourceDescriptor = null;
	
	//Variabili per memorizzare la scelta di visualizzazione
	private boolean classSelect;
	private Color backgroundColorCell;
	private Color foregroundColorCell;
	private boolean[] fieldUseForLabelCell;
	private boolean[] fieldInspect;
	
	private ClassDescriptor(String className)
	{
		this.className = className;
		//clearViewChoices();
	}
	
	public void clearViewChoices()
	{
		classSelect = false;
		backgroundColorCell = Color.decode(ConstantManager.STANDARD_BACKGROUND_COLOR_CELL);
		foregroundColorCell = Color.decode(ConstantManager.STANDARD_FOREGROUND_COLOR_CELL);
		if (fields==null)
		{
			fieldUseForLabelCell = null;
			fieldInspect = null;
		}
		else
		{
			fieldUseForLabelCell = new boolean[fields.length];
			fieldInspect = new boolean[fields.length];
			for (int i = 0; i < fields.length; i++)
			{
				fieldUseForLabelCell[i] = false;
				fieldInspect[i] = false;
			}
		}		
	}
	
	/*
	 * Unico metodo per creare un'istanza di ClassDescriptor;<br>
	 * Se il file non rappresenta una classe valida, il metodo ritorna null<br><br>
	 * Il metodo usa la bytecode instrumentation usando la libreria Apache Bcel,
	 * in quanto la semplice java.lang.reflect non e' capace di ispezionare anche i fields private
	 * 
	 */
	public static ClassDescriptor getClassDescriptorFromFile(File classFile)
	{
		Logger.log.debug("ByteCode Engineering Analyzing of file: " + classFile.getPath());
		ClassDescriptor result = null;
		try
		{
			ClassParser parser = new ClassParser(classFile.getPath());  
			JavaClass javaClass = parser.parse();
			Logger.log.debug("Done ByteCode Instrumentation for class:" + javaClass.getClassName());
			
			//Build new ClassDescriptor
			result = new ClassDescriptor(javaClass.getClassName());
			//popola altri campi di ClassDescriptor
			result.sourceFileName = javaClass.getSourceFileName();
			result.methods = javaClass.getMethods();
			result.fields = javaClass.getFields();
			result.packageName = javaClass.getPackageName();
			
			
			result.clearViewChoices();
			
			
			//Find Line numbers of class code
			Vector<Integer> lineNumbersVector = new Vector<Integer>(100);
			if (result.methods!=null)
			{
				for (int i = 0; i < result.methods.length; i++)
				{
					Method method = result.methods[i];
					{
						LineNumberTable lnt = method.getLineNumberTable();
						if (lnt!=null)
						{
							LineNumber[] lines = lnt.getLineNumberTable();
							if (lines!=null)
							{
								for (int k = 0; k < lines.length; k++)
								{
									//Logger.log.debug("Line: " + method.getName() + " -->" + lines[k].getLineNumber() );
									lineNumbersVector.addElement(lines[k].getLineNumber());
								}
							}
						}
					}
				}		
			}
			//Populate lineNumbers array 
			//Integer[] lineNumbersInteger = lineNumbersVector.toArray(new Integer[0]);
			result.lineNumbers = new int[lineNumbersVector.size()];
			for (int i = 0; i < lineNumbersVector.size(); i++)
			{
				result.lineNumbers[i] = lineNumbersVector.elementAt(i);
			}
			
			
		}
		catch (IOException ioe)
		{
			Logger.log.info("ByteCode Intrumentation IOException: file:" + classFile.getPath()); 
		}
		catch (ClassFormatException cfe)
		{
			Logger.log.info("ByteCode Intrumentation ClassFormatException: file:" + classFile.getPath()); 
		}
		
		return result;
	}

	/*
	 * Ritorna il nome del field scelto per la label oppure null se non e' stato scelto nessun field
	 */
	public String getFieldNameUseForLabelCell()
	{
		//TODO: gestione con cache di questo metodo, per evitare ogni volta di ciclare l'array
		
		String result = null;
		for (int i = 0; i < fieldUseForLabelCell.length; i++)
		{
			if (fieldUseForLabelCell[i])
			{
				result = fields[i].getName();
				break;
			}
		}
		return result;
	}
	
	/*
	 * Ritorna l'insieme dei nomi dei field richiesti per inspect 
	 */
	public Set<String> getFieldsNameToInspect()
	{
		//TODO: gestione con cache di questo metodo, per evitare ogni volta di ciclare l'array
		
		Set<String> result = new HashSet<String>();
		for (int i = 0; i < fieldInspect.length; i++)
		{
			if (fieldInspect[i])
			{
				result.add(fields[i].getName());
			}
		}
		return result;
	}
	
	/*
	 * Ritorna true se il field name e' un attributo selezionato come inspezionabile per la classe
	 */
	public boolean isInspectedField(String fieldName)
	{
		boolean result = false;
		for (String fieldNameToInspect : getFieldsNameToInspect())
		{
			if (fieldNameToInspect.equals(fieldName))
			{
				result = true;
				break;
			}
		}
		return result;
	}
	
	public String getClassName()
	{
		return className;
	}

	public String getSourceFileName()
	{
		return sourceFileName;
	}

	//public boolean isInnerClass()
	//{
	//	return innerClass;
	//}

	public Method[] getMethods()
	{
		return methods;
	}

	public Field[] getFields()
	{
		return fields;
	}
	
	public String getPackageName()
	{
		return packageName;
	}
	
	public String getSourcePathName()
	{
		return packageName.replace('.', File.separatorChar)+File.separatorChar+sourceFileName;
	}
		
	public void dumpFieldsAndMethods()
	{
		for (int i = 0; i < fields.length; i++)
		{
			Logger.log.debug("Field["+i+"]:"+fields[i]);
		}
		for (int i = 0; i < methods.length; i++)
		{
			Logger.log.debug("Method["+i+"]:"+methods[i]);
		}
	}
	
	/*
	 * Ritorna true se la classe contiene un metodo public static void main(String[]) e' quindi una main class
	 */
	public boolean isMainClass()
	{
		boolean result = false;
		for (int i = 0; i < methods.length; i++)
		{
			Method method = methods[i];
			Type[] arg_types = method.getArgumentTypes();
			if (method.isStatic() && 
					method.isPublic() && 
					method.getName().equals("main") && 
					(arg_types.length == 1) && 
					arg_types[0].equals(Type.getType("[Ljava.lang.String;")))
			{
				result = true;
				break;
			}
		}
		return result;
	}
	
	public static String getFieldAccessMode(Field field)
	{
		StringBuffer result = new StringBuffer();
		if (field.isAbstract())
		{
			result.append("abstract ");
		}
		if (field.isAnnotation())
		{
			result.append("annotation ");
		}
		if (field.isEnum())
		{
			result.append("enum ");
		}
		if (field.isFinal())
		{
			result.append("final ");
		}
		if (field.isPrivate())
		{
			result.append("private ");
		}
		if (field.isProtected())
		{
			result.append("protected ");
		}
		if (field.isPublic())
		{
			result.append("public ");
		}
		if (field.isStatic())
		{
			result.append("static ");
		}

		if (result.length()!=0)
		{
			result.deleteCharAt(result.length()-1);
			result.insert(0, "(");
			result.append(")");
			return result.toString();
		}
		else
		{
			return "";
		}
		
	}

	
	//Set e Get variabili per memorizzare la scelta di visualizzazione
	
	public boolean isClassSelect()
	{
		return classSelect;
	}

	public void setClassSelect(boolean classSelect)
	{
		this.classSelect = classSelect;
	}

	public Color getBackgroundColorCell()
	{
		return backgroundColorCell;
	}

	public void setBackgroundColorCell(Color backgroundColorCell)
	{
		this.backgroundColorCell = backgroundColorCell;
	}

	public Color getForegroundColorCell()
	{
		return foregroundColorCell;
	}

	public void setForegroundColorCell(Color foregroundColorCell)
	{
		this.foregroundColorCell = foregroundColorCell;
	}

	public boolean[] getFieldUseForLabelCell()
	{
		return fieldUseForLabelCell;
	}

	public void setFieldUseForLabelCell(boolean[] fieldUseForLabelCell)
	{
		this.fieldUseForLabelCell = fieldUseForLabelCell;
	}

	public boolean[] getFieldInspect()
	{
		return fieldInspect;
	}

	public void setFieldInspect(boolean[] fieldInspect)
	{
		this.fieldInspect = fieldInspect;
	}

	public SourceDescriptor getSourceDescriptor()
	{
		return sourceDescriptor;
	}

	public void setSourceDescriptor(SourceDescriptor sourceDescriptor)
	{
		this.sourceDescriptor = sourceDescriptor;
	}

	public int[] getLineNumbers()
	{
		return lineNumbers;
	}

	
}
