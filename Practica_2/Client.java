import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    public static final int Server_port = 3333;
    public static String name, li = "Initial";
    public static final String Server_host = "LocalHost";

    public static void main(String args[]) throws IOException {
        Scanner sc = new Scanner(System.in);
        MySocket socket = new MySocket(Server_host, Server_port);
        PrintWriter output = new PrintWriter(socket.MygetOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.MygetInpuStream()));

        Thread inputThread = new Thread(new Runnable() {
            public void run() {

                String linia;
                while ((linia = reader.nextLine()) != null) {
                    li = linia;
                    output.print(linia + "\n");
                    output.flush();
                }

            }
        });
        Thread outputThread = new Thread(new Runnable() {
            public void run() {

                try {
                    String msg;
                    while ((msg = reader.readLine()) != null) {
                        if (msg.contains(li) && msg.contains("joined")) {
                            name = li;
                        } else if (name == null) {
                            System.out.println(msg);

                        } else if (!msg.contains(name)) {
                            System.out.println(msg);

                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        inputThread.start();
        outputThread.start();
        System.out.println("Client has started");
    }
}