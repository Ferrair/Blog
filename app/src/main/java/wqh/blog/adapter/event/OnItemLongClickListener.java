package wqh.blog.adapter.event;

import android.view.View;

/**
 * Created by WQH on 2016/4/11  22:48.
 */
public interface OnItemLongClickListener<DataType> {
    void onItemLongClick(View view, DataType data);
}
