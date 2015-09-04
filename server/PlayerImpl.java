
public class PlayerImpl implements Player {
	Location location = null;
	int treasureCount = 0;
	int id=0;
	
	public PlayerImpl(int id) {
		this.id = id;
	}
	public void setLocation(int x, int y){
		location = new Location(x,y);
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
