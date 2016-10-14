package ppciarravano.algoexplorer.export;

import java.util.List;
import java.util.Vector;

public class ExportFrames
{
	private List<GraphFrame> frames;
	private String title;
	private String description;
		
	public ExportFrames()
	{
		this.frames = new Vector<GraphFrame>();
				
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

	public List<GraphFrame> getFrames()
	{
		return frames;
	}

	public void setFrames(List<GraphFrame> frames)
	{
		this.frames = frames;
	}

	@Override
	public String toString()
	{
		final int maxLen = 10;
		return "ExportFrames [description=" + description + ", frames=" + (frames != null ? frames.subList(0, Math.min(frames.size(), maxLen)) : null) + ", title=" + title + "]";
	}
	
	
	
}
