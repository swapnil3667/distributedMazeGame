import java.io.Console;
import java.io.IOException;
import java.io.Reader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class Client extends UnicastRemoteObject implements ClientInterface {
    int selfId = 0;
    private static Logger logObject = Logger.getLogger(Client.class.getName());
    Board board = null;
    Board backupBoard = null;
    ExecuteGame executeGameStub = null;
    ExecuteGame executeGameStubNew = null;
    volatile ExecuteGame backupExecuteGameStub = null;
    String host = null;
    
    
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
    
    public void setIsClientPrimary(boolean isClientPrimary) throws RemoteException{
    	this.isClientPrimary = isClientPrimary;
    }
    
    public boolean getIsClientPrimary() throws RemoteException{
    	return isClientPrimary;
    }
    
    public void setIsClientBackup(boolean isClientSecondary) throws RemoteException{
    	this.isClientSecondary = isClientSecondary;
    }
    
    public boolean getIsClientBackup() throws RemoteException{
    	return isClientSecondary;
    }
    
    public void setBackupExecuteGameStub(ExecuteGame backupExecuteGameStub) throws RemoteException{
    	this.backupExecuteGameStub = backupExecuteGameStub;
    }
    
    public ExecuteGame getBackupExecuteGameStub() throws RemoteException{
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
      
      //HERE BACK UP SHOULD START POLLING PRIMARY TO SEE IF IT IS STILL ALIVE
      /*if(isClientSecondary){
    	  new Thread(this).start();
      }*/
      
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
//    	synchronized(backupExecuteGameStub){
	    	resetConsoleMode();
	    	backupExecuteGameStub = executeGameObj;
//	    	logObject.info("Back Copy : Treasure Count is "+backupExecuteGameStub.getBoard().getTreasureListCurrentSize());
	    	changeConsoleModeStty();
	    	try{
	    		if(isClientSecondary) executeGameStub.isPrimaryAlive();
	    	}catch (RemoteException e){
	    		logObject.info("Primary is dead, making Backup as Primary");
				logObject.info("1 Current number of treasure"+backupExecuteGameStub.getBoard().getNoOfTreasure());
	    		try {
	    			ExecuteGame stub = null;
					logObject.info("2 Current number of treasure"+backupExecuteGameStub.getBoard().getNoOfTreasure());
					Registry registry = LocateRegistry.getRegistry();
//					registry.unbind("Primary");
					stub = (ExecuteGame) UnicastRemoteObject.exportObject(backupExecuteGameStub, 0);
					registry.bind("Primary", stub);
					logObject.info(stub.testStringReponse());
				} catch (RemoteException e1) {} catch (AlreadyBoundException e1) {}
	    	}
	    	return true;
//    	}
    }
    
    
    /**T
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
     * @throws InterruptedException 
     * @throws NotBoundException */
    public void enablePlayerMove() throws InterruptedException, IOException, NotBoundException{
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
				//IF EXCEPTION CATCHED, LOOK FOR NEW PRIMARY WITH SAME LOOKUP NAME "Primary"
				
//				executeGameStub = backupExecuteGameStub;
				if (System.getSecurityManager() == null) {
		    		System.setSecurityManager(new SecurityManager());
		    	}
				Registry registry = LocateRegistry.getRegistry(host);
	    		executeGameStubNew = (ExecuteGame) registry.lookup("Primary");
	    		logObject.info(executeGameStubNew.testStringReponse());
				executeGameStubNew.setFlagTwentySecOver(true);
				board = executeGameStubNew.movePlayer(getSelfId(), directionToMove);
				
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
    	host = (args.length < 1) ? null : args[0];
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
    			try {
					resetConsoleMode();
				} catch (InterruptedException | IOException e1) {}
    		    System.err.println("Client exception: " + e.toString());
    		    e.printStackTrace();
    		}
    }
    
    /*@Override
    public void run() {
    	try{
    		while(executeGameStub.isPrimaryAlive()){};
    	}catch(RemoteException e){
    		logObject.info("Primary is dead, making Backup as Primary");
    		try {
    			ExecuteGame stub = null;
				Registry registry = LocateRegistry.getRegistry();
//				registry.unbind("Primary");
				logObject.info("Current number of treasure"+backupExecuteGameStub.getBoard().getNoOfTreasure());
				stub = (ExecuteGame) UnicastRemoteObject.exportObject(backupExecuteGameStub, 0);
				registry.bind("Primary", stub);
				logObject.info(stub.testStringReponse());
			} catch (RemoteException e1) {} catch (AlreadyBoundException e1) {}
    	}
    }*/
    
public static void main(String[] args) throws InterruptedException, IOException {
    Client clientObj = new Client();
    clientObj.startClient(args);
    }
}
