package y;

/**
 *
 * @author anxovazquez
 */
public class Post {

    private String title;

    private User user;

    private String text;

    private Object body;

    private int likes;

    private int dislikes;

    public Post(User user, String title, String text) {
        this.title = title;
        this.user = user;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(this.title.toString()).append(this.user.toString()).append(this.text.toString());
        return str.toString();
    }

}
