package wqh.blog.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import wqh.blog.R;
import wqh.blog.download.DownLoadHelper;
import wqh.blog.download.WorkDownLoadService;
import wqh.blog.mvp.model.service.RemoteManager;
import wqh.blog.mvp.presenter.remote.base.DownLoadPresenter;
import wqh.blog.mvp.presenter.remote.work.WorkDownLoadPresenterImpl;
import wqh.blog.ui.adapter.WorkAdapter;
import wqh.blog.mvp.model.bean.Work;
import wqh.blog.ui.adapter.event.LayoutState;
import wqh.blog.ui.base.ScrollFragment;
import wqh.blog.ui.customview.Dialog;
import wqh.blog.mvp.view.LoadView;
import wqh.blog.util.IntentUtil;
import wqh.blog.util.StatusUtil;
import wqh.blog.util.ToastUtil;

/**
 * Created by WQH on 2016/4/11  20:17.
 */
public class WorkListFragment extends ScrollFragment {

    private static final String TAG = "WorkListFragment";
    WorkAdapter mAdapter;
    DownLoadPresenter<Work> mDownLoadPresenter = new WorkDownLoadPresenterImpl();
    DefaultLoadView mDefaultLoadDataView = new DefaultLoadView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new WorkAdapter(getActivity());
        // Show Download Dialog here.
        mAdapter.setOnItemLongClickListener(R.id.item_work, (view, data) -> Dialog.create(getActivity(), "是否下载 '" + data.title + "'").setPositiveListener("下载", v -> doDownload(data)).show());
        mDownLoadPresenter.loadAll(1, mDefaultLoadDataView);
        mAdapter.setOnBottomListener(this);
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
    public void onRefreshDelayed() {
        mDownLoadPresenter.loadAll(1, mDefaultLoadDataView);
    }

    @Override
    public void onLoadMore(int toToLoadPage) {
        mDownLoadPresenter.loadAll(toToLoadPage, mDefaultLoadDataView);
    }

    /*
     * Download the Work by given URL.
     */
    private void doDownload(Work data) {
        DownLoadHelper.instance().offer(data.fileName);

        IntentUtil.startService(getActivity(), WorkDownLoadService.class);
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
            if (errorCode == RemoteManager.NO_MORE) {
                mAdapter.setLoadState(LayoutState.FINISHED);
            }
            //If no network will show local-data instead of error-view.
            if (!StatusUtil.isNetworkAvailable(getActivity())) {
                ToastUtil.showToast("没有网络咯");
                mStateLayout.showErrorView();
            }
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }
}
