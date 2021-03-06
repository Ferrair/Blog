package wqh.blog.download;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import okhttp3.OkHttpClient;

/**
 * Created by WQH on 2016/5/22  19:44.
 */
public abstract class DownLoadService extends Service {
    protected OkHttpClient mOkHttpClient;
    protected Handler mHandler;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Do nothing here.
        return null;
    }

    protected abstract void startDownLoad();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void start() {
        init();
        startDownLoad();
    }

    private void init() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder().build();
        }
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
    }
}
