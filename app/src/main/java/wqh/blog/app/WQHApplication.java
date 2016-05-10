package wqh.blog.app;

import android.app.Application;

import com.litesuits.orm.LiteOrm;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import wqh.blog.util.ToastUtil;

/**
 * Created by WQH on 2016/4/11  16:48.
 */
public class WQHApplication extends Application {

    public static LiteOrm mDB;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
        initToast();
        initDB();
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
