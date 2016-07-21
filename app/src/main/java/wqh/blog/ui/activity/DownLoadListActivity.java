package wqh.blog.ui.activity;

import android.os.Bundle;


import com.litesuits.orm.LiteOrm;

import java.util.List;

import wqh.blog.R;
import wqh.blog.mvp.model.bean.Download;
import wqh.blog.ui.adapter.DownLoadAdapter;
import wqh.blog.ui.adapter.event.LayoutState;
import wqh.blog.ui.base.ScrollActivity;

public class DownLoadListActivity extends ScrollActivity {
    private static final String TAG = "DownLoadListActivity";
    private static LiteOrm liteOrm;
    private DownLoadAdapter mAdapter;

    @Override
    protected int layoutId() {
        return R.layout.activity_down_load_list_activty;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new DownLoadAdapter(this);
        mAdapter.setLoadState(LayoutState.GONE);
        if (liteOrm == null) {
            liteOrm = LiteOrm.newSingleInstance(this, "blog.db");
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

    @Override
    protected void onStart() {
        super.onStart();
        updateDownload();
    }

    private void updateDownload() {
        List<Download> mListData = liteOrm.query(Download.class);

        if (mListData.size() == 0) {
            mStateLayout.showEmptyView();
        } else {
            mAdapter.addAll(mListData);
            mStateLayout.showContentView();
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
