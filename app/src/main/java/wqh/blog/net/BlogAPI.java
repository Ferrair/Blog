package wqh.blog.net;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import wqh.blog.bean.Blog;

/**
 * Created by WQH on 2016/4/16  19:22.
 */
public interface BlogAPI {

    Call<Blog> queryById(@Query("id") int id);

    Call<Blog> queryByTitle(@Query("title") String title);

    Call<Blog> queryByTag(@Query("tag") String tag);

    Call<Blog> queryByTime(@Query("time") String time);

    Call<Blog> queryByType(@Query("type") String type);

    Call<Blog> queryAll(@Query("type") String type);

}
