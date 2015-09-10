import java.io.Serializable;
import java.util.List;


/**
 * This is a placeholder for maze board information.
 * There will be an implementation of this as BoardImpl.java 
 * */
public interface Board extends Serializable{
	int size = 0;
	int noOfTreasures = 0;
	List<Player> playersList = null;
	List<Treasure> treasureList = null;
	public void generateTreasures();
	public void addPlayer(Player player);
	public void printCurrentBoardState();
} 
