package ppciarravano.algoexplorer.export;

public abstract class GraphElement
{
	protected String label;
	protected Long objId;
	
	public GraphElement(String label)
	{
		this.label = label;
	}
	
	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public Long getObjId()
	{
		return objId;
	}

	public void setObjId(Long objId)
	{
		this.objId = objId;
	}

	@Override
	public String toString()
	{
		return "GraphElement [label=" + label + ", objId=" + objId + "]";
	}

		
}
