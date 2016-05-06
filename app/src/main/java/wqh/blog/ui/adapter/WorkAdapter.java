package wqh.blog.ui.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wqh.blog.R;
import wqh.blog.ui.adapter.base.BaseAdapter;
import wqh.blog.model.bean.Work;

/**
 * Created by WQH on 2016/4/11  23:43.
 * <p>
 * Adapter for list of Work
 */
public class WorkAdapter extends BaseAdapter<WorkAdapter.WorkHolder, Work> {

    public WorkAdapter(Context mContext, List<Work> mListData) {
        super(mContext, mListData);
    }

    public WorkAdapter(Context mContext) {
        super(mContext, null);
    }

    @Override
    protected void onBindItemDataToView(WorkHolder holder, Work itemData) {
        holder.title.setText(itemData.title);
        holder.description.setText(itemData.description);
    }

    @Override
    public WorkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WorkHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work, parent, false));
    }

    static class WorkHolder extends BaseAdapter.BaseHolder {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.description)
        TextView description;
        @Bind(R.id.logo)
        ImageView logo;

        public WorkHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
