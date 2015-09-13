import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.logging.Logger;

public class Server{
	private static Logger logObject = Logger.getLogger(ExecuteGameImpl.class.getName());

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.print("Enter size of board : ");
		int sizeOfBoard = s.nextInt();
		System.out.print("Enter number of treasures to be generated: ");
		int noOfTreasures = s.nextInt();
		logObject.info("Executing game!!");

		//System.setProperty("java.rmi.server.codebase", "file:///home/swapnil/Documents/Distributed_System/distributedMazeGame/server/");
		ExecuteGame stub = null;
		Registry registry = null;
		ExecuteGameImpl obj = ExecuteGameImpl.getInstance(sizeOfBoard, noOfTreasures);

		try {
			stub = (ExecuteGame) UnicastRemoteObject.exportObject(obj, 0);
			registry = LocateRegistry.getRegistry();
			registry.bind("Game", stub);
			System.err.println("Server ready");
			obj.waitTwentySeconds();

		} catch (Exception e) {
			try{
				registry.unbind("Game");
				registry.bind("Game",stub);
				System.err.println("Server ready");
				obj.waitTwentySeconds();
				}catch(Exception ee){
				System.err.println("Server exception: " + ee.toString());
				ee.printStackTrace();
			}
		}
	}
}
