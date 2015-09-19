import java.io.Console;
import java.io.IOException;
import java.io.Reader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.text.ChangedCharSetException;

public class Client {

	int selfId = 0;
    public Client() {}

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

    public static void main(String[] args) throws InterruptedException, IOException {

    Client clientObj = new Client();

    //Code to convert console mode for un-buffered input

	String host = (args.length < 1) ? null : args[0];
//	System.out.println("host is   " +  host);
	if (System.getSecurityManager() == null) {
		System.setSecurityManager(new SecurityManager());
	}
	try {
		Registry registry = LocateRegistry.getRegistry(host);
		ExecuteGame stub = (ExecuteGame) registry.lookup("Game");
		Board board = stub.joinGame();
		board.printBoard();
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
				System.out.println();
				board.printBoard();

			}
		} catch (Exception e) {
		    System.err.println("Client exception: " + e.toString());
		    e.printStackTrace();
		}
    }
}
