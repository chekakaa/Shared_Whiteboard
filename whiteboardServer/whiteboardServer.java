package whiteboardServer;

import rmiRemote.IClient;
import rmiRemote.IRemoteWhiteBoard;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * @author Rui Liu
 * @create 2022-05-24 15:28
 */


public class whiteboardServer extends UnicastRemoteObject implements IRemoteWhiteBoard {
    private ArrayList<client> clients;
    private byte[] bytes;
    // to store information of clients
    protected whiteboardServer() throws RemoteException {
        super();
        clients = new ArrayList<client>();
    }
    @Override
    // function of draw and load synchronize
    public void draw(byte[] bytes) throws RemoteException {
        this.bytes = bytes;
        for(client c : clients){
            try {
                c.getClient().load(bytes);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    //register new clients
    public synchronized void registerListener(String[] commandMessage) throws RemoteException {
        System.out.println(commandMessage[0] + " has joined the chat session");
        System.out.println(commandMessage[0] + "'s hostname : " + commandMessage[1]);
        System.out.println(commandMessage[0] + "'sRMI service : " + commandMessage[2]);

        try {
            IClient nextClient = (IClient)Naming.lookup("rmi://" + commandMessage[1] + "/" + commandMessage[2]);
            clients.add(new client(commandMessage[0], nextClient));
            if(clients.size() > 1) {
                if(judge(clients.get(clients.size() - 1).getName())) {
                    clients.remove(clients.size() - 1);
                    nextClient.reject("the manager does not approved your request\n");
                    return ;
                }
            }
            if(bytes != null) {
                nextClient.load(bytes);
            }
            nextClient.messageFromServer("[Server] : Hello " + commandMessage[0] + " you are now free to chat.\n");
            broadcast("[Server] : " + commandMessage[0] + " has joined the group.\n");
            clientUpdate();
        } catch (MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }
    }
    //update the user list
    private void clientUpdate() {
        String[] clientInWhiteBoard = new String[clients.size()];
        for(int i = 0; i< clientInWhiteBoard.length; i++){
            clientInWhiteBoard[i] = clients.get(i).getName();
        }
        for(client c : clients){
            try {
                c.getClient().clientUpdate(clientInWhiteBoard);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean clientsUnnull() throws RemoteException {
        return clients.size() != 0;
    }
    // to ask manager to judge whether client can join
    @Override
    public boolean judge(String judgement) throws RemoteException {
        return clients.get(0).getClient().judge(judgement);
    }
    // to kick out someone
    @Override
    public void kickOutClient(String username) throws RemoteException {
        int size = 0;
        for(int i = 0; i < clients.size(); i++) {
            if(clients.get(i).getName().equals(username)) {
                size = i;
            }
        }
        if(size == 0) {
            clients.get(0).getClient().info("Can not find the client");
        }
        else {
            IClient target = clients.get(size).getClient();
            String name = clients.get(size).getName();
            clients.remove(size);
            clients.get(0).getClient().info("remove success!");
            broadcast("[Manager] :" + name + " has been kicked out!\n");
            clientUpdate();
            Thread thread = new Thread(()->{
                try {
                    target.reject("Sorry, you have been kicked out by the manager\n");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }
    @Override
    public void broadcast(String message) {
        for(client c : clients){
            try {
                c.getClient().messageFromServer(message + "\n");
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void isEmpty(String[] commandMessage) throws RemoteException {
        if(clients.size() > 0) {
            try {
                IClient nextClient = (IClient)Naming.lookup("rmi://" + commandMessage[1] + "/" + commandMessage[2]);
                nextClient.reject("The room has been created\n");
            } catch (MalformedURLException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void isSameName(String[] commandMessage) throws RemoteException {
        try {
            IClient nextClient = (IClient) Naming.lookup("rmi://" + commandMessage[1] + "/" + commandMessage[2]);
            for(client c : clients){
                if(c.getName().equals(commandMessage[0])) {
                    nextClient.reject("The username has been taken\n");
                }
            }
        } catch (MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void exit(String username) throws RemoteException {
        int client = 0;
        for(int i = 0; i < clients.size(); i++) {
            if(clients.get(i).getName().equals(username)) {
                client = i;
            }
        }
        clients.remove(client);
        broadcast("[Server] :" + username + " has exited!\n");
        clientUpdate();
    }
    @Override
    public void end() throws RemoteException {
        System.exit(0);
    }

}
