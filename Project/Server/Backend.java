package y;

import java.util.HashMap;

/**
 *
 * @author anxovazquez
 */
public class Backend {
    
    public HashMap<String,User> users;

    public Backend() {
        this.users = new HashMap();
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void putUsers(User user) {
        this.users.put(user.getUsername(), user);
    }
    
}
