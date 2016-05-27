package wqh.blog.app;

import android.app.Application;

import com.github.moduth.blockcanary.BlockCanary;
import com.litesuits.orm.LiteOrm;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.leakcanary.LeakCanary;

import wqh.blog.util.ToastUtil;

/**
 * Created by WQH on 2016/4/11  16:48.
 */
public class WQHApplication extends Application {

    public static LiteOrm mDB;
    private static WQHApplication mApp;

    public static Application getApp() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        initImageLoader();
        initToast();
        initDB();
       // LeakCanary.install(this);
       // BlockCanary.install(this, new AppBlockCanaryContext()).start();
    }

    private void initDB() {
        mDB = LiteOrm.newSingleInstance(this, Config.DB_NAME);
        mDB.setDebugged(true);
    }

    private void initToast() {
        ToastUtil.register(getApplicationContext());
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(50 * 1024 * 1024)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
