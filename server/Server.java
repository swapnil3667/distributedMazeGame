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
		int sizeOfBoard = s.nextInt();
		int noOfTreasures = s.nextInt();
		logObject.info("Executing game!!");
//		ExecuteGameImpl testObj = new ExecuteGameImpl(sizeOfBoard, noOfTreasures);
//		System.out.println("Enter 1 ");
//		testObj.joinGame();
//		testObj.joinGame();
//		testObj.joinGame();


		ExecuteGame stub = null;
		Registry registry = null;

		try {
			ExecuteGameImpl obj = ExecuteGameImpl.getInstance(sizeOfBoard, noOfTreasures);
			stub = (ExecuteGame) UnicastRemoteObject.exportObject(obj, 0);
			registry = LocateRegistry.getRegistry();
			registry.bind("Game", stub);

			System.err.println("Server ready");
		} catch (Exception e) {
			try{
				registry.unbind("Game");
				registry.bind("Game",stub);
				System.err.println("Server ready");
			}catch(Exception ee){
				System.err.println("Server exception: " + ee.toString());
				ee.printStackTrace();
			}
		}

	}
}
