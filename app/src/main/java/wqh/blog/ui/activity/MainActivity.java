package wqh.blog.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import wqh.blog.R;
import wqh.blog.ui.adapter.FragmentAdapter;
import wqh.blog.ui.base.BaseActivity;
import wqh.blog.ui.fragment.BlogListFragment;
import wqh.blog.ui.fragment.WorkListFragment;
import wqh.blog.ui.customview.DrawerDelegate;

/**
 * Created by WQH on 2016/4/11  17:11.
 * 想办法把这个抽象到上层去
 */
public class MainActivity extends BaseActivity implements DrawerDelegate.DrawerListener {

    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;

    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    private DrawerDelegate mDrawerDelegate;

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mDrawerDelegate = new DrawerDelegate(this, mToolbar, this);
        mDrawerDelegate.init();
    }

    private void initView() {
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), getTabFragment()));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private List<Fragment> getTabFragment() {
        List<Fragment> list = new ArrayList<>();
        list.add(new BlogListFragment());
        list.add(new WorkListFragment());
        return list;
    }

    @Override
    public boolean onDrawerMenuSelected(View view, int position, IDrawerItem drawerItem) {
        return false;
    }

    @NonNull
    @Override
    public List<IDrawerItem> onDrawerMenuCreate() {
        List<IDrawerItem> list = new ArrayList<>();
        list.add(new PrimaryDrawerItem().withName("Windows").withIcon(FontAwesome.Icon.faw_windows));
        list.add(new PrimaryDrawerItem().withName("Linux").withIcon(FontAwesome.Icon.faw_linux));
        list.add(new PrimaryDrawerItem().withName("Apple").withIcon(FontAwesome.Icon.faw_apple));
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        mDrawerDelegate.destroy();
        mDrawerDelegate = null;
        super.onDestroy();
    }
}
