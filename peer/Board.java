import java.io.IOException;
import java.io.Serializable;
import java.util.List;


/**
 * This is a placeholder for maze board information.
 * There will be an implementation of this as BoardImpl.java
 * */
public interface Board extends Serializable{
//	List<Player> playersList = null;
//	List<Treasure> treasureList = null;
	public Player getPlayerWithId(int Id);
	public void generateTreasures();
	public void addPlayer(Player player);
	public void addTreasure(Treasure treasure);
	public void printCurrentBoardState();
	public void init(int size, int noOfTreasures);
	public int getSize();
	public int getNoOfTreasure();
	public void printBoard(int selfId) throws InterruptedException, IOException;
	public void updatedPlayerLocation(int playerId, String moveDirection) throws InterruptedException, IOException;
	public void setNewLocation(Player currPlayer, String moveDir) throws InterruptedException, IOException;
	public void checkPlayerOverlapwTreasure(Location location, Player player) throws InterruptedException, IOException;
	public int getTreasureListCurrentSize();
	public void setIsGameOverFlag(boolean flag);
	public boolean getIsGameOverFlag();
	public void printFinalResultForPlayers(int callingPlayerId) throws InterruptedException, IOException;
	public void printFinalResultForServer() throws InterruptedException, IOException;
	public void printScoresDuringGame(int callingPlayerId) throws InterruptedException, IOException;
	public boolean isCellOccupied(Location newLocation);
	public List<Player> getPlayerList();
	public void changeConsoleModeStty() throws InterruptedException, IOException;
	public void resetConsoleMode() throws InterruptedException, IOException;

}
