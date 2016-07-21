package wqh.blog.download;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import wqh.blog.mvp.model.bean.Download;
import wqh.blog.util.CollectionUtil;
import wqh.blog.util.ToastUtil;

/**
 * Created by WQH on 2016/5/22  19:51.
 */
public class DownLoadHelper {

    private static final String TAG = "DownLoadHelper";
    private Queue<Download> mDownLoadQueue = new LinkedList<>();
    private ExecutorService mService = Executors.newCachedThreadPool();
    private Map<String, DownLoadEvent> mDownLoadEventList = new HashMap<>();

    public List<Download> data() {
        return CollectionUtil.asList(mDownLoadQueue);
    }

    private static class ClassHolder {
        private static final DownLoadHelper INSTANCE = new DownLoadHelper();
    }

    public static DownLoadHelper instance() {
        return ClassHolder.INSTANCE;
    }

    public void offer(int id, String fileName, String title) {
        Download aDownLoad = new Download(id, fileName, title);
        if (mDownLoadQueue.contains(aDownLoad)) {
            if (aDownLoad.status != Download.FINISH)
                ToastUtil.showToast("该文件已在下载队列里面了");
        } else {
            mDownLoadQueue.offer(aDownLoad);
            DownLoadHelper.instance().dispatchPreStartEvent(aDownLoad);
        }
    }

    /**
     * Pool(take and remove) the element from the head of the queue.
     */
    public Download poll() {
        return mDownLoadQueue.poll();
    }

    public ExecutorService servicePool() {
        return mService;
    }

    public void dispatchOnPreProgressEvent(Download toDownload) {
        mDownLoadEventList.get(toDownload.fileName).onPreProgress(toDownload);
    }

    public void dispatchFailEvent(Download toDownload) {
        mDownLoadEventList.get(toDownload.fileName).onFail(toDownload);
    }

    public void dispatchStartEvent(Download toDownload) {
        mDownLoadEventList.get(toDownload.fileName).onStart(toDownload);
    }

    public void dispatchPreStartEvent(Download toDownload) {
        mDownLoadEventList.get(toDownload.fileName).onPreStart(toDownload);
    }

    public void dispatchSuccessEvent(Download toDownload) {
        mDownLoadEventList.get(toDownload.fileName).onSuccess(toDownload);
    }


    public void dispatchProgressEvent(Download toDownload, int percent) {
        mDownLoadEventList.get(toDownload.fileName).onProgress(toDownload, percent);
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

    public interface DownLoadEvent {

        void onPreProgress(Download toDownload);

        void onPreStart(Download toDownload);

        void onStart(Download toDownload);

        void onSuccess(Download toDownload);

        void onFail(Download toDownload);

        void onProgress(Download toDownload, int percent);
    }

    /**
     * A Simple implements of DownLoadEvent.
     * Provided some Toast.
     */
    public static class DownLoadEventAdapter implements DownLoadEvent {

        @Override
        public void onPreProgress(Download toDownload) {

        }

        @Override
        public void onPreStart(Download toDownload) {

        }

        @Override
        public void onStart(Download toDownload) {
            ToastUtil.showToast(toDownload.title + " 加入下载队列");
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


}
