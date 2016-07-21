package wqh.blog.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Request;
import okhttp3.Response;
import wqh.blog.app.Config;
import wqh.blog.mvp.model.bean.Download;
import wqh.blog.util.FileUtil;

/**
 * Created by WQH on 2016/5/22  20:21.
 */
public class WorkDownLoadService extends DownLoadService {

    @Override
    protected void startDownLoad() {
        DownLoadHelper.instance().servicePool().execute(new DownloadThread());
    }


    private class DownloadThread extends Thread {
        @Override
        public void run() {
            while (true) {
                Download toDownLoad = DownLoadHelper.instance().poll();
                if (toDownLoad == null)
                    break;
                if (toDownLoad.status != Download.DOWNLOADING) {
                    doDownload(toDownLoad);
                }
            }
        }

        private void doDownload(Download toDownLoad) {
            final File file = FileUtil.makeFile(Config.FILE_DIR, toDownLoad.fileName);
            if (file == null) {
                return;
            }
            final Request request = new Request.Builder().url("http://wangqihang.cn:8080/Blog/work/download?fileName=" + toDownLoad.fileName).build();
            OutputStream fos = null;
            InputStream is = null;
            mHandler.post(() -> DownLoadHelper.instance().dispatchStartEvent(toDownLoad));
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    long downloadLen = 0;
                    int readLen;
                    byte[] buffer = new byte[2048];
                    long totalLen = response.body().contentLength();

                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);

                    toDownLoad.status = Download.DOWNLOADING;
                    mHandler.post(() -> DownLoadHelper.instance().dispatchOnPreProgressEvent(toDownLoad));
                    while ((readLen = is.read(buffer)) != -1) {
                        downloadLen += readLen;
                        fos.write(buffer, 0, readLen);
                        final int percent = (int) (downloadLen * 1.0f / totalLen * 100);
                        mHandler.post(() -> DownLoadHelper.instance().dispatchProgressEvent(toDownLoad, percent));

                    }
                    fos.flush();
                    toDownLoad.filePath = file.getAbsolutePath();
                    toDownLoad.status = Download.FINISH;
                    mHandler.post(() -> DownLoadHelper.instance().dispatchSuccessEvent(toDownLoad));
                }
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.post(() -> DownLoadHelper.instance().dispatchFailEvent(toDownLoad));
            } catch (Throwable ignored) {
            } finally {
                try {
                    if (fos != null)
                        fos.close();
                    if (is != null)
                        is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.post(() -> DownLoadHelper.instance().dispatchFailEvent(toDownLoad));
                }
            }
        }
    }
}
