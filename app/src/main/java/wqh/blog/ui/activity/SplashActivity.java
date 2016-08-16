package wqh.blog.ui.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;

import wqh.blog.R;
import wqh.blog.manager.IntentManager;

/**
 * Full Screen Mode.(Immersive Mode,hide status bar nad navigation bar)
 * See Manifest.xml and style.xml.
 */

public class SplashActivity extends Activity {

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            IntentManager.goToOtherActivity(SplashActivity.this, MainActivity.class);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        task();
        new Thread(() -> {
            handler.sendMessageDelayed(Message.obtain(), 2000);
        }).start();
    }

    private void task() {
    }

}