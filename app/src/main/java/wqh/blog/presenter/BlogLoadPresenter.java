package wqh.blog.presenter;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wqh.blog.bean.Blog;
import wqh.blog.bean.Holder;
import wqh.blog.net.BlogAPI;
import wqh.blog.net.NetManager;
import wqh.blog.view.LoadDataView;

/**
 * Created by WQH on 2016/4/12  23:45.
 */
public class BlogLoadPresenter extends LoadDataPresenter<Blog> {
    BlogAPI mBlogAPI;
    private static final String TAG = "BlogLoadPresenter";

    @Override
    public void initAPI() {
        mBlogAPI = NetManager.create(BlogAPI.class);
    }

    @Override
    public void loadAll(LoadDataView<Blog> mLoadDataView) {
        //Todo:How to Query-All
    }

    @Override
    public void loadById(int id, LoadDataView<Blog> mLoadDataView) {
        Call<Holder<Blog>> call = mBlogAPI.queryById(id);
        doQuery(call, mLoadDataView);
    }

    private void doQuery(Call<Holder<Blog>> call, LoadDataView<Blog> mLoadDataView) {
        call.enqueue(new Callback<Holder<Blog>>() {
            @Override
            public void onResponse(Call<Holder<Blog>> call, Response<Holder<Blog>> response) {
                if (response.isSuccessful()) {
                    Holder<Blog> holder = response.body();
                    if (holder.Code == NetManager.OK) {
                        Log.i(TAG, holder.Result.toString());
                        List<Blog> mList = holder.dataList(Blog[].class);
                        mLoadDataView.onSuccess(mList);

                    } else {
                        mLoadDataView.onFail(holder.Code, "At " + TAG + "#onResponse-> " + holder.Msg);
                    }
                } else {
                    mLoadDataView.onFail(NetManager.UNKNOWN, "At " + TAG + "#onResponse-> " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Holder<Blog>> call, Throwable t) {
                mLoadDataView.onFail(NetManager.UNKNOWN, "At " + TAG + "#onFailure-> " + t.toString());
            }
        });

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
        }
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


    /*private void doQuery(Call<Holder<List<Blog>>> call, LoadDataView<Blog> mLoadDataView) {

        call.enqueue(new Callback<Holder<List<Blog>>>() {
            @Override
            public void onResponse(Call<Holder<List<Blog>>> call, Response<Holder<List<Blog>>> response) {
                if (response.isSuccessful()) {
                    Holder<List<Blog>> holder = response.body();
                    if (holder.Code == NetManager.OK) {
                        Log.i(TAG, holder.Result.toString());
                        List<Blog> mList = holder.dataList();
                        mLoadDataView.onSuccess(mList);

                    } else {
                        mLoadDataView.onFail(holder.Code, "At " + TAG + "#onResponse-> " + holder.Msg);
                    }
                } else {
                    mLoadDataView.onFail(NetManager.UNKNOWN, "At " + TAG + "#onResponse-> " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Holder<List<Blog>>> call, Throwable t) {
                mLoadDataView.onFail(NetManager.UNKNOWN, "At " + TAG + "#onFailure-> " + t.toString());
            }
        });
    }*/
}

