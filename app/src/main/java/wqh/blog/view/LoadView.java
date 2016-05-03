package wqh.blog.view;

import java.util.List;

/**
 * Created by WQH on 2016/4/12  23:47.
 */
public interface LoadView<DataType> {
    void onSuccess(List<DataType> data);

    void onFail(int errorCode, String errorMsg);
}
