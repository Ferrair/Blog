package wqh.blog.download;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import wqh.blog.mvp.model.bean.DownLoadBean;
import wqh.blog.util.CollectionUtil;
import wqh.blog.util.ToastUtil;

/**
 * Created by WQH on 2016/5/22  19:51.
 */
public class DownLoadHelper {

    private Queue<DownLoadBean> mDownLoadQueue = new LinkedList<>();
    private ExecutorService mService = Executors.newCachedThreadPool();
    private Map<String, DownLoadEvent> mDownLoadEventList = new HashMap<>();

    public List<DownLoadBean> data() {
        return CollectionUtil.asList(mDownLoadQueue);
    }

    private static class ClassHolder {
        private static final DownLoadHelper INSTANCE = new DownLoadHelper();
    }

    public static DownLoadHelper instance() {
        return ClassHolder.INSTANCE;
    }

    public void offer(String fileName, String title) {
        DownLoadBean aDownLoad = new DownLoadBean(fileName, title);
        if (mDownLoadQueue.contains(aDownLoad)) {
            if (aDownLoad.status != DownLoadBean.FINISH)
                ToastUtil.showToast("该文件已在下载目录里面了");
        } else {
            mDownLoadQueue.offer(aDownLoad);
        }
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
