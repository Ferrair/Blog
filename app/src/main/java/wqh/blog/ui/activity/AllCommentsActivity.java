package wqh.blog.ui.activity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import butterknife.OnClick;
import wqh.blog.R;
import wqh.blog.mvp.model.bean.Blog;
import wqh.blog.mvp.model.bean.Comment;
import wqh.blog.mvp.model.service.RemoteManager;
import wqh.blog.mvp.model.service.UserManager;
import wqh.blog.mvp.presenter.remote.comment.CommentDownLoadPresenter;
import wqh.blog.mvp.presenter.remote.comment.CommentDownLoadPresenterImpl;
import wqh.blog.ui.adapter.CommentsAdapter;
import wqh.blog.ui.base.ScrollActivity;
import wqh.blog.ui.customview.Dialog;
import wqh.blog.util.IntentUtil;
import wqh.blog.mvp.view.LoadView;

public class AllCommentsActivity extends ScrollActivity {
    private static final String TAG = "AllCommentsActivity";

    CommentsAdapter mAdapter;
    /**
     * A Load-Data Presenter,which means load data from server is it's function.
     * On the other hand,load-data can't be found in this class
     */
    CommentDownLoadPresenter mDownLoadCommentPresenter = new CommentDownLoadPresenterImpl();
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
        mDownLoadCommentPresenter.loadById(belongTo, 1, mDefaultLoadDataView);
    }

    @Override
    public void onRefreshDelayed() {
        mDownLoadCommentPresenter.loadById(belongTo, 1, mDefaultLoadDataView);
    }

    @Override
    public void onLoadMoreDelayed(int toToLoadPage) {
        mDownLoadCommentPresenter.loadById(belongTo, toToLoadPage, mDefaultLoadDataView);
    }

    /*
     * Footer Layout.
     * Using <code>@OnClick</code> to go to another Activity for post a Comment.
     */
    @OnClick(R.id.post_comment)
    public void postComment() {
        if (UserManager.instance().isLogged()) {
            IntentUtil.goToOtherActivity(this, PostCommentActivity.class, "id", belongTo);
        } else {
            Dialog.create(this, "只有登陆才可以发表评论咯，是否登陆???").setPositiveListener(
                    "前去登陆",
                    v -> IntentUtil.goToOtherActivity(AllCommentsActivity.this, LoginActivity.class)).show();
        }
    }

    /**
     * Show view by given data.
     */
    private void showContent(List<Comment> data) {
        mStateLayout.showContentView();
        mAdapter.addAll(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * A Load-Data View,which means show loaded-data is it's function.
     * What's more,the loaded-data is from DownLoadPresenter.
     * The default means that the view will exists forever unless a new Load-Data View is going to be added.
     */
    private class DefaultLoadView implements LoadView<Comment> {

        @Override
        public void onSuccess(List<Comment> data) {
            showContent(data);
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            if (errorCode == RemoteManager.PARSE) {
                mStateLayout.showErrorView();
            } else if (errorCode == RemoteManager.NO_OBJECT) {
                mStateLayout.showEmptyView();
            }

            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }
}
