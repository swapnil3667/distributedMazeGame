import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface Peer {
	public boolean isPrimaryOrBackup(String[] args);
	public boolean getIsPlayerPrimary();
	public boolean getIsPlayerBackup();
	public void startServerAsPrimary() throws RemoteException, InterruptedException, IOException, NotBoundException;
	public void startClientOnThisPeer(String[] args) throws RemoteException;
	public void selectPlayerForBackup() throws RemoteException, InterruptedException;
	public void startServerAsBackup() throws RemoteException;
	public void setServerObj(ServerInterface serverObj);
	public ServerInterface getServerObj();
	public void setClientObj(ClientInterface clientObj);
	public ClientInterface getClientObj();
}
