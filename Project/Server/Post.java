package y;

import java.time.LocalDate;

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

    private LocalDate currentDate;

    public Post(User user,LocalDate date, String text) {
        this.user = user;
        this.currentDate = date;
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

    public String getContent() {
        return text;
    }
    public LocalDate getDate() {
        return this.currentDate;
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
        str.append(this.user.toString()).append(this.text.toString());
        return str.toString();
    }

}
