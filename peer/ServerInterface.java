import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public interface ServerInterface {
//	public ExecuteGameImpl getExecuteGameObj();
	public void callBackAllClients() throws RemoteException;
	public void takeInputAtServer();
	public void waitTwentySecAtServer() throws RemoteException;
	public void bindNameToStubAtRegistry();
	public ExecuteGameImpl getExecuteGameObj();
	public void setExecuteGameObj(ExecuteGame executeGameObj);
	
}
