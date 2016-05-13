package wqh.blog.mvp.presenter.remote.base;

import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/5/2  18:46.
 * <p>
 * Load Presenter for upload data to server.
 */
public interface UpLoadPresenter<DataType> {
    void publish(DataType aData, LoadView<DataType> mLoadView);
}
