package com.future_melody.adapter.xiaowei;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.net.respone.XiaoWeiPlayerRespone;

import java.util.List;


public class XiaoWeiPlayerAdapter extends RecyclerView.Adapter<XiaoWeiPlayerAdapter.ViewHolder> {

    private Context context;
    private List<XiaoWeiPlayerRespone> trackList;

    public XiaoWeiPlayerAdapter(Context context, List<XiaoWeiPlayerRespone> trackList) {
        this.context = context;
        this.trackList = trackList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_playerxiaowei, null);
        XiaoWeiPlayerAdapter.ViewHolder viewHolder = new XiaoWeiPlayerAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        XiaoWeiPlayerRespone info = trackList.get(position);
        holder.music_item_num.setText(info.no + "");
        if (info.isListen == 0) {
            holder.music_item_type.setText("未播完");
        } else {
            holder.music_item_type.setText("已播完");
        }
        holder.music_item_name.setText(info.musicName + "");
        if (relieveClickListener != null) {
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
        private TextView music_item_num;
        private TextView music_item_name;
        private TextView music_item_type;
        private RelativeLayout btn_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            music_item_num = itemView.findViewById(R.id.music_item_num);
            music_item_name = itemView.findViewById(R.id.music_item_name);
            music_item_type = itemView.findViewById(R.id.music_item_type);
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
