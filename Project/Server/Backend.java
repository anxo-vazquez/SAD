package y;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author anxovazquez
 */
public class Backend {
    
    public HashMap<String,User> users;
    private HashMap<String,UserCredentials> credentials;

    public void setCredentials(HashMap<String, UserCredentials> credentials) {
        this.credentials = credentials;
    }

    public Backend() {
        this.users = new HashMap();
    }

    public HashMap<String, User> getUsers() {
        return users;
    }
    public User getUser(String Username) {
        return this.users.get(Username);
    }
    public void putUsers(User user) {
        this.users.put(user.getUsername(), user);
    }

    public List<Post> get_feed(String username) {
        List<Post> feedPosts = new ArrayList<>();
        User user = getUser(username);

        if (user != null) {
            for (String followingUsername : user.getFollowing()) {
                User followingUser = getUser(followingUsername); // Retrieve the User object for each username
                if (followingUser != null) {
                    feedPosts.addAll(followingUser.getPosts());
                }
            }

            // Sort by date (assuming Post has a date attribute) and limit to 100 posts
            feedPosts.sort(Comparator.comparing(Post::getDate).reversed());
            System.out.println(feedPosts.toString());
            return feedPosts.size() > 100 ? feedPosts.subList(0, 100) : feedPosts;
        }

        return new ArrayList<>(); // Return empty list if user not found
    }
}
