import java.rmi.RemoteException;
import java.rmi.Remote;


public interface ExecuteGame extends Remote {
//	Board board = null;
	public void startGame() throws RemoteException;
	public void joinGame() throws RemoteException;
	public void movePlayer() throws RemoteException;
}
