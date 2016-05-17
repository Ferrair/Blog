package wqh.blog.ui.adapter.base;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import java.util.List;

import wqh.blog.ui.adapter.event.OnItemClickListener;
import wqh.blog.ui.adapter.event.OnItemLongClickListener;
import wqh.blog.util.CollectionUtil;

/**
 * Created by WQH on 2016/4/11  21:07.
 * <p>
 * BaseAdapter extends RecyclerView.Adapter,subclass extends this class and do explicit works.
 * Such as bind the data to the view,that's mean show the data to the user.
 */
public abstract class BaseAdapter<Holder extends BaseAdapter.BaseHolder, DataType> extends RecyclerView.Adapter<Holder> implements Adapter<DataType> {
    protected Context mContext;
    protected List<DataType> mListData;


    /**
     * a SparseArray that stores a pair.
     * key is resId of a view.
     * value is Listener which can be triggered by click(or long click) the view in key.
     */
    protected SparseArray<OnItemClickListener<DataType>> mItemClickListener = new SparseArray<>();
    protected SparseArray<OnItemLongClickListener<DataType>> mLongItemClickListener = new SparseArray<>();

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
        for (int i = 0; i < mItemClickListener.size(); ++i) {
            int resId = mItemClickListener.keyAt(i);
            holder.getView(resId).setOnClickListener(view -> mItemClickListener.get(resId).onItemClick(view, itemData));
        }

        for (int i = 0; i < mLongItemClickListener.size(); ++i) {
            int resId = mLongItemClickListener.keyAt(i);
            holder.getView(resId).setOnLongClickListener(view -> {
                mLongItemClickListener.get(resId).onItemLongClick(view, itemData);
                // return true means this view consume this action.So will not dispatch this action.
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


    @Override
    public void refresh(List<DataType> newData) {
        mListData.clear();
        mListData.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(List<DataType> newData) {
        if (this.mListData == null) {
            this.mListData = newData;
        } else {
            int prePosition = mListData.size();
            CollectionUtil.addAllDistinct(mListData, newData);
            notifyItemRangeChanged(prePosition, mListData.size() - 1);
        }
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

    public void setOnItemClickListener(@IdRes int resId, OnItemClickListener<DataType> mOnItemClickListener) {
        mItemClickListener.append(resId, mOnItemClickListener);
    }

    public void setOnItemLongClickListener(@IdRes int resId, OnItemLongClickListener<DataType> mOnItemLongClickListener) {
        mLongItemClickListener.append(resId, mOnItemLongClickListener);
    }

    public abstract static class BaseHolder extends RecyclerView.ViewHolder {

        public BaseHolder(View itemView) {
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
}
