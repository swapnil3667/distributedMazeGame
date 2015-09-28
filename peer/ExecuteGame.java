import java.rmi.RemoteException;
import java.io.IOException;
import java.rmi.Remote;


public interface ExecuteGame extends Remote {
	public Location generatePlayerLocation(int id) throws RemoteException;
	public int generatePlayerId() throws RemoteException;
	public void startGame() throws RemoteException;
	public void setFlagTwentySecOver(boolean flag) throws RemoteException;
	public boolean joinGame(ClientInterface client) throws RemoteException;
	public Board movePlayer(int id, String moveDirection) throws RemoteException, InterruptedException, IOException;
	public void waitTwentySeconds() throws RemoteException;
	public String testStringReponse() throws RemoteException;
	public Board getBoard() throws RemoteException;
	public void setBoard(Board board)  throws RemoteException;
	public void changeConsoleModeStty() throws InterruptedException, IOException;
	public void resetConsoleMode() throws InterruptedException, IOException;
	public ClientInterface getClientObjectWithId(int clientPlayerId) throws RemoteException;
	public boolean isPrimaryAlive() throws RemoteException;
	public void removeClientFromClientList(ClientInterface removeClient) throws RemoteException;
}
