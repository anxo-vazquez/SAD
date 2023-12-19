package y;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 *
 * @author anxovazquez
 */
public class Server {

    private ServerSocket serverSocket;
    private ConcurrentHashMap<String, Socket> clients;
    protected Backend backend;

    public Server(Backend backend) {
        clients = new ConcurrentHashMap<>();
        this.backend = backend;
        try {
            serverSocket = new ServerSocket(5555);
            System.out.println("Server Initialised");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loginClient(Socket clientSocket, String client) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // Auto flush enabled
        ) {
            Gson gson = new Gson();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                Message received = gson.fromJson(inputLine, Message.class);
                System.out.println(inputLine);
                if (received.getType().equals("LOGIN")) {
                    UserCredentials credentials = gson.fromJson(inputLine, UserCredentials.class);
                    if (authenticate(credentials.getUsername(), credentials.getPassword())) {
                        out.println("OK"); // Login successful
                        handleClient(clientSocket, client, credentials.getUsername());
                    } else {
                        out.println("NULL_LOGIN"); // Login failed
                    }
                } else {
                    out.println("INVALID"); // Invalid message type
                }
            }
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace(); // Log or handle the exception
        }
    }

    private boolean authenticate(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("credentials.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    if (parts[0].equals(username) && parts[1].equals(password)) {
                        return true; // Username and password match
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // No matching credentials found
    }

    private void handleClient(Socket clientSocket, String client, String username) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // Auto flush enabled
            String inputLine;
            Gson gson = new Gson();
            Gson gsonP = new GsonBuilder().registerTypeAdapter(Post.class, new PostSerializer()).create();

            while ((inputLine = in.readLine()) != null) {
                Message received = gson.fromJson(inputLine, Message.class);
                System.out.println(inputLine);

                if (received.getType().equals("LOGOUT")) {
                    out.println("");
                    loginClient(clientSocket, client);
                } else if (received.getType().equals("CLOSE")) {
                    System.out.println(this.clients.toString());
                    System.out.println(inputLine);
                    System.out.println("Closing connection");
                    clientSocket.close();
                } else if (received.getType().equals("FEED")) {
                    List<Post> feed = this.backend.get_feed(username);
                    System.out.println(feed.toString());
                    String feedJson = gsonP.toJson(feed);
                    System.out.println(feed.toString());
                    out.println(feedJson); // Send the feed as a JSON string
                } else if (received.getType().equals("GET_USER")) {
                    if (this.backend.getUsers().containsKey(received.getUsername())) {
                        List<Post> usr = this.backend.getUser(received.getUsername()).getPosts();
                        System.out.println(usr.toString());
                        String usrJson = gsonP.toJson(usr);
                        System.out.println(usr.toString());
                        out.println(usrJson); // Send the feed as a JSON string
                    }} else if (received.getType().equals("POST")) {
                    if (this.backend.getUsers().containsKey(received.getUsername())) {
                        System.out.println(received.getContent());
                        Post post = new Post(this.backend.getUsers().get(received.getUsername()),LocalDate.now(),received.getContent());
                        this.backend.getUser(received.getUsername()).addPost(post);
                        System.out.println(post.toString());
                        out.println("POST_OK"); // Send the feed as a JSON string 
                    }
                    else {
                        out.println("NULL_USER"); // Send the error

                    }
                } else {
                    System.out.println(inputLine);
                    out.println("INVALID");
                }
            }

        } catch (

        IOException e) {
            e.printStackTrace();
        } finally {
            clients.remove(client);
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

                Thread thread = new Thread(() -> loginClient(clientSocket, client));
                thread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class PostSerializer implements JsonSerializer<Post> {
        @Override
        public JsonElement serialize(Post post, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();

            // Assuming you want to include the username and not the whole User object
            jsonObject.addProperty("username", post.getUser().getUsername());
            jsonObject.addProperty("date", post.getDate().toString());
            jsonObject.addProperty("content", post.getContent());

            return jsonObject;
        }
    }

}
