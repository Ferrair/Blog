package wqh.blog.model.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import wqh.blog.model.bean.Comment;
import wqh.blog.model.bean.Holder;

/**
 * Created by WQH on 2016/5/1  20:49.
 */
public interface CommentAPI {
    @GET("blog/queryComment")
    Call<Holder<Comment>> queryComment(@Query("id") int blogId);
}
