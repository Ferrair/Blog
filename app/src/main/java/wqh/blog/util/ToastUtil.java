package wqh.blog.util;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class ToastUtil {

    private static Context mContext;

    public static void showToast(final String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(@StringRes final int resId) {
        Toast.makeText(mContext, mContext.getString(resId), Toast.LENGTH_SHORT).show();
    }

    public static void register(Context context) {
        mContext = context.getApplicationContext();
    }

}
