import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;
/**
 * This is the class where logic of the game will go.
 * Methods like movePlayer and startGame will be defined here.
 * This class will handle all the place holders and data structures
 * at server side and update respective client as well.
 * Stub for this class will be registered in rmiregistry and
 * that will be looked up by client to call.
 * 
 * At the end of 20 Sec, startGame method will respond to all the
 * connected players with initial maze.
 * */
public class ExecuteGameImpl implements ExecuteGame {
	Board board = null;
	int sizeOfBoard=0;
	int noOfTreasures = 0;
	int noOfPlayers= 0;
	private static Logger logObject = Logger.getLogger(ExecuteGameImpl.class.getName());
	
	public static ExecuteGameImpl getInstance(int sizeOfBoard, int noOfTreasures){
		return new ExecuteGameImpl(sizeOfBoard, noOfTreasures);
	}
	
	private ExecuteGameImpl(int sizeOfBoard, int noOfTreasures) {
		this.sizeOfBoard = sizeOfBoard;
		this.noOfTreasures = noOfTreasures;
	}
	
	public void startGame(){
		logObject.info("Initializing the game board");
		//Initialize board
		board = BoardImpl.getInstance(sizeOfBoard, noOfTreasures);
		
	}	
	
	public Location generatePlayerLocation(int id){
		logObject.info("Creating location for new join game request by player "+id);
		Random randomGenerator = new Random();
		int x = randomGenerator.nextInt(sizeOfBoard);
		int y = randomGenerator.nextInt(sizeOfBoard);
		logObject.info("Location assigned");
		return new Location(x,y);
	}
	
	public int generatePlayerId(){
		logObject.info("Creating id for player number "+noOfPlayers);
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(8999)+1000;
	}
	
	/**
	 * Method that different clients will call to join the game
	 * */
	public void joinGame(){
		logObject.info("Join game request received by player number "+(++noOfPlayers));
		//When the first player calls this, start game is called.
		if(board == null) startGame();
		//Generate random 4 digit id for new player
		int id = generatePlayerId();
		//Generate random location for this new player 
		Location location = generatePlayerLocation(id);
		
		//Setting player parameters
		PlayerImpl player = new PlayerImpl(id);
		player.setLocation(location.getX(), location.getY());
		player.setTreasureCount(0);
		board.addPlayer(player);
		logObject.info("Join request processed successfully. Player added to players list.");
		
		//Generating treasures at the end of 20 seconds - to avoid overlap with any players generated so far
		board.generateTreasures();
		
		board.printCurrentBoardState();
		//Return location to client that called joinGame

	}
	
	public void movePlayer(){
		
	}
	
	public static void main(String[] args) {
		
	}
}
