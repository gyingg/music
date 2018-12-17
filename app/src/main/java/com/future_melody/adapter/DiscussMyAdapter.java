package com.future_melody.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.net.respone.MyInformRespone2;
import com.umeng.debug.log.I;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Y on 2018/5/18.
 */

public class DiscussMyAdapter extends RecyclerView.Adapter<DiscussMyAdapter.ViewHolder> {

    private Context context;
    private List<MyInformRespone2> list;

    public DiscussMyAdapter(Context context, List<MyInformRespone2> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_d_m, null);
        DiscussMyAdapter.ViewHolder viewHolder = new DiscussMyAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RequestOptions RequestOptions = new RequestOptions();
        RequestOptions.placeholder(R.mipmap.moren);
        Glide.with(context).load(list.get(position).getFromUserHeadUrl()).apply(RequestOptions).into(holder.dis_icon);
        holder.tv_name.setText(list.get(position).getFromNickname());
        holder.tv_time.setText(list.get(position).getCreateTime());
        holder.tuijian_music.setText(list.get(position).getFromThingContent());
        holder.tuijian_zhuti.setText("我的推荐主题:" + list.get(position).getToThingContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemRec.itemRec(position);
            }
        });
    }


    //rec条目点击
    private itemRec itemRec;
    public interface itemRec {
        void itemRec(int i);
    }

    public void setItemRec(itemRec itemRec) {
        this.itemRec = itemRec;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView dis_icon;
        private final TextView tv_name;
        private final TextView tv_time;
        private final TextView tuijian_music;
        private final TextView tuijian_zhuti;

        public ViewHolder(View itemView) {
            super(itemView);
            dis_icon = itemView.findViewById(R.id.dis_icon);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tuijian_music = itemView.findViewById(R.id.tuijian_music);
            tuijian_zhuti = itemView.findViewById(R.id.tuijian_zhuti);

        }
    }
}
