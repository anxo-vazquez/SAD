package y;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author anxovazquez
 */
public class Main { // We include the "Database Data"
    public static void main(String[] args) throws InterruptedException {
        Backend backend = new Backend();
        LocalDate date = LocalDate.now();
        User user1 = new User("Anxo_Vazquez");
        User user2 = new User("Joan_1");
        User user3 = new User("Arnau_1");
        User user4 = new User("Sara_1");
        User user5 = new User("Jordi_1");
        User user6 = new User("Irene_1");
        User user7 = new User("Miquel_1");
        User user8 = new User("ClaudiaR");
        User user9 = new User("Jon3001");
        User user10 = new User("Jan_22");
        User user11 = new User("Ale--4");
        User user12 = new User("Mr_J");
        User user13 = new User("Llorenç&");

        // Add users to the backend map
        backend.putUsers(user1);
        backend.putUsers(user2);
        backend.putUsers(user3);
        backend.putUsers(user4);
        backend.putUsers(user5);
        backend.putUsers(user6);
        backend.putUsers(user7);
        backend.putUsers(user8);
        backend.putUsers(user9);
        backend.putUsers(user10);
        backend.putUsers(user11);
        backend.putUsers(user12);
        backend.putUsers(user13);

        System.out.println(backend.getUser("Anxo_Vazquez").getPosts().toString());

        createPostsForUsers(backend);

        Collection<User> followingUsers = backend.getUsers().values();
        System.out.println(backend.getUser("Sara_1").getPosts().toString());
        LinkedList<String> followingUsernames = new LinkedList<>();

        for (User user : followingUsers) {
            followingUsernames.add(user.getUsername()); // Assuming getUsername() method exists
        }
        backend.getUser("Anxo_Vazquez").setFollowing(followingUsernames);

        backend.getUser("Joan_1").putFollowing("Sara_1");

        Server server = new Server(backend);
        server.startServer();
        System.out.println("End of Service");
    }

    public static void createPostsForUsers(Backend backend) {
        Random random = new Random();
        List<String> postContents = new ArrayList<>();
        // Read post contents from a file
        try (BufferedReader reader = new BufferedReader(new FileReader("posts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                postContents.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Iterate over the map's values
        int i;
        for (i = 0; i < 3; i++) {
            for (String user : backend.getUsers().keySet()) {

                // Generate a post with random content from postContents
                String content = postContents.get(random.nextInt(postContents.size()));
                LocalDate date = LocalDate.now(); // Using java.time.LocalDate for the date
                Post post = new Post(backend.getUser(user), date, content);
                backend.getUser(user).addPost(post); // Add post to the user
            }
        }
    }
}