package wqh.blog.mvp.model.bean;

import java.sql.Date;


/**
 * Created by WQH on 2016/4/11  16:43.
 */
public class Comment {
    public Integer id;
    public String content;
    public Date createdAt;
    public Integer belongTo;
    public Integer replyTo;

    public Integer createdBy;
    public String creatorAvatarUri;
    public String creatorName;

    @Override
    public String toString() {
        return "Comment@" + id + " :BelongTo-> " + belongTo + " :Content-> " + content;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Comment && ((Comment) other).id.equals(id);
    }
}
