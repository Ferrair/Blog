package wqh.blog.mvp.presenter.remote.upload;


import okhttp3.ResponseBody;
import retrofit2.Call;
import wqh.blog.mvp.model.bean.Comment;
import wqh.blog.mvp.model.service.CommentAPI;
import wqh.blog.mvp.model.service.RemoteManager;
import wqh.blog.mvp.presenter.remote.DefaultCallback;
import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/5/2  18:52.
 */
public class CommentUpLoadPresenter extends UpLoadPresenter<Comment> {
    CommentAPI mCommentAPI;

    @Override
    protected void initAPI() {
        mCommentAPI = RemoteManager.create(CommentAPI.class);
    }

    @Override
    public void publish(Comment aData, LoadView<Comment> mLoadView) {
        Call<ResponseBody> call = mCommentAPI.postComment(aData.belongTo, aData.content, aData.createdBy);
        doPublish(call, mLoadView);
    }

    private void doPublish(Call<ResponseBody> call, LoadView<Comment> mLoadView) {
        call.enqueue(new CommentCallback(mLoadView));
    }

    private class CommentCallback extends DefaultCallback<Comment> {
        public CommentCallback(LoadView<Comment> mLoadView) {
            super(mLoadView);
        }

        @Override
        protected void onParseResult(String result) {
            // Load null for LoadView
            mLoadView.onSuccess(null);
        }
    }
}
