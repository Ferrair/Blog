package wqh.blog.mvp.model.service;

import wqh.blog.mvp.model.bean.User;
import wqh.blog.util.Json;
import wqh.blog.util.SharePreferenceUtil;

/**
 * Created by WQH on 2016/5/14  21:44.
 *
 * Store User,store token,store user's status.
 */
public class UserManager {
    private User currentUser;
    private static final String KEY = "CurrentUser";

    private static class ClassHolder {
        private static final UserManager INSTANCE = new UserManager();
    }

    public UserManager() {
        currentUser = Json.fromJson(SharePreferenceUtil.read().getString(KEY, null), User.class);
    }

    public static UserManager instance() {
        return ClassHolder.INSTANCE;
    }

    public void saveUser(User aUser) {
        currentUser = aUser;
        SharePreferenceUtil.write().putString(KEY, Json.toJson(aUser)).commit();
    }

    public User currentUser() {
        return currentUser;
    }

    public boolean isLogged() {
        return currentUser == null;
    }

}
