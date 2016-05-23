package wqh.blog.mvp.model.bean;

/**
 * Created by WQH on 2016/4/11  16:45.
 */
public class Work {
    public Integer id;
    public String title;
    public String description;
    public String fileName;
    public String logoUrl;

    @Override
    public boolean equals(Object other) {
        return other instanceof Work && ((Work) other).id.equals(id);
    }
}
