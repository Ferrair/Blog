package wqh.blog.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import butterknife.Bind;
import wqh.blog.R;
import wqh.blog.ui.adapter.event.LoadMoreLayoutListener;

/**
 * Created by WQH on 2016/5/3  21:23.
 *
 * A Fragment that have a SwipeRefreshLayout(with layout id swipeRefreshLayout) and a RecyclerView(with layout id recyclerView)
 * NOTE: in subclass's XML file,those layout's is MUST be the same as above.
 */
public abstract class ScrollFragment extends StateFragment implements CanScroll {
    @Bind(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    //Todo: 加载之后 直接回到了顶部??
    protected LoadMoreLayoutListener mLoadMoreListener;

    @Override
    public boolean canRefresh() {
        return true;
    }

    @Override
    public boolean canLoadMore() {
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRefreshLayout.setEnabled(canRefresh());
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (canLoadMore()) {
            mLoadMoreListener = new LoadMoreLayoutListener((LinearLayoutManager) mRecyclerView.getLayoutManager(), this);
            mRecyclerView.addOnScrollListener(mLoadMoreListener);
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> {
            onRefreshDelayed();
            mRefreshLayout.setRefreshing(false);
        }, 2000);
    }

    @Override
    public void onLoadMore(int toToLoadPage) {
        if (canLoadMore()) {
            new Handler().postDelayed(() -> {
                onLoadMoreDelayed(toToLoadPage);
                mLoadMoreListener.setLoading(false);
            }, 2000);
        }
    }

    //Call in Activity
    public void onToolbarClick() {
        mRecyclerView.smoothScrollToPosition(0);
    }
}
