import java.rmi.RemoteException;
import java.rmi.Remote;


public interface ExecuteGame extends Remote {
	Board board = null;
	public Location generatePlayerLocation(int id) throws RemoteException;
	public int generatePlayerId() throws RemoteException;
	public void startGame() throws RemoteException;
	public Board joinGame() throws RemoteException;
	public void movePlayer() throws RemoteException;
	public void waitTwentySeconds() throws RemoteException;
	public String testStringReponse() throws RemoteException;
}
