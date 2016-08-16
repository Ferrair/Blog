package wqh.blog.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import wqh.blog.R;
import wqh.blog.mvp.model.service.RemoteManager;
import wqh.blog.mvp.presenter.remote.base.DownLoadPresenter;
import wqh.blog.ui.activity.BlogItemActivity;
import wqh.blog.ui.adapter.BlogAdapter;
import wqh.blog.mvp.model.bean.Blog;
import wqh.blog.mvp.presenter.remote.blog.BlogDownLoadPresenterImpl;
import wqh.blog.ui.adapter.animation.AnimationManager;
import wqh.blog.ui.adapter.event.LayoutState;
import wqh.blog.ui.base.ScrollFragment;
import wqh.blog.ui.customview.DividerItemDecoration;
import wqh.blog.util.CollectionUtil;
import wqh.blog.manager.IntentManager;
import wqh.blog.util.JsonUtil;
import wqh.blog.util.StatusUtil;
import wqh.blog.util.ToastUtil;
import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/4/11  19:14.
 */
public class BlogListFragment extends ScrollFragment {

    private static final String TAG = "BlogListFragment";
    BlogAdapter mAdapter;
    /**
     * A Load-Data Presenter,which means load data from server is it's function.
     * On the other hand,load-data can't be found in this class
     */
    DownLoadPresenter mDownLoadPresenter = new BlogDownLoadPresenterImpl();
    /**
     * A Load-Data View,which means show loaded-data is it's function.
     * What's more,the loaded-data is from DownLoadPresenter.
     */
    DefaultLoadView mDefaultLoadDataView = new DefaultLoadView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //init RecyclerView, Adapter,and set listener
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        mAdapter = new BlogAdapter(mContext);
        mAdapter.setOnItemClickListener(R.id.item_blog, (view, data) -> IntentManager.goToOtherActivity(getActivity(), BlogItemActivity.class, "id", data.id));
        mAdapter.openAnimation(AnimationManager.EnterInRight);
        mAdapter.setOnBottomListener(this);

        mDownLoadPresenter.loadAll(1, mDefaultLoadDataView);

    }

    @Override
    public void onRefreshDelayed() {
        mDownLoadPresenter.loadAll(1, mDefaultLoadDataView);
    }

    @Override
    public void onLoadMore(int toToLoadPage) {
        Log.i(TAG, String.valueOf(toToLoadPage));
        mDownLoadPresenter.loadAll(toToLoadPage, mDefaultLoadDataView);
    }

    /**
     * Show view by given data.
     */
    private void showContent(List<Blog> data) {
        mStateLayout.showContentView();
        mAdapter.addAll(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public int layoutId() {
        return R.layout.layout_list;
    }

    @Override
    public String toString() {
        return "Blog";
    }

    /**
     * A Load-Data View,which means show loaded-data is it's function.
     * What's more,the loaded-data is from DownLoadPresenter.
     * The default means that the view will exists forever unless a new Load-Data View is going to be added.
     */
    private class DefaultLoadView implements LoadView {

        @Override
        public void onSuccess(String resultJson) {
            showContent(CollectionUtil.asList(JsonUtil.fromJson(resultJson, Blog[].class)));
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            if (errorCode == RemoteManager.NO_MORE) {
                mAdapter.setLoadState(LayoutState.FINISHED);
            }
            //If no network will show local-data instead of error-view.
            if (!StatusUtil.isNetworkAvailable(getActivity())) {
                ToastUtil.showToast(R.string.no_network);
                mStateLayout.showErrorView();
            }
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }

}
