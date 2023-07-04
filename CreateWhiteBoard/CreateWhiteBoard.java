package CreateWhiteBoard;

import rmiRemote.IClient;
import rmiRemote.IRemoteWhiteBoard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Rui Liu 1111181
 * @create 2022-05-24 15:25
 */

// to create a whiteboard object and enable the manager to perform operations.
public class CreateWhiteBoard extends UnicastRemoteObject implements Serializable, IClient {

    @Serial
    private static final long serialVersionUID = 1L;
    protected String userName;
    protected String hostName;
    protected String serviceName;
    protected String clientServiceName;
    protected managerUI GUI;
    protected IRemoteWhiteBoard whiteBoard;

    public CreateWhiteBoard(String username,String IP,String port) throws RemoteException{
        this.userName = username.trim();
        this.hostName  = IP + ":" + port;
        this.serviceName = "whiteboard";
        this.clientServiceName = "Create";
        this.GUI = new managerUI();
    }

    public static void main(String [] args) {
        CreateWhiteBoard createWhiteBoard;
        try {
            if(args[2].equals("")) {
                JOptionPane.showMessageDialog(null, "The username cannot be empty", "error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            createWhiteBoard = new CreateWhiteBoard(args[2],args[0],args[1]);
            createWhiteBoard.connect();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    // connect with server by rmi
    public void connect() {
        try {
            //RMI
            String[] commandMessage = {userName,hostName,clientServiceName};
            Naming.rebind("rmi://" + hostName + "/" + clientServiceName, this);
            whiteBoard = (IRemoteWhiteBoard) Naming.lookup("rmi://" + hostName + "/" + serviceName);
            whiteBoard.isEmpty(commandMessage);
            whiteBoard.registerListener(commandMessage);
            GUI.createWhiteBoard(whiteBoard);
            GUI.setUsername(userName);
            GUI.getpanel().setwb(whiteBoard);
        } catch (RemoteException | NotBoundException e) {
            JOptionPane.showMessageDialog(null, "Connection failed", "error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(0);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public String getName() throws RemoteException {
        return userName;
    }
    //receive broadcast
    @Override
    public void messageFromServer(String message) throws RemoteException {
        GUI.gettextArea().append(message);
    }
    //update information
    @Override
    public void clientUpdate(String[] userList) throws RemoteException {
        GUI.getJlist().setListData(userList);
    }
    //to judge whether client can join
    @Override
    public boolean judge(String clientName) throws RemoteException {
        int flag = JOptionPane.showConfirmDialog(null,clientName + " wants to join the whiteboard\n" + "approve or not?","Judge", JOptionPane.YES_NO_OPTION);
        return flag == 1;
    }
    //load the picture
    @Override
    public void load(byte[] bytes) throws RemoteException {
        try {
            ByteArrayInputStream picture = new ByteArrayInputStream(bytes);
            BufferedImage image = ImageIO.read(picture);
            GUI.getpanel().load(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reject(String message) throws RemoteException {
        JOptionPane.showMessageDialog(null, message + "the request has been rejected", "error", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }

    @Override
    public void info(String message) throws RemoteException {
        Thread thread = new Thread(()->{
            JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
        });
        thread.start();
    }
}

