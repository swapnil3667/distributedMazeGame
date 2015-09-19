import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class BoardImpl implements Board, Serializable{
	public int size = 0;
	public int noOfTreasures = 0;
	List<Player> playersList = null;
	List<Treasure> treasureList = null;
	private static Logger logObject = Logger.getLogger(ExecuteGameImpl.class.getName());
	private static Board board = null;

	public static Board getInstance(){
		if (board == null){
			board = new BoardImpl();
		}
		return board;
	}

	private BoardImpl() {}


	/**
	 * Getter for size of board
	 * */
	public int getSize(){
		return this.size;
	}

	/**
	 * Getter for no of treasures of board
	 * */
	public int getNoOfTreasure(){
		return this.noOfTreasures;
	}

	/**
	 * Method to initialize board parameters.
	 * Doing this outside constructor because
	 * this is how singleton is implemented
	 * */
	public void init(int size, int noOfTreasures){
		logObject.info("Initializing board with size = "+size+" and no of treasures = "+noOfTreasures);
		this.size = size;
		this.noOfTreasures = noOfTreasures;
	}


	/**
	 * Method to add player to players list
	 * @param player : player to be added to playersList
	 * */
	public void addPlayer(Player player){
		if(playersList == null) playersList = new ArrayList<>();
		playersList.add(player);
	}

	/**
	 * Method to check if randomly generated x,y for treasure
	 * overlaps with a player's location. If yes, look for some
	 * other x,y
	 * @param x : randomly generated x for treasure
	 * @param y : randomly generated y for treasure
	 * */
	public boolean checkTreasureOverlapWithPlayer(int x, int y){
		for(Player eachPlayer : playersList){
			if (eachPlayer.getLocation().getX() == x && eachPlayer.getLocation().getY() == y){
				return true;
			}
		}
		return false;
	}


	/**
	 * Method to generate random treasures with location and value
	 */
	public void generateTreasures(){
		logObject.info("Initialize treasures list");
		treasureList = new ArrayList<>();
		Random randomGenerator = new Random();
		for(int i = 0;i<noOfTreasures;i++){
			int x = randomGenerator.nextInt(size);
			int y = randomGenerator.nextInt(size);
			if(checkTreasureOverlapWithPlayer(x, y)) continue;
			TreasureImpl treasure = new TreasureImpl();
			treasure.setLocation(x, y);
			treasure.setValue(1);
			treasureList.add(treasure);
		}
		logObject.info("Treasures generated");
	}

	/**
	 * Method to return player object with
	 * a particular id passed as parameter
	 * @param id: id of the player to be fetched
	 * */
	public Player getPlayerWithId(int Id){
		for(Player eachPlayer : playersList){
			if (eachPlayer.getId() == Id) return eachPlayer;
		}
		return null;
	}


	public void printCurrentBoardState(){
		for(Player eachPlayer: playersList){
			Location location = eachPlayer.getLocation();
			System.out.println("Player ["+eachPlayer.getId()+"] is at ("+location.getX()+", "+location.getY()+")");
		}

		for(Treasure eachTreasure: treasureList){
			Location location = eachTreasure.getLocation();
			System.out.println("Treasure [val = "+eachTreasure.getValue()+"] is at ("+location.getX()+", "+location.getY()+")");
		}

	}

	public void printBoard(){
		
		List<List<Integer>> rowList = new ArrayList<List<Integer>>();
		String si = new String();
		System.out.println();
		for(int i = 0; i < size; i++){
			List<Integer> l1 = new ArrayList<Integer>();
			for(int j = 0; j < size; j++){
					l1.add(0);
			}
			rowList.add(l1);
		}
		
		for(Player eachPlayer: playersList){
			Location location = eachPlayer.getLocation();
			int x  = location.getX();
			int y  = location.getY();
			(rowList.get(x)).set(y,1);
		}

		for (int i = 0; i < rowList.size(); i++) {
			List<Integer> t1 = rowList.get(i);
			for (int j = 0; j < t1.size(); j++) {
				System.out.print(t1.get(j) + " ");
			}
			System.out.println();
		}
	}

	/**
	 * updates location for curr player based on moving direction
	 * @param currPlayer : current location of player
	 * @param moveDir : Direction in which move is requested
	 * @return
	 * */
	public void setNewLocation(Player currPlayer, String moveDir){
		if(moveDir.equals("up")){
			currPlayer.setLocation(currPlayer.getLocation().getX() - 1, currPlayer.getLocation().getY());
		}
		if(moveDir.equals("down")){
			currPlayer.setLocation(currPlayer.getLocation().getX() + 1, currPlayer.getLocation().getY());
		}
		if(moveDir.equals("left")){
			currPlayer.setLocation(currPlayer.getLocation().getX(), currPlayer.getLocation().getY() - 1);
		}
		if(moveDir.equals("right")){
			currPlayer.setLocation(currPlayer.getLocation().getX(), currPlayer.getLocation().getY() + 1);
		}
	}


	/**
	 * Update location of player with id
	 * @param playerId : id of player to be moved
	 * @param moveDirection : direction in which player is to be moved
	 * */
	public void updatedPlayerLocation(int playerId, String moveDirection){
		Player currPlayer = getPlayerWithId(playerId);
		setNewLocation(currPlayer, moveDirection);
	}

}
