package wqh.blog.mvp.presenter.remote.base;

import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/4/12  23:44.
 * <p>
 * Load Presenter for download data from server.
 * Other class CAN implements it or implements their own Presenter.
 */
public interface DownLoadPresenter {

    //Load all data paginate.
    void loadAll(int pageNum, LoadView mLoadView);

    //Load data by id
    void loadById(int id, LoadView mLoadView);

    /**
     * Load data by the given condition,subclass MUST switch type to decide the condition
     *
     * @param condition the action param that will give to server
     * @param type      type in Type class
     */
    void loadByCondition(String condition, Type type, LoadView mLoadView);

    enum Type {
        TITLE, TAG, TIME, TYPE
    }

}
