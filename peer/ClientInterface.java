import java.io.IOException;
import java.rmi.*;
import java.util.List;

/** Client -- the interface for the client callback */
public interface ClientInterface extends Remote {
	public void callBackFromServer(Board board) throws RemoteException;
	public void setSelfId(int id) throws RemoteException;
	public int getSelfId() throws RemoteException;
	public void startClient(String[] args) throws RemoteException;
	public void enablePlayerMove() throws RemoteException, InterruptedException, IOException, NotBoundException;
	public void setExecuteGameObj(ExecuteGame executeGameStub) throws RemoteException;
	public void setIsClientPrimary(boolean isClientPrimary) throws RemoteException;
    public boolean getIsClientPrimary() throws RemoteException;
    public void setIsClientBackup(boolean isClientSecondary) throws RemoteException, InterruptedException;
    public boolean getIsClientBackup() throws RemoteException;
    public boolean isBackupAlive(Board board,List<ClientInterface> listOfClients, ClientInterface primaryClient) throws RemoteException, InterruptedException, IOException, CloneNotSupportedException;
    public void setBackupExecuteGameStub(ExecuteGame backupExecuteGameStub) throws RemoteException;
    public ExecuteGame getBackupExecuteGameStub() throws RemoteException;
    public Thread getPollingWithBackup() throws RemoteException;  
}
