package wqh.blog.mvp.presenter.remote.comment;


import okhttp3.ResponseBody;
import retrofit2.Call;
import wqh.blog.mvp.model.service.CommentAPI;
import wqh.blog.mvp.model.service.RemoteManager;
import wqh.blog.mvp.presenter.remote.base.DefaultCallback;
import wqh.blog.mvp.presenter.remote.base.LoadPresenter;
import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/5/1  20:49.
 */
public class CommentDownLoadPresenterImpl extends LoadPresenter implements CommentDownLoadPresenter {
    private static final String TAG = "CommentDownLoadPresenterImpl";
    CommentAPI mCommentAPI;


    @Override
    protected void initAPI() {
        mCommentAPI = RemoteManager.create(CommentAPI.class);
    }

    public void loadById(int blogId, int pageNum, LoadView mLoadView) {
        Call<ResponseBody> call = mCommentAPI.queryComment(blogId, pageNum);
        doQuery(call, mLoadView);
    }

    @Override
    public void deleteById(int id, LoadView mLoadView) {
        Call<ResponseBody> call = mCommentAPI.deleteById(id);
        doQuery(call, mLoadView);
    }

    private void doQuery(Call<ResponseBody> call, LoadView mLoadView) {
        call.enqueue(new DefaultCallback(mLoadView));
    }
}
