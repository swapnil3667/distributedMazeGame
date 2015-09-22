import java.io.Serializable;

/**
 * This class is a data structure to hold x,y coordinates
 * representing a location on maze
 * */
public class Location implements Serializable {
	private int x;
	private int y;
	
	Location(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public int getX(){
		return x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public int getY(){
		return y;
	}
}
