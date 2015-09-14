import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {}

    public static void main(String[] args) {

	String host = (args.length < 1) ? null : args[0];
	System.out.println("host is   " +  host);
	if (System.getSecurityManager() == null) {
		System.setSecurityManager(new SecurityManager());
	}
	try {
		Registry registry = LocateRegistry.getRegistry(host);
		ExecuteGame stub = (ExecuteGame) registry.lookup("Game");
		
		System.out.println(stub.testStringReponse());
//        System.setProperty("java.rmi.server.codebase", "file:/home/swapnil/Documents/Distributed_System/distributedMazeGame/server/");
		Board board = stub.joinGame();
//		    System.out.println("response: "+stub.joinGame());
		System.out.println("Status on Client side Board Size : " + board.getSize());
//		    board.printCurrentBoardState();
		
		} catch (Exception e) {
		    System.err.println("Client exception: " + e.toString());
		    e.printStackTrace();
		}
    }
}
