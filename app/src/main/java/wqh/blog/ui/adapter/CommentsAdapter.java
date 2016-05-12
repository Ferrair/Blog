package wqh.blog.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wqh.blog.R;
import wqh.blog.mvp.model.bean.Comment;
import wqh.blog.ui.adapter.base.BaseAdapter;

/**
 * Created by WQH on 2016/5/2  20:43.
 */
public class CommentsAdapter extends BaseAdapter<CommentsAdapter.CommentsHolder, Comment> {

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
        holder.createdBy.setText(itemData.createdBy);
    }

    @Override
    public CommentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments, parent, false));
    }

    public static class CommentsHolder extends BaseAdapter.BaseHolder {
        @Bind(R.id.createdAt)
        TextView createdAt;
        @Bind(R.id.createdBy)
        TextView createdBy;
        @Bind(R.id.content)
        TextView content;

        public CommentsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
