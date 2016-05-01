package wqh.blog.model.bean;

import java.sql.Date;


/**
 * Created by WQH on 2016/4/11  16:43.
 */
public class Comment {
    int id;
    String content;
    String createdBy;
    Date createdAt;
    int belongTo;

    @Override
    public String toString() {
        return "Comment@" + id + " :BelongTo-> " + belongTo + " :Content-> " + content;
    }
}
