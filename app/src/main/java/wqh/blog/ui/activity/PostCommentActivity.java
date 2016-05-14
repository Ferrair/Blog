package wqh.blog.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import wqh.blog.R;
import wqh.blog.mvp.model.bean.Comment;
import wqh.blog.mvp.presenter.remote.comment.CommentUpLoadPresenterImpl;
import wqh.blog.mvp.presenter.remote.base.UpLoadPresenter;
import wqh.blog.ui.base.ToolbarActivity;
import wqh.blog.util.IntentUtil;
import wqh.blog.util.ToastUtil;
import wqh.blog.mvp.view.LoadView;


public class PostCommentActivity extends ToolbarActivity {
    private static final String TAG = "PostCommentActivity";

    @Bind(R.id.comment_content)
    EditText mCommentContent;

    int blogId;
    UpLoadPresenter<Comment> mCommentUpLoadPresenter = new CommentUpLoadPresenterImpl();

    @Override
    protected int layoutId() {
        return R.layout.activity_post_comment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blogId = getIntent().getIntExtra("id", 0);
        initView();
    }

    private void initView() {

    }

    @OnClick(R.id.do_post)
    public void postComment() {
        String comment = mCommentContent.getText().toString();
        if (!isValid(comment))
            return;

        Comment aComment = new Comment();
        aComment.belongTo = blogId;
        aComment.content = comment;
        aComment.createdBy = 1;

        mCommentUpLoadPresenter.publish(aComment, new DefaultCommentUpLoadView());
    }

    private boolean isValid(String text) {
        if (TextUtils.isEmpty(text)) {
            ToastUtil.showToast("评论为空啊");
            return false;
        }
        return true;
    }

    private class DefaultCommentUpLoadView implements LoadView<Comment> {
        @Override
        public void onSuccess(List<Comment> data) {
            IntentUtil.goToOtherActivity(PostCommentActivity.this, AllCommentsActivity.class, "id", blogId);
            finish();
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }
}
