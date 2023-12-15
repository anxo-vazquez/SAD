package Y.Server;

import java.util.HashMap;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author anxovazquez
 */
public class Server {
    
    public HashMap<String,User> users;

    public Server() {
        this.users = new HashMap();
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void putUsers(User user) {
        this.users.put(user.getUsername(), user);
    }
    
}
