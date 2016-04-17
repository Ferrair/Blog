package wqh.blog.bean;

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
}
