import java.util.List;


/**
 * This is a placeholder for maze board information.
 * There will be an implementation of this as BoardImpl.java 
 * */
public interface Board {
	int size = 0;
	int noOfTreasures = 0;
	List<PlayerImpl> playersList = null;
	List<TreasureImpl> treasureList = null;
	public void generateTreasures();
	public void addPlayer(PlayerImpl player);
	public void printCurrentBoardState();
} 
