package Y.Server;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author anxovazquez
 */
public class Feed { //Pasar al front Frontend
   
   private List following;

   private LinkedList posts;

    public Feed(User user) {
        this.following = user.getFollowing();
        this.posts = new LinkedList();
    }
   
  public Feed getFeed(User user) {
      return this;
  }

  public LinkedList<Post> getPosts() {
    return this.posts;
}
}
