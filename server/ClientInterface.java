import java.rmi.*;

/** Client -- the interface for the client callback */
public interface ClientInterface extends Remote {
	public void alert(String mesg) throws RemoteException;
	public void setSelfId(int id) throws RemoteException;
}
