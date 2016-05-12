package wqh.blog.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import wqh.blog.R;
import wqh.blog.mvp.presenter.remote.download.DownLoadPresenter;
import wqh.blog.mvp.presenter.remote.download.WorkDownLoadPresenter;
import wqh.blog.ui.adapter.WorkAdapter;
import wqh.blog.mvp.model.bean.Work;
import wqh.blog.ui.base.ScrollFragment;
import wqh.blog.ui.customview.Dialog;
import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/4/11  20:17.
 */
public class WorkListFragment extends ScrollFragment {

    private static final String TAG = "WorkListFragment";
    WorkAdapter mAdapter;
    DownLoadPresenter<Work> mDownLoadPresenter = new WorkDownLoadPresenter();
    DefaultLoadView mDefaultLoadDataView = new DefaultLoadView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new WorkAdapter(getActivity());
        // Show Download Dialog here.
        mAdapter.setOnItemLongClickListener(R.id.item_work, (view, data) -> Dialog.create(getActivity(), "是否下载 '" + data.title + "'").setPositiveListener("下载", v -> doDownload(data)).show());
        mDownLoadPresenter.loadAll(mDefaultLoadDataView);
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
        mDownLoadPresenter.loadAll(mDefaultLoadDataView);
    }

    /*
     * Download the Work by given URL.
     */
    private void doDownload(Work data) {
        //Todo:Download Work from given URL.
    }

    /**
     * A Load-Data View,which means show loaded-data is it's function.
     * What's more,the loaded-data is from DownLoadPresenter.
     * The default means that the view will exists forever unless a new Load-Data View is going to be added.
     */
    private class DefaultLoadView implements LoadView<Work> {

        @Override
        public void onSuccess(List<Work> data) {
            mStateLayout.showContentView();
            mAdapter.addAll(data);
            mRecyclerView.setAdapter(mAdapter);
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            mStateLayout.showErrorView();
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }
}
