
public class TreasureImpl implements Treasure {
	Location location = null;
	int value = 0;
	
	public void setLocation(int x, int y){
		location = new Location(x,y);
	}
	
	public Location getLocation(){
		return location;
	}
	
	public void setValue(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
	
}
