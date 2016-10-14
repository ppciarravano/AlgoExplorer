package ppciarravano.algoexplorer.export;

public class GraphEdge extends GraphElement
{

	public GraphEdge(String label)
	{
		super(label);
	}
	
	private boolean direct;
	private double xFrom;
	private double yFrom;
	private double xTo;
	private double yTo;
		
	public boolean isDirect()
	{
		return direct;
	}
	public void setDirect(boolean direct)
	{
		this.direct = direct;
	}
	public double getxFrom()
	{
		return xFrom;
	}
	public void setxFrom(double xFrom)
	{
		this.xFrom = xFrom;
	}
	public double getyFrom()
	{
		return yFrom;
	}
	public void setyFrom(double yFrom)
	{
		this.yFrom = yFrom;
	}
	public double getxTo()
	{
		return xTo;
	}
	public void setxTo(double xTo)
	{
		this.xTo = xTo;
	}
	public double getyTo()
	{
		return yTo;
	}
	public void setyTo(double yTo)
	{
		this.yTo = yTo;
	}
		
	@Override
	public String toString()
	{
		return "GraphEdge [label=" + label + ", objId=" + objId + "]"+"[direct=" + direct + ", xFrom=" + xFrom + ", xTo=" + xTo + ", yFrom=" + yFrom + ", yTo=" + yTo + "]";
	}	
	
	
	
}
