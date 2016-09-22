package wqh.blog.ui.adapter.base;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by WQH on 2016/9/2  19:07.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * find a View by given resId in parent view container.
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int resId) {
        return (T) itemView.findViewById(resId);
    }
}
