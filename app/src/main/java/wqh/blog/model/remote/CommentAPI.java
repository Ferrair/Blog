package wqh.blog.model.remote;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import wqh.blog.model.bean.Comment;
import wqh.blog.model.bean.Holder;

/**
 * Created by WQH on 2016/5/1  20:49.
 */
public interface CommentAPI {
    @GET("blog/queryComment")
    Call<Holder<Comment>> queryComment(@Query("id") int blogId);

    /*
     * Todo : Using POST instead of GET
     * @FormUrlEncoded and @Field
     * Request for multi param.
     */
    @GET("blog/appendComment")
    Call<Holder<Comment>> postComment(@Query("belongTo") int belongTo, @Query("content") String content, @Query("createdBy") String createdBy);
}
