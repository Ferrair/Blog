package wqh.blog.ui.activity;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import wqh.blog.R;
import wqh.blog.mvp.model.bean.User;
import wqh.blog.mvp.presenter.remote.user.UserPresenter;
import wqh.blog.mvp.presenter.remote.user.UserPresenterImpl;
import wqh.blog.mvp.view.LoadView;
import wqh.blog.ui.base.ToolbarActivity;
import wqh.blog.util.IntentUtil;
import wqh.blog.util.ToastUtil;

public class RegisterActivity extends ToolbarActivity {
    private static final String TAG = "RegisterActivity";
    @Bind(R.id.username)
    EditText mUserNameEditText;

    @Bind(R.id.password)
    EditText mPassWordEditText;

    @Bind(R.id.password_confirm)
    EditText mPassWordConfirmEditText;

    private UserPresenter mUserPresenter = new UserPresenterImpl();
    private LoadView<User> mUserLoadView = new UserLoadView();

    @Override
    protected int layoutId() {
        return R.layout.activity_register;
    }

    @OnClick(R.id.register)
    public void register() {
        String username = mUserNameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            ToastUtil.showToast("用户名为空");
            return;
        }
        String password = mPassWordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast("密码为空");
            return;
        }
        if (!password.equals(mPassWordConfirmEditText.getText().toString().trim())) {
            ToastUtil.showToast("两次密码不一致");
            return;
        }

        mUserPresenter.register(username, password, mUserLoadView);
    }


    private class UserLoadView implements LoadView<User> {
        @Override
        public void onSuccess(List<User> data) { //data in null in this section.
            ToastUtil.showToast("注册成功");
            new Handler().postDelayed(() -> IntentUtil.goToOtherActivity(RegisterActivity.this, MainActivity.class), 1000);
        }

        @Override
        public void onFail(int errorCode, String errorMsg) {
            ToastUtil.showToast("注册失败");
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }
}
