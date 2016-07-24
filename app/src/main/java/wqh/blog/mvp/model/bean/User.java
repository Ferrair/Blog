package wqh.blog.mvp.model.bean;

/**
 * Created by WQH on 2016/5/13  22:10.
 */
public class User {
    public Integer id;
    public String username;
    public String password;
    public String token;
    public String avatarUri;
    public String coverUri; // user's cover background image url.

    @Override
    public String toString() {
        return "User=> ID->" + id + " Name->" + username + " Token->" + token;
    }
}
