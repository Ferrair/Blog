package wqh.blog.ui.activity;

import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;


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
import wqh.blog.util.CollectionUtil;
import wqh.blog.manager.IntentManager;
import wqh.blog.util.JsonUtil;
import wqh.blog.util.ToastUtil;

public class LoginActivity extends ToolbarActivity {

    private static final String TAG = "LoginActivity";
    @Bind(R.id.username)
    TextInputEditText mUserNameEditText;

    @Bind(R.id.password)
    TextInputEditText mPasswordEditText;

    private UserPresenter mUserPresenter = new UserPresenterImpl();
    private LoadView mUserLoadView = new UserLoadView();

    @Override
    protected int layoutId() {
        return R.layout.activity_login;
    }


    @OnClick(R.id.login)
    public void login() {
        String username = mUserNameEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            ToastUtil.showToast(R.string.username_empty);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast(R.string.password_empty);
            return;
        }

        mUserPresenter.login(username, password, mUserLoadView);
    }

    @OnClick(R.id.do_register)
    public void doRegister() {
        IntentManager.goToOtherActivity(this, RegisterActivity.class);
    }

    private class UserLoadView implements LoadView {

        @Override
        public void onSuccess(String resultJson) {
            List<User> data = CollectionUtil.asList(JsonUtil.fromJson(resultJson, User[].class));
            if (data.size() == 0) {
                ToastUtil.showToast(R.string.login_error);
                return;
            }
            UserManager.instance().saveUser(data.get(0));
            ToastUtil.showToast(R.string.login_success);
            IntentManager.goToOtherActivity(LoginActivity.this, MainActivity.class);
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }
}
