import java.rmi.RemoteException;
import java.rmi.Remote;


public interface ExecuteGame extends Remote {
	public Location generatePlayerLocation(int id) throws RemoteException;
	public int generatePlayerId() throws RemoteException;
	public void startGame() throws RemoteException;
	public Board joinGame() throws RemoteException;
	public Board movePlayer(int id, String moveDirection) throws RemoteException;
	public void waitTwentySeconds() throws RemoteException;
	public String testStringReponse() throws RemoteException;
	public Board getBoard() throws RemoteException;
	public void setBoard(Board board)  throws RemoteException;
	public int getFirstPlayerId() throws RemoteException;
}
