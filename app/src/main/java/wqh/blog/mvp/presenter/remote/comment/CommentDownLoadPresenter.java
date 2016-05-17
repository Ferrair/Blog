package wqh.blog.mvp.presenter.remote.comment;

import wqh.blog.mvp.model.bean.Comment;
import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/5/17  15:32.
 */
public interface CommentDownLoadPresenter {
    void loadById(int blogId, int pageNum, LoadView<Comment> mLoadView);
}
