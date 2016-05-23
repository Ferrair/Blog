package wqh.blog.download;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import wqh.blog.util.ToastUtil;

/**
 * Created by WQH on 2016/5/22  19:51.
 */
public class DownLoadHelper {

    public static final int BEFORE = 0;
    public static final int PAUSE = 1;
    public static final int DOWNLOAD = 2;
    public static final int FINISH = 3;
    public static final int FAIL = 4;

    @IntDef({BEFORE, PAUSE, DOWNLOAD, FINISH, FAIL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DownLoadStatus {
    }

    private Queue<DownLoadBean> mDownLoadQueue = new LinkedList<>();
    private ExecutorService mService = Executors.newCachedThreadPool();
    private Map<String, DownLoadEvent> mDownLoadEventList = new HashMap<>();

    private static class ClassHolder {
        private static final DownLoadHelper INSTANCE = new DownLoadHelper();
    }

    public static DownLoadHelper instance() {
        return ClassHolder.INSTANCE;
    }

    public void offer(String fileName) {
        DownLoadBean aDownLoad = new DownLoadBean(fileName);
        if (mDownLoadQueue.contains(aDownLoad)) {
            // Callback
            ToastUtil.showToast("该文件已在下载目录里面了");
        } else {
            mDownLoadQueue.offer(aDownLoad);
            // Callback
            ToastUtil.showToast("已添加到下载目录");
        }
    }

    public DownLoadBean poll() {
        return mDownLoadQueue.poll();
    }

    public ExecutorService servicePool() {
        return mService;
    }

    public void dispatchFailEvent(String fileName, String errorMsg) {
        mDownLoadEventList.get(fileName).onFail(errorMsg);
    }

    public void dispatchStartEvent(String fileName, String targetTitle) {
        mDownLoadEventList.get(fileName).onStart(targetTitle);
    }


    public void dispatchSuccessEvent(String fileName, String filePath) {
        mDownLoadEventList.get(fileName).onSuccess(filePath);
    }


    public void dispatchPregressEvent(String fileName, int percent) {
        mDownLoadEventList.get(fileName).onProgress(percent);
    }

    public void addDownLoadEvent(String fileName, DownLoadEvent aDownLoadEvent) {
        mDownLoadEventList.put(fileName, aDownLoadEvent);
    }

    public static class DownLoadBean {
        String fileName;
        @DownLoadStatus
        int status;

        public DownLoadBean(String fileName) {
            this.fileName = fileName;
            this.status = BEFORE;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof DownLoadBean && ((DownLoadBean) other).fileName.equals(fileName);
        }
    }

    public interface DownLoadEvent {
        void onStart(String targetTitle);

        void onSuccess(String filePath);

        void onFail(String errorMsg);

        void onProgress(int percent);
    }

    public class DownLoadEventAdapter implements DownLoadEvent {

        @Override
        public void onStart(String targetTitle) {

        }

        @Override
        public void onSuccess(String filePath) {

        }

        @Override
        public void onFail(String errorMsg) {

        }

        @Override
        public void onProgress(int percent) {

        }
    }

}
