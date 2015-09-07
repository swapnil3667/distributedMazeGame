import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class BoardImpl implements Board{
	int size = 0;
	int noOfTreasures = 0;
	List<PlayerImpl> playersList = null;
	List<TreasureImpl> treasureList = null;
	private static Logger logObject = Logger.getLogger(ExecuteGameImpl.class.getName());
	
	public static BoardImpl getInstance(int size, int noOfTreasures){
		return new BoardImpl(size, noOfTreasures);
	}
	
	private BoardImpl(int size, int noOfTreasures) {
		this.size = size;
		this.noOfTreasures = noOfTreasures;
	}
	
	public void addPlayer(PlayerImpl player){
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
		for(PlayerImpl eachPlayer : playersList){
			if (eachPlayer.getLocation().getX() == x && eachPlayer.getLocation().getY() == y){
				return true;
			}
		}
		return false;
	}
	
	//Method to populate treasureList with random treasure locations
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
	
	
	public void printCurrentBoardState(){
		for(PlayerImpl eachPlayer: playersList){
			Location location = eachPlayer.getLocation();
			System.out.println("Player ["+eachPlayer.getId()+"] is at ("+location.getX()+", "+location.getY()+")");
		}
		
		for(TreasureImpl eachTreasure: treasureList){
			Location location = eachTreasure.getLocation();
			System.out.println("Treasure [val = "+eachTreasure.getValue()+"] is at ("+location.getX()+", "+location.getY()+")");
		}
		
	}
	
}

