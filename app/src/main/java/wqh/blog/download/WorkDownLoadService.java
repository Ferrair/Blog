package wqh.blog.download;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Request;
import okhttp3.Response;
import wqh.blog.app.Config;
import wqh.blog.mvp.model.bean.DownLoadBean;
import wqh.blog.util.FileUtil;

/**
 * Created by WQH on 2016/5/22  20:21.
 */
public class WorkDownLoadService extends DownLoadService {

    @Override
    protected void startDownLoad() {
        DownLoadBean toDownLoad = DownLoadHelper.instance().peek();
        if (toDownLoad != null && toDownLoad.status != DownLoadBean.DOWNLOADING) {
            DownLoadHelper.instance().servicePool().execute(new DownloadThread(toDownLoad));
        }
    }

    private class DownloadThread extends Thread {
        DownLoadBean toDownLoad;

        public DownloadThread(@NonNull DownLoadBean toDownLoad) {
            this.toDownLoad = toDownLoad;
        }

        public void run() {
            final File file = FileUtil.makeFile(Config.FILE_DIR, toDownLoad.fileName);
            if (file == null) {
                return;
            }
            final Request request = new Request.Builder().url("http://wangqihang.cn:8080/Blog/work/download?fileName=" + toDownLoad.fileName).build();
            OutputStream fos = null;
            InputStream is = null;
            mHandler.post(() -> DownLoadHelper.instance().dispatchStartEvent(toDownLoad.fileName, toDownLoad.title));
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    long downloadLen = 0;
                    int readLen;
                    byte[] buffer = new byte[2048];
                    long totalLen = response.body().contentLength();

                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);

                    toDownLoad.status = DownLoadBean.DOWNLOADING;
                    while ((readLen = is.read(buffer)) != -1) {
                        downloadLen += readLen;
                        fos.write(buffer, 0, readLen);
                        final int percent = (int) (downloadLen * 1.0f / totalLen * 100);
                        DownLoadHelper.instance().dispatchProgressEvent(toDownLoad.fileName, percent);

                    }
                    fos.flush();
                    mHandler.post(() -> DownLoadHelper.instance().dispatchSuccessEvent(toDownLoad.fileName, file.getAbsolutePath()));
                    toDownLoad.status = DownLoadBean.FINISH;
                    toDownLoad.filePath = file.getAbsolutePath();
                }
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.post(() -> DownLoadHelper.instance().dispatchFailEvent(toDownLoad.fileName));
            } catch (Throwable ignored) {
            } finally {
                try {
                    if (fos != null)
                        fos.close();
                    if (is != null)
                        is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.post(() -> DownLoadHelper.instance().dispatchFailEvent(toDownLoad.fileName));
                }
            }
        }
    }
}
