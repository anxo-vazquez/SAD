import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySocket extends Socket{
    Socket socket;
    public MySocket(String host,int port){
        try{
            this.socket=new Socket(host,port);
        }catch(IOException ex){
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    public MySocket(Socket soc){
            this.socket=soc;
        }
    public void MyConnect(SocketAddress endpoint){
        try{
            this.socket.connect(endpoint);
        }catch(IOException ex){
             Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    public InputStream MygetInpuStream(){
        try{
            return socket.getInputStream();
        }catch(IOException ex){
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public OutputStream MygetOutputStream(){
        try{
            return this.socket.getOutputStream();
        }catch(IOException ex){
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public void Myclose(){
        try{
            this.socket.close();
        }catch(IOException ex){
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
