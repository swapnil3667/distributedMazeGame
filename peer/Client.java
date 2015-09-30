import java.io.Console;
import java.io.IOException;
import java.io.Reader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.text.DefaultEditorKit.CopyAction;

import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;


public class Client extends UnicastRemoteObject implements ClientInterface, Runnable {
    int selfId = 0;
    private static Logger logObject = Logger.getLogger(Client.class.getName());
    Board board = null;
    Board backupBoard = null;
    ExecuteGame executeGameStub = null;
    ExecuteGame executeGameStubNew = null;
    ExecuteGame backupExecuteGameStub = (ExecuteGameImpl) ExecuteGameImpl.getInstance();
    String host = null;
    Thread pollingWithBackup = null;
    ClientInterface primaryClient = null;
    
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
    
    public void setIsClientBackup(boolean isClientSecondary) throws RemoteException, InterruptedException{
    	this.isClientSecondary = isClientSecondary;
    	Thread pollingWithPrim = new Thread(this);
    	pollingWithPrim.start();
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
      this.board = board;
      synchronized (lock) {
    	    lock.notifyAll();
      }
      
      
    }
    
    
    /**
     * Method that primary server calls to see if back up
     * is up and running
     * @throws IOException 
     * @throws InterruptedException 
     * @throws CloneNotSupportedException 
     * */
    public boolean isBackupAlive(Board board,List<ClientInterface> listOfClients, ClientInterface primaryClient) throws InterruptedException, IOException, RemoteException, CloneNotSupportedException{
	    	
	    	backupExecuteGameStub.setBoard(board);
	    	backupExecuteGameStub.setClientList(listOfClients);
	    	this.primaryClient = primaryClient;
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
				
			}catch (RemoteException e1){
				Thread.sleep(1000);
				//IF EXCEPTION CAUGHT, LOOK FOR NEW PRIMARY WITH SAME LOOKUP NAME "Primary"
				if (System.getSecurityManager() == null) {
		    		System.setSecurityManager(new SecurityManager());
		    	}
				Registry registry = LocateRegistry.getRegistry(host);
	    		executeGameStub = (ExecuteGame) registry.lookup("Primary");
				executeGameStub.setFlagTwentySecOver(true);
				board = executeGameStub.movePlayer(getSelfId(), directionToMove);
				
			}
			
			finally{
				//Printing current score sheet
				board.printScoresDuringGame(getSelfId());
			}
			 
			Runtime.getRuntime().exec("clear");
			board.printBoard(selfId);
			if(board.getIsGameOverFlag()){
				resetConsoleMode();
				logObject.info("All treasures taken, game is over");
				changeConsoleModeStty();
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
    
    @Override
    public void run() {
    	try{
    		while(executeGameStub.isPrimaryAlive()){};
    	}catch(RemoteException e){
    		
    		try{
    			if (System.getSecurityManager() == null) {
		    		System.setSecurityManager(new SecurityManager());
		    	}
				Registry registry = LocateRegistry.getRegistry(host);
	    		executeGameStub = (ExecuteGame) registry.lookup("Primary");
	    		while(executeGameStub.isPrimaryAlive()){};
    		}catch (RemoteException e7){
//	    		logObject.info("Primary is dead, making Backup as Primary");
	    		try{
	    		resetConsoleMode();
	    		logObject.info("Primary is dead, making Backup as Primary");
	    		changeConsoleModeStty();
	    		}catch (InterruptedException | IOException e5){}
	    		try {
	    			ExecuteGame stub = null;
	    			resetConsoleMode();
	//				logObject.info("Current number of treasure : "+backupExecuteGameStub.getBoard().getTreasureListCurrentSize());
					Registry registry = LocateRegistry.getRegistry();
					registry.unbind("Primary");
					backupExecuteGameStub.getClientList().remove(primaryClient);
					stub = (ExecuteGame) UnicastRemoteObject.exportObject(backupExecuteGameStub, 0);
					registry.bind("Primary", stub);
					
					
					ServerInterface newServer = new Server();
					newServer.setExecuteGameObj(backupExecuteGameStub);
					Peer newPeer = new PeerImpl();
					newPeer.setClientObj(this);
					newPeer.setServerObj(newServer);
					newPeer.selectPlayerForBackup();	//Is back up alive thread
					pollingWithBackup = new Thread((Runnable) newPeer);
					pollingWithBackup.start();
					changeConsoleModeStty();
	//					new Thread((Runnable) newPeer).start();
						
					} catch (RemoteException e2) {} catch (AlreadyBoundException e3) {} catch (NotBoundException e4) {} catch (InterruptedException e1) {} catch (IOException e1) {}
		    	} catch (NotBoundException e1) {}
    		}
    	}
    
    
public static void main(String[] args) throws InterruptedException, IOException {
    Client clientObj = new Client();
    clientObj.startClient(args);
    }
}
