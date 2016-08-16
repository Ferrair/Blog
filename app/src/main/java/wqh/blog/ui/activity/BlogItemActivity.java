package wqh.blog.ui.activity;

import android.content.Intent;
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
import wqh.blog.mvp.model.service.UserManager;
import wqh.blog.mvp.presenter.local.LocalPresenter;
import wqh.blog.mvp.presenter.remote.blog.BlogDownLoadPresenterImpl;
import wqh.blog.mvp.presenter.remote.base.DownLoadPresenter;
import wqh.blog.mvp.presenter.remote.blog.BlogUpLoadPresenter;
import wqh.blog.mvp.presenter.remote.blog.BlogUpLoadPresenterImpl;
import wqh.blog.ui.base.StateActivity;
import wqh.blog.ui.customview.Dialog;
import wqh.blog.util.CollectionUtil;
import wqh.blog.manager.IntentManager;
import wqh.blog.util.JsonUtil;
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
    DownLoadPresenter mBlogDownLoadPresenter = new BlogDownLoadPresenterImpl();
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
        Blog localBlog = mLocalPresenter.db().queryById(blogId, Blog.class);
        if (localBlog != null)
            showContent(localBlog);
        mBlogDownLoadPresenter.loadById(blogId, mDefaultBlogLoadDataView);
        //Add view-times.
        mBlogUpLoadPresenter.addTimes(blogId);
    }


    /*
     * Footer Layout.
     */
    @OnClick(R.id.post_comment)
    public void postComment() {
        if (UserManager.instance().isLogged()) {
            IntentManager.goToOtherActivity(this, PostCommentActivity.class, "id", blogId);
        } else {
            Dialog.create(this, getString(R.string.need_login)).setPositiveListener(
                    getString(R.string.go_login),
                    v -> IntentManager.goToOtherActivity(BlogItemActivity.this, LoginActivity.class)).show();
        }
    }

    @OnClick(R.id.all_comments)
    public void allComments() {
        IntentManager.goToOtherActivity(this, AllCommentsActivity.class, "id", blogId);
    }

    @OnClick(R.id.share)
    public void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); //MIME type
        intent.putExtra(Intent.EXTRA_SUBJECT, getTitle());
        intent.putExtra(Intent.EXTRA_TEXT, "Share");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, getTitle()));
        }
    }

    @Override
    protected void onToolbarClick() {
        mScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    /**
     * Show view by given data.
     */
    private void showContent(Blog itemData) {
        mStateLayout.showContentView();
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
    private class DefaultBlogDownLoadView implements LoadView {

        @Override
        public void onSuccess(String resultJson) {
            List<Blog> data = CollectionUtil.asList(JsonUtil.fromJson(resultJson, Blog[].class));
            itemData = data.get(0);
            mLocalPresenter.db().save(itemData);
            showContent(itemData);
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            if (StatusUtil.isNetworkAvailable(BlogItemActivity.this)) {
                mStateLayout.showErrorView();
            } else {
                ToastUtil.showToast(R.string.no_network);
            }
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }


}
