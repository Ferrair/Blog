package wqh.blog.mvp.presenter.remote.user;

import okhttp3.ResponseBody;
import retrofit2.Call;
import wqh.blog.mvp.model.bean.User;
import wqh.blog.mvp.model.service.RemoteManager;
import wqh.blog.mvp.model.service.UserAPI;
import wqh.blog.mvp.presenter.remote.base.DefaultCallback;
import wqh.blog.mvp.presenter.remote.base.LoadPresenter;
import wqh.blog.mvp.view.LoadView;
import wqh.blog.util.CollectionUtil;
import wqh.blog.util.Json;

/**
 * Created by WQH on 2016/5/13  22:22.
 */
public class UserPresenterImpl extends LoadPresenter<User> implements UserPresenter {

    private UserAPI mUserAPI;

    @Override
    protected void initAPI() {
        mUserAPI = RemoteManager.create(UserAPI.class);
    }

    @Override
    public void login(String username, String password, LoadView<User> mLoadView) {
        Call<ResponseBody> call = mUserAPI.login(username, password);
        doAction(call, mLoadView);
    }

    @Override
    public void register(String username, String password, LoadView<User> mLoadView) {
        Call<ResponseBody> call = mUserAPI.register(username, password);
        doAction(call, mLoadView);
    }

    private void doAction(Call<ResponseBody> call, LoadView<User> mLoadView) {
        call.enqueue(new UserCallback(mLoadView));
    }

    @Override
    public void logout(int id, LoadView<User> mLoadView) {
        Call<ResponseBody> call = mUserAPI.logout(id);
        doAction(call, mLoadView);
    }

    @Override
    public boolean status(int id, LoadView<User> mLoadView) {
        return false;
    }

    private class UserCallback extends DefaultCallback<User> {

        public UserCallback(LoadView<User> mLoadView) {
            super(mLoadView);
        }

        @Override
        protected void onParseResult(String result) {
            mLoadView.onSuccess(CollectionUtil.asList(Json.fromJson(result, User[].class)));
        }
    }
}
