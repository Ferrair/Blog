package wqh.blog.ui.adapter.event;

import android.view.View;

/**
 * Created by WQH on 2016/4/11  22:48.
 */
public interface OnItemLongClickListener<DataType> {
    boolean onItemLongClick(View view, DataType data);
}
