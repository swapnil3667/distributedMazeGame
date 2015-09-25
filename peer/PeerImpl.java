
import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.rmi.RemoteException;
import java.util.logging.Logger;
import java.util.List;
import java.util.Random;

public class PeerImpl  implements Peer, Serializable, Runnable {
	 private static final class Lock { }
	 private final Object lock = new Lock();
	    
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
	ClientInterface backupClientPlayer = null;
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
	 * @throws IOException 
	 * @throws InterruptedException 
	 * */
	public void startServerAsPrimary() throws InterruptedException, IOException{
		serverObj = new Server();
		serverObj.takeInputAtServer();
		serverObj.bindNameToStubAtRegistry();
		serverObj.waitTwentySecAtServer();
		
		logObject.info("Waiting period over, board set!! Size = "+serverObj.getExecuteGameObj().getBoard().getSize());
		
		//Initializing a player on this peer.
		clientObj = new Client();
		serverObj.getExecuteGameObj().joinGame(clientObj);
		/*synchronized (lock) {
	        lock.wait();
		}*/
		serverObj.getExecuteGameObj().setFlagTwentySecOver(true);
		//Primary Generates treasure and prints board in text form 
		serverObj.getExecuteGameObj().getBoard().generateTreasures();
		serverObj.getExecuteGameObj().getBoard().printCurrentBoardState();
		
		selectPlayerForBackup();
		serverObj.callBackAllClients();
		serverObj.getExecuteGameObj().getBoard().printBoard(clientObj.getSelfId());
		//This thread does polling with back up server
		new Thread(this).start();
		
		
		/*Setting execute game stub (which is a remote stub for remote player) for client/player
		at this peer*/
		clientObj.setIsClientPrimary(true);
		clientObj.setExecuteGameObj(serverObj.getExecuteGameObj());
		clientObj.enablePlayerMove();
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
		int randPlayerIdx = randomGenerator.nextInt(numberOfPlayer);
		while( serverObj.getExecuteGameObj().clientList.get(randPlayerIdx).getSelfId()== clientObj.getSelfId()){
			randPlayerIdx = randomGenerator.nextInt(numberOfPlayer);
		}
		
//		Player backupPlayer = playerList.get(randPlayerIdx);
		backupClientPlayer = serverObj.getExecuteGameObj().clientList.get(randPlayerIdx);
		logObject.info("Player with id "+backupClientPlayer.getSelfId()+" selected as backup server");
	}
	
	
	public static void main(String[] args) throws InterruptedException, IOException {
		PeerImpl peerObj = new PeerImpl();
		boolean isThisPrimaryOrBackup = peerObj.isPrimaryOrBackup(args);
		if(isThisPrimaryOrBackup) peerObj.startServerAsPrimary();
		else peerObj.startClientOnThisPeer(args);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
				while(backupClientPlayer.isBackupAlive(serverObj.getExecuteGameObj().getBoard())){}
		}catch(RemoteException e){
			try {
				serverObj.getExecuteGameObj().resetConsoleMode();
			} catch (InterruptedException e1) {} catch (IOException e1) {}
			logObject.info("Back up server is down, picking another backup");
			try {
				serverObj.getExecuteGameObj().changeConsoleModeStty();
			} catch (InterruptedException e1) {} catch (IOException e1) {}
		} 
	}
}
