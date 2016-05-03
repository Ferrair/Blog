package wqh.blog.ui.activity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import butterknife.OnClick;
import wqh.blog.R;
import wqh.blog.model.bean.Comment;
import wqh.blog.presenter.download.CommentDownLoadPresenter;
import wqh.blog.presenter.download.DownLoadPresenter;
import wqh.blog.ui.adapter.CommentsAdapter;
import wqh.blog.ui.base.ScrollActivity;
import wqh.blog.util.IntentUtil;
import wqh.blog.view.LoadView;

public class AllCommentsActivity extends ScrollActivity {
    private static final String TAG = "AllCommentsActivity";

    CommentsAdapter mAdapter;
    /**
     * A Load-Data Presenter,which means load data from server is it's function.
     * On the other hand,load-data can't be found in this class
     */
    DownLoadPresenter<Comment> mDownLoadCommentPresenter = new CommentDownLoadPresenter();
    /**
     * A Load-Data View,which means show loaded-data is it's function.
     * What's more,the loaded-data is from DownLoadPresenter.
     */
    LoadView<Comment> mDefaultLoadDataView = new DefaultLoadView();
    int belongTo;

    @Override
    protected int layoutId() {
        return R.layout.activity_all_comments;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        belongTo = getIntent().getIntExtra("id", 0);
        mAdapter = new CommentsAdapter(this);
        mDownLoadCommentPresenter.loadById(belongTo, mDefaultLoadDataView);
    }

    @Override
    protected void onRefreshDelayed() {
        mDownLoadCommentPresenter.loadAll(mDefaultLoadDataView);
    }

    /*
     * Footer Layout.
     * Using <code>@OnClick</code> to go to another Activity for post a Comment.
     */
    @OnClick(R.id.post_comment)
    public void postComment() {
        IntentUtil.goToOtherActivity(this, PostCommentActivity.class, "id", belongTo);
    }

    /**
     * A Load-Data View,which means show loaded-data is it's function.
     * What's more,the loaded-data is from DownLoadPresenter.
     * The default means that the view will exists forever unless a new Load-Data View is going to be added.
     */
    private class DefaultLoadView implements LoadView<Comment> {

        @Override
        public void onSuccess(List<Comment> data) {
            mAdapter.addAll(data);
            mRecyclerView.setAdapter(mAdapter);
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }
}
