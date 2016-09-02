package wqh.blog.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import wqh.blog.R;
import wqh.blog.mvp.model.bean.Download;
import wqh.blog.ui.adapter.base.AdapterProvider;
import wqh.blog.ui.adapter.base.BaseViewHolder;

/**
 * Created by WQH on 2016/5/27  23:50.
 */
public class DownLoadAdapter extends AdapterProvider<DownLoadAdapter.DownLoadHolder, Download> {

    public DownLoadAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public DownLoadHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new DownLoadHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download, parent, false));
    }

    @Override
    public void onBindItemDataToView(DownLoadHolder holder, Download itemData) {
        holder.fileName.setText(itemData.title);
        holder.fileStatus.setText(Download.convert(itemData.status));
    }

    public static class DownLoadHolder extends BaseViewHolder {
        @Bind(R.id.download_name)
        TextView fileName;
        @Bind(R.id.download_status)
        TextView fileStatus;
        @Bind(R.id.download_progress)
        TextView fileProgress;

        public DownLoadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
