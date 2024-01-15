import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;
public class MySocket extends Socket{
   Socket sc; 
   public MySocket(String host, int port){
       try {
           this.sc = new Socket(host, port);
       } catch (IOException ex) {
           Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
   public MySocket(Socket socket){
       this.sc = socket;
   }
   public void MyConnect(SocketAddress endpoint){
       try {
           this.sc.connect(endpoint);
       } catch (IOException ex) {
           Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
   public InputStream MyGetInputStream(){
       try {
           return sc.getInputStream();
       } catch (IOException ex) {
           Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
       }
       return null;
   }
   public OutputStream MyGetOutputStream(){
       try {
           return sc.getOutputStream();
       } catch (IOException ex) {
           Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
       }
       return null;
   }
   @Override
   public void close(){
       try {
           sc.close();
       } catch (IOException ex) {
           Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
}  