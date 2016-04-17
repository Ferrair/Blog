package wqh.blog.net;

import retrofit2.Call;
import retrofit2.http.Query;
import wqh.blog.bean.Work;

/**
 * Created by WQH on 2016/4/16  20:49.
 */
public interface WorkAPI {

    Call<Work> queryById(@Query("id") int id);

    Call<Work> queryByTitle(@Query("title") String title);

}
