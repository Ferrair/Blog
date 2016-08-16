package wqh.blog.util;

import java.io.File;

import wqh.blog.app.Config;

/**
 * Created by WQH on 2016/5/22  20:47.
 */
public class FileUtil {

    /**
     * The diffence between <code>mkdir()</code> and  <code>mkdirs()</code>
     * mkdir() : Creates the directory named by this abstract pathname.
     * mkdirs() : Creates the directory named by this abstract pathname, including any necessary but nonexistent parent directories. Note that if this operation fails it may have succeeded in creating some of the necessary parent directories.
     *
     * <link>http://stackoverflow.com/questions/9820088/difference-between-mkdir-and-mkdirs-in-java-for-java-io-file</link>
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File makeFile(File dirFile, String fileName) {
        File dir = new File(dirFile, Config.APP_NAME);
        if (!dir.exists())
            dir.mkdirs();
        return new File(dir, fileName);
    }
}
