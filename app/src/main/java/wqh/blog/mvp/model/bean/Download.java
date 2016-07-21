package wqh.blog.mvp.model.bean;

import android.support.annotation.IntDef;

import com.litesuits.orm.db.annotation.Default;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by WQH on 2016/5/27  23:53.
 */
@Table("download")
public class Download {
    /*
    * Status of download file.
    */
    @Ignore
    public static final int BEFORE = 0;
    @Ignore
    public static final int PAUSE = 1;
    @Ignore
    public static final int DOWNLOADING = 2;
    @Ignore
    public static final int FINISH = 3;

    @IntDef({BEFORE, PAUSE, DOWNLOADING, FINISH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DownLoadStatus {
    }


    /*
     * Member.
     */
    @PrimaryKey(AssignType.BY_MYSELF)
    public int id;
    public String filePath; // the apk filePath after download.
    public String title;    // the title that shown to user.
    @DownLoadStatus
    public int status;
    @Ignore
    public String fileName; // The apk fileName


    public Download(int id, String fileName, String title) {
        this.id = id;
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
        return other instanceof Download && ((Download) other).id == id;
    }
}
