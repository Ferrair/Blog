package wqh.blog.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.Bind;
import wqh.blog.R;
import wqh.blog.ui.customview.StateLayout;

/**
 * Created by WQH on 2016/5/8  22:45.
 *
 * A Fragment with four-states by <code>StateLayout</code>.
 * And LoadingView-State is it's init-state.So MUST call other's state after.
 */
public abstract class StateFragment extends BaseFragment {
    @Bind(R.id.stateLayout)
    protected StateLayout mStateLayout;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mStateLayout.showLoadingView();
    }
}
