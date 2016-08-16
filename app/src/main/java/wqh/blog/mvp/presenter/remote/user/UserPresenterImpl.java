package wqh.blog.mvp.presenter.remote.user;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import wqh.blog.mvp.model.service.RemoteManager;
import wqh.blog.mvp.model.service.UserAPI;
import wqh.blog.mvp.presenter.remote.base.DefaultCallback;
import wqh.blog.mvp.presenter.remote.base.LoadPresenter;
import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/5/13  22:22.
 */
public class UserPresenterImpl extends LoadPresenter implements UserPresenter {

    private UserAPI mUserAPI;

    @Override
    protected void initAPI() {
        mUserAPI = RemoteManager.create(UserAPI.class);
    }

    @Override
    public void login(String username, String password, LoadView mLoadView) {
        Call<ResponseBody> call = mUserAPI.login(username, password);
        doAction(call, mLoadView);
    }

    @Override
    public void register(String username, String password, LoadView mLoadView) {
        Call<ResponseBody> call = mUserAPI.register(username, password);
        doAction(call, mLoadView);
    }

    @Override
    public void logout(int id, LoadView mLoadView) {
        Call<ResponseBody> call = mUserAPI.logout(id);
        doAction(call, mLoadView);
    }

    @Override
    public boolean status(int id, LoadView mLoadView) {
        return false;
    }

    @Override
    public void changeAvatar(int id, File avatarFile, LoadView mLoadView) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), avatarFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", avatarFile.getName(), requestFile);
        Call<ResponseBody> call = mUserAPI.changeAvatar(id, body);
        doAction(call, mLoadView);
    }

    @Override
    public void changeCover(int id, File coverFile, LoadView mLoadView) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), coverFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("cover", coverFile.getName(), requestFile);
        Call<ResponseBody> call = mUserAPI.changeCover(id, RequestBody.create(MediaType.parse("image/*"), coverFile));
    }

    private void doAction(Call<ResponseBody> call, LoadView mLoadView) {
        call.enqueue(new DefaultCallback(mLoadView));
    }
}
