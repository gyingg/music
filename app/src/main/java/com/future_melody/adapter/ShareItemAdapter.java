package com.future_melody.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.bean.ShareItemBean;

import java.util.List;

/**
 */
public class ShareItemAdapter extends RecyclerView.Adapter<ShareItemAdapter.Holder> {

    private List<ShareItemBean> list;

    public void setList(List<ShareItemBean> list) {
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adtail_share_layout, parent, false);
        return new Holder(inflate);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ShareItemBean shareItemBean = list.get(position);
        holder.iv_item_share.setImageResource(shareItemBean.getImgId());
        holder.tv_item_share.setText(shareItemBean.getShareTv());
        holder.setOnItemClickListener(position);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        private final ImageView iv_item_share;
        private final TextView tv_item_share;

        public Holder(View itemView) {
            super(itemView);
            iv_item_share = itemView.findViewById(R.id.iv_item_share);
            tv_item_share = itemView.findViewById(R.id.tv_item_share);
        }
        public void setOnItemClickListener(int position){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.setOnItemClickListener(position,v);
                }
            });
        }
    }

    public interface OnItemClickListener{
        void setOnItemClickListener(int position, View v);
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
