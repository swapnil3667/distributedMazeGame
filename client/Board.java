import java.io.Serializable;
import java.util.List;


/**
 * This is a placeholder for maze board information.
 * There will be an implementation of this as BoardImpl.java
 * */
public interface Board extends Serializable{
	List<Player> playersList = null;
	List<Treasure> treasureList = null;
	public Player getPlayerWithId(int Id);
	public void generateTreasures();
	public void addPlayer(Player player);
	public void printCurrentBoardState();
	public void init(int size, int noOfTreasures);
	public int getSize();
	public int getNoOfTreasure();
	public void printBoard();
	public void updatedPlayerLocation(int playerId, String moveDirection);
	public void setNewLocation(Player currPlayer, String moveDir);
}
