package wqh.blog.mvp.presenter.remote.download;


import okhttp3.ResponseBody;
import retrofit2.Call;
import wqh.blog.mvp.model.bean.Work;
import wqh.blog.mvp.model.service.RemoteManager;
import wqh.blog.mvp.model.service.WorkAPI;
import wqh.blog.mvp.presenter.remote.DefaultCallback;
import wqh.blog.util.CollectionUtil;
import wqh.blog.util.Json;
import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/4/16  19:58.
 */
public class WorkDownLoadPresenter extends DownLoadPresenter<Work> {

    WorkAPI mWorkAPI;

    @Override
    protected void initAPI() {
        mWorkAPI = RemoteManager.create(WorkAPI.class);
    }

    @Override
    public void loadAll(LoadView<Work> mLoadView) {
        Call<ResponseBody> call = mWorkAPI.queryAll();
        doQuery(call, mLoadView);
    }

    @Override
    public void loadById(int id, LoadView<Work> mLoadView) {
        Call<ResponseBody> call = mWorkAPI.queryById(id);
        doQuery(call, mLoadView);
    }

    @Override
    public void loadByCondition(String condition, Type type, LoadView<Work> mLoadView) {
        switch (type) {
            case TITLE:
                loadByTitle(condition);
                break;
        }
    }

    protected void loadByTitle(String title) {
        Call<ResponseBody> call = mWorkAPI.queryByTitle(title);
        doQuery(call, mLoadView);
    }

    private void doQuery(Call<ResponseBody> call, LoadView<Work> mLoadView) {
        call.enqueue(new WorkCallback(mLoadView));
    }

    private class WorkCallback extends DefaultCallback<Work> {

        public WorkCallback(LoadView<Work> mLoadView) {
            super(mLoadView);
        }

        @Override
        protected void onParseResult(String result) {
            mLoadView.onSuccess(CollectionUtil.asList(Json.fromJson(result, Work[].class)));
        }
    }
}