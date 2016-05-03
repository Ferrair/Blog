package wqh.blog.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import wqh.blog.R;
import wqh.blog.presenter.download.DownLoadPresenter;
import wqh.blog.presenter.download.WorkDownLoadPresenter;
import wqh.blog.ui.adapter.WorkAdapter;
import wqh.blog.model.bean.Work;
import wqh.blog.ui.base.ScrollFragment;

/**
 * Created by WQH on 2016/4/11  20:17.
 */
public class WorkListFragment extends ScrollFragment {

    private static final String TAG = "WorkListFragment";
    WorkAdapter mAdapter;
    DownLoadPresenter<Work> mDownLoadPresenter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDownLoadPresenter = new WorkDownLoadPresenter();
    }

    @Override
    public int layoutId() {
        return R.layout.layout_list;
    }

    @Override
    public String toString() {
        return "产品";
    }

    @Override
    protected void onRefreshDelayed() {

    }
}
