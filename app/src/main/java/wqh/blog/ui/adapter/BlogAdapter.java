package wqh.blog.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wqh.blog.R;
import wqh.blog.ui.adapter.base.BaseAdapter;
import wqh.blog.model.bean.Blog;

/**
 * Created by WQH on 2016/4/11  23:31.
 * <p>
 * Adapter for list of Blog
 */
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
        holder.createdAt.setText(itemData.createdAt.toString().substring(10));

        //Todo: 怎么展示用户头像?
    }

    @Override
    public BlogAdapter.BlogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BlogHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog, parent, false));
    }

    static class BlogHolder extends BaseAdapter.BaseHolder{
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
