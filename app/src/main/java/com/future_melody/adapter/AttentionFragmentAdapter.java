package com.future_melody.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.net.respone.MineReconmendFollowRespone;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Y on 2018/9/18.
 */

public class AttentionFragmentAdapter extends RecyclerView.Adapter<AttentionFragmentAdapter.ViewHolder> {

    private Context context;
    private List<MineReconmendFollowRespone> list;

    public AttentionFragmentAdapter(Context context, List<MineReconmendFollowRespone> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public AttentionFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.give_decibel_item1, null);
        AttentionFragmentAdapter.ViewHolder viewHolder = new AttentionFragmentAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttentionFragmentAdapter.ViewHolder holder, int position) {
        RequestOptions RequestOptions=new RequestOptions();
        RequestOptions.placeholder(R.mipmap.moren);
        Glide.with(context).load(list.get(position).getHead_portrait()).apply(RequestOptions).into(holder.mine_circle_icon);
        holder.mine_name.setText(list.get(position).getNickname());
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

    public void setItemRec(AttentionFragmentAdapter.itemRec itemRec) {
        this.itemRec = itemRec;
    }

    @Override
    public int getItemCount() {
        return  list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView mine_circle_icon;
        private final TextView mine_name;
        public ViewHolder(View itemView) {
            super(itemView);
            mine_circle_icon = itemView.findViewById(R.id.mine_circle_icon);
            mine_name = itemView.findViewById(R.id.mine_name);
        }
    }
}
