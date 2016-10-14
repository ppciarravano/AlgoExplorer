package ppciarravano.algoexplorer.export;

public class GraphCell extends GraphElement
{
	
	public GraphCell(String label)
	{
		super(label);
	}
	
	private double x;
	private double y;
	private double width;
	private double height;
	private String background;
	private String foreground;
	
	public double getX()
	{
		return x;
	}
	public void setX(double x)
	{
		this.x = x;
	}
	public double getY()
	{
		return y;
	}
	public void setY(double y)
	{
		this.y = y;
	}
	public double getWidth()
	{
		return width;
	}
	public void setWidth(double width)
	{
		this.width = width;
	}
	public double getHeight()
	{
		return height;
	}
	public void setHeight(double height)
	{
		this.height = height;
	}
	public String getBackground()
	{
		return background;
	}
	public void setBackground(String background)
	{
		this.background = background;
	}
	public String getForeground()
	{
		return foreground;
	}
	public void setForeground(String foreground)
	{
		this.foreground = foreground;
	}
	
	@Override
	public String toString()
	{
		return "GraphCell [label=" + label + ", objId=" + objId + "]"+"[background=" + background + ", foreground=" + foreground + ", height=" + height + ", width=" + width + ", x=" + x + ", y=" + y + "]";
	}	
	
}
