package wqh.blog.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import wqh.blog.R;
import wqh.blog.adapter.WorkAdapter;
import wqh.blog.bean.Work;
import wqh.blog.presenter.LoadDataPresenter;
import wqh.blog.presenter.WorkLoadPresenter;
import wqh.blog.ui.base.BaseFragment;
import wqh.blog.view.LoadDataView;

/**
 * Created by WQH on 2016/4/11  20:17.
 */
public class WorkListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    WorkAdapter mAdapter;
    LoadDataPresenter mLoadDataPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mLoadDataPresenter = new WorkLoadPresenter();
    }

    private List<Work> getData() {
        List<Work> list = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            Work work = new Work();
            work.title = "Work Title => " + i;
            work.description = "Work Description => " + i;
            list.add(work);
        }
        return list;
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

    public void onSuccess(List<Work> data) {
        mAdapter = new WorkAdapter(getActivity(), getData());
        mRecyclerView.setAdapter(mAdapter);
    }

    public void onFail(int errorCode, String errorMsg) {
        Logger.i("BlogListFragment => " + errorCode + "  " + errorMsg);
    }
}
