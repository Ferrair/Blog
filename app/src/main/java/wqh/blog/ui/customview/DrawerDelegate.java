package wqh.blog.ui.customview;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;

import wqh.blog.R;
import wqh.blog.mvp.model.service.UserManager;
import wqh.blog.ui.activity.LoginActivity;
import wqh.blog.ui.activity.SettingActivity;
import wqh.blog.ui.activity.UserCenterActivity;
import wqh.blog.manager.IntentManager;

/**
 * Created by WQH on 2016/4/11  18:30.
 * <p>
 * Drawer for user on the left of the screen,this class is a delegate to reduce the coupling of the <code>MainActivity<code/>
 */
public class DrawerDelegate {
    private Toolbar toolbar;
    private Activity activity;

    private DrawerListener drawerListener;

    protected Drawer drawer;
    protected AccountHeader header;
    protected ProfileDrawerItem profileDrawerItem;

    public DrawerDelegate(@NonNull Activity activity, @NonNull Toolbar toolbar, @NonNull DrawerListener listener) {
        this.activity = activity;
        this.toolbar = toolbar;
        this.drawerListener = listener;
    }

    public void init() {
        header = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.mipmap.user_info_bg)
                .addProfiles(profileDrawerItem = new ProfileDrawerItem().withName(activity.getString(R.string.no_login)))
                .withOnAccountHeaderListener((view, profile, current) -> {
                    /*
                     * User is logged => UserCenterActivity
                     * User is not logged => LoginActivity
                     */
                    if (UserManager.instance().isLogged())
                        IntentManager.goToOtherActivity(activity, UserCenterActivity.class);
                    else
                        IntentManager.goToOtherActivity(activity, LoginActivity.class);
                    return true;
                })
                .build();

        final PrimaryDrawerItem setting = new PrimaryDrawerItem().withIcon(FontAwesome.Icon.faw_cogs).withName(R.string.action_settings);
        drawer = new DrawerBuilder()
                .withToolbar(toolbar)
                .withActivity(activity)
                .withDisplayBelowStatusBar(false)
                .withDrawerItems(drawerListener.onDrawerMenuCreate())
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    if (drawerItem == setting) {
                        IntentManager.goToOtherActivity(activity, SettingActivity.class);
                        return true;
                    }
                    return drawerListener.onDrawerMenuSelected(view, position, drawerItem);
                })
                .withAccountHeader(header)
                .build();
        drawer.addStickyFooterItem(setting);
        drawer.getDrawerLayout().setFitsSystemWindows(true);
        drawer.getSlider().setFitsSystemWindows(true);
    }

    public void setName(String name) {
        profileDrawerItem.withName(name);
        header.updateProfile(profileDrawerItem);
    }

    public void setAvatar(String url) {
        profileDrawerItem.withIcon(url);
        header.updateProfile(profileDrawerItem);
    }

    public void setAvatar(@DrawableRes int resId) {
        profileDrawerItem.withIcon(resId);
        header.updateProfile(profileDrawerItem);
    }

    public void setHeaderBackground(String url) {
        header.setHeaderBackground(new ImageHolder(url));
    }

    public void setHeaderBackground(@DrawableRes int resId) {
        header.setHeaderBackground(new ImageHolder(resId));
    }

    public boolean onDrawerMenuSelected(View view, int position, IDrawerItem drawerItem) {
        return false;
    }

    public void setDrawerListener(DrawerListener drawerListener) {
        this.drawerListener = drawerListener;
    }

    public void destroy() {
        header.clear();
        header = null;
        profileDrawerItem = null;
        drawerListener = null;
        activity = null;
        toolbar = null;
        drawer = null;
    }

    /**
     * A drawer listener which can create AND listen a item in the parent drawer
     */
    public interface DrawerListener {
        boolean onDrawerMenuSelected(View view, int position, IDrawerItem drawerItem);

        @NonNull
        List<IDrawerItem> onDrawerMenuCreate();
    }
}
