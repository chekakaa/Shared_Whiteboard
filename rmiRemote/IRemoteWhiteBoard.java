package rmiRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Rui Liu 1111181
 * @create 2022-05-24 15:28
 */

// allows client to use method of server
public interface IRemoteWhiteBoard extends Remote {
    void draw(byte[] bytes) throws RemoteException;
    boolean clientsUnnull() throws RemoteException;
    void registerListener(String[] commandMessage)throws RemoteException;
    boolean judge(String judgement) throws RemoteException;
    void kickOutClient(String username) throws RemoteException;
    void broadcast(String message) throws RemoteException;
    void isEmpty(String[] commandMessage) throws RemoteException;
    void isSameName(String[] commandMessage) throws RemoteException;
    void exit(String username) throws RemoteException;
    void end() throws RemoteException;
}
