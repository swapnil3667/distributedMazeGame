import java.io.Serializable;

/**
 * This class is a place holder for all the treasures
 * that we will assign randomly
 * There will be an implementation of this as TreasureImpl.java
 */
public interface Treasure extends Serializable{
	public void setLocation(int x, int y);
	public Location getLocation();
	public void setValue(int value);
	public int getValue();
}
