package wqh.blog.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import wqh.blog.R;
import wqh.blog.util.IntentUtil;

public class SplashActivity extends AppCompatActivity {

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            IntentUtil.goToOtherActivity(SplashActivity.this, MainActivity.class);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Thread(() -> {
            task();
            handler.sendMessageDelayed(Message.obtain(), 2000);
        }).start();
    }

    private void task() {
    }

}