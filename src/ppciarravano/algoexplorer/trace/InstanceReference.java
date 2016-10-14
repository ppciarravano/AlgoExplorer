package ppciarravano.algoexplorer.trace;

/**
 * Classe InstanceReference
 * 
 * <br><br>License: 	GNU General Public License Version 3<br>
 * 
 * @author  	Pier Paolo Ciarravano  
 * @version  	Vers. 0.9 (11/01/2010)
 */
public class InstanceReference
{
	private long uniqueID;
	private String name;
	private boolean direct;
	private long parentID;	
	
	public InstanceReference(long uniqueID, String name, boolean direct, long parentID)
	{
		this.uniqueID = uniqueID;
		this.name = name;
		this.direct = direct;
		this.parentID = parentID;
	}

	public long getUniqueID()
	{
		return uniqueID;
	}

	public void setUniqueID(long uniqueID)
	{
		this.uniqueID = uniqueID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isDirect()
	{
		return direct;
	}

	public void setDirect(boolean direct)
	{
		this.direct = direct;
	}

	public long getParentID()
	{
		return parentID;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (parentID ^ (parentID >>> 32));
		result = prime * result + (int) (uniqueID ^ (uniqueID >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstanceReference other = (InstanceReference) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (parentID != other.parentID)
			return false;
		if (uniqueID != other.uniqueID)
			return false;
		return true;
	}
	
	
}
