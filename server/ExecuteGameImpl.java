import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;
import java.util.List;
/**
 * This is the class where logic of the game will go.
 * Methods like movePlayer and startGame will be defined here.
 * This class will handle all the place holders and data structures
 * at server side and update respective client as well.
 * Stub for this class will be registered in rmi-registry and
 * that will be looked up by client to call.
 *
 * At the end of 20 Sec, startGame method will respond to all the
 * connected players with initial maze.
 * */
public class ExecuteGameImpl implements ExecuteGame, Serializable {
	Board board = null;
	int sizeOfBoard=0;
	int noOfTreasures = 0;
	int noOfPlayers= 0;
	boolean isTwentySecOver = false;
	private static Logger logObject = Logger.getLogger(ExecuteGameImpl.class.getName());
	private static ExecuteGame execGame = null;
	int firstPlayerId = 0;	//This is only for testing purpose
	List<ClientInterface> clientList = new ArrayList<ClientInterface>();

	public static ExecuteGame getInstance(){
		if(execGame  == null){
			execGame = new ExecuteGameImpl();
		}
		return execGame;
	}

	/*
	 * Default constructor made private for singleton-ness*/
	private ExecuteGameImpl() {	}


	public void setBoard(Board board){
		this.board = board;
	}

	public Board getBoard(){
		return board;
	}

	/**
	 * Method to initialize attributes,
	 * in case of singleton, this is not
	 * done inside constructor
	 * */
	public void init(int sizeOfBoard, int noOfTreasures){
		this.sizeOfBoard = sizeOfBoard;
		this.noOfTreasures = noOfTreasures;
	}

	/**
	 * Method is called when first player tries
	 * to join the game ( when board is null )
	 * */
	public void startGame(){
		logObject.info("Initializing the game board");
		//Initialize board
		board =  BoardImpl.getInstance();
		board.init(this.sizeOfBoard, this.noOfTreasures);
		logObject.info("Board initialized with board size = "+board.getSize()+" and no of treasures = "+board.getNoOfTreasure());
	}

	/**
	 * Generates random location for each player
	 * @param id : id of the player for which location is being generated
	 * @return location of player
	 * */
	public Location generatePlayerLocation(int id){
		logObject.info("Creating location for new join game request by player "+id);
		Random randomGenerator = new Random();
		int x = randomGenerator.nextInt(sizeOfBoard);
		int y = randomGenerator.nextInt(sizeOfBoard);
		logObject.info("Location assigned");
		return new Location(x,y);
	}

	/**
	 * Generates random id for each player
	 * @param
	 * @return randomly generated integer id
	 * */
	public int generatePlayerId(){
		logObject.info("Creating id for player number "+noOfPlayers);
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(8999)+1000;
	}


	public void setFlag(boolean flag){
		this.isTwentySecOver = flag;
	}

	/**
	 * Method that makes the server wait for twenty seconds
	 * so that all players can join
	 * */
	public void waitTwentySeconds(){
		setFlag(false);
		long end = System.currentTimeMillis() + 20000;
		while(System.currentTimeMillis() < end){} 	//runs for 20 sec
		System.out.println("Joining period over");
		setFlag(true);
		logObject.info("Joining time over at server side");

		//Generating treasures at the end of 20 seconds
		board.generateTreasures();
		board.printCurrentBoardState();
	}

	/**
	 * Method that different clients will call to join the game
	 * @throws RemoteException 
	 * */
	public Board joinGame(ClientInterface client) throws RemoteException{
		if(!isTwentySecOver){
			logObject.info("Join game request received by player number "+(++noOfPlayers));
			//When the first player calls this, start game is called.
			if(board == null) startGame();
			//Generate random 4 digit id for new player
			int id = generatePlayerId();
			client.setSelfId(id);
			//Generate random location for this new player
			Location location = generatePlayerLocation(id);
			clientList.add(client);
			

			//Setting player parameters
			Player player = new PlayerImpl(id);
			player.setLocation(location.getX(), location.getY());
			player.setTreasureCount(0);
			board.addPlayer(player);
			logObject.info("Join request processed successfully. Player added to players list.");
			return board;
			//Return location to client that called joinGame
//			return "Game joined by player "+noOfPlayers+" with id : "+id;
		}
		return null;
	}


	public String testStringReponse(){
		return "This is a test message from server";
	}

	public Board movePlayer(int id, String moveDirection){
		board.updatedPlayerLocation(id, moveDirection);
		return board;
	}
}
