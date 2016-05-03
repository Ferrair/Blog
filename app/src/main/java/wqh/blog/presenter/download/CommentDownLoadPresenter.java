package wqh.blog.presenter.download;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wqh.blog.model.bean.Comment;
import wqh.blog.model.bean.Holder;
import wqh.blog.model.remote.CommentAPI;
import wqh.blog.model.remote.RemoteManager;
import wqh.blog.view.LoadView;

/**
 * Created by WQH on 2016/5/1  20:49.
 */
public class CommentDownLoadPresenter extends DownLoadPresenter<Comment> {
    CommentAPI mCommentAPI;
    private static final String TAG = "CommentDownLoadPresenter";

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
        Call<Holder<Comment>> call = mCommentAPI.queryComment(blogId);
        doQuery(call, mLoadView);
    }


    private void doQuery(Call<Holder<Comment>> call, LoadView<Comment> mLoadView) {
        call.enqueue(new CommentCallback(mLoadView));
    }

    class CommentCallback implements Callback<Holder<Comment>> {

        LoadView<Comment> mLoadView;

        public CommentCallback(LoadView<Comment> mLoadView) {
            this.mLoadView = mLoadView;
        }

        @Override
        public void onResponse(Call<Holder<Comment>> call, Response<Holder<Comment>> response) {
            if (response.isSuccessful()) {
                Holder<Comment> holder = response.body();
                if (holder.Code == RemoteManager.OK) {
                    List<Comment> mList = holder.dataList(Comment[].class);
                    if (mList.size() != 0)
                        mLoadView.onSuccess(mList);
                    else
                        mLoadView.onFail(101, "Can not find Object");
                } else {
                    mLoadView.onFail(holder.Code, "At " + TAG + "#onResponse-> " + holder.Msg);
                }
            } else {
                mLoadView.onFail(RemoteManager.UNKNOWN, "At " + TAG + "#onResponse-> " + response.errorBody().toString());
            }
        }

        @Override
        public void onFailure(Call<Holder<Comment>> call, Throwable t) {
            mLoadView.onFail(RemoteManager.UNKNOWN, "At " + TAG + "#onFailure-> " + t.toString());
        }
    }
}
