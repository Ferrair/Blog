package wqh.blog.ui.activity;

import android.os.Handler;
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

public class RegisterActivity extends ToolbarActivity {
    private static final String TAG = "RegisterActivity";
    @Bind(R.id.username)
    TextInputEditText mUserNameEditText;

    @Bind(R.id.password)
    TextInputEditText mPassWordEditText;

    @Bind(R.id.password_confirm)
    TextInputEditText mPassWordConfirmEditText;

    private UserPresenter mUserPresenter = new UserPresenterImpl();
    private LoadView mUserLoadView = new UserLoadView();

    @Override
    protected int layoutId() {
        return R.layout.activity_register;
    }

    @OnClick(R.id.register)
    public void register() {
        String username = mUserNameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            ToastUtil.showToast(R.string.username_empty);
            return;
        }
        String password = mPassWordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast(R.string.password_empty);
            return;
        }
        if (!password.equals(mPassWordConfirmEditText.getText().toString().trim())) {
            ToastUtil.showToast(R.string.wrong_password_confirm);
            return;
        }

        mUserPresenter.register(username, password, mUserLoadView);
    }


    private class UserLoadView implements LoadView {

        @Override
        public void onSuccess(String resultJson) {
            List<User> data = CollectionUtil.asList(JsonUtil.fromJson(resultJson, User[].class));
            ToastUtil.showToast(R.string.register_success);
            UserManager.instance().saveUser(data.get(0));
            new Handler().postDelayed(() -> IntentManager.goToOtherActivity(RegisterActivity.this, MainActivity.class), 1000);
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            ToastUtil.showToast(R.string.register_fail);
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }
}
