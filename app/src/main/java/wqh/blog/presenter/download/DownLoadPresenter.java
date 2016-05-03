package wqh.blog.presenter.download;

import wqh.blog.presenter.LoadPresenter;
import wqh.blog.view.LoadView;

/**
 * Created by WQH on 2016/4/12  23:44.
 * <p>
 * Load Presenter for download data from server.
 */
public abstract class DownLoadPresenter<DataType> extends LoadPresenter<DataType> {

    //Load all data
    public abstract void loadAll(LoadView<DataType> mLoadView);

    //Load data by id
    public abstract void loadById(int id, LoadView<DataType> mLoadView);

    /**
     * Load data by the given condition,subclass MUST switch type to decide the condition
     *
     * @param condition the action param that will give to server
     * @param type      type in Type class
     */
    public abstract void loadByCondition(String condition, Type type, LoadView<DataType> mLoadView);

    public void loadAll() {
        if (mLoadView != null)
            loadAll(mLoadView);
    }

    public void loadById(int id) {
        if (mLoadView != null)
            loadById(id, mLoadView);
    }

    public void loadByCondition(String condition, Type type) {
        if (mLoadView != null)
            loadByCondition(condition, type, mLoadView);
    }

    public enum Type {
        TITLE, TAG, TIME, TYPE
    }

}
