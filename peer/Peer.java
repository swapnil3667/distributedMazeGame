import java.rmi.RemoteException;

public interface Peer {
	public boolean isPrimaryOrBackup(String[] args);
	public boolean getIsPlayerPrimary();
	public boolean getIsPlayerBackup();
	public void startServerAsPrimary() throws RemoteException;
	public void startClientOnThisPeer(String[] args) throws RemoteException;
	public void selectPlayerForBackup() throws RemoteException;

}
