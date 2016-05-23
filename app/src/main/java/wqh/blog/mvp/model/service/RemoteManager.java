package wqh.blog.mvp.model.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by WQH on 2016/4/16  19:13.
 */
public class RemoteManager {

    public static final String DOMAIN = "http://wangqihang.cn:8080/Blog/";
    public static final int OK = 100; // Request OK.
    public static final int PARSE = 109; // Some error in parse JSON.
    public static final int NO_MORE = 107; // No More Data from server.
    public static final int SYNTAX = 110; // JSONNull
    private Retrofit retrofit;


    private static class ClassHolder {
        private static RemoteManager INSTANCE = new RemoteManager();
    }

    private RemoteManager() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client =
                new OkHttpClient
                        .Builder()
                        .readTimeout(10, TimeUnit.SECONDS)
                        .build();

        retrofit = new Retrofit
                .Builder()
                .baseUrl(DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static <T> T create(final Class<T> service) {
        return ClassHolder.INSTANCE.retrofit.create(service);
    }
}
