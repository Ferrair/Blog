package wqh.blog.app;

import android.os.Environment;

import java.io.File;

/**
 * Created by WQH on 2016/5/10  21:46.
 */
public class Config {
    public static final File FILE_DIR = Environment.getExternalStorageDirectory();
    public static String DB_NAME = "blog.db";
    public static String APP_NAME = "Blog";
}
