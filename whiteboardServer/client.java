package whiteboardServer;

import rmiRemote.IClient;

/**
 * @author Rui Liu 1111181
 * @create 2022-05-24 15:28
 */

//to store the information of clients
public class client {
    public String name;
    public IClient client;

    //constructor
    public client(String name, IClient client){
        this.name = name;
        this.client = client;
    }
    //getters and setters
    public String getName(){
        return name;
    }
    public IClient getClient(){
        return client;
    }
}
