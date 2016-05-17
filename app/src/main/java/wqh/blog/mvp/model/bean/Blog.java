package wqh.blog.mvp.model.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.util.Date;


/**
 * Created by WQH on 2016/4/11  16:43.
 */
@Table("blog")
public class Blog {

    @PrimaryKey(AssignType.BY_MYSELF)
    public Integer id;
    @Column("title")
    public String title;
    @Column("type")
    public String type;
    @Column("abstractStr")
    public String abstractStr;
    @Column("content")
    public String content;
    @Column("createdAt")
    public Date createdAt;
    @Column("tag")
    public String tag;
    @Column("times")
    public int times;

    @Override
    public String toString() {
        return "Blog@" + id + "-->" + title;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Blog && ((Blog) other).id.equals(id);
    }
}
