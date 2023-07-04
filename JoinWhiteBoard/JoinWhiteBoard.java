package JoinWhiteBoard;

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
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import rmiRemote.IClient;
import rmiRemote.IRemoteWhiteBoard;

/**
 * @author Rui Liu 1111181
 * @create 2022-05-24 15:29
 */

// to join the whiteboard object
public class JoinWhiteBoard extends UnicastRemoteObject implements Serializable, IClient {

    @Serial
    private static final long serialVersionUID = 1L;
    protected String userName;
    protected String hostName;
    protected String serviceName;
    protected String clientServiceName;
    protected clientUI GUI;
    protected IRemoteWhiteBoard whiteBoard;


    public JoinWhiteBoard(String username,String IP,String port) throws RemoteException {
        this.userName = username.trim();
        this.hostName  = IP + ":" + port;
        this.serviceName = "whiteboard";
        this.clientServiceName = "Create";
        this.GUI = new clientUI();
    }

    public static void main(String [] args) {
        JoinWhiteBoard joinWhiteBoard;
        try {
            if(args[2].equals("")) {
                JOptionPane.showMessageDialog(null, "The username cannot be empty", "error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            joinWhiteBoard = new JoinWhiteBoard(args[2],args[0],args[1]);
            joinWhiteBoard.connect();
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
            if(!whiteBoard.clientsUnnull()) {
                JOptionPane.showMessageDialog(null, "Connection failed, this is a empty room", "error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            whiteBoard.isSameName(commandMessage);
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
        return false;
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
