package wqh.blog.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import wqh.blog.R;
import wqh.blog.ui.adapter.base.AdapterProvider;
import wqh.blog.mvp.model.bean.Work;
import wqh.blog.ui.adapter.base.BaseViewHolder;

/**
 * Created by WQH on 2016/4/11  23:43.
 * <p>
 * Adapter for list of Work
 */
public class WorkAdapter extends AdapterProvider<WorkAdapter.WorkHolder, Work> {

    public WorkAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public WorkHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new WorkHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work, parent, false));
    }

    @Override
    public void onBindItemDataToView(WorkHolder holder, Work itemData) {
        holder.title.setText(itemData.title);
        holder.description.setText(itemData.description);
    }

    public static class WorkHolder extends BaseViewHolder {
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
