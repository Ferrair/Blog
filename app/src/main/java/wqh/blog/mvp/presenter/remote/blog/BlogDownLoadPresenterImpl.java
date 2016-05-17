package wqh.blog.mvp.presenter.remote.blog;


import okhttp3.ResponseBody;
import retrofit2.Call;
import wqh.blog.mvp.model.bean.Blog;
import wqh.blog.mvp.model.service.BlogAPI;
import wqh.blog.mvp.model.service.RemoteManager;
import wqh.blog.mvp.presenter.remote.base.DefaultCallback;
import wqh.blog.mvp.presenter.remote.base.DownLoadPresenter;
import wqh.blog.mvp.presenter.remote.base.LoadPresenter;
import wqh.blog.util.CollectionUtil;
import wqh.blog.util.Json;
import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/4/12  23:45.
 */
public class BlogDownLoadPresenterImpl extends LoadPresenter<Blog> implements DownLoadPresenter<Blog> {
    BlogAPI mBlogAPI;

    @Override
    protected void initAPI() {
        mBlogAPI = RemoteManager.create(BlogAPI.class);
    }

    @Override
    public void loadAll(int pageNum, LoadView<Blog> mLoadView) {
        Call<ResponseBody> call = mBlogAPI.queryAll(pageNum);
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
            mLoadView.onSuccess(CollectionUtil.asList(Json.fromJson(result, Blog[].class)));
        }
    }
}

