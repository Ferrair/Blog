package wqh.blog.ui.adapter.base;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wqh.blog.R;
import wqh.blog.ui.adapter.animation.AnimationManager;
import wqh.blog.ui.adapter.animation.BaseAnimation;
import wqh.blog.ui.adapter.event.LayoutState;
import wqh.blog.ui.adapter.event.OnBottomListener;
import wqh.blog.ui.adapter.event.OnItemClickListener;
import wqh.blog.ui.adapter.event.OnItemLongClickListener;
import wqh.blog.util.CollectionUtil;


/**
 * Created by WQH on 2016/4/11  21:07.
 * <p>
 * AdapterPool extends RecyclerView.Adapter,subclass extends this class and do explicit works.
 * Such as bind the data to the view,that's mean show the data to the user.
 */
public class AdapterPool<Holder extends BaseViewHolder, Model> extends RecyclerView.Adapter<Holder> implements Adapter<Model> {
    private static final String TAG = "AdapterPool";
    protected Context mContext;
    protected List<Model> mListData;
    protected List<AdapterProvider> mAdapterProvider = new ArrayList<>();
    /*
     * This is called a list of unknown type，There are two scenarios where an unbounded wildcard is a useful approach:
     * 1. If you are writing a method that can be implemented using functionality provided in the Object class.
     * 2. When the code is using methods in the generic class that don't depend on the type parameter. For example, List.size or List.clear. In fact, Class<?> is so often used because most of the methods in Class<T> do not depend on T.
     */
    protected List<Class<?>> mModel = new ArrayList<>();

    private boolean isOpenAnimation;
    private int mLastPosition;

    private BaseAnimation mBaseAnimation;
    private FooterViewHolder mFooterViewHolder;
    private OnBottomListener mOnBottomListener;

    /**
     * The current data page in RecyclerView.And will increase when the user scroll and loadMore.
     */
    private int mCurrentPage = 1;
    /**
     * The current state of FooterView.
     */
    private int mState = LayoutState.LOAD;

    public static final int ITEM_TYPE_FOOTER = Integer.MAX_VALUE;
    /**
     * a SparseArray that stores a pair.
     * key is resId of a view.
     * value is Listener which can be triggered by click(or long click) the view in key.
     */
    protected SparseArray<OnItemClickListener<Model>> mItemClickListener = new SparseArray<>();
    protected SparseArray<OnItemLongClickListener<Model>> mLongItemClickListener = new SparseArray<>();

    public AdapterPool(Context mContext) {
        this.mContext = mContext;
    }

    public void register(@NonNull Class<Model> model, @NonNull AdapterProvider<Holder, Model> viewHolderProvider) {
        synchronized (this) {
            if (mModel.contains(model))
                throw new IllegalArgumentException(TAG + " register: You have registered this model:" + model.getName());
            mModel.add(model);
            mAdapterProvider.add(viewHolderProvider);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (ITEM_TYPE_FOOTER == viewType) {
            if (mFooterViewHolder == null) {
                mFooterViewHolder = new FooterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_footer_load_more, parent, false));
            }
            return (Holder) mFooterViewHolder;
        }
        return (Holder) mAdapterProvider.get(viewType).onCreateViewHolder(parent);
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (position == mListData.size()) {
            type = ITEM_TYPE_FOOTER;
        } else {
            type = mModel.indexOf(mListData.get(position).getClass());
        }
        return type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void onBindViewHolder(Holder holder, int position) {
        Log.i(TAG, "onBindViewHolder: " + position + "  " + mListData.size());
        if (position == mListData.size()) {
            mFooterViewHolder.bind();
        } else {
            final Model itemData = mListData.get(position);
            mAdapterProvider.get(getItemViewType(position)).onBindItemDataToView(holder, itemData);
            bindListener(holder, itemData);
            bindAnimation(holder);
        }
    }

    /**
     * Bind animation to item view when the isOpenAnimation is ON.
     *
     * @param holder a RecyclerView.ViewHolder that hold the view.That's means the
     *               holder can add animation.
     */
    private void bindAnimation(Holder holder) {
        if (isOpenAnimation) {
            if (holder.getLayoutPosition() > mLastPosition) {
                for (Animator anim : mBaseAnimation.getAnimators(holder.itemView)) {
                    anim.setDuration(500).start();
                    anim.setInterpolator(new LinearInterpolator());
                }
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }

    /**
     * Bind OnItemClickListener and OnItemLongClickListener on the holder if exists
     *
     * @param holder   a RecyclerView.ViewHolder that hold the view.That's means the
     *                 holder can add Listener
     * @param itemData item data from the <code>List<Model><code/>
     */
    private void bindListener(Holder holder, Model itemData) {
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

    /**
     * When the adapter add/delete a item,animate to user by the param <param>type<param> in <code>@AnimationManager.AnimationType</code>
     */
    public void openAnimation(@AnimationManager.AnimationType int type) {
        this.isOpenAnimation = true;
        mBaseAnimation = AnimationManager.get(type);
    }

    /**
     * Because of the Footer-View,So the count MUST contains the Footer-View(means +1).
     */
    @Override
    public int getItemCount() {
        if (mListData == null)
            return 0;
        return mListData.size() + 1;

    }

    @Override
    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    @Override
    public void fill(@NonNull List<Model> newDataList) {
        if (this.mListData == null) {
            this.mListData = newDataList;
        } else {
            int prePosition = mListData.size();
            CollectionUtil.addAllDistinct(mListData, newDataList);
            notifyItemRangeChanged(prePosition, mListData.size() - 1);
        }
    }

    @Override
    public void update(@NonNull Model newData) {
        int index = mListData.indexOf(newData);
        mListData.remove(index);
        mListData.add(index, newData);
        notifyItemChanged(index);
    }


    @Override
    public void addAtTail(@NonNull Model data) {
        this.addOne(data, mListData.size() - 1);
    }

    @Override
    public void addAtHead(@NonNull Model data) {
        this.addOne(data, 0);
    }

    @Override
    public List<Model> getAllData() {
        return mListData;
    }

    @Override
    public void addOne(@NonNull Model data, int position) {
        this.mListData.add(position, data);
        notifyItemInserted(position);
        if (position != mListData.size() - 1) {
            notifyItemRangeChanged(position, mListData.size() - position);
        }
    }

    @Override
    public void removeOne(@NonNull Model item) {
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
    public Model getOne(int which) {
        return mListData.get(which);
    }

    public void setOnItemClickListener(@IdRes int resId, OnItemClickListener<Model> mOnItemClickListener) {
        mItemClickListener.append(resId, mOnItemClickListener);
    }

    public void setOnItemLongClickListener(@IdRes int resId, OnItemLongClickListener<Model> mOnItemLongClickListener) {
        mLongItemClickListener.append(resId, mOnItemLongClickListener);
    }

    /**
     * Trigger that this Adapter can LoadMore.
     * So if do not call this method,and the adapter will not call OnBottomListener().
     */
    public void setOnBottomListener(OnBottomListener mOnBottomListener) {
        this.mOnBottomListener = mOnBottomListener;
    }

    public void setLoadState(@LayoutState.State int state) {
        this.mState = state;
        if (mFooterViewHolder != null)
            mFooterViewHolder.bind();
    }

    /**
     * A Footer-View holds in recyclerView's footer.
     * This view can show 3 states:
     * -- LayoutState.LOAD : the data is loading from server.
     * -- LayoutState.FINISHED : the data have loaded from server.
     * --  LayoutState.GONE : don't show this view.
     */
    public class FooterViewHolder extends BaseViewHolder {
        @Bind(R.id.footerText)
        TextView footerText;
        @Bind(R.id.footerProgressBar)
        ProgressBar footerProgressBar;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind() {
            switch (mState) {
                case LayoutState.LOAD:
                    loadMore();
                    if (mOnBottomListener != null) {
                        mOnBottomListener.onLoadMore(++mCurrentPage);
                    }
                    break;
                case LayoutState.FINISHED:
                    noMore();
                    break;
                case LayoutState.GONE:
                    hide();
                    break;
            }
        }

        private void loadMore() {
            show();
            if (footerProgressBar.getVisibility() != View.VISIBLE) {
                footerProgressBar.setVisibility(View.VISIBLE);
            }
            footerText.setText(R.string.footer_load_more);
        }

        private void noMore() {
            show();
            if (footerProgressBar.getVisibility() != View.GONE) {
                footerProgressBar.setVisibility(View.GONE);
            }
            footerText.setText(R.string.footer_load_finish);
        }

        private void hide() {
            if (itemView.getVisibility() != View.GONE) {
                itemView.setVisibility(View.GONE);
            }
        }

        private void show() {
            if (itemView.getVisibility() != View.VISIBLE) {
                itemView.setVisibility(View.VISIBLE);
            }
        }
    }
}
