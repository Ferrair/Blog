package wqh.blog.ui.activity;

import android.os.Bundle;

import java.util.List;

import wqh.blog.R;
import wqh.blog.download.DownLoadHelper;
import wqh.blog.mvp.model.bean.DownLoadBean;
import wqh.blog.ui.adapter.DownLoadAdapter;
import wqh.blog.ui.base.ScrollActivity;

public class DownLoadListActivity extends ScrollActivity {
    DownLoadAdapter mAdapter;
    List<DownLoadBean> mList;

    @Override
    protected int layoutId() {
        return R.layout.activity_down_load_list_activty;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = DownLoadHelper.instance().data();
        if (mList.size() == 0) {
            mStateLayout.showEmptyView();
        } else {
            mAdapter = new DownLoadAdapter(this, DownLoadHelper.instance().data());
            mStateLayout.showContentView();
        }
    }

    @Override
    public void onRefreshDelayed() {
        //Do nothing here.
    }

    @Override
    public boolean canRefresh() {
        return false;
    }

    @Override
    public void onLoadMore(int toToLoadPage) {
        //Do nothing here.
    }
}
