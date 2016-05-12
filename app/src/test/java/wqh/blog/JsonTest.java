package wqh.blog;

import org.json.JSONException;
import org.junit.Test;

import wqh.blog.mvp.model.bean.Comment;
import wqh.blog.mvp.model.bean.Holder;
import wqh.blog.util.Json;

/**
 * Created by WQH on 2016/5/8  16:02.
 */
public class JsonTest {

    @Test
    public void json() throws JSONException {
        String jsonStr = "{\"Msg\":\"OK\",\"Code\":100,\"Result\":[{\"createdAt\":\"2016-01-12 03:27:30\",\"createdBy\":\"王启航\",\"belongTo\":1,\"id\":1,\"content\":\"我靠，中文是不是真的可以了 ？？？\"},{\"createdAt\":\"2016-05-04 07:15:21\",\"createdBy\":\"王启航\",\"belongTo\":1,\"id\":2,\"content\":\"这篇文章写的好好好。\"},{\"createdAt\":\"2016-05-04 23:06:24\",\"createdBy\":\"WQH\",\"belongTo\":1,\"id\":4,\"content\":\"????6666?????\"},{\"createdAt\":\"2016-05-04 23:06:40\",\"createdBy\":\"WQH\",\"belongTo\":1,\"id\":5,\"content\":\"??\"},{\"createdAt\":\"2016-05-08 12:49:11\",\"createdBy\":\"WQH\",\"belongTo\":1,\"id\":7,\"content\":\"comment in  Post method.\"}]}";
        Holder holder = Json.fromJson(jsonStr, Holder.class);
        System.out.println(holder.Msg);
        System.out.println(holder.Code);
        System.out.println(holder.Result);
        System.out.println("\n");

        Comment[] comments = Json.fromJson(holder.Result.toString(), Comment[].class);
        for (int i = 0; i < comments.length; ++i) {
            System.out.println(comments[i].content);
        }
    }
}
