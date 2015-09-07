import java.rmi.RemoteException;
import java.rmi.Remote;


public interface ExecuteGame extends Remote {
//	Board board = null;
	public void startGame() throws RemoteException;
	public String joinGame() throws RemoteException;
	public void movePlayer() throws RemoteException;
}
