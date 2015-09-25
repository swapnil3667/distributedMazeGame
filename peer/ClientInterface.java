import java.io.IOException;
import java.rmi.*;

/** Client -- the interface for the client callback */
public interface ClientInterface extends Remote {
	public void callBackFromServer(Board board) throws RemoteException;
	public void setSelfId(int id) throws RemoteException;
	public int getSelfId() throws RemoteException;
	public void startClient(String[] args) throws RemoteException;
	public void enablePlayerMove() throws RemoteException, InterruptedException, IOException;
	public void setExecuteGameObj(ExecuteGame executeGameStub) throws RemoteException;
	public void setIsClientPrimary(boolean isClientPrimary) throws RemoteException;
    public boolean getIsClientPrimary() throws RemoteException;
    public void setIsClientBackup(boolean isClientSecondary) throws RemoteException;
    public boolean getIsClientBackup() throws RemoteException;
    public boolean isBackupAlive(Board board) throws RemoteException;
}
