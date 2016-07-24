package wqh.blog.mvp.model.service;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by WQH on 2016/5/13  22:14.
 */
public interface UserAPI {
    @FormUrlEncoded
    @POST("user/login")
    Call<ResponseBody> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/register")
    Call<ResponseBody> register(@Field("username") String username, @Field("password") String password);

    @GET("user/logout")
    Call<ResponseBody> logout(@Query("id") int id);

    @GET("user/status")
    Call<ResponseBody> status(@Query("id") int id);

    // @Path
    // Todo 使用Url来下载
    @GET("user/downloadCover")
    Call<ResponseBody> downloadCover(@Query("id") int id);

    @GET("user/downloadAvatar")
    Call<ResponseBody> downloadAvatar(@Query("id") int id);

    @Multipart
    @POST("/user/changeAvatar")
    Call<ResponseBody> changeAvatar(@Field("id") int id, @Part MultipartBody.Part avatarFile);

    @Multipart
    @POST("/user/changeCover")
    Call<ResponseBody> changeCover(@Field("id") int id, @Part MultipartBody.Part coverFile);

}
