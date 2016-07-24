package wqh.blog.ui.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import wqh.blog.R;
import wqh.blog.ui.base.ToolbarActivity;

/**
 * Created by WQH on 2016/7/22  21:42.
 *
 * Setting Activity that hold a static inner class SettingFragment, but the main logic in res.xml.setting.
 */
public class SettingActivity extends ToolbarActivity {

    @Override
    protected int layoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.settingContainer, new SettingsFragment()).commit();
        }
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting);
        }
    }
}
