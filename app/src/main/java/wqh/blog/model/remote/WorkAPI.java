package wqh.blog.model.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import wqh.blog.model.bean.Holder;
import wqh.blog.model.bean.Work;

/**
 * Created by WQH on 2016/4/16  20:49.
 */
public interface WorkAPI {

    @GET("work/queryById")
    Call<Holder<Work>> queryById(@Query("id") int id);

    @GET("work/queryByTitle")
    Call<Holder<Work>> queryByTitle(@Query("title") String title);

    @GET("work/")
    Call<Holder<Work>> queryAll();
}
