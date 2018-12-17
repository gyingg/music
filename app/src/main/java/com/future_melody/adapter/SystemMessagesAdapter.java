package com.future_melody.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.net.respone.MyInformRespone1;
import com.future_melody.net.respone.MyInformRespone2;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Y on 2018/5/18.
 */

public class SystemMessagesAdapter extends RecyclerView.Adapter<SystemMessagesAdapter.ViewHolder>  {

    private Context context;
    private List<MyInformRespone1> list;

    public SystemMessagesAdapter(Context context, List<MyInformRespone1> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public SystemMessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_s_m, null);
        SystemMessagesAdapter.ViewHolder viewHolder = new SystemMessagesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SystemMessagesAdapter.ViewHolder holder, int position) {
        RequestOptions RequestOptions=new RequestOptions();
        RequestOptions.placeholder(R.mipmap.moren);
        Glide.with(context).load(list.get(position).getFromUserHeadUrl()).apply(RequestOptions).into(holder.cir_img);
        holder.tv_name.setText(list.get(position).getFromNickname());
        holder.tv_time.setText(list.get(position).getCreateTime());
        holder.tv_cont.setText(list.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView cir_img;
        private final TextView tv_name;
        private final TextView tv_time;
        private final TextView tv_cont;

        public ViewHolder(View itemView) {
            super(itemView);
            cir_img = itemView.findViewById(R.id.cir_img);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_cont = itemView.findViewById(R.id.tv_cont);

        }
    }
}
