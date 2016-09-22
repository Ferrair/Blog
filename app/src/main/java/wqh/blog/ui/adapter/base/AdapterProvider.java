package wqh.blog.ui.adapter.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by WQH on 2016/9/2  19:06.
 */
public abstract class AdapterProvider<Holder extends BaseViewHolder, Model> {
    protected Context mContext;

    public AdapterProvider(Context mContext) {
        this.mContext = mContext;
    }

    /*
     * create VIewHolder in subclass.
     */
    public abstract Holder onCreateViewHolder(@NonNull ViewGroup parent);

    /**
     * abstract method for subclass to bind ITEM data to the view.
     * so the subclass can show this item data by views holden by holder
     * <p>
     * NOTE: Use this method instead of @see{#onBindViewHolder} which is final in this class
     *
     * @param holder   a RecyclerView.ViewHolder that hold the view.
     * @param itemData item data from the <code>List<Model><code/>
     */
    public abstract void onBindItemDataToView(Holder holder, Model itemData);
}
