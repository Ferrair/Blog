package wqh.blog.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wqh.blog.R;
import wqh.blog.app.Config;
import wqh.blog.mvp.model.bean.Comment;
import wqh.blog.ui.adapter.base.AdapterProvider;
import wqh.blog.ui.adapter.base.BaseViewHolder;
import wqh.blog.util.ImageLoaderOption;

/**
 * Created by WQH on 2016/5/2  20:43.
 */
public class CommentAdapter extends AdapterProvider<CommentAdapter.CommentsHolder, Comment> {

    private static final String TAG = "CommentAdapter";

    public CommentAdapter(Context mContext) {
        super(mContext);
    }


    @Override
    public CommentsHolder onCreateViewHolder( @NonNull ViewGroup parent) {
        return new CommentsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindItemDataToView(CommentsHolder holder, Comment itemData) {

        holder.createdAt.setText(itemData.createdAt.toString());
        holder.creatorName.setText(String.valueOf(itemData.creatorName));
        if (itemData.creatorAvatarUri != null && !TextUtils.isEmpty(itemData.creatorAvatarUri))
            ImageLoader.getInstance().displayImage(Config.REMOTE_DIR + itemData.creatorAvatarUri, holder.avatar, ImageLoaderOption.getOptions());

        if (itemData.replyTo == null) {
            holder.content.setText(itemData.content);
        } else {
            Spannable span = new SpannableString(itemData.content);
            int startIndex = itemData.content.indexOf("//@");
            int endIndex = itemData.content.indexOf(":");
            if (startIndex >= 0 && endIndex >= 0) {
                span.setSpan(new ForegroundColorSpan(Color.BLUE), startIndex, endIndex + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.content.setText(span);
            } else {
                holder.content.setText(itemData.content);
            }
        }
    }

    public static class CommentsHolder extends BaseViewHolder {
        @Bind(R.id.createdAt)
        TextView createdAt;
        @Bind(R.id.creatorName)
        TextView creatorName;
        @Bind(R.id.content)
        TextView content;
        @Bind(R.id.user_avatar)
        CircleImageView avatar;

        public CommentsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
