package wqh.blog.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import butterknife.Bind;
import wqh.blog.R;
import wqh.blog.ui.customview.swipeback.SwipeBackLayout;

/**
 * Created by WQH on 2016/4/11  17:01.
 * ToolbarActivity for all activity that have a Toolbar, provides an Toolbar that liked some hooked-method.
 */
public abstract class ToolbarActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;

    protected SwipeBackLayout mSwipeBackLayout;

    /*
     * Whether this Activity can back by click left-top back-button.
     */
    protected boolean canBack() {
        return true;
    }

    // A hook method that do something when click the Toolbar.
    protected void onToolbarClick() {
        //Do nothing here.
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) throws RuntimeException {
        super.onCreate(savedInstanceState);
        //Toolbar
        if (mToolbar == null) {
            throw new RuntimeException("Toolbar must be set at id(toolbar)");
        }
        setSupportActionBar(mToolbar);
        mToolbar.setOnClickListener(view -> onToolbarClick());

        //SwipeBack
        setSwipeBackEnable(canBack());
        if (canBack()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Set SwipeBack Action.
            mSwipeBackLayout = getSwipeBackLayout();
            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        }
    }


    //Set the home button listener which can finish this activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
