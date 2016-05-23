package wqh.blog.download;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Request;
import okhttp3.Response;
import wqh.blog.app.Config;
import wqh.blog.util.FileUtil;

/**
 * Created by WQH on 2016/5/22  20:21.
 */
public class WorkDownLoadService extends DownLoadService {
    private static final String TAG = "WorkDownLoadService";

    @Override
    protected void startDownLoad() {
        DownLoadHelper.DownLoadBean toDownLoad = DownLoadHelper.instance().poll();
        if (toDownLoad != null && toDownLoad.status != DownLoadHelper.DOWNLOAD) {
            DownLoadHelper.instance().servicePool().execute(new DownloadThread(toDownLoad));
        }
    }


    private class DownloadThread extends Thread {
        DownLoadHelper.DownLoadBean toDownLoad;

        public DownloadThread(@NonNull DownLoadHelper.DownLoadBean toDownLoad) {
            this.toDownLoad = toDownLoad;
        }

        public void run() {
            final File file = FileUtil.makeFile(Config.FILE_DIR, toDownLoad.fileName);
            if (file == null) {
                Log.i(TAG, "File Is Null");
                return;
            }
            Log.i(TAG, "FilePath-> " + file.getAbsolutePath());

            final Request request = new Request.Builder().url("http://wangqihang.cn:8080/Blog/work/download?fileName=" + toDownLoad.fileName).build();
            FileOutputStream fos = null;
            InputStream is = null;

            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    long downloadLen = 0;
                    int readLen;
                    byte[] buffer = new byte[2048];

                    long totalLen = response.body().contentLength();
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((readLen = is.read(buffer)) != -1) {
                        downloadLen += readLen;
                        fos.write(buffer, 0, readLen);
                        final int percent = (int) (downloadLen * 1.0f / totalLen * 100);
                        Log.i(TAG, "Percent-> " + String.valueOf(percent));

                    }
                    fos.flush();

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable ignored) {
            } finally {
                try {
                    if (fos != null)
                        fos.close();
                    if (is != null)
                        is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
