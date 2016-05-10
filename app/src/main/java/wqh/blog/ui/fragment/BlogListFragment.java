package wqh.blog.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import wqh.blog.R;
import wqh.blog.presenter.local.LocalPresenter;
import wqh.blog.presenter.remote.download.DownLoadPresenter;
import wqh.blog.ui.activity.BlogItemActivity;
import wqh.blog.ui.adapter.BlogAdapter;
import wqh.blog.model.bean.Blog;
import wqh.blog.presenter.remote.download.BlogDownLoadPresenter;
import wqh.blog.ui.base.ScrollFragment;
import wqh.blog.util.IntentUtil;
import wqh.blog.view.LoadView;

/**
 * Created by WQH on 2016/4/11  19:14.
 */
public class BlogListFragment extends ScrollFragment {

    private static final String TAG = "BlogListFragment";
    BlogAdapter mAdapter;
    /**
     * A Load-Data Presenter,which means load data from server is it's function.
     * On the other hand,load-data can't be found in this class
     */
    DownLoadPresenter<Blog> mDownLoadPresenter = new BlogDownLoadPresenter();
    /**
     * A Load-Data View,which means show loaded-data is it's function.
     * What's more,the loaded-data is from DownLoadPresenter.
     */
    DefaultLoadView mDefaultLoadDataView = new DefaultLoadView();
    /**
     * A Local-Data Presenter,that store data into local-database.
     */
    LocalPresenter mLocalPresenter = new LocalPresenter();

    @Override
    protected void onRefreshDelayed() {
        mDownLoadPresenter.loadAll(mDefaultLoadDataView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //init Adapter,and set listener
        mAdapter = new BlogAdapter(getActivity());
        mAdapter.setOnItemClickListener(R.id.item_blog, (view, data) -> IntentUtil.goToOtherActivity(getActivity(), BlogItemActivity.class, "id", data.id));

        //Load data
        if (mLocalPresenter.db().queryCount(Blog.class) != 0)
            showContent(mLocalPresenter.db().query(Blog.class));
        //Todo: run this code when the net is OK. Or will override previous Content-View.
        mDownLoadPresenter.loadAll(mDefaultLoadDataView);

    }

    /**
     * Show view by given data.
     */
    private void showContent(List<Blog> data) {
        mStateLayout.showContentView();
        mAdapter.addAll(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public int layoutId() {
        return R.layout.layout_list;
    }

    @Override
    public String toString() {
        return "博客";
    }

    /**
     * A Load-Data View,which means show loaded-data is it's function.
     * What's more,the loaded-data is from DownLoadPresenter.
     * The default means that the view will exists forever unless a new Load-Data View is going to be added.
     */
    private class DefaultLoadView implements LoadView<Blog> {

        @Override
        public void onSuccess(List<Blog> data) {
            showContent(data);
            mLocalPresenter.db().save(data);
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            mStateLayout.showErrorView();
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }

}
