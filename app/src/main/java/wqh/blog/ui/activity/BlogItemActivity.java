package wqh.blog.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import wqh.blog.R;
import wqh.blog.model.bean.Blog;
import wqh.blog.model.bean.Comment;
import wqh.blog.presenter.BlogLoadPresenter;
import wqh.blog.presenter.CommentLoadPresenter;
import wqh.blog.presenter.LoadDataPresenter;
import wqh.blog.ui.base.BaseActivity;
import wqh.blog.view.LoadDataView;

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
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    /*
     * A Load-Data Presenter,which means load data from server is it's function.
     * On the other hand,load-data can't be found in this class
     *
     * And this below two class is a Presenter for Blog,and a Presenter for Comment which belongs to a Blog.
     * that is why the two Presenter can be existed in one Activity
     */
    LoadDataPresenter<Blog> mBlogLoadDataPresenter = new BlogLoadPresenter();
    LoadDataPresenter<Comment> mCommentLoadDataPresenter = new CommentLoadPresenter();
    /*
     * A Load-Data View,which means show loaded-data is it's function.
     * What's more,the loaded-data is from LoadDataPresenter.
     *
     * The default means that the view will exists forever unless a new Load-Data View is going to be added.
     * So,there are two LoadDataView,one for Blog and another for Comment which belongs to a Blog
     */
    DefaultBlogLoadDataView mDefaultBlogLoadDataView = new DefaultBlogLoadDataView();
    DefaultCommentLoadDataView mDefaultCommentLoadDataView = new DefaultCommentLoadDataView();

    @Override
    protected int layoutId() {
        return R.layout.activity_blogitem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        int blogId = getIntent().getIntExtra("id", 0);
        mBlogLoadDataPresenter.loadById(blogId, mDefaultBlogLoadDataView);
        mCommentLoadDataPresenter.loadById(blogId, mDefaultCommentLoadDataView);
    }

    private void initView() {
    }

    private class DefaultBlogLoadDataView implements LoadDataView<Blog> {

        @Override
        public void onSuccess(List<Blog> data) {
            Blog itemData = data.get(0);
            Log.i(TAG, itemData.toString());
            mCollapsingToolbarLayout.setTitle(itemData.title);
            mTagTextView.setText(itemData.type);
            mTimesTextVIew.setText(String.valueOf(itemData.times));
            mCreatedAtTextView.setText(itemData.createdAt.toString());
            //mContentTextView.setText(itemData.content);
            mDescriptionTextView.setText(itemData.abstractStr);
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }

    private class DefaultCommentLoadDataView implements LoadDataView<Comment> {

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
