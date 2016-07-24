package wqh.blog.mvp.presenter.remote.base;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wqh.blog.mvp.model.bean.Holder;
import wqh.blog.mvp.model.service.RemoteManager;
import wqh.blog.util.Json;
import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/5/8  19:25.
 */
public class DefaultCallback implements Callback<ResponseBody> {

    private static final String TAG = "DefaultCallback";
    protected LoadView mLoadView;

    public DefaultCallback(LoadView mLoadView) {
        this.mLoadView = mLoadView;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                /**
                 * ResponseBody:
                 * A one-shot stream from the origin server to the client application with the raw bytes of the response body.
                 * Each response body is supported by an active connection to the WebServer.
                 * This imposes both obligations and limits on the client application.
                 */
                String jsonStr = response.body().string();
                Holder holder = Json.fromJson(jsonStr, Holder.class);

                if (holder.Code == RemoteManager.OK) {
                    mLoadView.onSuccess(holder.Result.toString());
                } else {
                    mLoadView.onFail(holder.Code, holder.Msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Can't Find Object.
                mLoadView.onFail(RemoteManager.SYNTAX, "At " + TAG + "#onResponse-> Can't Find Object.Because JSONNull");
            }
        } else {
            mLoadView.onFail(RemoteManager.PARSE, "At " + TAG + "#onResponse-> " + response.errorBody().toString());
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mLoadView.onFail(RemoteManager.PARSE, "At " + TAG + "#onFailure-> " + t.toString());
    }
}
