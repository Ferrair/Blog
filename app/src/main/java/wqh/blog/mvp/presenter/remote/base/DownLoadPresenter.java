package wqh.blog.mvp.presenter.remote.base;

import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/4/12  23:44.
 * <p>
 * Load Presenter for download data from server.
 */
public interface DownLoadPresenter<DataType> {

    //Load all data
    void loadAll(LoadView<DataType> mLoadView);

    //Load data by id
    void loadById(int id, LoadView<DataType> mLoadView);

    /**
     * Load data by the given condition,subclass MUST switch type to decide the condition
     *
     * @param condition the action param that will give to server
     * @param type      type in Type class
     */
    void loadByCondition(String condition, Type type, LoadView<DataType> mLoadView);

    enum Type {
        TITLE, TAG, TIME, TYPE
    }

}
