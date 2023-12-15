package Y.Server;

import java.util.LinkedList;
import java.util.List;

public class User {

    private String username;

    private String bio;

    protected List posts;

    private List followers;
    private List following;

    public User(String username) {
        this.username = username;
        this.posts = new LinkedList<Post>();
        this.followers = new LinkedList<String>();
        this.following = new LinkedList<String>();
    }

    public String getUsername() {
        return this.username;
    }
    public List getFollowers() {
        return followers;
    }

    public List getFollowing() {
        return following;
    }

    public List<Post> getPosts() {
        return this.posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
       
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(this.username.toString()).append(this.followers.toString()).append(this.following.toString());
        return str.toString();
    }

}
