package ppciarravano.algoexplorer.export;

import java.util.List;
import java.util.Vector;

public class GraphFrame
{
	private List<GraphElement> elements;
	private String title;
	private String description;
		
	public GraphFrame(String title, String description)
	{
		this.elements = new Vector<GraphElement>();
		this.title = title;
		this.description = description;
		
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public List<GraphElement> getElements()
	{
		return elements;
	}

	public void setElements(List<GraphElement> elements)
	{
		this.elements = elements;
	}

	@Override
	public String toString()
	{
		final int maxLen = 10;
		return "GraphFrame [description=" + description + ", elements=" + (elements != null ? elements.subList(0, Math.min(elements.size(), maxLen)) : null) + ", title=" + title + "]";
	}
	
	
}
