package wqh.blog.mvp.presenter.remote.upload;

import wqh.blog.mvp.model.bean.Blog;
import wqh.blog.mvp.model.service.BlogAPI;
import wqh.blog.mvp.model.service.RemoteManager;
import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/5/5  21:57.
 */
public class BlogUpLoadPresenter extends UpLoadPresenter<Blog> {


    BlogAPI mBlogAPI;
    private static final String TAG = "BlogDownLoadPresenter";

    @Override
    protected void initAPI() {
        mBlogAPI = RemoteManager.create(BlogAPI.class);
    }

    @Override
    public void publish(Blog aData, LoadView<Blog> mLoadView) {
        //do nothing here.
    }

    public void addTimes(int blogId) {
        mBlogAPI.addTimes(blogId);
    }

}
