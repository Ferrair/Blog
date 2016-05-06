package wqh.blog.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import wqh.blog.R;
import wqh.blog.model.bean.Blog;
import wqh.blog.model.bean.Comment;
import wqh.blog.presenter.download.BlogDownLoadPresenter;
import wqh.blog.presenter.download.CommentDownLoadPresenter;
import wqh.blog.presenter.download.DownLoadPresenter;
import wqh.blog.presenter.upload.BlogUpLoadPresenter;
import wqh.blog.presenter.upload.CommentUpLoadPresenter;
import wqh.blog.ui.base.BaseActivity;
import wqh.blog.util.IntentUtil;
import wqh.blog.util.TimeUtil;
import wqh.blog.view.LoadView;

/**
 * Created by WQH on 2016/4/24  20:45.
 */
public class BlogItemActivity extends BaseActivity {

    private static final String TAG = "BlogItemActivity";
    @Bind(R.id.tag)
    TextView mTagTextView;
    @Bind(R.id.times)
    TextView mTimesTextVIew;
    @Bind(R.id.createdAt)
    TextView mCreatedAtTextView;
    @Bind(R.id.content)
    TextView mContentTextView;
    @Bind(R.id.abstractStr)
    TextView mDescriptionTextView;
    @Bind(R.id.title)
    TextView mTitleTextView;
    /*
     * A Down-Load-Data Presenter,which means download data from server is it's function.
     * On the other hand,download-data action can't be found in this class
     *
     * And this below two class is a Presenter for Blog,and a Presenter for Comment which belongs to a Blog.
     * that is why the two Presenter will be existed in one Activity
     */
    DownLoadPresenter<Blog> mBlogDownLoadPresenter = new BlogDownLoadPresenter();
    DownLoadPresenter<Comment> mCommentDownLoadPresenter = new CommentDownLoadPresenter();
    /*
     * A Up-Load-Data Presenter that add view-times for this blog.
     * But it can tolerate error in this method.HaHa......
     */
    BlogUpLoadPresenter mBlogUpLoadPresenter = new BlogUpLoadPresenter();
    /*
     * A Down-Load-Data View,which means show downloaded-data is it's function.
     * And more,the downloaded-data is from DownLoadPresenter.
     *
     * The default means that the view will exists forever unless a new Down-Load-Data View is going to be added.
     * So,there are two LoadView,one for Blog and another for Comment which belongs to a Blog
     */
    DefaultBlogDownLoadView mDefaultBlogLoadDataView = new DefaultBlogDownLoadView();
    DefaultCommentDownLoadView mDefaultCommentLoadDataView = new DefaultCommentDownLoadView();

    int blogId;

    @Override
    protected int layoutId() {
        return R.layout.activity_blogitem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get data from Intent
        blogId = getIntent().getIntExtra("id", 0);

        mBlogDownLoadPresenter.loadById(blogId, mDefaultBlogLoadDataView);
        mCommentDownLoadPresenter.loadById(blogId, mDefaultCommentLoadDataView);
        mBlogUpLoadPresenter.addTimes(blogId);
    }

    /*
     * Footer Layout.
     * Using <code>@OnClick</code> to go to another Activity.
     */
    @OnClick(R.id.post_comment)
    public void postComment() {
        IntentUtil.goToOtherActivity(this, PostCommentActivity.class, "id", blogId);
    }

    @OnClick(R.id.all_comments)
    public void allComments() {
        IntentUtil.goToOtherActivity(this, AllCommentsActivity.class, "id", blogId);
    }

    /*
     * The below class will show View after fetch data from server.
     * So see initView() method in this class.
     */
    private class DefaultBlogDownLoadView implements LoadView<Blog> {

        @Override
        public void onSuccess(List<Blog> data) {
            Blog itemData = data.get(0);
            Log.i(TAG, itemData.toString());
            mTitleTextView.setText(itemData.title);
            mTagTextView.setText(itemData.type);
            mTimesTextVIew.setText(String.valueOf(itemData.times));
            mCreatedAtTextView.setText(TimeUtil.date2time(itemData.createdAt.toString()));
            mContentTextView.setText(itemData.content);
            mDescriptionTextView.setText(itemData.abstractStr);
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }

    private class DefaultCommentDownLoadView implements LoadView<Comment> {

        @Override
        public void onSuccess(List<Comment> data) {
            for (Comment itemData : data)
                Log.i(TAG, itemData.toString());
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }
}
