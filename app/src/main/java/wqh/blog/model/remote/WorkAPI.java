package wqh.blog.model.remote;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by WQH on 2016/4/16  20:49.
 */
public interface WorkAPI {

    @GET("work/queryById")
    Call<ResponseBody> queryById(@Query("id") int id);

    @GET("work/queryByTitle")
    Call<ResponseBody> queryByTitle(@Query("title") String title);

    @GET("work/")
    Call<ResponseBody> queryAll();
}
