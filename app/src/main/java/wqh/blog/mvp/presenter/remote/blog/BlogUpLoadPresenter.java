package wqh.blog.mvp.presenter.remote.blog;

import wqh.blog.mvp.model.bean.Blog;
import wqh.blog.mvp.presenter.remote.base.UpLoadPresenter;

/**
 * Created by WQH on 2016/5/13  22:32.
 */
public interface BlogUpLoadPresenter extends UpLoadPresenter<Blog> {

    public void addTimes(int blogId);
}
