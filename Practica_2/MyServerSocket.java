import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyServerSocket extends ServerSocket{
    Socket socket;
    ServerSocket serverSocket;
    MySocket mysocket;
    public MyServerSocket(int port) throws IOException{
        this.serverSocket=new ServerSocket(port);
    }
    public MySocket accept(){
       try{
           this.socket=serverSocket.accept();
           this.mysocket=new MySocket(socket);
           return mysocket;
       }catch(IOException ex){
           Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
       }
       return null;
    }
    public void close(){
        try {
            this.serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(MyServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
