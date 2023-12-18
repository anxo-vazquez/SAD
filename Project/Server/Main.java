package y;

/**
 *
 * @author anxovazquez
 */
public class Main { 
    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        Backend backend = new Backend();
        server.startServer();
        System.out.println("End of Service");
    



    }
}