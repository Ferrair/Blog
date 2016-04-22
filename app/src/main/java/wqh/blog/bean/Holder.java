package wqh.blog.bean;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

import wqh.blog.util.Json;

/**
 * Created by WQH on 2016/4/17  23:34.
 * An Holder that hold the objects fetch from server.
 * <p>
 * <Code>   the code that the server return, all case are in @see{wqh.blog.net.NetManager}.
 * <Msg>    the message that the server return, the message is an description of the code.
 * <Result> the entity that the server return,and the type od the field is JsonArray because the JSON data the server are all in Array though the number of data is 1
 *
 * @param <Result> the type of an entity class,like <Blog> or <Work>
 */
public class Holder<Result> {
    @SerializedName(value = "Code")
    public int Code;
    @SerializedName(value = "Msg")
    public String Msg;
    @SerializedName(value = "Result")
    public JsonArray Result;

    public List<Result> dataList(Class<Result[]> clazz) {
        Result[] arr = Json.fromJson(this.Result.toString(), clazz);
        return Arrays.asList(arr);
    }
}