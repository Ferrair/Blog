package wqh.blog.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
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

/**
 * Created by WQH on 2016/5/2  20:43.
 */
public class CommentsAdapter extends BaseAdapter<CommentsAdapter.CommentsHolder, Comment> {

    private static final String TAG = "CommentsAdapter";

    public CommentsAdapter(Context mContext) {
        super(mContext, null);
    }

    public CommentsAdapter(Context mContext, List<Comment> mListData) {
        super(mContext, mListData);
    }

    @Override
    protected void onBindItemDataToView(CommentsHolder holder, Comment itemData) {
        holder.content.setText(itemData.content);
        holder.createdAt.setText(itemData.createdAt.toString());
        holder.creatorName.setText(String.valueOf(itemData.creatorName));
        if (itemData.creatorAvatarUri != null && !TextUtils.isEmpty(itemData.creatorAvatarUri))
            ImageLoader.getInstance().displayImage(Config.REMOTE_DIR + itemData.creatorAvatarUri, holder.avatar);
    }

    @Override
    public CommentsHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new CommentsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments, parent, false));
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
