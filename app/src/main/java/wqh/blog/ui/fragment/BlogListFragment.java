package wqh.blog.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wqh.blog.R;
import wqh.blog.ui.activity.BlogItemActivity;
import wqh.blog.ui.adapter.BlogAdapter;
import wqh.blog.model.bean.Blog;
import wqh.blog.presenter.BlogLoadPresenter;
import wqh.blog.presenter.LoadDataPresenter;
import wqh.blog.ui.adapter.base.BaseAdapter;
import wqh.blog.ui.adapter.event.OnItemClickListener;
import wqh.blog.ui.base.BaseFragment;
import wqh.blog.util.IntentUtil;
import wqh.blog.util.ToastUtil;
import wqh.blog.view.LoadDataView;

/**
 * Created by WQH on 2016/4/11  19:14.
 * <p>
 * <p>
 * 所有业务逻辑放在 LoadDataPresenter里面，Activity只负责UI的状态
 * 并提供LoadDataView给LoadDataPresenter，给其回掉使用
 */
public class BlogListFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "BlogListFragment";
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    BlogAdapter mAdapter;
    /**
     * A Load-Data Presenter,which means load data from server is it's function.
     * On the other hand,load-data can't be found in this class
     */
    LoadDataPresenter<Blog> mLoadDataPresenter = new BlogLoadPresenter();
    /**
     * A Load-Data View,which means show loaded-data is it's function.
     * What's more,the loaded-data is from LoadDataPresenter.
     */
    DefaultLoadDataView mDefaultLoadDataView = new DefaultLoadDataView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mLoadDataPresenter.loadAll(mDefaultLoadDataView);
        mAdapter = new BlogAdapter(getActivity());
        mAdapter.setOnItemClickListener((view, data) -> IntentUtil.goToOtherActivity(getActivity(), BlogItemActivity.class, "id", data.id));
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_bloglist;
    }

    @Override
    public String toString() {
        return "博客";
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> {
            mLoadDataPresenter.loadAll(mDefaultLoadDataView);
            mRefreshLayout.setRefreshing(false);
        }, 2000);
    }

    /*@OnClick(R.id.item_blog)
    public void onClickBlog() {
        ToastUtil.showToast(getActivity(), "OnClick Blog");
    }*/

    private class DefaultLoadDataView implements LoadDataView<Blog> {

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
