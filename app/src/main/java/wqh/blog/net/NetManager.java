package wqh.blog.net;

import java.io.IOException;

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
public class NetManager {

    public static final String DOMAIN = "https://api.github.com/";
    private Retrofit retrofit;


    private static class ClassHolder {
        private static NetManager INSTANCE = new NetManager();
    }

    private NetManager() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client =
                new OkHttpClient
                        .Builder()
                        .addNetworkInterceptor(new AutInterceptor())
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
