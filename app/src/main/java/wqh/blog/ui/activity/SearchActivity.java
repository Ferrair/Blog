package wqh.blog.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.SearchView;

import butterknife.Bind;
import wqh.blog.R;
import wqh.blog.ui.base.StateActivity;
import wqh.blog.util.ToastUtil;

public class SearchActivity extends StateActivity {
    @Bind(R.id.searchView)
    SearchView mSearchView;

    @Override
    protected int layoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchView.onActionViewExpanded();
        mStateLayout.showContentView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void search() {
        mStateLayout.showLoadingView();
        mStateLayout.postDelayed(() -> ToastUtil.showToast("No Thing"), 1000);
    }

}
