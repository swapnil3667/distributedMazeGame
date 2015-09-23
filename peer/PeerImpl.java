
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class PeerImpl  implements Peer, Serializable {
	
	protected PeerImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}


	String primaryIp = null;
	boolean isPlayerPrimary = false;
	boolean isPlayerBackup = false;
	private static Logger logObject = Logger.getLogger(PeerImpl.class.getName());
	ServerInterface serverObj = null;
	ClientInterface clientObj = null;
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
			primaryIp = args[0];
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
	 * method to start primary server on this peer
	 * @throws RemoteException 
	 * */
	public void startServerAsPrimary() throws RemoteException{
		serverObj = new Server();
		serverObj.takeInputAtServer();
		serverObj.bindNameToStubAtRegistry();
		serverObj.waitTwentySecAtServer();
		logObject.info("Waiting period over, board set!! Size = "+serverObj.getExecuteGameObj().getBoard().getSize());
		serverObj.callBackAllClients();
	}
	
	/**
	 * Method to start a client on this peer
	 * @throws RemoteException 
	 * */
	public void startClientOnThisPeer(String[] args) throws RemoteException{
		clientObj = new Client();
		clientObj.startClient(args);
	}
	
	/**
	 * Method that makes a player as back up server
	 * chosen at random. This method will be called 
	 * right before call back to all players/clients
	 * @throws RemoteException 
	 * */
	public void selectPlayerForBackup() throws RemoteException{
		logObject.info("Selecting back up from all players");
		Board board = serverObj.getExecuteGameObj().getBoard();
		List<Player> playerList = board.getPlayerList(); 
		int numberOfPlayer = playerList.size();
		Random randomGenerator = new Random();
		int randPlayer = randomGenerator.nextInt(numberOfPlayer);
		while( playerList.get(randPlayer).getId() == clientObj.getSelfId()){
			randPlayer = randomGenerator.nextInt(numberOfPlayer);
		}
		
		Player backupPlayer = playerList.get(randPlayer);
		logObject.info("Player with id "+playerList.get(randPlayer).getId()+" selected as backup server");
	}
	
	
	public static void main(String[] args) throws RemoteException {
		PeerImpl peerObj = new PeerImpl();
		boolean isThisPrimaryOrBackup = peerObj.isPrimaryOrBackup(args);
		if(isThisPrimaryOrBackup) peerObj.startServerAsPrimary();
		else peerObj.startClientOnThisPeer(args);
	}
}
