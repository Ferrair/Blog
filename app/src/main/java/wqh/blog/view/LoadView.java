package wqh.blog.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by WQH on 2016/4/12  23:47.
 */
public interface LoadView<DataType> {
    /**
     * Null only occurs in POST action.means success.
     */
    @Nullable
    void onSuccess(List<DataType> data);

    @NonNull
    void onFail(int errorCode, String errorMsg);
}
