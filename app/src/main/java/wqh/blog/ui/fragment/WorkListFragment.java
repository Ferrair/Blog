package wqh.blog.ui.fragment;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.LinearLayout;

import com.litesuits.orm.LiteOrm;

import java.util.List;

import butterknife.Bind;
import wqh.blog.R;
import wqh.blog.download.DownLoadHelper;
import wqh.blog.download.WorkDownLoadService;
import wqh.blog.manager.PermissionManager;
import wqh.blog.mvp.model.bean.Download;
import wqh.blog.mvp.model.bean.Work;
import wqh.blog.mvp.model.service.RemoteManager;
import wqh.blog.mvp.presenter.remote.base.DownLoadPresenter;
import wqh.blog.mvp.presenter.remote.work.WorkDownLoadPresenterImpl;
import wqh.blog.mvp.view.LoadView;
import wqh.blog.ui.activity.DownLoadListActivity;
import wqh.blog.ui.adapter.WorkAdapter;
import wqh.blog.ui.adapter.event.LayoutState;
import wqh.blog.ui.base.ScrollFragment;
import wqh.blog.ui.customview.Dialog;
import wqh.blog.util.CollectionUtil;
import wqh.blog.manager.IntentManager;
import wqh.blog.util.JsonUtil;
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
            if (PermissionManager.hasPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Dialog.create(getActivity(), getString(R.string.download_start) + data.title + "'").setPositiveListener(getString(R.string.download), v -> doDownload(data)).show();
            } else {
                PermissionManager.requestPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        });

        mDownLoadPresenter.loadAll(1, mDefaultLoadDataView);
    }

    /**
     * Callback for the result from requesting permissions. This method is invoked for every call on requestPermissions(android.app.Activity, String[], int).
     *
     * Note: It is possible that the permissions request interaction with the user is interrupted.
     * In this case you will receive empty permissions and results arrays which should be treated as a cancellation.
     *
     * @param requestCode  int: The request code passed in requestPermissions(android.app.Activity, String[], int)
     * @param permissions  String: The requested permissions. Never null.
     * @param grantResults int: The grant results for the corresponding permissions which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            for (String permission : permissions) {
                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    for (int grantResult : grantResults) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            // Download.
                        }
                    }
                }

            }
        }
    }


    @Override
    public int layoutId() {
        return R.layout.layout_list;
    }

    @Override
    public String toString() {
        return "Work";
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
        DownLoadHelper.instance().addDownLoadEvent(data.fileName, new WorkDownLoadEvent());
        DownLoadHelper.instance().offer(data.id, data.fileName, data.title);
        IntentManager.startService(getActivity(), WorkDownLoadService.class);
    }


    /**
     * A Load-Data View,which means show loaded-data is it's function.
     * What's more,the loaded-data is from DownLoadPresenter.
     * The default means that the view will exists forever unless a new Load-Data View is going to be added.
     */
    private class DefaultLoadView implements LoadView {

        @Override
        public void onSuccess(String resultJson) {
            List<Work> data = CollectionUtil.asList(JsonUtil.fromJson(resultJson, Work[].class));
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
                ToastUtil.showToast(R.string.no_network);
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
            Snackbar.make(mRootView, toDownload.title + getString(R.string.start), Snackbar.LENGTH_LONG).setAction(getString(R.string.undo), v -> {
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
            ToastUtil.showToast(getString(R.string.save_in) + "->" + toDownload.filePath);
        }

        @Override
        public void onFail(Download toDownload) {
            ToastUtil.showToast(R.string.download_fail);
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

            mBuilder.setContentTitle(getString(R.string.download_file)).setContentText(toDownload.title + getString(R.string.downloading)).setSmallIcon(R.mipmap.ic_launcher).setProgress(100, 0, false);
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
            mBuilder.setContentText(getString(R.string.download_finish)).setProgress(0, 0, false);
            mNotifyManager.notify(1, mBuilder.build());
            liteOrm.update(toDownload);
        }

        @Override
        public void onFail(Download toDownload) {
            super.onFail(toDownload);
        }
    }
}
