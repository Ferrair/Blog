package wqh.blog.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import wqh.blog.R;
import wqh.blog.app.Config;
import wqh.blog.mvp.model.bean.User;
import wqh.blog.mvp.model.service.UserManager;
import wqh.blog.ui.adapter.FragmentAdapter;
import wqh.blog.ui.base.BaseFragment;
import wqh.blog.ui.base.ScrollFragment;
import wqh.blog.ui.base.ToolbarActivity;
import wqh.blog.ui.fragment.BlogListFragment;
import wqh.blog.ui.customview.DrawerDelegate;
import wqh.blog.ui.fragment.WorkListFragment;
import wqh.blog.manager.IntentManager;

/**
 * Created by WQH on 2016/4/11  17:11.
 */
public class MainActivity extends ToolbarActivity implements DrawerDelegate.DrawerListener {

    private static final String TAG = "MainActivity";
    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;

    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    /**
     * A Delegate that holds Left-Drawer.
     */
    private DrawerDelegate mDrawerDelegate;
    /**
     * A List that holds Fragment.
     */
    private List<BaseFragment> mContentList = new ArrayList<>();

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
        initDrawer();
    }

    private void initDrawer() {
        mDrawerDelegate = new DrawerDelegate(this, mToolbar, this);
        mDrawerDelegate.init();
    }

    private void initContentView() {
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), getTabFragment()));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private List<BaseFragment> getTabFragment() {
        mContentList.add(new BlogListFragment());
        mContentList.add(new WorkListFragment());
        return mContentList;
    }

    @Override
    public boolean onDrawerMenuSelected(View view, int position, IDrawerItem drawerItem) {
        switch (position) {
            case 1:
                IntentManager.goToOtherActivity(this, DownLoadListActivity.class);
                break;
            case 2:
                IntentManager.goToOtherActivity(this, LoginActivity.class);
                break;
        }
        return false;
    }

    @NonNull
    @Override
    public List<IDrawerItem> onDrawerMenuCreate() {
        List<IDrawerItem> list = new ArrayList<>();
        list.add(new PrimaryDrawerItem().withName(R.string.download_list).withIcon(FontAwesome.Icon.faw_download));
        list.add(new PrimaryDrawerItem().withName("登陆").withIcon(FontAwesome.Icon.faw_download));
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                IntentManager.goToOtherActivity(this, SearchActivity.class);
                break;
        }
        return true;
    }

    /**
     * Dispatch click action on <code>Toolbar</code> on each ScrollFragment.
     */
    @Override
    protected void onToolbarClick() {
        ((ScrollFragment) mContentList.get(mViewPager.getCurrentItem())).onToolbarClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (UserManager.instance().isLogged()) {
            User currentUser = UserManager.instance().currentUser();
            mDrawerDelegate.setName(currentUser.username);
            if (currentUser.avatarUri != null && !TextUtils.isEmpty(currentUser.avatarUri))
                mDrawerDelegate.setAvatar(Config.REMOTE_DIR + currentUser.avatarUri);
        }
    }

    @Override
    protected boolean canBack() {
        return false;
    }

    @Override
    protected void onDestroy() {
        mDrawerDelegate.destroy();
        mDrawerDelegate = null;
        super.onDestroy();
    }
}
