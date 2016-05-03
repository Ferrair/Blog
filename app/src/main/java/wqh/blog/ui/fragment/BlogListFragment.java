package wqh.blog.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import wqh.blog.R;
import wqh.blog.presenter.download.DownLoadPresenter;
import wqh.blog.ui.activity.BlogItemActivity;
import wqh.blog.ui.adapter.BlogAdapter;
import wqh.blog.model.bean.Blog;
import wqh.blog.presenter.download.BlogDownLoadPresenter;
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

    @Override
    protected void onRefreshDelayed() {
        mDownLoadPresenter.loadAll(mDefaultLoadDataView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Load data
        mDownLoadPresenter.loadAll(mDefaultLoadDataView);

        //init Adapter,and set listener
        mAdapter = new BlogAdapter(getActivity());
        mAdapter.setOnItemClickListener(R.id.item_blog, (view, data) ->
        {
            Intent mIntent = new Intent(getActivity(), BlogItemActivity.class);
            mIntent.putExtra("id", data.id);
            mIntent.putExtra("title", data.title);
            IntentUtil.goToOtherActivity(getActivity(), mIntent);
        });
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
            mAdapter.addAll(data);
            mRecyclerView.setAdapter(mAdapter);
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }
}
