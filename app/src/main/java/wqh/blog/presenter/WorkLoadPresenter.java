package wqh.blog.presenter;

import wqh.blog.bean.Work;
import wqh.blog.net.NetManager;
import wqh.blog.net.WorkAPI;
import wqh.blog.view.LoadDataView;

/**
 * Created by WQH on 2016/4/16  19:58.
 */
public class WorkLoadPresenter extends LoadDataPresenter<Work> {

    WorkAPI mWorkAPI;

    @Override
    public void initAPI() {
        mWorkAPI = NetManager.create(WorkAPI.class);
    }

    @Override
    public void loadAll(LoadDataView<Work> mLoadDataView) {

    }

    @Override
    public void loadById(int id, LoadDataView<Work> mLoadDataView) {

    }

    @Override
    public void loadByCondition(String condition, Type type, LoadDataView<Work> mLoadDataView) {
        switch (type) {
            case TITLE:
                loadByTitle(condition);
                break;
        }
    }

    protected void loadByTitle(String title) {

    }
}
