import java.io.Serializable;

/**
 * This is place holder that will hold
 * information about a particular player
 * There will be an implementation of this as PlayerImpl.java
 * */
public interface Player extends Serializable{
	public void setLocation(Location location);
	public Location getLocation();
	public void setTreasureCount(int treasureCount);
	public int getTreasureCount();
	public void setId(int id);
	public int getId();
}

