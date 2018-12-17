package com.future_melody.adapter.xiaowei;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.activity.xiaowei.NoirPerleActivity;
import com.future_melody.mode.XiaoWeiInfo;

import java.util.List;


public class ProfitAdapter extends RecyclerView.Adapter<ProfitAdapter.ViewHolder> {

    private Context context;
    private List<XiaoWeiInfo> trackList;

    public ProfitAdapter(Context context, List<XiaoWeiInfo> trackList) {
        this.context = context;
        this.trackList = trackList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_profit_xiaowei, null);
        ProfitAdapter.ViewHolder viewHolder = new ProfitAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        XiaoWeiInfo info = trackList.get(position);
        holder.noir_item_name.setText(info.deviceName);
        if (info.type == 1) {
            holder.noir_item_type.setText("听星歌奖励");
        } else {
            holder.noir_item_type.setText("其他奖励");
        }
        holder.noir_item_time.setText(info.createTimeStr);
        holder.noir_item_number.setText(info.incomeBp + "黑珍珠");
        if (relieveClickListener!=null){
            holder.btn_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relieveClickListener.relieve(position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView noir_item_name;
        private TextView noir_item_type;
        private TextView noir_item_time;
        private TextView noir_item_number;
        private RelativeLayout btn_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            noir_item_name = itemView.findViewById(R.id.noir_item_name);
            noir_item_type = itemView.findViewById(R.id.noir_item_type);
            noir_item_time = itemView.findViewById(R.id.noir_item_time);
            noir_item_number = itemView.findViewById(R.id.noir_item_number);
            btn_layout = itemView.findViewById(R.id.btn_layout);
        }
    }

    private RelieveClickListener relieveClickListener;

    public interface RelieveClickListener {
        void relieve(int i);
    }

    public void setRelieveClickListener(RelieveClickListener relieveClickListener) {
        this.relieveClickListener = relieveClickListener;
    }
}
