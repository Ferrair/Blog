package wqh.blog.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.Bind;
import wqh.blog.R;
import wqh.blog.ui.customview.StateLayout;

/**
 * Created by WQH on 2016/5/8  22:55.
 *
 * A Activity with four-states by <code>StateLayout</code>.
 * And LoadingView-State is it's init-state.So MUST call other's state after.
 */
public abstract class StateActivity extends ToolbarActivity {

    @Bind(R.id.stateLayout)
    protected StateLayout mStateLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStateLayout.showLoadingView();
    }
}
