package wqh.blog.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import wqh.blog.R;
import wqh.blog.app.Config;
import wqh.blog.mvp.model.bean.Comment;
import wqh.blog.ui.adapter.base.BaseAdapter;
import wqh.blog.util.ImageLoaderOption;

/**
 * Created by WQH on 2016/5/2  20:43.
 */
public class CommentAdapter extends BaseAdapter<CommentAdapter.CommentsHolder, Comment> {

    private static final String TAG = "CommentAdapter";

    public CommentAdapter(Context mContext) {
        this(mContext, null);
    }

    public CommentAdapter(Context mContext, List<Comment> mListData) {
        super(mContext, mListData);
    }

    @Override
    protected void onBindItemDataToView(CommentsHolder holder, Comment itemData) {

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
            span.setSpan(new ForegroundColorSpan(Color.BLUE), startIndex, endIndex + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.content.setText(span);
        }
    }

    @Override
    public CommentsHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new CommentsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false));
    }

    public static class CommentsHolder extends BaseAdapter.BaseHolder {
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
