package wqh.blog.presenter.upload;

import wqh.blog.presenter.LoadPresenter;
import wqh.blog.view.LoadView;

/**
 * Created by WQH on 2016/5/2  18:46.
 * <p>
 * Load Presenter for upload data to server.
 */
public abstract class UpLoadPresenter<DataType> extends LoadPresenter<DataType> {

    public void publish(DataType aData) {
        if (mLoadView != null)
            this.publish(aData, mLoadView);
    }

    public abstract void publish(DataType aData, LoadView<DataType> mLoadView);
}
