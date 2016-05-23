package wqh.blog.util;

import android.content.Context;
import android.content.SharedPreferences;

import wqh.blog.app.WQHApplication;

/**
 * Created by WQH on 2016/5/14  21:44.
 * A Util that can store data by <code>SharedPreferences</code>.
 */
public class SharePreferenceUtil {
    private SharedPreferences sharedPreferences;

    private static class ClassHolder {
        private static final SharePreferenceUtil INSTANCE = new SharePreferenceUtil();
    }

    private SharePreferenceUtil() {
        sharedPreferences = WQHApplication.getApp().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
    }

    /**
     * Open read Mode. The operation after MUST be read.
     */
    public static SharedPreferences read() {
        return ClassHolder.INSTANCE.sharedPreferences;
    }

    /**
     * Open read Mode. The operation after MUST be write.
     */
    public static SharedPreferences.Editor write() {
        return read().edit();
    }

}
