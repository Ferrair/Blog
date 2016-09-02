package wqh.blog.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wqh.blog.R;
import wqh.blog.ui.adapter.base.AdapterProvider;
import wqh.blog.mvp.model.bean.Blog;
import wqh.blog.ui.adapter.base.BaseViewHolder;
import wqh.blog.util.TimeUtil;

/**
 * Created by WQH on 2016/4/11  23:31.
 * Adapter for list of Blog.
 */
public class BlogAdapter extends AdapterProvider<BlogAdapter.BlogHolder, Blog> {

    public BlogAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public BlogHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new BlogHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog, parent, false));
    }

    @Override
    public void onBindItemDataToView(BlogAdapter.BlogHolder holder, Blog itemData) {
        holder.title.setText(itemData.title);
        holder.tag.setText(itemData.tag);
        holder.times.setText(String.valueOf(itemData.times));
        holder.createdAt.setText(TimeUtil.date2time(itemData.createdAt.toString()));
        //if (itemData. != null && !TextUtils.isEmpty(itemData.creatorAvatarUri))
        //    ImageLoader.getInstance().displayImage(Config.REMOTE_DIR + itemData.creatorAvatarUri, holder.avatar);
    }


   public static class BlogHolder extends BaseViewHolder {
        @Bind(R.id.title)
        TextView title;
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
