package wqh.blog.presenter.download;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wqh.blog.model.bean.Holder;
import wqh.blog.model.bean.Work;
import wqh.blog.model.remote.RemoteManager;
import wqh.blog.model.remote.WorkAPI;
import wqh.blog.view.LoadView;

/**
 * Created by WQH on 2016/4/16  19:58.
 */
public class WorkDownLoadPresenter extends DownLoadPresenter<Work> {

    WorkAPI mWorkAPI;
    private static final String TAG = "WorkDownLoadPresenter";

    @Override
    protected void initAPI() {
        mWorkAPI = RemoteManager.create(WorkAPI.class);
    }

    @Override
    public void loadAll(LoadView<Work> mLoadView) {

    }

    @Override
    public void loadById(int id, LoadView<Work> mLoadView) {
        Call<Holder<Work>> call = mWorkAPI.queryById(id);
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
        Call<Holder<Work>> call = mWorkAPI.queryByTitle(title);
        doQuery(call, mLoadView);
    }

    private void doQuery(Call<Holder<Work>> call, LoadView<Work> mLoadView) {
        call.enqueue(new Callback<Holder<Work>>() {
            @Override
            public void onResponse(Call<Holder<Work>> call, Response<Holder<Work>> response) {
                if (response.isSuccessful()) {
                    Holder<Work> holder = response.body();
                    if (holder.Code == RemoteManager.OK) {
                        List<Work> mList = holder.dataList(Work[].class);
                        mLoadView.onSuccess(mList);

                    } else {
                        mLoadView.onFail(holder.Code, "At " + TAG + "#onResponse-> " + holder.Msg);
                    }
                } else {
                    mLoadView.onFail(RemoteManager.UNKNOWN, "At " + TAG + "#onResponse-> " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Holder<Work>> call, Throwable t) {
                mLoadView.onFail(RemoteManager.UNKNOWN, "At " + TAG + "#onFailure-> " + t.toString());
            }
        });
    }
}
