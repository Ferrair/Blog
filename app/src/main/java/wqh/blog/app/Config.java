package wqh.blog.app;

import android.os.Environment;

import java.io.File;

/**
 * Created by WQH on 2016/5/10  21:46.
 */
public class Config {
    public static final String FILE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Blog";
    public static String DB_NAME = "blog.db";
}
