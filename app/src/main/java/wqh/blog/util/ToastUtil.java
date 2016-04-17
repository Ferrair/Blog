package wqh.blog.util;

import android.content.Context;
import android.widget.Toast;


public class ToastUtil {
    public static void showToast(final Context mContext, final String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

}
