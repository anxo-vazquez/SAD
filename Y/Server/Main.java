package Y.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author anxovazquez
 */
public class Main { // Incluye el test por terminal

    public static void main(String[] args) {
        Server server = new Server();
        User me = new User("Manolo");
        Feed feed = new Feed(me);
        Post post = new Post(me, "Post1", "Awesome");
        server.putUsers(me);
        User another = new User("Pepe");
        server.putUsers(another);
        System.out.println(server.getUsers().values().toString());

        me.posts.add(post);

        System.out.println(feed.getPosts().toString());

        try {
            ServerSocket serversocket = new ServerSocket(12345);
            Socket client = serversocket.accept();

            final DataInputStream input = new DataInputStream(client.getInputStream());
            final DataOutputStream output = new DataOutputStream(client.getOutputStream());

            output.writeUTF(me.getPosts().toString());

            String message = (String)input.readUTF();
            System.out.println(message);

        } catch (IOException e) {
            e.printStackTrace();
        }

    System.out.println("End of service");
    }

}
