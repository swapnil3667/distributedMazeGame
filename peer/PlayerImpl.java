import java.io.Serializable;

public class PlayerImpl implements Player,Serializable {
	Location location = null;
	int treasureCount = 0;
	int id=0;
	
	public PlayerImpl(int id) {
		this.id = id;
	}
	public void setLocation(Location location){
		this.location = location;
	}
	
	public Location getLocation(){
		return location;
	}
	
	public void setTreasureCount(int treasureCount){
		this.treasureCount= treasureCount;
	}
	
	public int getTreasureCount(){
		return treasureCount;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
}
