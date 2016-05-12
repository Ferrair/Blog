package wqh.blog.mvp.model.bean;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

/**
 * Created by WQH on 2016/5/8  12:53.
 *
 * An Holder that hold the objects fetch from server.
 * <p>
 * <Code>   the code that the server return, all case are in @see{wqh.blog.mvp.model.net.RemoteManager}.
 * <Msg>    the message that the server return, the message is an description of the code.
 * <Result> the entity that the server return,and the type od the field is <code>JsonArray</code> because the JSON data the server are all in Array though the number of data is 1
 */
public class Holder {

    @SerializedName(value = "Code")
    public int Code;
    @SerializedName(value = "Msg")
    public String Msg;
    @SerializedName(value = "Result")
    public JsonArray Result;
}
