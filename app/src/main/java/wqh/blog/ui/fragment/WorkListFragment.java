package wqh.blog.ui.fragment;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.LinearLayout;

import com.litesuits.orm.LiteOrm;

import java.util.List;

import butterknife.Bind;
import wqh.blog.R;
import wqh.blog.download.DownLoadHelper;
import wqh.blog.download.WorkDownLoadService;
import wqh.blog.mvp.model.bean.Download;
import wqh.blog.mvp.model.service.RemoteManager;
import wqh.blog.mvp.presenter.remote.base.DownLoadPresenter;
import wqh.blog.mvp.presenter.remote.work.WorkDownLoadPresenterImpl;
import wqh.blog.ui.activity.DownLoadListActivity;
import wqh.blog.ui.adapter.WorkAdapter;
import wqh.blog.mvp.model.bean.Work;
import wqh.blog.ui.adapter.event.LayoutState;
import wqh.blog.ui.base.ScrollFragment;
import wqh.blog.ui.customview.Dialog;
import wqh.blog.mvp.view.LoadView;
import wqh.blog.util.CollectionUtil;
import wqh.blog.util.IntentUtil;
import wqh.blog.util.Json;
import wqh.blog.util.StatusUtil;
import wqh.blog.util.ToastUtil;

/**
 * Created by WQH on 2016/4/11  20:17.
 */
public class WorkListFragment extends ScrollFragment {

    @Bind(R.id.rootView)
    LinearLayout mRootView;
    private static final String TAG = "WorkListFragment";
    WorkAdapter mAdapter;
    DownLoadPresenter mDownLoadPresenter = new WorkDownLoadPresenterImpl();
    DefaultLoadView mDefaultLoadDataView = new DefaultLoadView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new WorkAdapter(getActivity());
        mAdapter.setOnBottomListener(this);

        // Show Download Dialog here.
        mAdapter.setOnItemLongClickListener(R.id.item_work, (view, data) -> {
            if (grantPermission())
                Dialog.create(getActivity(), "是否下载 '" + data.title + "'").setPositiveListener("下载", v -> doDownload(data)).show();
        });

        mDownLoadPresenter.loadAll(1, mDefaultLoadDataView);
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
    * Request Permission for WRITE_EXTERNAL_STORAGE.
    */
    private boolean grantPermission() {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ToastUtil.showToast("Give me Permission for SDCard");
                return false;
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
                return true;
            }
        }
        return true;
    }

    /*
     * Download the Work by given URL.
     */
    private void doDownload(Work data) {
        DownLoadHelper.instance().addDownLoadEvent(data.fileName, new WorkDownLoadEvent());
        DownLoadHelper.instance().offer(data.id, data.fileName, data.title);
        IntentUtil.startService(getActivity(), WorkDownLoadService.class);
    }


    /**
     * A Load-Data View,which means show loaded-data is it's function.
     * What's more,the loaded-data is from DownLoadPresenter.
     * The default means that the view will exists forever unless a new Load-Data View is going to be added.
     */
    private class DefaultLoadView implements LoadView {

        @Override
        public void onSuccess(String resultJson) {
            List<Work> data = CollectionUtil.asList(Json.fromJson(resultJson, Work[].class));
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

    /**
     * A Simple implements of DownLoadHelper.DownLoadEvent.
     * Provided some Toast.
     */
    public class DownLoadEventAdapter implements DownLoadHelper.DownLoadEvent {
        LiteOrm liteOrm;

        @Override
        public void onPreStart(Download toDownload) {
            if (liteOrm == null) {
                liteOrm = LiteOrm.newSingleInstance(getActivity(), "blog.db");
            }
        }

        @Override
        public void onStart(Download toDownload) {
            Snackbar.make(mRootView, toDownload.title + "开始下载", Snackbar.LENGTH_LONG).setAction("取消", v -> {
                DownLoadHelper.instance().data().remove(toDownload);
                liteOrm.delete(toDownload);
            }).show();
        }

        @Override
        public void onPreProgress(Download toDownload) {

        }

        @Override
        public void onSuccess(Download toDownload) {
            DownLoadHelper.instance().data().remove(toDownload);
            ToastUtil.showToast("已保存到->" + toDownload.filePath);
        }

        @Override
        public void onFail(Download toDownload) {
            ToastUtil.showToast("下载失败");
        }

        @Override
        public void onProgress(Download toDownload, int percent) {

        }
    }

    private class WorkDownLoadEvent extends DownLoadEventAdapter {
        NotificationManager mNotifyManager;
        NotificationCompat.Builder mBuilder;


        // Waiting to download
        @Override
        public void onPreStart(Download toDownload) {
            super.onPreStart(toDownload);
            mNotifyManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(getActivity());
            liteOrm.save(toDownload);
        }

        @Override
        public void onStart(Download toDownload) {
            super.onStart(toDownload);

            mBuilder.setContentTitle("下载文件").setContentText(toDownload.title + "正在下载").setSmallIcon(R.mipmap.ic_launcher).setProgress(100, 0, false);
            Intent resultIntent = new Intent(getActivity(), DownLoadListActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(getActivity(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            mNotifyManager.notify(1, mBuilder.build());
        }

        // Downloading
        @Override
        public void onPreProgress(Download toDownload) {
            super.onPreProgress(toDownload);
            liteOrm.update(toDownload);
        }

        @Override
        public void onProgress(Download toDownload, int percent) {
            super.onProgress(toDownload, percent);
            mBuilder.setProgress(100, percent, false);
            mNotifyManager.notify(1, mBuilder.build());
        }

        // Finish
        @Override
        public void onSuccess(Download toDownload) {
            super.onSuccess(toDownload);
            mBuilder.setContentText("下载完成").setProgress(0, 0, false);
            mNotifyManager.notify(1, mBuilder.build());
            liteOrm.update(toDownload);
        }

        @Override
        public void onFail(Download toDownload) {
            super.onFail(toDownload);
        }
    }
}
