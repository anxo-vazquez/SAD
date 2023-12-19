package y;

import java.util.LinkedList;
import java.util.List;

public class User {

    private String username;

    private String bio;

    private LinkedList<Post> posts;

    private LinkedList<String> followers;
    private LinkedList<String> following;

    public User(String username) {
        this.username = username;
        this.posts = new LinkedList<Post>();
        this.followers = new LinkedList<String>();
        this.following = new LinkedList<String>();
    }

    public String getUsername() {
        return this.username;
    }

    public LinkedList<String> addPost(Post post) {
        this.posts.add(post);
    return followers;
    }
    
    public LinkedList<String> getFollowers() {
        return followers;
    }

    public LinkedList<String> getFollowing() {
        return following;
    }
    public LinkedList<String> putFollowing(String user) {
        this.following.add(user);
        return following;
    }
    public LinkedList<String> setFollowing(LinkedList<String> list) {
        this.following = list;
        return following;
    }
    public LinkedList<String> putFollower(String user) {
        this.followers.add(user);
        return followers;
    }
    public LinkedList<String> removeFollowing(String user) {
        this.following.remove(user);
        return following;
    }
    public LinkedList<String> removeFollower(String user) {
        this.following.remove(user);
        return following;
    }
    public LinkedList<Post> getPosts() {
        return this.posts;
    }

    public void setPosts(LinkedList<Post> posts) {
        this.posts = posts;
    }
       
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(this.username.toString());
        return str.toString();
    }

}
