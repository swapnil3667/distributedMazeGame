import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.logging.Logger;

public class server{
	private static Logger logObject = Logger.getLogger(ExecuteGameImpl.class.getName());
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int sizeOfBoard = s.nextInt();
		int noOfTreasures = s.nextInt();
		logObject.info("Executing game!!");
		ExecuteGameImpl testObj = new ExecuteGameImpl(sizeOfBoard, noOfTreasures);
		testObj.joinGame();
		testObj.joinGame();
		testObj.joinGame();
			
	}
}