package wqh.blog.ui.activity;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;


import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import wqh.blog.R;
import wqh.blog.mvp.model.bean.User;
import wqh.blog.mvp.model.service.UserManager;
import wqh.blog.mvp.presenter.remote.user.UserPresenter;
import wqh.blog.mvp.presenter.remote.user.UserPresenterImpl;
import wqh.blog.mvp.view.LoadView;
import wqh.blog.ui.base.ToolbarActivity;
import wqh.blog.util.IntentUtil;
import wqh.blog.util.ToastUtil;

public class LoginActivity extends ToolbarActivity {

    private static final String TAG = "LoginActivity";
    @Bind(R.id.username)
    EditText mUserNameEditText;

    @Bind(R.id.password)
    EditText mPasswordEditText;

    private UserPresenter mUserPresenter = new UserPresenterImpl();
    private LoadView<User> mUserLoadView = new UserLoadView();

    @Override
    protected int layoutId() {
        return R.layout.activity_login;
    }


    @OnClick(R.id.login)
    public void login() {
        String username = mUserNameEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            ToastUtil.showToast("用户名为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast("密码为空");
            return;
        }

        mUserPresenter.login(username, password, mUserLoadView);
    }

    @OnClick(R.id.do_register)
    public void doregister() {
        IntentUtil.goToOtherActivity(this, RegisterActivity.class);
    }

    private class UserLoadView implements LoadView<User> {
        @Override
        public void onSuccess(List<User> data) {
            if (data.size() == 0) {
                ToastUtil.showToast("登陆失败");
                return;
            }
            UserManager.instance().saveUser(data.get(0));
            ToastUtil.showToast("登陆成功");
            IntentUtil.goToOtherActivity(LoginActivity.this, MainActivity.class);
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }
}
