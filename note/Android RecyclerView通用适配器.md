一：定义接口
------
首先定义一个`Adapter`接口，用来描述适配器中的一些基本的方法。

```
import java.util.List;

/**
 * Created by WQH on 2016/4/11  21:21.
 * <p>
 * Interface for all <code>Adapter<code/>.
 * subclass MUST have a <code>List<DataType><code/> stores the data.
 */
public interface Adapter<DataType> {

    boolean isEmpty();

    /**
     * Refresh the Adapter by the given newData.
     * Means clear the last data.
     */
    void refresh(List<DataType> newData);

    /**
     * Add newData to this Adapter.
     * instead of #refresh,this method do NOT clear the last data.
     * <p>
     * One more thing:the Adapter MUST judge whether the Adapter holds data before.
     */
    void addAll(List<DataType> newData);

    void addOne(DataType data, int position);

    void addAtTail(DataType data);

    void addAtHead(DataType data);

    void removeOne(DataType item);

    void removeOne(int position);

    void removeAll();

    List<DataType> getAllData();

    DataType getOne(int which);
}
```

对于上面的方法，从名字就可以看出它的作用是什么，所以我就不多说了。但是要注意其中的几个方法：

 - `refresh(List<DataType> newData)`：更新`Adapter`里面的数据，这意味着要把原来的数据全部清除掉，然后在添加新的数据。（所以在用户刷新界面的时候就可以调用这个方法了）
 - `addAll(List<DataType> newData)`：这里不会把原来的数据清除掉，而是直接向里面添加。但是要判断`Adapter`里面是否已经存在数据。（所以在第一次向`Adapter`填充数据可以用）

二：`BaseAdapter`的实现
------------------

```
public abstract class BaseAdapter<Holder extends BaseAdapter.BaseHolder, DataType> extends RecyclerView.Adapter<Holder> implements Adapter<DataType>
```
这一段很长的类的声明，看着就有点怕。好吧，这是我一直在修改最后形成的类。

 - 首先类要继承`RecyclerView.Adapter`，这一点母庸置疑。然后实现刚刚我们定义的接口。
 - 类要是抽象类，因为里面有一些抽象方法，必须要让子类去实现。
 - 那么里面的2个泛型是什么意思呢？`DataType`：`Adapter`要适配的数据类型；`Holder`：一个`ViewHolder`，来存储`View`信息（这就是`Adapter`的功能，把数据信息适配为View信息），那么`BaseHolder`又是什么鬼呢？

咱们现在来看看把：

```
public abstract static class BaseHolder extends RecyclerView.ViewHolder {

        public BaseHolder(View itemView) {
            super(itemView);
        }
    }
```
就是继承了`RecyclerView.ViewHolder`，没有什么其他的方法咯。（当然这里规定了`BaseAdapter`的所有子类的`Holder`都必须是`BaseHolder`的子类，这样就和一些公用的方法提供了很多的便利）

**`Data`环节：**

下面看看`BaseAdapter`里面的成员变量与构造函数：

```
protected Context mContext; //Activity的上下文
protected List<DataType> mListData; //Adapter适配的数据集合

public BaseAdapter(Context mContext, List<DataType> mListData) {
        this.mContext = mContext;
        this.mListData = mListData;
    }
```

有了数据集合，就来实现`Adapter`里面的方法：

```
@Override
    public int getItemCount() {
        if (mListData == null)
            return 0;
        // 这里等哈会进行说明。。
        return mListData.size() + 1;
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
	        // 这里我也在纠结，怎么整。
            int prePosition = mListData.size();
            // 防止加入重复的元素(每个元素都会比较一个 O = O(n^2))
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
```

还是比较简单的，就是集合的一些基本操作。注意里面有个函数`notifyDataSetChanged();`通知`Adapter`，更新数据。


**下面进入`View`的环节：**

```
    @Override
    public final void onBindViewHolder(Holder holder, int position) {
        if (position == mListData.size()) {
            mFooterViewHolder.bind();
        } else {
            final DataType itemData = mListData.get(position);
            onBindItemDataToView(holder, itemData);
        }
    }
	

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
```
对于`onBindViewHolder`进行了以下的改进，对于每一项的data，调用抽象方法`nBindItemDataToView(Holder holder, DataType itemData)`。所以这是对于每一项的data进行操作的，这点要注意。

而对与` onCreateViewHolder`：
```
@SuppressWarnings("unchecked")
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (ITEM_TYPE_FOOTER == viewType) {
            if (mFooterViewHolder == null) {
                mFooterViewHolder = new FooterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_footer_load_more, parent, false));
            }
            return (Holder) mFooterViewHolder;
        }
        return onCreateHolder(parent, viewType);
    }

	/**
     * Create normal data ViewHolder in subclass.
     */
    protected abstract Holder onCreateHolder(ViewGroup parent, int viewType);
```

由于添加了一个`FooterView`，所以要添加以下属性：

```
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

    public static final int ITEM_TYPE_FOOTER = 0;
    public static final int ITEM_TYPE_NORMAL = 1;
```
下面一一介绍：
接口`OnBottomListener`，定义了Footer-View在加载过程中的回调方法（既然可以滚动加载，那么服务器肯定返回了分页查询的结果了，所以`toToLoadPage`就是`Activity`要加载的那一页数据。而成员变量`mCurrentPage `就是为了记录当前的页数。）:

```
/**
 * Created by WQH on 2016/5/16  21:53.
 * Call when RecyclerView scroll to bottom.
 */
public interface OnBottomListener {
    /**
     * @param toToLoadPage the page to be load.
     */
    void onLoadMore(int toToLoadPage);
}
```
`FooterView`是存在状态的，所以就会定义一系列的状态，有`mState`记录，而这些状态有哪些呢？

```
public interface LayoutState {
    int LOAD = 0;
    int FINISHED = 1;
    int GONE = 2;

    @IntDef({LOAD, FINISHED, GONE})
    @Retention(RetentionPolicy.SOURCE)
    @interface State {
    }
}
```
这里采用注解的形式来标明有哪些状态，注意注解的`Retention`属性是`RetentionPolicy.SOURCE`，也就是说只在源代码级别存在，在编译过后得到的class文件里面就不会有这个注解了。可能有些人会问为什么不用枚举呢？因为谷歌官方说了：在Android里面用枚举会比较慢啦。

然后看看`FooterView`是怎么实现的吧：

```
/**
     * A Footer-View holds in recyclerView's footer.
     * This view can show 3 states:
     * -- LayoutState.LOAD : the data is loading from server.
     * -- LayoutState.FINISHED : the data have loaded from server.
     * --  LayoutState.GONE : don't show this view.
     */
    public class FooterViewHolder extends BaseHolder {
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
            footerText.setText("Load");
        }

        private void noMore() {
            show();
            if (footerProgressBar.getVisibility() != View.GONE) {
                footerProgressBar.setVisibility(View.GONE);
            }
            footerText.setText("End");
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
```
就是这么简单。。。继承了`BaseHolder`，所以在`onCreateViewHolder`的时候，可以编译通过。

然后就是一些基本的方法了：

```
public void setLoadState(@LayoutState.State int state) {
        this.mState = state;
        if (mFooterViewHolder != null)
            mFooterViewHolder.bind();
    }
```

三：子类实现
------
这是我原来写的Blog的一个`Adapter`，可以看到里面的操作还是比较简单的。
```
public class BlogAdapter extends BaseAdapter<BlogAdapter.BlogHolder, Blog> {

    public BlogAdapter(Context mContext, List<Blog> mListData) {
        super(mContext, mListData);
    }

    public BlogAdapter(Context mContext) {
        super(mContext, null);
    }


    @Override
    protected void onBindItemDataToView(BlogAdapter.BlogHolder holder, Blog itemData) {
        holder.title.setText(itemData.title);
        holder.abstractStr.setText(itemData.abstractStr);
        holder.tag.setText(itemData.tag);
        holder.times.setText(String.valueOf(itemData.times));
        holder.createdAt.setText(TimeUtil.date2time(itemData.createdAt.toString()));
        //Todo: How t show user's avatar?
    }

    @Override
    public BlogAdapter.BlogHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new BlogHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog, parent, false));
    }

    static class BlogHolder extends BaseAdapter.BaseHolder {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.abstractStr)
        TextView abstractStr;
        @Bind(R.id.tag)
        TextView tag;
        @Bind(R.id.times)
        TextView times;
        @Bind(R.id.createdAt)
        TextView createdAt;
        @Bind(R.id.user_avatar)
        CircleImageView userAvatar;

        public BlogHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
```
可以这样使用：

```
mAdapter = new BlogAdapter(getActivity());
        mAdapter.setOnItemClickListener(R.id.item_blog, (view, data) -> IntentUtil.goToOtherActivity(getActivity(), BlogItemActivity.class, "id", data.id));
        mAdapter.openAnimation(AnimationManager.EnterInRight);
        mAdapter.setOnBottomListener(this);
```

四：一点改进
------
但是我们却忘了考虑一个问题，那就是监听器应该放在哪里？原来写的时候，放在了`Adapter`的内部，但是这样`Activity`的代码虽然减少了。但是`Adapter`却没有遵守单一职责的原则。修改bug的时候就比较痛苦了，不知道代码写在那里了。。。所以这里应该利用回调方法，把放在`Activity`里面。

首先定义接口（接口是个好东西啊^_^），表示`Click`与`LongClick`事件

```
public interface OnItemClickListener<DataType> {
    void onItemClick(View view, DataType data);
}

public interface OnItemLongClickListener<DataType> {
    boolean onItemLongClick(View view, DataType data);
}

```
那么还要考虑一个问题：`Adapter`里面有许多的控件，那么怎么标识和记录每个控件，并为其设置监听器了？所以就可以用一个`Map<Interger,OnInteClickListener<DataType>>`来存储。但Google官方推荐使用`SparseArray`来代替`Map`来存储，`SparseArray`是一个稀疏矩阵（用三元组来存储），下面对其进行简单的说明：

> SparseArrays map integers to Objects. Unlike a normal array of Objects, there can be gaps in the indices. It is intended to be more memory efficient than using a HashMap to map Integers to Objects, both because it avoids auto-boxing keys and its data structure doesn't rely on an extra entry object for each mapping.
>To help with performance, the container includes an optimization when removing keys: instead of compacting its array immediately, it leaves the removed entry marked as deleted. The entry can then be re-used for the same key, or compacted later in a single garbage collection step of all removed entries. This garbage collection will need to be performed at any time the array needs to be grown or the the map size or entry values are retrieved.
> It is possible to iterate over the items in this container using keyAt(int) and valueAt(int). Iterating over the keys using keyAt(int) with ascending values of the index will return the keys in ascending order, or the values corresponding to the keys in ascending order in the case of valueAt(int).


对于`SparseArray`相对于`HashMap`

 - 优势：  没有`HashMap`里面那么多的成员变量，所以减少了内存的占有，由于`HashMap`是要对每个元素进行hash运算的，所以就减少了hash运算，没有了自动装箱和拆箱的过程。
 - 劣势：  `SparseArray`内部只有key/value2个数组，所以查找就是用了二分查找，对于元素很多的时候（hundreds of items）性能就会减少50%。
所以在这里是使用  `SparseArray`优于`HashMap的`
为了提高性能，`SparseArray`再删除的时候，不真正的删除元素，而是把value设为`DELETED`。
```
public void delete(int key) {
        int i = ContainerHelpers.binarySearch(mKeys, mSize, key);

        if (i >= 0) {
            if (mValues[i] != DELETED) {
                mValues[i] = DELETED;
                mGarbage = true;
            }
        }
    }
```
然后还有一个`gc()`方法用来清除这些元素（在这里就不贴源码了）。
现在回到正题里面，所以那么这里的代码就是这样：

```
/**
     * a SparseArray that stores a pair.
     * key is resId of a view.
     * value is Listener which can be triggered by click(or long click) the view in key.
     */
    protected SparseArray<OnItemClickListener<DataType>> mItemClickListener = new SparseArray<>();
    protected SparseArray<OnItemLongClickListener<DataType>> mLongItemClickListener = new SparseArray<>();
```

所以还要在`onBindViewHolder`里面进行监听器的绑定。。

```
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
            holder.getView(resId).setOnLongClickListener(view -> mLongItemClickListener.get(resId).onItemLongClick(view, itemData));
        }
    }
```
最后修改`BaseHolder`

```
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
```

再添加几个方法：

```
public void setOnItemClickListener(@IdRes int resId, OnItemClickListener<DataType> mOnItemClickListener) {
        mItemClickListener.append(resId, mOnItemClickListener);
    }

    public void setOnItemLongClickListener(@IdRes int resId, OnItemLongClickListener<DataType> mOnItemLongClickListener) {
        mLongItemClickListener.append(resId, mOnItemLongClickListener);
    }
```

这样就可以了。。亲测可用，哈哈。


五：一些问题
-------
由于这里的泛型把`ViewHolder`写的比较死，所以不能出现适配多个类型的适配器，怎么解决?

最后整体的代码：
https://github.com/Ferrair/Blog/blob/master/app/src/main/java/wqh/blog/ui/adapter/base/BaseAdapter.java



----------------------------------------------------------
大二学生，写的比较浅，错误比较多，还请指正。
邮箱：1906362072@qq.com
	