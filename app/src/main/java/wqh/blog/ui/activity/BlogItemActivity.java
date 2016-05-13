package wqh.blog.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import us.feras.mdv.MarkdownView;
import wqh.blog.R;
import wqh.blog.mvp.model.bean.Blog;
import wqh.blog.mvp.presenter.local.LocalPresenter;
import wqh.blog.mvp.presenter.remote.blog.BlogDownLoadPresenterImpl;
import wqh.blog.mvp.presenter.remote.base.DownLoadPresenter;
import wqh.blog.mvp.presenter.remote.blog.BlogUpLoadPresenter;
import wqh.blog.mvp.presenter.remote.blog.BlogUpLoadPresenterImpl;
import wqh.blog.ui.base.StateActivity;
import wqh.blog.util.IntentUtil;
import wqh.blog.util.ShareUtil;
import wqh.blog.util.StatusUtil;
import wqh.blog.util.TimeUtil;
import wqh.blog.util.ToastUtil;
import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/4/24  20:45.
 */
public class BlogItemActivity extends StateActivity {

    private static final String TAG = "BlogItemActivity";
    @Bind(R.id.scrollView)
    NestedScrollView mScrollView;
    @Bind(R.id.tag)
    TextView mTagTextView;
    @Bind(R.id.times)
    TextView mTimesTextVIew;
    @Bind(R.id.createdAt)
    TextView mCreatedAtTextView;
    @Bind(R.id.content)
    MarkdownView mContentMarkDown;  //Todo : Can show my own web.
    @Bind(R.id.abstractStr)
    TextView mDescriptionTextView;
    @Bind(R.id.title)
    TextView mTitleTextView;

    Blog itemData;
    /*
     * A Down-Load-Data Presenter,which means download data from server is it's function.
     * On the other hand,download-data action can't be found in this class
     *
     * And this below two class is a Presenter for Blog,and a Presenter for Comment which belongs to a Blog.
     * that is why the two Presenter will be existed in one Activity
     */
    DownLoadPresenter<Blog> mBlogDownLoadPresenter = new BlogDownLoadPresenterImpl();
    /*
     * A Up-Load-Data Presenter that add view-times for this blog.
     * But it can tolerate error in this method.HaHa......
     */
    BlogUpLoadPresenter mBlogUpLoadPresenter = new BlogUpLoadPresenterImpl();
    /*
     * A Down-Load-Data View,which means show downloaded-data is it's function.
     * And more,the downloaded-data is from DownLoadPresenter.
     *
     * The default means that the view will exists forever unless a new Down-Load-Data View is going to be added.
     * So,there are two LoadView,one for Blog and another for Comment which belongs to a Blog
     */
    DefaultBlogDownLoadView mDefaultBlogLoadDataView = new DefaultBlogDownLoadView();
    /**
     * A Local-Data Presenter,that store data into local-database.
     */
    LocalPresenter mLocalPresenter = new LocalPresenter();

    int blogId;

    @Override
    protected int layoutId() {
        return R.layout.activity_blogitem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get data from Intent
        blogId = getIntent().getIntExtra("id", 0);
        //Load data.
        //Load data
        if (mLocalPresenter.db().queryCount(Blog.class) != 0)
            showContent(mLocalPresenter.db().queryById(blogId, Blog.class));
        mBlogDownLoadPresenter.loadById(blogId, mDefaultBlogLoadDataView);
        //Add view-times.
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

    @OnClick(R.id.share)
    public void share() {
        if (itemData != null)
            ShareUtil.share(getApplicationContext(), itemData.title);
    }

    @Override
    protected void onToolbarClick() {
        mScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    /**
     * Show view by given data.
     */
    private void showContent(Blog itemData) {
        Log.i(TAG, itemData.toString());
        mTitleTextView.setText(itemData.title);
        mTagTextView.setText(itemData.type);
        mTimesTextVIew.setText(String.valueOf(itemData.times));
        mCreatedAtTextView.setText(TimeUtil.date2time(itemData.createdAt.toString()));
        mContentMarkDown.loadMarkdown(itemData.content);
        mDescriptionTextView.setText(itemData.abstractStr);
    }

    /*
     * The below class will show View after fetch data from server.
     * So see initView() method in this class.
     */
    private class DefaultBlogDownLoadView implements LoadView<Blog> {

        @Override
        public void onSuccess(List<Blog> data) {
            mLocalPresenter.db().update(data);
            mStateLayout.showContentView();
            itemData = data.get(0);
            showContent(itemData);
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            if (StatusUtil.isNetworkAvailable(BlogItemActivity.this)) {
                mStateLayout.showErrorView();
            } else {
                ToastUtil.showToast("没有网络咯");
            }
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }


}
