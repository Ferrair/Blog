package wqh.blog.mvp.presenter.remote.base;

import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/5/2  19:06.
 * <p>
 * Load Presenter. provided some init methods.
 * So the subclass can add some specific load jobs.@link{DownLoadPresenter} and @link{UpLoadPresenter}
 */
public abstract class LoadPresenter<DataType> {
    /**
     * The default LoadView,the LoadView will not change.
     */
    protected LoadView<DataType> mLoadView;

    public LoadPresenter(LoadView<DataType> mLoadView) {
        this.mLoadView = mLoadView;
        initAPI();
    }

    public LoadPresenter() {
        initAPI();
    }

    /**
     * Init the NewManager in vary subclass
     */
    protected abstract void initAPI();
}
