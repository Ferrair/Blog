package wqh.blog.mvp.presenter.local;


import com.litesuits.orm.db.DataBase;

import wqh.blog.app.WQHApplication;

/**
 * Created by WQH on 2016/5/10  22:14.
 *
 * A Presenter that store data into local database.
 */
public class LocalPresenter {
    public DataBase db() {
        return WQHApplication.mDB;
    }
}
