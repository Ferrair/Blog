package wqh.blog.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wqh.blog.R;
import wqh.blog.mvp.model.bean.Download;
import wqh.blog.ui.adapter.base.BaseAdapter;

/**
 * Created by WQH on 2016/5/27  23:50.
 */
public class DownLoadAdapter extends BaseAdapter<DownLoadAdapter.DownLoadHolder, Download> {

    public DownLoadAdapter(Context mContext, List<Download> mListData) {
        super(mContext, mListData);
    }

    public DownLoadAdapter(Context mContext) {
        super(mContext, null);
    }

    @Override
    protected void onBindItemDataToView(DownLoadHolder holder, Download itemData) {
        holder.fileName.setText(itemData.title);
        holder.fileStatus.setText(Download.convert(itemData.status));
    }

    @Override
    protected DownLoadHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new DownLoadHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download, parent, false));
    }

    public static class DownLoadHolder extends BaseAdapter.BaseHolder {
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
