import java.io.PrintWriter;
import java.util.concurrent.Executors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final int PORT= 3000;
    public static ConcurrentHashMap<String, Handler> clients = new ConcurrentHashMap<String, Handler>();
    public static void main(String[] args) throws Exception {
        System.out.println("The server is running...");
        ExecutorService pool = Executors.newFixedThreadPool(500);
        try (MyServerSocket listener = new MyServerSocket(PORT)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }
    }
    
    public static class Handler implements Runnable {
        private String name;
        private MySocket socket;
        final int time = 50;
        private String lastMsg = "reseted"; 
        public BufferedReader in = null;
        public PrintWriter out = null;
        public Handler(MySocket sc){
            this.socket = sc;
            this.in = new BufferedReader(new InputStreamReader(this.socket.MyGetInputStream()));
            this.out = new PrintWriter(socket.MyGetOutputStream(), true);
        }
        public void run(){
            
            while(true){
                
                this.out.print("Enter username: \n");
                this.out.flush();
                try{
                    this.name = this.in.readLine();
                }catch(IOException e){
                    System.out.println(e);
                }
                if(!clients.containsKey(this.name)){
                    clients.put(this.name, this);
                    for(Handler ms : Server.clients.values()){
                        ms.out.print("             "+this.name+" has joined the chat\n");
                        ms.out.flush();
                    }
                    
                    System.out.println("NEW USER: "+this.name);
                    
                    String temp = "updateUser";
                    for(Handler ms : Server.clients.values()){
                          temp += "-" + ms.name;
                    }
                    for(Handler ms : Server.clients.values()){
                         ms.out.print(temp + "\n");
                         ms.out.flush();
                    }
                    break;
                }else{
                    this.out.println("NAME ALREADY TAKEN\n");
                    this.out.flush();
                }
                clients.put(this.name, this);
                
            }
            while(true){
                try {
                    
                    if (this.in.ready()) {
                        this.lastMsg = this.in.readLine();
                        System.out.println("receive message: '" + this.lastMsg + "'");
                        if(this.lastMsg.equals("ClosingOperation_ACK_1234")){                           
                            clients.remove(this.name);
                            String temp = "updateUser";
                            for(Handler ms : Server.clients.values()){
                                  temp += "-" + ms.name;
                            }
                            for(Handler ms : Server.clients.values()){
                                ms.out.print("             "+this.name+" has left the chat\n");
                                ms.out.flush();
                                ms.out.print(temp + "\n");
                                ms.out.flush();
                            }
                            break;
                        }
                        
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                if(this.lastMsg.contains("updateUser")){
                    String temp = "updateUser";
                    for(Handler ms : Server.clients.values()){
                          temp += "-" + ms.name;
                    }
                    for(Handler ms : Server.clients.values()){
                         ms.out.print(temp + "\n");
                         ms.out.flush();
                    }
                }else if(!"reseted".equals(this.lastMsg) && !this.lastMsg.contains("null")){
                    for(Handler ms : Server.clients.values()){
                        if(!ms.name.equals(this.name)){
                            ms.out.print("- "+this.name+": "+this.lastMsg+"\n");
                            ms.out.flush();
                        }
                    }
                }
                this.lastMsg = "reseted";
            }
            this.socket.close();
            try {
                this.in.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.out.close();
            

            
        }
    }
}
