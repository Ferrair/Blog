package wqh.blog.model.remote;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by WQH on 2016/4/16  19:13.
 */
public class RemoteManager {

    public static final String DOMAIN = "http://wangqihang.cn:8080/Blog/";
    public static final int OK = 100; // Request OK
    public static final int NO_OBJECT = 101; // Can't Find Object
    public static final int PARSE = 109; // Some error in parse JSON
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
                        .addNetworkInterceptor(new AutInterceptor())
                        .readTimeout(10, TimeUnit.SECONDS)
                        /*.addInterceptor(loggingInterceptor)*/
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

    private static class AutInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originRequest = chain.request();
            return chain.proceed(originRequest);
        }
    }
}
