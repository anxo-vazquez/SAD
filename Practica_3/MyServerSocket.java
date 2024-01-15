import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyServerSocket extends ServerSocket {
    ServerSocket serverSocket;
    MySocket socket;
    public MyServerSocket(int port) throws IOException{
       
        this.serverSocket = new ServerSocket(port);
    }
    
    @Override
    public MySocket accept(){
        try {
            this.socket = new MySocket(serverSocket.accept());
            return socket;
        } catch (IOException ex) {
            Logger.getLogger(MyServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return  null;
    }
    @Override
    public void close(){
        try {
            this.serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(MyServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}


