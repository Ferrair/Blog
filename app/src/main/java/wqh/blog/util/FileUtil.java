package wqh.blog.util;

import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by WQH on 2016/5/22  20:47.
 */
public class FileUtil {

    private static final String TAG = "FileUtil";

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File makeFile(String dirName, String fileName) {
        File dir = new File(dirName);
        if (!dir.exists())
            if(!dir.mkdir())
            {
                Log.i(TAG,"Create DIR Fail");
            }

        File file = new File(dirName, fileName);
        Log.i(TAG, file.getAbsolutePath());

        if (!file.exists()) {
            try {
                if (file.createNewFile())
                    return file;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
