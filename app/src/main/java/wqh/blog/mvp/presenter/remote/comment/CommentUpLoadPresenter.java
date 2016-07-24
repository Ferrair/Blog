package wqh.blog.mvp.presenter.remote.comment;

import wqh.blog.mvp.model.bean.Comment;
import wqh.blog.mvp.presenter.remote.base.UpLoadPresenter;
import wqh.blog.mvp.view.LoadView;

/**
 * Created by WQH on 2016/7/24  16:44.
 */
public interface CommentUpLoadPresenter extends UpLoadPresenter<Comment> {
    void reply(Comment aData, LoadView mLoadView);
}
