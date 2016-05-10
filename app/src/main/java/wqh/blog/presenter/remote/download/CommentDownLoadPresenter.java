package wqh.blog.presenter.remote.download;


import okhttp3.ResponseBody;
import retrofit2.Call;
import wqh.blog.model.bean.Comment;
import wqh.blog.model.remote.CommentAPI;
import wqh.blog.model.remote.RemoteManager;
import wqh.blog.presenter.remote.DefaultCallback;
import wqh.blog.util.CollectionUtil;
import wqh.blog.util.Json;
import wqh.blog.view.LoadView;

/**
 * Created by WQH on 2016/5/1  20:49.
 */
public class CommentDownLoadPresenter extends DownLoadPresenter<Comment> {
    CommentAPI mCommentAPI;

    @Override
    protected void initAPI() {
        mCommentAPI = RemoteManager.create(CommentAPI.class);
    }

    @Override
    public void loadAll(LoadView<Comment> mLoadView) {
        //do nothing here
    }

    @Override
    public void loadByCondition(String condition, Type type, LoadView<Comment> mLoadView) {
        //do nothing here
    }

    @Override
    public void loadById(int blogId, LoadView<Comment> mLoadView) {
        Call<ResponseBody> call = mCommentAPI.queryComment(blogId);
        doQuery(call, mLoadView);
    }

    private void doQuery(Call<ResponseBody> call, LoadView<Comment> mLoadView) {
        call.enqueue(new CommentCallback(mLoadView));
    }

    class CommentCallback extends DefaultCallback<Comment> {

        public CommentCallback(LoadView<Comment> mLoadView) {
            super(mLoadView);
        }

        @Override
        protected void onParseResult(String result) {
            mLoadView.onSuccess(CollectionUtil.asList(Json.fromJson(result, Comment[].class)));
        }
    }
}
