package wqh.blog.mvp.view;

/**
 * Created by WQH on 2016/4/12  23:47.
 */
public interface LoadView {
    /**
     * @param resultJson a JSON string associated with <code>Holder.Result</code>,So the subclass MUST parse the JSON string.
     */
    void onSuccess(String resultJson);

    void onFail(int errorCode, String errorMsg);
}
