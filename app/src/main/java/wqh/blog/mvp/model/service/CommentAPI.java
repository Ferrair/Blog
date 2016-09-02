package wqh.blog.mvp.model.service;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by WQH on 2016/5/1  20:49.
 */
public interface CommentAPI {
    @GET("blog/queryComment")
    Call<ResponseBody> queryComment(@Query("belongTo") int blogId, @Query("pageNum") int pageNum);

    /**
     * Todo : Error when post a comment.
     * Post a comment.
     * Using<code>@FormUrlEncoded</code> , <code> @POST</code> and <code>@Field</code>.
     *
     * And MUST holds a Token in HTTP's Header.
     */
    @FormUrlEncoded
    @POST("blog/appendComment")
    Call<ResponseBody> postComment(
            @Field("belongTo") int belongTo,
            @Field("content") String content,
            @Field("createdBy") int createdBy,
            @Header("userID") int userID,
            @Header("token") String token
    );


    @FormUrlEncoded
    @POST("blog/replyComment")
    Call<ResponseBody> replyComment(
            @Field("belongTo") int belongTo,
            @Field("content") String content,
            @Field("createdBy") int createdBy,
            @Field("replyTo") int replyTo,
            @Header("userID") int userID,
            @Header("token") String token
    );

    /**
     * Delete a comment by given id.
     * And the comment.belongTo == currentUser.id.
     */

    @DELETE("/blog/deleteComment")
    Call<ResponseBody> deleteById(@Query("id") int id);
}
