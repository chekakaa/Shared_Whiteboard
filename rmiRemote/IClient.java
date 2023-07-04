package rmiRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Rui Liu 1111181
 * @create 2022-05-24 15:27
 */

//allows server to use method of client
public interface IClient  extends Remote {
    void messageFromServer(String message) throws RemoteException;
    public void clientUpdate(String[] userList) throws RemoteException;
    public boolean judge(String clientName) throws RemoteException;
    public void load(byte[] bytes) throws RemoteException;
    public void reject(String message)throws RemoteException;
    public void info(String message) throws RemoteException;
}
