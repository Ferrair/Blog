package wqh.blog.util;
import java.io.File;

import wqh.blog.app.Config;

/**
 * Created by WQH on 2016/5/22  20:47.
 */
public class FileUtil {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File makeFile(File dirFile, String fileName) {
        File dir = new File(dirFile, Config.APP_NAME);
        if (!dir.exists())
            dir.mkdir();
        return new File(dir, fileName);
    }
}
