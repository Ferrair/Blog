package wqh.blog.manager;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import wqh.blog.R;
import wqh.blog.util.ToastUtil;

/**
 * Created by WQH on 2016/8/4  17:36.
 *
 * In Android M ,dynastic grant permission.
 */
public class PermissionManager {
    /**
     * Query if user grant this permission.
     *
     *
     * SDCard : Manifest.permission.WRITE_EXTERNAL_STORAGE
     * Camera : Manifest.permission.CAMERA
     */
    public static boolean hasPermission(Activity mActivity, String permission) {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    /**
     * Request Permission.
     */
    public static void requestPermission(Activity mActivity, String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
            ToastUtil.showToast(R.string.permission_denied);
        } else {
            ActivityCompat.requestPermissions(mActivity, new String[]{permission}, 10);
        }
    }
}

