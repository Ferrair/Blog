package wqh.blog.model.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import wqh.blog.model.bean.Blog;
import wqh.blog.model.bean.Comment;
import wqh.blog.model.bean.Holder;

/**
 * Created by WQH on 2016/4/16  19:22.
 */
public interface BlogAPI {

    @GET("blog/queryById")
    Call<Holder<Blog>> queryById(@Query("id") int id);

    @GET("blog/queryByTitle")
    Call<Holder<Blog>> queryByTitle(@Query("title") String title);

    @GET("blog/queryByTag")
    Call<Holder<Blog>> queryByTag(@Query("tag") String tag);

    @GET("blog/queryByTime")
    Call<Holder<Blog>> queryByTime(@Query("time") String time);

    @GET("blog/queryByType")
    Call<Holder<Blog>> queryByType(@Query("type") String type);

    //Request to BlogController#index() in server
    @GET("blog/")
    Call<Holder<Blog>> queryAll();

    @GET("blog/addTimes")
    Call<Holder<Blog>> addTimes(@Query("id") int id);
}
