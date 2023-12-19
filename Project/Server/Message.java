package y;

/**
 *
 * @author anxovazquez
 */

public class Message {
    private String type;
    private String username;
    private String content;

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getType() {
        return type;
    }
    public String getUsername() {
        return username;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
