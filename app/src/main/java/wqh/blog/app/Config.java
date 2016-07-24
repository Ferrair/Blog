package wqh.blog.app;

import android.os.Environment;

import java.io.File;

/**
 * Created by WQH on 2016/5/10  21:46.
 */
public class Config {
    /*
     * Net
     */
    public static final String DOMAIN = "http://wangqihang.cn:8080/Blog/";
    public static final String REMOTE_DIR = "http://wangqihang.cn:8080/Blog/file/";

    /*
     * Local Data Storage.
     */
    public static final File FILE_DIR = Environment.getExternalStorageDirectory();
    public static String DB_NAME = "blog.db";
    public static String APP_NAME = "Blog";

    /*
     * Operation.
     */
    public static final int REQUEST_ALBUM = 100; //request to open album.
}
