package wqh.blog.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

    private static final String TAG = "WorkListFragment";
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    WorkAdapter mAdapter;
    LoadDataPresenter<Work> mLoadDataPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mLoadDataPresenter = new WorkLoadPresenter();
        /*mLoadDataPresenter.loadById(1, new LoadDataView<Work>() {
            @Override
            public void onSuccess(List<Work> data) {
                mAdapter = new WorkAdapter(getActivity(), data);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFail(int errorCode, String errorMsg) {
                Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
            }
        });*/
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
