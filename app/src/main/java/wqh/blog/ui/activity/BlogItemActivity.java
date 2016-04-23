package wqh.blog.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import wqh.blog.R;
import wqh.blog.model.bean.Blog;
import wqh.blog.presenter.BlogLoadPresenter;
import wqh.blog.presenter.LoadDataPresenter;
import wqh.blog.ui.base.BaseActivity;
import wqh.blog.view.LoadDataView;

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

    LoadDataPresenter<Blog> mLoadDataPresenter = new BlogLoadPresenter();
    DefaultLoadDataView mDefaultLoadDataView = new DefaultLoadDataView();

    @Override
    protected int layoutId() {
        return R.layout.activity_blogitem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int blogId = getIntent().getIntExtra("id", 0);
        Log.i(TAG, "ID - > " + blogId);
        mLoadDataPresenter.loadById(blogId, mDefaultLoadDataView);
    }

    private class DefaultLoadDataView implements LoadDataView<Blog> {

        @Override
        public void onSuccess(List<Blog> data) {
            Blog itemData = data.get(0);
            mTagTextView.setText(itemData.type);
            mTimesTextVIew.setText(itemData.times);
            mCreatedAtTextView.setText(itemData.createdAt.toString());
            mContentTextView.setText(itemData.content);
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }
}
