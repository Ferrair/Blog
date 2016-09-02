package wqh.blog.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import wqh.blog.R;
import wqh.blog.mvp.model.bean.Comment;
import wqh.blog.mvp.model.service.RemoteManager;
import wqh.blog.mvp.model.service.UserManager;
import wqh.blog.mvp.presenter.remote.comment.CommentDownLoadPresenter;
import wqh.blog.mvp.presenter.remote.comment.CommentDownLoadPresenterImpl;
import wqh.blog.ui.adapter.CommentAdapter;
import wqh.blog.ui.adapter.base.AdapterPool;
import wqh.blog.ui.adapter.event.LayoutState;
import wqh.blog.ui.base.ScrollActivity;
import wqh.blog.ui.customview.Dialog;
import wqh.blog.ui.customview.DividerItemDecoration;
import wqh.blog.util.CollectionUtil;
import wqh.blog.manager.IntentManager;
import wqh.blog.mvp.view.LoadView;
import wqh.blog.util.JsonUtil;

public class AllCommentsActivity extends ScrollActivity {
    private static final String TAG = "AllCommentsActivity";

    @Bind(R.id.rootView)
    CoordinatorLayout mRootView;

    AdapterPool<CommentAdapter.CommentsHolder, Comment> mAdapter;
    /**
     * A Load-Data Presenter,which means load data from server is it's function.
     * On the other hand,load-data can't be found in this class
     */
    CommentDownLoadPresenter mDownLoadCommentPresenter = new CommentDownLoadPresenterImpl();
    /**
     * A Load-Data View,which means show loaded-data is it's function.
     * What's more,the loaded-data is from DownLoadPresenter.
     */
    LoadView mDefaultLoadDataView = new DefaultLoadView();
    int belongTo;

    @Override
    protected int layoutId() {
        return R.layout.activity_all_comments;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        belongTo = getIntent().getIntExtra("id", 0);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        mAdapter = new AdapterPool<>(this);
        mAdapter.register(Comment.class, new CommentAdapter(this));
        mAdapter.setOnBottomListener(this);
        mAdapter.setOnItemClickListener(R.id.item_comment, (view, data) -> createDialog(data));
        mDownLoadCommentPresenter.loadById(belongTo, 1, mDefaultLoadDataView);
    }

    private void createDialog(Comment data) {
        if (!UserManager.instance().isLogged()) {
            Dialog.create(this, getString(R.string.need_login)).setPositiveListener(getString(R.string.go_login), v -> IntentManager.goToOtherActivity(AllCommentsActivity.this, LoginActivity.class)).show();
            return;
        }
        if (data.createdBy.equals(UserManager.instance().currentUser().id)) {
            new AlertDialog
                    .Builder(AllCommentsActivity.this)
                    .setItems(new String[]{getString(R.string.copy_comment), getString(R.string.delete_comment)}, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                copyToClipboard(data);
                                break;
                            case 1:
                                deleteComment(data);
                                break;
                        }
                    })
                    .create()
                    .show();
        } else {
            new AlertDialog
                    .Builder(AllCommentsActivity.this)
                    .setItems(new String[]{getString(R.string.copy_comment), getString(R.string.reply_comment)}, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                copyToClipboard(data);
                                break;
                            case 1:
                                replyComment(data);
                                break;
                        }
                    })
                    .create()
                    .show();
        }

    }


    private void copyToClipboard(Comment data) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText("Comment", data.content));
        Snackbar.make(mRootView, getString(R.string.copy_to_clipboard), Snackbar.LENGTH_LONG).setAction(getString(R.string.undo), v -> {
            clipboardManager.setPrimaryClip(ClipData.newPlainText("Comment", ""));
        }).show();
    }

    private void replyComment(Comment data) {
        Intent intent = new Intent(this, PostCommentActivity.class);
        intent.putExtra("belongTo", belongTo); //评论的文章
        intent.putExtra("replyToUserName", data.creatorName);//回复的UserName
        intent.putExtra("replyTo", data.id); //回复的CommentId
        startActivity(intent);
    }

    private void deleteComment(Comment data) {
        mDownLoadCommentPresenter.deleteById(data.id, mDefaultLoadDataView);
    }

    @Override
    public void onRefreshDelayed() {
        mDownLoadCommentPresenter.loadById(belongTo, 1, mDefaultLoadDataView);
    }

    @Override
    public void onLoadMore(int toToLoadPage) {
        Log.i(TAG, String.valueOf(toToLoadPage));
        mDownLoadCommentPresenter.loadById(belongTo, toToLoadPage, mDefaultLoadDataView);
    }

    /*
     * Footer Layout.
     * Using <code>@OnClick</code> to go to another Activity for post a Comment.
     */
    @OnClick(R.id.post_comment)
    public void postComment() {
        if (UserManager.instance().isLogged()) {
            IntentManager.goToOtherActivity(this, PostCommentActivity.class, "belongTo", belongTo);
        } else {
            Dialog.create(this, getString(R.string.need_login)).setPositiveListener(getString(R.string.go_login), v -> IntentManager.goToOtherActivity(AllCommentsActivity.this, LoginActivity.class)).show();
        }
    }

    /**
     * Show view by given data.
     */
    private void showContent(List<Comment> data) {
        mStateLayout.showContentView();
        mAdapter.fill(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * A Load-Data View,which means show loaded-data is it's function.
     * What's more,the loaded-data is from DownLoadPresenter.
     * The default means that the view will exists forever unless a new Load-Data View is going to be added.
     */
    private class DefaultLoadView implements LoadView {


        @Override
        public void onSuccess(String resultJson) {
            if (resultJson.equals("[true]")) {

            } else {
                showContent(CollectionUtil.asList(JsonUtil.fromJson(resultJson, Comment[].class)));
            }
        }

        // Todo : handle the errorCode. Why can find data return 107???
        @Override
        public void onFail(int errorCode, String errorMsg) {
            if (errorCode == RemoteManager.PARSE) {
                mStateLayout.showErrorView();
            } else if (errorCode == RemoteManager.SYNTAX) {
                mStateLayout.showEmptyView();
            } else if (errorCode == RemoteManager.NO_MORE) {
                mAdapter.setLoadState(LayoutState.FINISHED);
            }

            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }
}
