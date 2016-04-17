package wqh.blog.presenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wqh.blog.bean.Blog;
import wqh.blog.net.BlogAPI;
import wqh.blog.net.NetManager;
import wqh.blog.view.LoadDataView;

/**
 * Created by WQH on 2016/4/12  23:45.
 */
public class BlogLoadPresenter extends LoadDataPresenter<Blog> {
    BlogAPI mBlogAPI;

    @Override
    public void initAPI() {
        mBlogAPI = NetManager.create(BlogAPI.class);
    }

    @Override
    public void loadAll(LoadDataView<Blog> mLoadDataView) {

    }

    @Override
    public void loadById(int id, LoadDataView<Blog> mLoadDataView) {
        Call<Blog> call = mBlogAPI.queryById(id);
        doQuery(call, mLoadDataView);
    }

    private void doQuery(Call<Blog> call, LoadDataView<Blog> mLoadDataView) {
        call.enqueue(new Callback<Blog>() {
            @Override
            public void onResponse(Call<Blog> call, Response<Blog> response) {
                Blog blog = response.body();
                //mLoadDataView.onSuccess();
            }

            @Override
            public void onFailure(Call<Blog> call, Throwable t) {

            }
        });
    }

    @Override
    public void loadByCondition(String condition, Type type, LoadDataView<Blog> mLoadDataView) {
        switch (type) {
            case TITLE:
                loadByTitle(condition);
                break;
            case TAG:
                loadByTag(condition);
                break;
            case TIME:
                loadByTime(condition);
                break;
        }
    }

    private void loadByTime(String time) {
        Call<Blog> call = mBlogAPI.queryByTime(time);
        doQuery(call, mLoadDataView);
    }

    protected void loadByTag(String tag) {
        Call<Blog> call = mBlogAPI.queryByTag(tag);
        doQuery(call, mLoadDataView);
    }

    protected void loadByTitle(String title) {
        Call<Blog> call = mBlogAPI.queryByTitle(title);
        doQuery(call, mLoadDataView);
    }

}

