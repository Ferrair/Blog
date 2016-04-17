package wqh.blog.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.orhanobut.logger.Logger;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import wqh.blog.R;
import wqh.blog.adapter.BlogAdapter;
import wqh.blog.bean.Blog;
import wqh.blog.presenter.LoadDataPresenter;
import wqh.blog.presenter.BlogLoadPresenter;
import wqh.blog.ui.base.BaseFragment;

/**
 * Created by WQH on 2016/4/11  19:14.
 * <p>
 * <p>
 * 所有业务逻辑放在 LoadDataPresenter里面，Activity只负责UI的状态
 * 并提供LoadDataView给LoadDataPresenter，给其回掉使用
 */
public class BlogListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    BlogAdapter mAdapter;
    LoadDataPresenter<Blog> mLoadDataPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mLoadDataPresenter = new BlogLoadPresenter();

    }

    private List<Blog> getData() {
        List<Blog> list = new ArrayList<>();

        for (int i = 0; i < 100; ++i) {
            Blog blog = new Blog();
            blog.title = "Title " + i;
            blog.tag = i + " ";
            blog.abstractStr = "abstractStr " + i;
            blog.times = i;
            blog.type = "type " + i;
            blog.createdAt = new Date(System.currentTimeMillis());
            list.add(blog);
        }
        return list;
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_bloglist;
    }

    @Override
    public String toString() {
        return "博客";
    }

    @Override
    public void onRefresh() {

    }

    //onSuccess is called when mLoadDataPresenter.loadAll() success loadAll data from net
    public void onSuccess(List<Blog> data) {
        mAdapter = new BlogAdapter(getActivity(), getData());
        mRecyclerView.setAdapter(mAdapter);
    }

    public void onFail(int errorCode, String errorMsg) {
        Logger.i("BlogListFragment => " + errorCode + "  " + errorMsg);
    }
}
