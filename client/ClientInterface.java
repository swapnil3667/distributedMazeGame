import java.rmi.*;

/** Client -- the interface for the client callback */
public interface ClientInterface extends Remote {
	public void alert(Board board) throws RemoteException;
	public void setSelfId(int id) throws RemoteException;
	public int getSelfId() throws RemoteException;
}
