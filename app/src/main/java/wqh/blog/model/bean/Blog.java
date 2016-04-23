package wqh.blog.model.bean;

import java.util.Date;


/**
 * Created by WQH on 2016/4/11  16:43.
 */
public class Blog {
    public int id;
    public String title;
    public String type;
    public String abstractStr;
    public String content;
    public Date createdAt;
    public String tag;
    public int times;

    @Override
    public String toString() {
        return "Blog@" + id + "-->" + title;
    }
}
