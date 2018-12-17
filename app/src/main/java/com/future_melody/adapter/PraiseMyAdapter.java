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
import com.future_melody.net.respone.MyInformRespone3;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Y on 2018/5/18.
 */

public class PraiseMyAdapter  extends RecyclerView.Adapter<PraiseMyAdapter.ViewHolder>   {

    private Context context;
    private List<MyInformRespone3>  list;

    public PraiseMyAdapter(Context context, List<MyInformRespone3> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public PraiseMyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_p_m, null);
        PraiseMyAdapter.ViewHolder viewHolder = new PraiseMyAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PraiseMyAdapter.ViewHolder holder, int position) {
        RequestOptions RequestOptions=new RequestOptions();
        RequestOptions.placeholder(R.mipmap.moren);
        Glide.with(context).load(list.get(position).getFromUserHeadUrl()).apply(RequestOptions).into(holder.praise_cir);
        holder.praise_name.setText(list.get(position).getFromNickname());
        holder.praise_time.setText(list.get(position).getCreateTime());
        holder.praise_tuijian.setText("我的推荐音乐:"+list.get(position).getFromThingContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView praise_cir;
        private final TextView praise_name;
        private final TextView praise_time;
        private final TextView praise_tuijian;

        public ViewHolder(View itemView) {
            super(itemView);
            praise_cir = itemView.findViewById(R.id.praise_cir);
            praise_name = itemView.findViewById(R.id.praise_name);
            praise_time = itemView.findViewById(R.id.praise_time);
            praise_tuijian = itemView.findViewById(R.id.praise_tuijian);
        }
    }
}
