package wqh.blog.presenter;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wqh.blog.model.bean.Blog;
import wqh.blog.model.bean.Comment;
import wqh.blog.model.bean.Holder;
import wqh.blog.model.remote.CommentAPI;
import wqh.blog.model.remote.RemoteManager;
import wqh.blog.view.LoadDataView;

/**
 * Created by WQH on 2016/5/1  20:49.
 */
public class CommentLoadPresenter extends LoadDataPresenter<Comment> {
    CommentAPI mCommentAPI;
    private static final String TAG = "CommentLoadPresenter";

    @Override
    public void initAPI() {
        mCommentAPI = RemoteManager.create(CommentAPI.class);
    }

    @Override
    public void loadAll(LoadDataView<Comment> mLoadDataView) {
        //do nothing here
    }

    @Override
    public void loadByCondition(String condition, Type type, LoadDataView<Comment> mLoadDataView) {
        //do nothing here
    }

    @Override
    public void loadById(int blogId, LoadDataView<Comment> mLoadDataView) {
        Call<Holder<Comment>> call = mCommentAPI.queryComment(blogId);
        doQuery(call, mLoadDataView);
    }


    private void doQuery(Call<Holder<Comment>> call, LoadDataView<Comment> mLoadDataView) {
        call.enqueue(new CommentCallback(mLoadDataView));
    }

    class CommentCallback implements Callback<Holder<Comment>> {

        LoadDataView<Comment> mLoadDataView;

        public CommentCallback(LoadDataView<Comment> mLoadDataView) {
            this.mLoadDataView = mLoadDataView;
        }

        @Override
        public void onResponse(Call<Holder<Comment>> call, Response<Holder<Comment>> response) {
            if (response.isSuccessful()) {
                Holder<Comment> holder = response.body();
                if (holder.Code == RemoteManager.OK) {
                    Log.i(TAG, holder.Result.toString());
                    List<Comment> mList = holder.dataList(Comment[].class);
                    if (mList.size() != 0)
                        mLoadDataView.onSuccess(mList);
                    else
                        mLoadDataView.onFail(101, "Can not find Object");
                } else {
                    mLoadDataView.onFail(holder.Code, "At " + TAG + "#onResponse-> " + holder.Msg);
                }
            } else {
                mLoadDataView.onFail(RemoteManager.UNKNOWN, "At " + TAG + "#onResponse-> " + response.errorBody().toString());
            }
        }

        @Override
        public void onFailure(Call<Holder<Comment>> call, Throwable t) {
            mLoadDataView.onFail(RemoteManager.UNKNOWN, "At " + TAG + "#onFailure-> " + t.toString());
        }
    }
}
