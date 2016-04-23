package wqh.blog.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Created by WQH on 2016/4/17  23:19.
 * <p>
 * Json parser using Google Gson.
 */
public class Json {
    private static Gson gson;

    private static Gson getGson() {
        if (gson == null) {
            synchronized (Json.class) {
                if (gson == null) {
                    gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                }
            }
        }
        return gson;
    }

    public static String toJson(Object src) {
        return getGson().toJson(src);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return getGson().fromJson(json, classOfT);
    }
}