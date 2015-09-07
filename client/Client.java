import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {}

    public static void main(String[] args) {

	String host = (args.length < 1) ? null : args[0];
	System.out.println("host is   " +  host);
		try {
		    Registry registry = LocateRegistry.getRegistry(host);
		    ExecuteGame stub = (ExecuteGame) registry.lookup("Game");
		    System.out.println("response: "+stub.joinGame());
		    
		} catch (Exception e) {
		    System.err.println("Client exception: " + e.toString());
		    e.printStackTrace();
		}
    }
}
