package whiteboardServer;

import rmiRemote.IRemoteWhiteBoard;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author Rui Liu 1111181
 * @create 2022-05-24 15:28
 */

//to start the server and rmi
public class server {
    public static void main(String[] args) {

        try {
            IRemoteWhiteBoard server =  new whiteboardServer();
            int port = Integer.parseInt(args[0]);
            if ( port > 65535 || port < 1024 ){
                System.out.print("Error: Port is wrong! Please try again!");
                return;
            }
            Registry rmiregistry = LocateRegistry.createRegistry(port);
            rmiregistry.bind("whiteboard", server);
            System.out.println("Port is: "+ port + " \nserver is ready");
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
