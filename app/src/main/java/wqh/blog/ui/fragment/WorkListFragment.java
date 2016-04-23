package wqh.blog.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;
import wqh.blog.R;
import wqh.blog.ui.adapter.WorkAdapter;
import wqh.blog.model.bean.Work;
import wqh.blog.presenter.LoadDataPresenter;
import wqh.blog.presenter.WorkLoadPresenter;
import wqh.blog.ui.base.BaseFragment;

/**
 * Created by WQH on 2016/4/11  20:17.
 */
public class WorkListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "WorkListFragment";
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    WorkAdapter mAdapter;
    LoadDataPresenter<Work> mLoadDataPresenter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, mRootView);
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mLoadDataPresenter = new WorkLoadPresenter();
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_worklist;
    }

    @Override
    public String toString() {
        return "产品";
    }

    @Override
    public void onRefresh() {

    }
}
