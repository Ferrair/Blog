package wqh.blog.mvp.model.bean;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by WQH on 2016/5/27  23:53.
 */
public class DownLoadBean {
    public static final int BEFORE = 0;
    public static final int PAUSE = 1;
    public static final int DOWNLOADING = 2;
    public static final int FINISH = 3;

    @IntDef({BEFORE, PAUSE, DOWNLOADING, FINISH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DownLoadStatus {
    }

    public String filePath; // the apk filePath after download.
    public String fileName; // The apk fileName
    public String title;    // the title that shown to user.
    @DownLoadStatus
    public int status;


    public DownLoadBean(String fileName, String title) {
        this.fileName = fileName;
        this.status = BEFORE;
        this.title = title;
    }

    public static String convert(@DownLoadStatus int status) {
        String result = "";
        switch (status) {
            case BEFORE:
                result = "未下载";
                break;
            case PAUSE:
                result = "暂停";
                break;
            case DOWNLOADING:
                result = "正在下载";
                break;
            case FINISH:
                result = "下载完成";
                break;
        }
        return result;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof DownLoadBean && ((DownLoadBean) other).fileName.equals(fileName);
    }
}
