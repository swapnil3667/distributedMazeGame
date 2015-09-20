import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.logging.Logger;

public class Server{
	private static Logger logObject = Logger.getLogger(ExecuteGameImpl.class.getName());
	ExecuteGameImpl executeGameObj = null;
	
	/**
	 * Call back to all clients with beginning board
	 * state
	 * @throws RemoteException 
	 * */
	public void callBackAllClients() throws RemoteException{
		int i =1;
		for( ClientInterface eachClient : executeGameObj.clientList){
			logObject.info("Call back executed for player with id "+eachClient.getSelfId());
			String mesg = ("Client "+ i +" has been registered");
			// Send the alert to the given user.
			// If this fails, remove them from the list
			try {
				eachClient.alert(this.executeGameObj.board);
			} catch (RemoteException re) {
                System.out.println(
					"Exception alerting client, removing it.");
				System.out.println(re);
				executeGameObj.clientList.remove(eachClient);
			}
			i++;
		}
	}
	
	public static void main(String[] args) {
		Server serverObj = new Server();
		Scanner s = new Scanner(System.in);
		System.out.print("Enter size of board : ");
		int sizeOfBoard = s.nextInt();
		System.out.print("Enter number of treasures to be generated: ");
		int noOfTreasures = s.nextInt();
		logObject.info("Executing game!!");

		//System.setProperty("java.rmi.server.codebase", "file:///home/swapnil/Documents/Distributed_System/distributedMazeGame/server/");
		ExecuteGame stub = null;
		Registry registry = null;
		serverObj.executeGameObj = (ExecuteGameImpl) ExecuteGameImpl.getInstance();
		serverObj.executeGameObj.init(sizeOfBoard, noOfTreasures);

		try {
			stub = (ExecuteGame) UnicastRemoteObject.exportObject(serverObj.executeGameObj, 0);
			registry = LocateRegistry.getRegistry();
			registry.bind("Game", stub);
			System.err.println("Server ready");

			serverObj.executeGameObj.waitTwentySeconds();
			logObject.info("Waiting period over, board set!! Size = "+serverObj.executeGameObj.board.getSize());
			
			serverObj.callBackAllClients();
		} catch (Exception e) {
				e.printStackTrace();
			
			try{
				registry.unbind("Game");
				registry.bind("Game",stub);
				System.err.println("Server ready");
 				serverObj.executeGameObj.waitTwentySeconds();
 				serverObj.callBackAllClients();

				}catch(Exception ee){
				System.err.println("Server exception: " + ee.toString());
				ee.printStackTrace();
			}
		}
	}
}
