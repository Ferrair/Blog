package wqh.blog.ui.customview;

import android.content.Context;
import android.view.View;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by WQH on 2016/5/5  20:06.
 *
 * Dialog-Builder by building MaterialDialog.
 */
public class Dialog {
    private MaterialDialog mMaterialDialog;

    private Dialog(Context mContext, String title, String message) {
        mMaterialDialog = new MaterialDialog(mContext);
        mMaterialDialog.setTitle(title);
        mMaterialDialog.setMessage(message);
    }

    public Dialog setPositiveListener(String text, View.OnClickListener listener) {
        mMaterialDialog.setPositiveButton(text, v -> {
            listener.onClick(v);
            mMaterialDialog.dismiss();
        });
        return this;
    }

    public void show() {
        mMaterialDialog.show();
    }

    /**
     * Create a Dialog that have a Message and a PositiveButton.
     *
     * Using like this:
     * <code>Dialog.create(getActivity(), "是否下载 '" + data.title + "'").setPositiveListener("下载", v -> doDownload(data)).show()</code>
     */
    public static Dialog create(Context context, String message) {
        return new Dialog(context, null, message).setNegativeListener("取消");
    }

    private Dialog setNegativeListener(String text, View.OnClickListener listener) {
        mMaterialDialog.setNegativeButton(text, listener);
        return this;
    }

    private Dialog setNegativeListener(String text) {
        return this.setNegativeListener(text, v -> mMaterialDialog.dismiss());
    }


}
