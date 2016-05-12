package wqh.blog.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * MUST add <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/> in AndroidManifest.xml
 */
public class StatusUtil {

    private static final String TAG = "StatusUtil";

    public static boolean isSDCardExist() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager connectivityManager;
        try {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return connectivityManager.getActiveNetworkInfo();
        } catch (NullPointerException e) {
            Log.e(TAG, "Unknown Reason");
        }
        return null;
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        if (networkInfo != null)
            return networkInfo.isAvailable();
        return false;
    }

    public static boolean isWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
        }
        return false;
    }

    public static boolean isMobileNetWork(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
        }
        return false;
    }
}
