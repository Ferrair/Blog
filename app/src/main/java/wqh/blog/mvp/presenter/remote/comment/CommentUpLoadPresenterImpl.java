package wqh.blog.mvp.presenter.remote.comment;


import android.util.Log;

import okhttp3.ResponseBody;
import retrofit2.Call;
import wqh.blog.mvp.model.bean.Comment;
import wqh.blog.mvp.model.bean.User;
import wqh.blog.mvp.model.service.CommentAPI;
import wqh.blog.mvp.model.service.RemoteManager;
import wqh.blog.mvp.model.service.UserManager;
import wqh.blog.mvp.presenter.remote.base.DefaultCallback;
import wqh.blog.mvp.presenter.remote.base.LoadPresenter;
import wqh.blog.mvp.presenter.remote.base.UpLoadPresenter;
import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/5/2  18:52.
 */
public class CommentUpLoadPresenterImpl extends LoadPresenter implements CommentUpLoadPresenter {
    CommentAPI mCommentAPI;

    @Override
    protected void initAPI() {
        mCommentAPI = RemoteManager.create(CommentAPI.class);
    }

    @Override
    public void publish(Comment aData, LoadView mLoadView) {
        User currentUser = UserManager.instance().currentUser();
        Call<ResponseBody> call = mCommentAPI.postComment(aData.belongTo, aData.content, aData.createdBy, currentUser.id, currentUser.token);
        Log.i("User", currentUser.toString());
        doPublish(call, mLoadView);
    }


    @Override
    public void reply(Comment aData, LoadView mLoadView) {
        User currentUser = UserManager.instance().currentUser();
        Call<ResponseBody> call = mCommentAPI.replyComment(aData.belongTo, aData.content, aData.createdBy, aData.replyTo, currentUser.id, currentUser.token);
        Log.i("User", currentUser.toString());
        doPublish(call, mLoadView);
    }

    private void doPublish(Call<ResponseBody> call, LoadView mLoadView) {
        call.enqueue(new DefaultCallback(mLoadView));
    }
}
