import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    private static final int PORT = 3333;
    public static ConcurrentHashMap<String, Handler> clients = new ConcurrentHashMap<String, Handler>();

    public static void main(String[] args) throws IOException {
        System.out.println("Servidor en marxa...");
        ExecutorService pool = Executors.newFixedThreadPool(500);
        try (MyServerSocket reciever = new MyServerSocket(PORT)) {
            do {
                pool.execute(new Handler(reciever.accept()));
            } while (true);
        }
    }

    public static class Handler implements Runnable {
        public PrintWriter out = null;
        public BufferedReader in = null;
        private String msg, Name;
        int time = 50;
        private MySocket socket;

        public Handler(MySocket so) {
            this.socket = so;
            this.in = new BufferedReader(new InputStreamReader(this.socket.MygetInpuStream()));
            this.out = new PrintWriter(this.socket.MygetOutputStream(), true);
        }

        public void run() {
            do {
                this.out.println("Digues el teu nick: ");
                try {
                    this.Name = in.readLine();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
                if (!clients.containsKey(this.Name)) {
                    clients.put(this.Name, this);
                    for (Handler h : Servidor.clients.values()) {
                        h.out.println(this.Name + "s'ha unit al xat");
                        h.out.flush();
                    }
                    System.out.println("Nick:" + this.Name);
                    break;
                } else {
                    this.out.println("Aquest nick ja està agafat");
                    this.out.flush();
                }
                clients.put(this.Name, this);
            } while (true);

            do {
                try {
                    if (this.in.ready()) {
                        this.msg = this.in.readLine();
                        System.out.println("Missatge rebut " + this.msg);
                        if (this.msg.equals("Log out")) {
                            clients.remove(this.Name);
                            for (Handler h : Servidor.clients.values()) {
                                h.out.println(this.Name + " s'ha anat del grup");
                                h.out.flush();
                            }
                            break;
                        }

                    }
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (this.msg == null || !this.msg.equals("Reseted")) {
                    for (Handler h : Servidor.clients.values()) {
                        h.out.println(this.Name + " : " + this.msg);
                        h.out.flush();
                    }
                }
                this.msg = "Reseted";
            } while (true);
            try {
                this.socket.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                this.in.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.out.close();
        }
    }

}
