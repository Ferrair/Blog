package wqh.blog.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.List;

import butterknife.Bind;
import wqh.blog.R;
import wqh.blog.mvp.model.bean.Comment;
import wqh.blog.mvp.model.service.UserManager;
import wqh.blog.mvp.presenter.remote.comment.CommentUpLoadPresenter;
import wqh.blog.mvp.presenter.remote.comment.CommentUpLoadPresenterImpl;
import wqh.blog.ui.base.ToolbarActivity;
import wqh.blog.util.CollectionUtil;
import wqh.blog.manager.IntentManager;
import wqh.blog.util.JsonUtil;
import wqh.blog.util.ToastUtil;
import wqh.blog.mvp.view.LoadView;


public class PostCommentActivity extends ToolbarActivity {
    private static final String TAG = "PostCommentActivity";

    @Bind(R.id.comment_content)
    EditText mCommentContent;

    int blogId;
    String replyToUserName;
    int replyTo;
    CommentUpLoadPresenter mCommentUpLoadPresenter = new CommentUpLoadPresenterImpl();

    @Override
    protected int layoutId() {
        return R.layout.activity_post_comment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blogId = getIntent().getIntExtra("belongTo", 0);
        replyToUserName = getIntent().getStringExtra("replyToUserName");
        replyTo = getIntent().getIntExtra("replyTo", 0);
        initView();
    }

    private void initView() {
        if (replyTo != 0) {
            mCommentContent.setHint(getString(R.string.reply) + replyToUserName + ":");
        }
    }


    private void postComment() {
        String comment = mCommentContent.getText().toString();
        if (!isValid(comment))
            return;

        Comment aComment = new Comment();
        aComment.belongTo = blogId;
        aComment.content = comment;
        aComment.createdBy = UserManager.instance().currentUser().id;
        if (replyTo != 0) {
            aComment.replyTo = replyTo; // reply to which comment.
            mCommentUpLoadPresenter.reply(aComment, new DefaultCommentUpLoadView());
        } else {
            mCommentUpLoadPresenter.publish(aComment, new DefaultCommentUpLoadView());
        }

    }

    private boolean isValid(String text) {
        if (TextUtils.isEmpty(text)) {
            ToastUtil.showToast(R.string.comment_empty);
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_comment, menu);
        return true;
    }

    /**
     * When toolbar set menu.If too;bar can response the home button,MUST set it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post_comment:
                postComment();
                item.setCheckable(false);
                break;
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return true;
    }

    private class DefaultCommentUpLoadView implements LoadView {
        @Override
        public void onSuccess(String resultJson) {
            List<Comment> data = CollectionUtil.asList(JsonUtil.fromJson(resultJson, Comment[].class));
            IntentManager.goToOtherActivity(PostCommentActivity.this, AllCommentsActivity.class, "id", blogId);
            finish();
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            ToastUtil.showToast("服务器繁忙->" + errorCode);
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }
}
