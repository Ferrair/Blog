package wqh.blog.presenter;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wqh.blog.model.bean.Holder;
import wqh.blog.model.bean.Work;
import wqh.blog.model.remote.RemoteManager;
import wqh.blog.model.remote.WorkAPI;
import wqh.blog.view.LoadDataView;

/**
 * Created by WQH on 2016/4/16  19:58.
 */
public class WorkLoadPresenter extends LoadDataPresenter<Work> {

    WorkAPI mWorkAPI;
    private static final String TAG = "WorkLoadPresenter";

    @Override
    public void initAPI() {
        mWorkAPI = RemoteManager.create(WorkAPI.class);
    }

    @Override
    public void loadAll(LoadDataView<Work> mLoadDataView) {

    }

    @Override
    public void loadById(int id, LoadDataView<Work> mLoadDataView) {
        Call<Holder<Work>> call = mWorkAPI.queryById(id);
        doQuery(call, mLoadDataView);
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
        Call<Holder<Work>> call = mWorkAPI.queryByTitle(title);
        doQuery(call, mLoadDataView);
    }

    private void doQuery(Call<Holder<Work>> call, LoadDataView<Work> mLoadDataView) {
        call.enqueue(new Callback<Holder<Work>>() {
            @Override
            public void onResponse(Call<Holder<Work>> call, Response<Holder<Work>> response) {
                if (response.isSuccessful()) {
                    Holder<Work> holder = response.body();
                    if (holder.Code == RemoteManager.OK) {
                        Log.i(TAG, holder.Result.toString());
                        List<Work> mList = holder.dataList(Work[].class);
                        mLoadDataView.onSuccess(mList);

                    } else {
                        mLoadDataView.onFail(holder.Code, "At " + TAG + "#onResponse-> " + holder.Msg);
                    }
                } else {
                    mLoadDataView.onFail(RemoteManager.UNKNOWN, "At " + TAG + "#onResponse-> " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Holder<Work>> call, Throwable t) {
                mLoadDataView.onFail(RemoteManager.UNKNOWN, "At " + TAG + "#onFailure-> " + t.toString());
            }
        });
    }
}
