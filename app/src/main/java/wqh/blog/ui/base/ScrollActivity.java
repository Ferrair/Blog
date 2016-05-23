package wqh.blog.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import wqh.blog.R;

/**
 * Created by WQH on 2016/5/3  21:38.
 *
 * A Activity that have a SwipeRefreshLayout(with layout id swipeRefreshLayout) and a RecyclerView(with layout id recyclerView)
 * NOTE: in subclass's XML file,those layout's is MUST be the same as above.
 */
public abstract class ScrollActivity extends StateActivity implements CanScroll {
    @Bind(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    @Override
    public boolean canRefresh() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRefreshLayout.setEnabled(canRefresh());
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onRefresh() {
        if (canRefresh()) {
            new Handler().postDelayed(() -> {
                onRefreshDelayed();
                mRefreshLayout.setRefreshing(false);
            }, 2000);
        }
    }

    /**
     * All the ScrollActivity can scroll to top when click the Toolbar.
     */
    @Override
    protected void onToolbarClick() {
        mRecyclerView.smoothScrollToPosition(0);
    }
}
