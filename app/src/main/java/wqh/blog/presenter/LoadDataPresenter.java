package wqh.blog.presenter;

import wqh.blog.view.LoadDataView;

/**
 * Created by WQH on 2016/4/12  23:44.
 */
public abstract class LoadDataPresenter<DataType> {

    /**
     * The default LoadDataView,the LoadDataView will not change.
     */
    protected LoadDataView<DataType> mLoadDataView;

    public LoadDataPresenter(LoadDataView<DataType> mLoadDataView) {
        this.mLoadDataView = mLoadDataView;
        initAPI();
    }

    public LoadDataPresenter() {
        initAPI();
    }

    /**
     * Init the NewManager in vary subclass
     */
    public abstract void initAPI();

    //Load all data
    public abstract void loadAll(LoadDataView<DataType> mLoadDataView);

    //Load data by id
    public abstract void loadById(int id, LoadDataView<DataType> mLoadDataView);

    /**
     * Load data by the given condition,subclass MUST switch type to decide the condition
     *
     * @param condition the action param that will give to server
     * @param type      type in Type class
     */
    public abstract void loadByCondition(String condition, Type type, LoadDataView<DataType> mLoadDataView);

    public void loadAll() {
        if (mLoadDataView != null)
            loadAll(mLoadDataView);
    }

    public void loadById(int id) {
        if (mLoadDataView != null)
            loadById(id, mLoadDataView);
    }

    public void loadByCondition(String condition, Type type) {
        if (mLoadDataView != null)
            loadByCondition(condition, type, mLoadDataView);
    }

    public enum Type {
        TITLE, TAG, TIME
    }

}
