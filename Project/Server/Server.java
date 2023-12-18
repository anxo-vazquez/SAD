package y;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import com.google.gson.Gson;

/**
 *
 * @author anxovazquez
 */
public class Server {

    private ServerSocket serverSocket;
    private ConcurrentHashMap<String, Socket> clients;

    public Server() {
        clients = new ConcurrentHashMap<>();
        try {
            serverSocket = new ServerSocket(5555);
            System.out.println("Welcome to Server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket, String username) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            Gson gson = new Gson();
            while ((inputLine = in.readLine()) != null) {
                System.out.println(this.clients.toString());
                Message received = gson.fromJson(inputLine, Message.class); 

                if (received.getType().equals("LOGIN")) {
                    System.out.println(inputLine);
                    UserCredentials credentials = gson.fromJson(inputLine, UserCredentials.class); // Correct variable name

                }
                if (received.getType().equals("CLOSE")) {
                    System.out.println(this.clients.toString());
                    System.out.println(inputLine);
                    System.out.println("Closing connection");
                    clientSocket.close();
                } else {
                    System.out.println(inputLine);
                }
            }

        } catch (

        IOException e) {
            e.printStackTrace();
        } finally {
            clients.remove(username);
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startServer() throws InterruptedException {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                String client = clientSocket.getRemoteSocketAddress().toString();
                System.out.println("New connection with " + client);

                clients.put(client, clientSocket);

                Thread thread = new Thread(() -> handleClient(clientSocket, client));
                thread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
