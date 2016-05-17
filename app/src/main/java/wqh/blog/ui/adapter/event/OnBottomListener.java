package wqh.blog.ui.adapter.event;

/**
 * Created by WQH on 2016/5/16  21:53.
 * Call when RecyclerView scroll to bottom.
 */
public interface OnBottomListener {
    void onLoadMore(int toToLoadPage);
}
