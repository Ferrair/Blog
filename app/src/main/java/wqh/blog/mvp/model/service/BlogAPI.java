package wqh.blog.mvp.model.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by WQH on 2016/4/16  19:22.
 */
public interface BlogAPI {

    @GET("blog/queryById")
    Call<ResponseBody> queryById(@Query("id") int id);

    @GET("blog/queryByTitle")
    Call<ResponseBody> queryByTitle(@Query("title") String title);

    @GET("blog/queryByTag")
    Call<ResponseBody> queryByTag(@Query("tag") String tag);

    @GET("blog/queryByTime")
    Call<ResponseBody> queryByTime(@Query("time") String time);

    @GET("blog/queryByType")
    Call<ResponseBody> queryByType(@Query("type") String type);

    //Request to BlogController#index() in server
    @GET("blog/")
    Call<ResponseBody> queryAll();

    @GET("blog/addTimes")
    Call<ResponseBody> addTimes(@Query("id") int id);
}
