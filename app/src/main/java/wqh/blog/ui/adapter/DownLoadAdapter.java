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
import wqh.blog.mvp.model.bean.DownLoadBean;
import wqh.blog.ui.adapter.base.BaseAdapter;

/**
 * Created by WQH on 2016/5/27  23:50.
 */
public class DownLoadAdapter extends BaseAdapter<DownLoadAdapter.DownLoadHolder, DownLoadBean> {

    public DownLoadAdapter(Context mContext, List<DownLoadBean> mListData) {
        super(mContext, mListData);
    }

    @Override
    protected void onBindItemDataToView(DownLoadHolder holder, DownLoadBean itemData) {
        holder.fileName.setText(itemData.title);
        holder.fileStatus.setText(DownLoadBean.convert(itemData.status));
    }

    @Override
    protected DownLoadHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new DownLoadHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download, parent, false));
    }

    public static class DownLoadHolder extends BaseAdapter.BaseHolder {
        @Bind(R.id.fileName)
        TextView fileName;
        @Bind(R.id.fileStatus)
        TextView fileStatus;

        public DownLoadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
