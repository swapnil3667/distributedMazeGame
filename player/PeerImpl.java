
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.logging.Logger;
import java.util.Scanner;

public class PeerImpl implements Peer {
	
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
	 * method to start primary server on this peer
	 * */
	public void startServerAsPrimary(){
		serverObj = new Server();
		serverObj.takeInputAtServer();
		serverObj.bindNameToStubAtRegistry();
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
	 * */
	public void selectPlayerForBackup(){
		Board board = serverObj.getExecuteGameObj().getBoard();
//		int numberOfPlayer = 
	}
	
	
	public static void main(String[] args) throws RemoteException {
		PeerImpl peerObj = new PeerImpl();
		boolean isThisPrimaryOrBackup = peerObj.isPrimaryOrBackup(args);
		if(isThisPrimaryOrBackup) peerObj.startServerAsPrimary();
		else peerObj.startClientOnThisPeer(args);
	}
}
