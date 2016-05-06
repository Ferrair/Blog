package wqh.blog.presenter.download;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wqh.blog.model.bean.Blog;
import wqh.blog.model.bean.Holder;
import wqh.blog.model.remote.BlogAPI;
import wqh.blog.model.remote.RemoteManager;
import wqh.blog.view.LoadView;

/**
 * Created by WQH on 2016/4/12  23:45.
 */
public class BlogDownLoadPresenter extends DownLoadPresenter<Blog> {
    BlogAPI mBlogAPI;
    private static final String TAG = "BlogDownLoadPresenter";

    @Override
    protected void initAPI() {
        mBlogAPI = RemoteManager.create(BlogAPI.class);
    }

    @Override
    public void loadAll(LoadView<Blog> mLoadView) {
        Call<Holder<Blog>> call = mBlogAPI.queryAll();
        doQuery(call, mLoadView);
    }

    @Override
    public void loadById(int id, LoadView<Blog> mLoadView) {
        Call<Holder<Blog>> call = mBlogAPI.queryById(id);
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
        Call<Holder<Blog>> call = mBlogAPI.queryByType(type);
        doQuery(call, mLoadView);

    }

    private void loadByTime(String time, LoadView<Blog> mLoadView) {
        Call<Holder<Blog>> call = mBlogAPI.queryByTime(time);
        doQuery(call, mLoadView);
    }

    protected void loadByTag(String tag, LoadView<Blog> mLoadView) {
        Call<Holder<Blog>> call = mBlogAPI.queryByTag(tag);
        doQuery(call, mLoadView);
    }

    protected void loadByTitle(String title, LoadView<Blog> mLoadView) {
        Call<Holder<Blog>> call = mBlogAPI.queryByTitle(title);
        doQuery(call, mLoadView);
    }

    private void doQuery(Call<Holder<Blog>> call, LoadView<Blog> mLoadView) {
        call.enqueue(new BlogCallback(mLoadView));
    }

    // The inner class that do the callback work after fetch data from server
    // MUST specify the type of the generics.(like this:Blog)
    // Why? GSON parse Json must know the type at RUNTIME
    class BlogCallback implements Callback<Holder<Blog>> {

        LoadView<Blog> mLoadView;

        public BlogCallback(LoadView<Blog> mLoadView) {
            this.mLoadView = mLoadView;
        }

        @Override
        public void onResponse(Call<Holder<Blog>> call, Response<Holder<Blog>> response) {
            if (response.isSuccessful()) {
                Holder<Blog> holder = response.body();
                if (holder.Code == RemoteManager.OK) {
                    List<Blog> mList = holder.dataList(Blog[].class);
                    if (mList.size() != 0)
                        mLoadView.onSuccess(mList);
                    else
                        mLoadView.onFail(101, "Can not find Object");
                } else {
                    mLoadView.onFail(holder.Code, "At " + TAG + "#onResponse-> " + holder.Msg);
                }
            } else {
                mLoadView.onFail(RemoteManager.UNKNOWN, "At " + TAG + "#onResponse-> " + response.errorBody().toString());
            }
        }

        @Override
        public void onFailure(Call<Holder<Blog>> call, Throwable t) {
            mLoadView.onFail(RemoteManager.UNKNOWN, "At " + TAG + "#onFailure-> " + t.toString());
        }
    }
}

