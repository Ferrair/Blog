package wqh.blog.presenter;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wqh.blog.model.bean.Blog;
import wqh.blog.model.bean.Holder;
import wqh.blog.model.remote.BlogAPI;
import wqh.blog.model.remote.RemoteManager;
import wqh.blog.view.LoadDataView;

/**
 * Created by WQH on 2016/4/12  23:45.
 */
public class BlogLoadPresenter extends LoadDataPresenter<Blog> {
    BlogAPI mBlogAPI;
    private static final String TAG = "BlogLoadPresenter";

    @Override
    public void initAPI() {
        mBlogAPI = RemoteManager.create(BlogAPI.class);
    }

    @Override
    public void loadAll(LoadDataView<Blog> mLoadDataView) {
        Call<Holder<Blog>> call = mBlogAPI.queryAll();
        doQuery(call, mLoadDataView);
    }

    @Override
    public void loadById(int id, LoadDataView<Blog> mLoadDataView) {
        Call<Holder<Blog>> call = mBlogAPI.queryById(id);
        doQuery(call, mLoadDataView);
    }

    @Override
    public void loadByCondition(String condition, Type type, LoadDataView<Blog> mLoadDataView) {
        switch (type) {
            case TITLE:
                loadByTitle(condition, mLoadDataView);
                break;
            case TAG:
                loadByTag(condition, mLoadDataView);
                break;
            case TIME:
                loadByTime(condition, mLoadDataView);
                break;
            case TYPE:
                loadByType(condition, mLoadDataView);
        }
    }

    private void loadByType(String type, LoadDataView<Blog> mLoadDataView) {
        Call<Holder<Blog>> call = mBlogAPI.queryByType(type);
        doQuery(call, mLoadDataView);

    }

    private void loadByTime(String time, LoadDataView<Blog> mLoadDataView) {
        Call<Holder<Blog>> call = mBlogAPI.queryByTime(time);
        doQuery(call, mLoadDataView);
    }

    protected void loadByTag(String tag, LoadDataView<Blog> mLoadDataView) {
        Call<Holder<Blog>> call = mBlogAPI.queryByTag(tag);
        doQuery(call, mLoadDataView);
    }

    protected void loadByTitle(String title, LoadDataView<Blog> mLoadDataView) {
        Call<Holder<Blog>> call = mBlogAPI.queryByTitle(title);
        doQuery(call, mLoadDataView);
    }

    private void doQuery(Call<Holder<Blog>> call, LoadDataView<Blog> mLoadDataView) {
        call.enqueue(new BlogCallback(mLoadDataView));
    }

    //The inner class that do the callback work after fetch data from server
    class BlogCallback implements Callback<Holder<Blog>> {

        LoadDataView<Blog> mLoadDataView;

        public BlogCallback(LoadDataView<Blog> mLoadDataView) {
            this.mLoadDataView = mLoadDataView;
        }

        @Override
        public void onResponse(Call<Holder<Blog>> call, Response<Holder<Blog>> response) {
            if (response.isSuccessful()) {
                Holder<Blog> holder = response.body();
                if (holder.Code == RemoteManager.OK) {
                    Log.i(TAG, holder.Result.toString());
                    List<Blog> mList = holder.dataList(Blog[].class);
                    if (mList.size() != 0)
                        mLoadDataView.onSuccess(mList);
                    else
                        mLoadDataView.onFail(101, "Can not find Object");
                } else {
                    mLoadDataView.onFail(holder.Code, "At " + TAG + "#onResponse-> " + holder.Msg);
                }
            } else {
                mLoadDataView.onFail(RemoteManager.UNKNOWN, "At " + TAG + "#onResponse-> " + response.errorBody().toString());
            }
        }

        @Override
        public void onFailure(Call<Holder<Blog>> call, Throwable t) {
            mLoadDataView.onFail(RemoteManager.UNKNOWN, "At " + TAG + "#onFailure-> " + t.toString());
        }
    }
}

