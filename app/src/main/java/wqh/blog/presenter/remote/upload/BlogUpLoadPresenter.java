package wqh.blog.presenter.remote.upload;

import wqh.blog.model.bean.Blog;
import wqh.blog.model.remote.BlogAPI;
import wqh.blog.model.remote.RemoteManager;
import wqh.blog.view.LoadView;

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
