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

    public void offer(String fileName, String title) {
        DownLoadBean aDownLoad = new DownLoadBean(fileName, title);
      /*  if (mDownLoadQueue.contains(aDownLoad)) {
            // Callback
            ToastUtil.showToast("该文件已在下载目录里面了");
        } else {*/
            mDownLoadQueue.offer(aDownLoad);
        //}
    }

    /**
     * Peek the element from the head of the queue.
     */
    public DownLoadBean peek() {
        return mDownLoadQueue.peek();
    }

    public ExecutorService servicePool() {
        return mService;
    }

    public void dispatchFailEvent(String fileName) {
        mDownLoadEventList.get(fileName).onFail();
    }

    public void dispatchStartEvent(String fileName, String targetTitle) {
        mDownLoadEventList.get(fileName).onStart(targetTitle);
    }


    public void dispatchSuccessEvent(String fileName, String filePath) {
        mDownLoadEventList.get(fileName).onSuccess(filePath);
    }


    public void dispatchProgressEvent(String fileName, int percent) {
        mDownLoadEventList.get(fileName).onProgress(percent);
    }

    /**
     * Add a LoadEvent listener in download progress.
     * by add this,some dispatch* method that can work.
     *
     * @param fileName       the key
     * @param aDownLoadEvent the listener
     */
    public void addDownLoadEvent(String fileName, DownLoadEvent aDownLoadEvent) {
        mDownLoadEventList.put(fileName, aDownLoadEvent);
    }

    public static class DownLoadBean {
        String fileName; // The apk fileName
        String title;    // the title that shown to user.
        @DownLoadStatus
        int status;

        public DownLoadBean(String fileName, String title) {
            this.fileName = fileName;
            this.status = BEFORE;
            this.title = title;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof DownLoadBean && ((DownLoadBean) other).fileName.equals(fileName);
        }
    }

    public interface DownLoadEvent {
        void onStart(String targetTitle);

        void onSuccess(String filePath);

        void onFail();

        void onProgress(int percent);
    }

    /**
     * A Simple implements of DownLoadEvent.
     * Provided some Toast.
     */
    public static class DownLoadEventAdapter implements DownLoadEvent {

        @Override
        public void onStart(String targetTitle) {
            ToastUtil.showToast(targetTitle + " 加入下载队列");
        }

        @Override
        public void onSuccess(String filePath) {
            ToastUtil.showToast("已保存到->" + filePath);
        }

        @Override
        public void onFail() {
            ToastUtil.showToast("下载失败");
        }

        @Override
        public void onProgress(int percent) {

        }
    }

}
