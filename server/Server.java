import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.List;

public class Server{
	private static Logger logObject = Logger.getLogger(ExecuteGameImpl.class.getName());

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.print("Enter size of board : ");
		int sizeOfBoard = s.nextInt();
		System.out.print("Enter number of treasures to be generated: ");
		int noOfTreasures = s.nextInt();
		logObject.info("Executing game!!");

		//System.setProperty("java.rmi.server.codebase", "file:///home/swapnil/Documents/Distributed_System/distributedMazeGame/server/");
		ExecuteGame stub = null;
		Registry registry = null;
		ExecuteGameImpl obj = (ExecuteGameImpl) ExecuteGameImpl.getInstance();
		obj.init(sizeOfBoard, noOfTreasures);

		try {
			stub = (ExecuteGame) UnicastRemoteObject.exportObject(obj, 0);
			registry = LocateRegistry.getRegistry();
			registry.bind("Game", stub);
			System.err.println("Server ready");

			obj.waitTwentySeconds();
			System.out.print("Value of Twentysecflag: " + obj.isTwentySecOver);
			if(obj.isTwentySecOver == false){

					Iterator it = obj.list.iterator();
					int i =1;
					while (it.hasNext()){
							String mesg = ("Client "+ i +" has been registered");
							// Send the alert to the given user.
							// If this fails, remove them from the list
							try {
								((ClientInterface)it.next()).alert(mesg);
							} catch (RemoteException re) {
			                    System.out.println(
									"Exception alerting client, removing it.");
								System.out.println(re);
								it.remove();
							}
						i++;
					}
			}
		} catch (Exception e) {
//				e.printStackTrace();
			try{
				registry.unbind("Game");
				registry.bind("Game",stub);
				System.err.println("Server ready");
 				obj.waitTwentySeconds();
				System.out.print("Value of Twentysecflag: " + obj.isTwentySecOver);
				if(obj.isTwentySecOver == false){

						Iterator it = obj.list.iterator();
						int i =1;
						while (it.hasNext()){
								String mesg = ("Client "+ i +" has been registered");
								// Send the alert to the given user.
								// If this fails, remove them from the list
								try {
									((ClientInterface)it.next()).alert(mesg);
								} catch (RemoteException re) {
														System.out.println(
										"Exception alerting client, removing it.");
									System.out.println(re);
									it.remove();
								}
							i++;
						}
				}

				}catch(Exception ee){
				System.err.println("Server exception: " + ee.toString());
				ee.printStackTrace();
			}
		}
	}
}
