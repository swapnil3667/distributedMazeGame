import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.logging.Logger;

public class Server implements ServerInterface{
	private static Logger logObject = Logger.getLogger(ExecuteGameImpl.class.getName());
	private ExecuteGameImpl executeGameObj = null;
	int sizeOfBoard = 0;
	int noOfTreasures = 0;
	
	public ExecuteGameImpl getExecuteGameObj(){
		return executeGameObj;
	}
	
	public void setExecuteGameObj(ExecuteGame executeGameObj){
		this.executeGameObj = (ExecuteGameImpl) executeGameObj;
	}
	
	
	
	/**
	 * Call back to all clients with beginning board
	 * state
	 * @throws RemoteException 
	 * */
	public void callBackAllClients() throws RemoteException{
		for( ClientInterface eachClient : executeGameObj.clientList){
			logObject.info("Call back executed for player with id "+eachClient.getSelfId());
			// Send the alert to the given user.
			// If this fails, remove them from the list
			try {
				eachClient.callBackFromServer(this.executeGameObj.getBoard());
			} catch (RemoteException re) {
                System.out.println(
					"Exception alerting client, removing it.");
				System.out.println(re);
				executeGameObj.clientList.remove(eachClient);
			}
		}
	}
	
	/**
	 * Method that takes size of board and 
	 * number of treasures as input from user*/
	public void takeInputAtServer(){
		Scanner s = new Scanner(System.in);
		System.out.print("Enter size of board : ");
		sizeOfBoard = s.nextInt();
		System.out.print("Enter number of treasures to be generated: ");
		noOfTreasures = s.nextInt();
		logObject.info("Executing game!!");
	}
	
	/**
	 * Method to start waiting time of 20 seconds
	 * at server side
	 * @throws RemoteException 
	 * */
	public void waitTwentySecAtServer() throws RemoteException{
		executeGameObj.waitTwentySeconds();
	}
	
	/**
	 * Method to bind a stub to a name in 
	 * rmi registry on local system
	 * */
	public void bindNameToStubAtRegistry(){
		ExecuteGame stub = null;
		Registry registry = null;
		executeGameObj = (ExecuteGameImpl) ExecuteGameImpl.getInstance();
		executeGameObj.init(sizeOfBoard, noOfTreasures);

		try {
            System.setProperty("java.rmi.server.hostname","127.0.0.1");
			stub = (ExecuteGame) UnicastRemoteObject.exportObject(executeGameObj, 0);
			registry = LocateRegistry.getRegistry();
			registry.bind("Primary", stub);
			System.err.println("Server ready");

		} catch (Exception e) {
			try{
				registry.unbind("Primary");
				registry.bind("Primary",stub);
				System.err.println("Server ready");
				}catch(Exception ee){
				System.err.println("Server exception: " + ee.toString());
				ee.printStackTrace();
			}
		}

	}
	
	public static void main(String[] args) throws RemoteException {
		Server serverObj = new Server();
		serverObj.takeInputAtServer();
		serverObj.bindNameToStubAtRegistry();
		serverObj.waitTwentySecAtServer();
		logObject.info("Waiting period over, board set!! Size = "+serverObj.executeGameObj.getBoard().getSize());
		serverObj.callBackAllClients();
	}
}
