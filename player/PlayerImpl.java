
import java.rmi.registry.Registry;
import java.util.logging.Logger;
import java.util.Scanner;

public class PlayerImpl implements Player {
	
	String primaryIp = null;
	boolean isPlayerPrimary = false;
	boolean isPlayerBackup = false;
	private static Logger logObject = Logger.getLogger(PlayerImpl.class.getName());
	Server serverObj = null;
	int sizeOfBoard = 0;
	int noOfTreasures = 0;
	
	
	
	/**
	 * Method to check if player is amongst
	 * first two players or is just joining
	 * the game by connecting to primary
	 * */
	public boolean isPrimaryOrBackup(String[] args){
		if(args.length == 2){
			isPlayerPrimary = (args[0].equals("primary")) ?  true : false;
			primaryIp = args[1];
			return true;
		}else{
			primaryIp = args[1];
		}
		return false;
	}
	
	public boolean getIsPlayerPrimary(){
		return isPlayerPrimary;
	}
	
	public boolean getIsPlayerBackup(){
		return isPlayerBackup;
	}
	
	/**
	 * method to start primary server on this player
	 * */
	public void startServerAsPrimary(){
		serverObj = new Server();
		Scanner s = new Scanner(System.in);
		System.out.print("Enter size of board : ");
		sizeOfBoard = s.nextInt();
		System.out.print("Enter number of treasures to be generated: ");
		noOfTreasures = s.nextInt();
		logObject.info("Primary Server is up!!");
	}
	
	/**
	 * method to register stub as primary
	 * */
	public void registerStubAsPrimary(){
		ExecuteGame stub = null;
		Registry registry = null;
		serverObj.executeGameObj = (ExecuteGameImpl) ExecuteGameImpl.getInstance();
		serverObj.executeGameObj.init(sizeOfBoard, noOfTreasures);
	}
	
	
	public static void main(String[] args) {
		PlayerImpl playerObj = new PlayerImpl();
		boolean isThisPrimaryOfBackup = playerObj.isPrimaryOrBackup(args);
		
		
		
	}
}
