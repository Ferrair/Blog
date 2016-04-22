package wqh.blog.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import wqh.blog.adapter.event.OnItemClickListener;
import wqh.blog.adapter.event.OnItemLongClickListener;

/**
 * Created by WQH on 2016/4/11  21:07.
 * <p>
 * BaseAdapter extends RecyclerView.Adapter,subclass extends this class and do explicit works.
 * Such as bind the data to the view,that's mean show the data to the user.
 */
public abstract class BaseAdapter<Holder extends RecyclerView.ViewHolder, DataType> extends RecyclerView.Adapter<Holder> implements Adapter<DataType> {
    protected Context mContext;
    protected List<DataType> mListData;
    protected OnItemClickListener<DataType> mOnItemClickListener;
    protected OnItemLongClickListener<DataType> mOnItemLongClickListener;

    /**
     * abstract method for subclass to bind ITEM data to the view.
     * so the subclass can show this item data by views holden by holder
     * <p>
     * NOTE: Use this method instead of @see{#onBindViewHolder} which is final in this class
     *
     * @param holder   a RecyclerView.ViewHolder that hold the view.
     * @param itemData item data from the <code>List<DataType><code/>
     */
    protected abstract void onBindItemDataToView(Holder holder, DataType itemData);


    public BaseAdapter(Context mContext, List<DataType> mListData) {
        this.mContext = mContext;
        this.mListData = mListData;
    }

    @Override
    public final void onBindViewHolder(Holder holder, int position) {
        final DataType itemData = mListData.get(position);
        onBindItemDataToView(holder, itemData);
        bindListener(holder, itemData);
    }

    /**
     * Add OnItemClickListener and OnItemLongClickListener on the holder if exists
     *
     * @param holder   a RecyclerView.ViewHolder that hold the view.That's means the
     *                 holder can add Listener
     * @param itemData item data from the <code>List<DataType><code/>
     */
    private void bindListener(Holder holder, DataType itemData) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(view -> mOnItemClickListener.onItemClick(view, itemData));
        }

        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(v -> {
                mOnItemLongClickListener.onItemLongClick(v, itemData);
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mListData == null)
            return 0;
        return mListData.size();
    }

    @Override
    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    /**
     * Refresh the Adapter by the given newData
     */
    @Override
    public void refresh(List<DataType> newData) {
        mListData.clear();
        mListData.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public void addAtTail(DataType data) {
        this.addOne(data, mListData.size() - 1);
    }

    @Override
    public void addAtHead(DataType data) {
        this.addOne(data, 0);
    }

    public List<DataType> getAllData() {
        return mListData;
    }

    @Override
    public void addOne(DataType data, int position) {
        this.mListData.add(position, data);
        notifyItemInserted(position);
        if (position != mListData.size() - 1) {
            notifyItemRangeChanged(position, mListData.size() - position);
        }
    }

    @Override
    public void removeOne(DataType item) {
        notifyItemRemoved(this.mListData.indexOf(item));
        this.mListData.remove(item);
    }

    @Override
    public void removeOne(int position) {
        this.mListData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void removeAll() {
        this.mListData.clear();
        notifyDataSetChanged();
    }

    @Override
    public DataType getOne(int which) {
        return mListData.get(which);
    }

    public void setOnItemClickListener(OnItemClickListener<DataType> mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<DataType> mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }
}
