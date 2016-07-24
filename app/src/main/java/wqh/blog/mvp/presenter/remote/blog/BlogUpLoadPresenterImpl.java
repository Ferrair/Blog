package wqh.blog.mvp.presenter.remote.blog;

import wqh.blog.mvp.model.bean.Blog;
import wqh.blog.mvp.model.service.BlogAPI;
import wqh.blog.mvp.model.service.RemoteManager;
import wqh.blog.mvp.presenter.remote.base.LoadPresenter;
import wqh.blog.mvp.presenter.remote.base.UpLoadPresenter;
import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/5/5  21:57.
 */
public class BlogUpLoadPresenterImpl extends LoadPresenter implements BlogUpLoadPresenter {

    BlogAPI mBlogAPI;
    private static final String TAG = "BlogUpLoadPresenterImpl";

    @Override
    protected void initAPI() {
        mBlogAPI = RemoteManager.create(BlogAPI.class);
    }

    @Override
    public void publish(Blog aData, LoadView mLoadView) {
        //do nothing here.
    }

    @Override
    public void addTimes(int blogId) {
        mBlogAPI.addTimes(blogId);
    }

}
