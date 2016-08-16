package wqh.blog.mvp.presenter.remote.user;

import java.io.File;

import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/5/13  22:36.
 */
public interface UserPresenter {
    void login(String username, String password, LoadView mLoadView);

    void register(String username, String password, LoadView mLoadView);

    void logout(int id, LoadView mLoadView);

    boolean status(int id, LoadView mLoadView);

    void changeCover(int id, File coverFile, LoadView mLoadView);

    void changeAvatar(int id, File avatarFile, LoadView mLoadView);
}
