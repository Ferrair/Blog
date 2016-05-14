package wqh.blog.mvp.model.bean;

/**
 * Created by WQH on 2016/5/13  22:10.
 */
public class User {
    public Integer id;
    public String username;
    public String passwork;
    public String token;
    public String avatarUrl;

    @Override
    public String toString() {
        return "User=> ID->" + id + " Name->" + username + " Token->" + token;
    }
}
