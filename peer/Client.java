import java.io.Console;
import java.io.IOException;
import java.io.Reader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;
import java.rmi.RemoteException;


public class Client extends UnicastRemoteObject implements ClientInterface {
    int selfId = 0;
    private static Logger logObject = Logger.getLogger(Client.class.getName());
    Board board = null;
    Board backupBoard = null;
    ExecuteGame executeGameStub = null;
    ExecuteGame backupExecuteGameStub = null;
    
    private static final class Lock { }
    private final Object lock = new Lock();
    boolean isClientPrimary = false;
    boolean isClientSecondary = false;
    
    public Client() throws RemoteException{}

    public int getSelfId() throws RemoteException{
    	return selfId;
    }
    
    public void setSelfId(int id) throws RemoteException {
		this.selfId = id;
	}
    
    public void setExecuteGameObj(ExecuteGame executeGameStub){
    	this.executeGameStub = executeGameStub;
    }
    
    public void setIsClientPrimary(boolean isClientPrimary){
    	this.isClientPrimary = isClientPrimary;
    }
    
    public boolean getIsClientPrimary(){
    	return isClientPrimary;
    }
    
    public void setIsClientBackup(boolean isClientSecondary){
    	this.isClientSecondary = isClientSecondary;
    }
    
    public boolean getIsClientBackup(){
    	return isClientSecondary;
    }
    
    public void setBackupExecuteGameStub(ExecuteGame backupExecuteGameStub){
    	this.backupExecuteGameStub = backupExecuteGameStub;
    }
    
    public ExecuteGame getBackupExecuteGameStub(){
    	return backupExecuteGameStub;
    }
    
    /**
     * Method to change console mode from
     * buffered to real time
     * */
    public void changeConsoleModeStty() throws InterruptedException, IOException{
    	String[] cmd = {"/bin/sh", "-c", "stty raw </dev/tty"};
		Runtime.getRuntime().exec(cmd).waitFor();
    }

    /**
     * Resetting console mode to buffered
     * */
    public void resetConsoleMode() throws InterruptedException, IOException{
    	String[] cmd = new String[] {"/bin/sh", "-c", "stty sane </dev/tty"};
		Runtime.getRuntime().exec(cmd).waitFor();
    }

    public void callBackFromServer(Board board){
      System.out.println("Call Back : This is your starting board\n");
//      board.printBoard(selfId);
      this.board = board;
      synchronized (lock) {
    	    lock.notifyAll();
      }
      
    }
    
    public void startBackupServerOnThisPeer() throws RemoteException{
    }
    
    /**
     * Method that primary server calls to see if back up
     * is up and running
     * @throws IOException 
     * @throws InterruptedException 
     * */
    public boolean isBackupAlive(ExecuteGame executeGameObj) throws InterruptedException, IOException{
//    	resetConsoleMode();
    	backupExecuteGameStub = executeGameObj;
//    	logObject.info("Back Copy : Treasure Count is "+backupExecuteGameStub.getBoard().getTreasureListCurrentSize());
//    	changeConsoleModeStty();
    	return true;
    }
    
    
    /**
     * Returns direction as string based on key
     * pressed on key board
     * @param consoleInput : ascii value of key pressed on console
     * @return direction : direction as string
     * */
    public String getDirectionOnKeyPress(int consoleInput){
    	if(consoleInput == 119) return "up";
    	else if(consoleInput == 97) return "left";
    	else if(consoleInput == 115) return "down";
    	else if(consoleInput == 100) return "right";
    	else if (consoleInput == 114) return "refresh";
    	else return "invalid";
    }
    

    /**
     * Method that enables player to move
     * @throws IOException 
     * @throws InterruptedException */
    public void enablePlayerMove() throws InterruptedException, IOException{
		while(true){
			changeConsoleModeStty();
			Console console = System.console();
			Reader reader = console.reader();
			int consoleInput = reader.read();
			resetConsoleMode();

				if(consoleInput == 120) break; //breaks on 'X' press

			String directionToMove = getDirectionOnKeyPress(consoleInput);
			try{
				//Calling move player to test
				board = executeGameStub.movePlayer(getSelfId(), directionToMove);
			}catch (RemoteException e){
//				setBackupExecuteGameStub(executeGameStub);
				executeGameStub = backupExecuteGameStub;
				executeGameStub.setFlagTwentySecOver(true);
				board = executeGameStub.movePlayer(getSelfId(), directionToMove);
				
			}finally{
				//Printing current score sheet
				board.printScoresDuringGame(getSelfId());
			}
			 
//			System.out.println("After move");
			Runtime.getRuntime().exec("clear");
			board.printBoard(selfId);
			if(board.getIsGameOverFlag()){
				logObject.info("game over");
				System.out.println("All treasures taken, game is over");
				board.printFinalResultForPlayers(getSelfId());
				break;
			}
		}
		return;
    }
    
    /**
     * Method to start player by getting execute
     * game remote stub and joining game*/
    public void startClient(String[] args){
    	String host = (args.length < 1) ? null : args[0];
    	if (System.getSecurityManager() == null) {
    		System.setSecurityManager(new SecurityManager());
    	}
    	try {
    		Registry registry = LocateRegistry.getRegistry(host);
    		executeGameStub = (ExecuteGame) registry.lookup("Primary");
    		if(executeGameStub.joinGame(this)){
    			logObject.info("Request processed at server side, id assigned : "+selfId);
    		}
    		
    		synchronized (lock) {
    		        lock.wait();
    		}
    		
    		board.printBoard(selfId);
    		enablePlayerMove();
    		
    		} catch (Exception e) {
    		    System.err.println("Client exception: " + e.toString());
    		    e.printStackTrace();
    		}
    }
    
    
public static void main(String[] args) throws InterruptedException, IOException {
    Client clientObj = new Client();
    clientObj.startClient(args);
    }
}
