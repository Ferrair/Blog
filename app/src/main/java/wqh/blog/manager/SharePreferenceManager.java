package wqh.blog.manager;

import android.content.Context;
import android.content.SharedPreferences;

import wqh.blog.app.WQHApplication;

/**
 * Created by WQH on 2016/5/14  21:44.
 * A Util that can store data by <code>SharedPreferences</code>.
 */
public class SharePreferenceManager {
    private SharedPreferences sharedPreferences;

    private static class ClassHolder {
        private static final SharePreferenceManager INSTANCE = new SharePreferenceManager();
    }

    private SharePreferenceManager() {
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
