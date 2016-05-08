package wqh.blog.presenter.download;


import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import wqh.blog.model.bean.Blog;
import wqh.blog.model.remote.BlogAPI;
import wqh.blog.model.remote.RemoteManager;
import wqh.blog.presenter.DefaultCallback;
import wqh.blog.util.Json;
import wqh.blog.view.LoadView;

/**
 * Created by WQH on 2016/4/12  23:45.
 */
public class BlogDownLoadPresenter extends DownLoadPresenter<Blog> {
    BlogAPI mBlogAPI;

    @Override
    protected void initAPI() {
        mBlogAPI = RemoteManager.create(BlogAPI.class);
    }

    @Override
    public void loadAll(LoadView<Blog> mLoadView) {
        Call<ResponseBody> call = mBlogAPI.queryAll();
        doQuery(call, mLoadView);
    }

    @Override
    public void loadById(int id, LoadView<Blog> mLoadView) {
        Call<ResponseBody> call = mBlogAPI.queryById(id);
        doQuery(call, mLoadView);
    }

    @Override
    public void loadByCondition(String condition, Type type, LoadView<Blog> mLoadView) {
        switch (type) {
            case TITLE:
                loadByTitle(condition, mLoadView);
                break;
            case TAG:
                loadByTag(condition, mLoadView);
                break;
            case TIME:
                loadByTime(condition, mLoadView);
                break;
            case TYPE:
                loadByType(condition, mLoadView);
        }
    }

    private void loadByType(String type, LoadView<Blog> mLoadView) {
        Call<ResponseBody> call = mBlogAPI.queryByType(type);
        doQuery(call, mLoadView);

    }

    private void loadByTime(String time, LoadView<Blog> mLoadView) {
        Call<ResponseBody> call = mBlogAPI.queryByTime(time);
        doQuery(call, mLoadView);
    }

    protected void loadByTag(String tag, LoadView<Blog> mLoadView) {
        Call<ResponseBody> call = mBlogAPI.queryByTag(tag);
        doQuery(call, mLoadView);
    }

    protected void loadByTitle(String title, LoadView<Blog> mLoadView) {
        Call<ResponseBody> call = mBlogAPI.queryByTitle(title);
        doQuery(call, mLoadView);
    }

    private void doQuery(Call<ResponseBody> call, LoadView<Blog> mLoadView) {
        call.enqueue(new BlogCallback(mLoadView));
    }

    // The inner class that do the callback work after fetch data from server
    // MUST specify the type of the generics.(like this:Blog)
    // Why? GSON parse Json must know the type at RUNTIME
    class BlogCallback extends DefaultCallback<Blog> {

        public BlogCallback(LoadView<Blog> mLoadView) {
            super(mLoadView);
        }

        @Override
        protected void onParseResult(String result) {
            mLoadView.onSuccess(Arrays.asList(Json.fromJson(result, Blog[].class)));
        }
    }
}

