package wqh.blog.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;
import wqh.blog.R;
import wqh.blog.app.Config;
import wqh.blog.mvp.model.bean.User;
import wqh.blog.mvp.model.service.UserManager;
import wqh.blog.mvp.presenter.remote.user.UserPresenter;
import wqh.blog.mvp.presenter.remote.user.UserPresenterImpl;
import wqh.blog.mvp.view.LoadView;
import wqh.blog.ui.base.ToolbarActivity;

/**
 * Created by WQH on 2016/7/22  21:02.
 */
public class UserCenterActivity extends ToolbarActivity {
    private static final String TAG = "UserCenterActivity";
    @Bind(R.id.username)
    TextView mUserNameTextView;

    @Bind(R.id.email)
    TextView mEmailTextView;

    @Bind(R.id.avatar)
    ImageView mUserAvatarImageView;

    @Bind(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @Bind(R.id.user_info)
    LinearLayout linearLayout;

    @Bind(R.id.cover_image)
    CollapsingToolbarLayout mCoverView;


    private UserPresenter mUserPresenter = new UserPresenterImpl();
    private LoadView mLoadView = new DefaultLoadView();
    private User currentUser;

    @Override
    protected int layoutId() {
        return R.layout.activity_user_center;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        appBarLayout.addOnOffsetChangedListener(new OnOffsetChangedListenerHelper());
        currentUser = UserManager.instance().currentUser();
        if (currentUser == null)
            throw new NullPointerException("CurrentUser is null,please check it before enter UserCenterActivity");
        mUserNameTextView.setText(currentUser.username);
        if (currentUser.avatarUri != null && !TextUtils.isEmpty(currentUser.avatarUri))
            ImageLoader.getInstance().displayImage(Config.REMOTE_DIR + currentUser.avatarUri, mUserAvatarImageView);
    }

    @OnClick(R.id.cover_image)
    public void operateCover() {
        new AlertDialog
                .Builder(UserCenterActivity.this)
                .setItems(new String[]{getString(R.string.change_cover), getString(R.string.save_cover)}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            changeCover();
                            break;
                        case 1:
                            saveCover();
                            break;
                    }
                })
                .create()
                .show();
    }

    private void saveCover() {

    }

    /**
     * Try to open album to pick a photo.
     */
    private void changeCover() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, Config.REQUEST_ALBUM);
    }

    /**
     * handle the photo picked in album,and upload it.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.REQUEST_ALBUM && resultCode == RESULT_OK) {
            if (data == null) return;
            if (data.getData() == null) return;
            Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                File coverFile = new File(cursor.getString(cursor.getColumnIndex("_data")));

                doUpload("cover", coverFile);
                Log.i(TAG, "LocalPath-> " + cursor.getString(cursor.getColumnIndex("_data")));
                cursor.close();
            } else
                Log.e(TAG, "Fail to pick photo in album");
        }
    }

    private void doUpload(String name, File uploadFile) {
        mUserPresenter.changeCover(currentUser.id, uploadFile, mLoadView);
    }


    private class OnOffsetChangedListenerHelper implements AppBarLayout.OnOffsetChangedListener {
        boolean avatarCanFadeOut = true, avatarCanFadeIn = false;
        boolean hasFadeOut = false, hasFadeIn = true;
        int totalScrollRange;

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

            totalScrollRange = appBarLayout.getTotalScrollRange();

            float positiveOffset = -verticalOffset;
            float percent = positiveOffset / totalScrollRange;

            avatarCanFadeOut = percent >= 0.65f;

            avatarCanFadeIn = percent < 0.65f;

            if (!hasFadeOut && avatarCanFadeOut) {
                hasFadeOut = true;
                animateOut(linearLayout);
                /*animateOut(username_tv);*/

            } else if (!hasFadeIn && avatarCanFadeIn) {
                hasFadeIn = true;
                animateIn(linearLayout);
                /*animateIn(username_tv);*/
            }
        }


        private void animateOut(View target) {
            target.animate()
                    .scaleX(0)
                    .scaleY(0)
                    .alpha(0)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            hasFadeIn = false;
                        }
                    })
                    .start();
        }

        private void animateIn(View target) {
            target.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .alpha(1)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            hasFadeOut = false;
                        }
                    })
                    .start();
        }
    }

    private class DefaultLoadView implements LoadView {
        @Override
        public void onSuccess(String resultJson) {
            Log.i(TAG, resultJson);
        }

        // Todo : handle the errorCode. Why can find data return 107???
        @Override
        public void onFail(int errorCode, String errorMsg) {
            Log.e(TAG, "ErrorCode-> " + errorCode + ", ErrorMsg-> " + errorMsg);
        }
    }
}
