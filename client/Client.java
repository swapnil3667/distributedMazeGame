import java.io.Console;
import java.io.IOException;
import java.io.Reader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

import javax.swing.text.ChangedCharSetException;

public class Client extends UnicastRemoteObject implements ClientInterface {
    int selfId = 0;
    public Client() throws RemoteException{}

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

    public void alert(String message){
      System.out.println(message);
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
    	return null;
    }

    /**
     * This is test function that populates self id
     * with first id in board.playerList.get(0) because
     * there is no call back functionality as of yet
     * */
    public void testFunction_populateSelfId(Board board){
    	selfId = 1234;
    }

    public static void main(String[] args) throws InterruptedException, IOException {

    Client clientObj = new Client();


	String host = (args.length < 1) ? null : args[0];
	if (System.getSecurityManager() == null) {
		System.setSecurityManager(new SecurityManager());
	}
	try {
		Registry registry = LocateRegistry.getRegistry(host);
		ExecuteGame stub = (ExecuteGame) registry.lookup("Game");
		Board board = stub.joinGame(clientObj);

		/*TO BE REMOVED*/
		clientObj.testFunction_populateSelfId(board);
		/*TO BE REMOVED*/

		board.printBoard(clientObj.selfId);
		while(true){

				clientObj.changeConsoleModeStty();
				Console console = System.console();
				Reader reader = console.reader();
				int consoleInput = reader.read();
				clientObj.resetConsoleMode();

					if(consoleInput == 120) break; //breaks on 'X' press

				String directionToMove = clientObj.getDirectionOnKeyPress(consoleInput);

				//Calling move player to test
				int id = stub.getFirstPlayerId();
				board = stub.movePlayer(id, directionToMove);
//				System.out.println("After move");
				Runtime.getRuntime().exec("clear");
				board.printBoard(clientObj.selfId);

			}
		} catch (Exception e) {
		    System.err.println("Client exception: " + e.toString());
		    e.printStackTrace();
		}
    }
}
