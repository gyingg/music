package com.future_melody.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.activity.UserInfoActivity;
import com.future_melody.net.respone.MineReconmendFansRespone;
import com.future_melody.net.respone.MineReconmendFollowRespone;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Y on 2018/5/17.
 * 我的粉丝
 */

public class MineRecommendFansAdapter  extends RecyclerView.Adapter<MineRecommendFansAdapter.ViewHolder> {

    private Context context;
    private List<MineReconmendFansRespone>  list;

    public MineRecommendFansAdapter(Context context, List<MineReconmendFansRespone> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MineRecommendFansAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_minerecommendfans, null);
        MineRecommendFansAdapter.ViewHolder viewHolder = new MineRecommendFansAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MineRecommendFansAdapter.ViewHolder holder, int position) {
        RequestOptions RequestOptions=new RequestOptions();
        RequestOptions.placeholder(R.mipmap.moren);
        Glide.with(context).load(list.get(position).getHead_portrait()).apply(RequestOptions).into(holder.mine_circle_icon);
        holder.mine_name.setText(list.get(position).getNickname());
        holder.mine_lighten.setText("来自"+list.get(position).getPlanet_name()+"的"+list.get(position).getAsteroid_name());
        holder.mine_attention.setText(list.get(position).getGuanzhu());
        holder.mine_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getGuanzhu().equals("关注")) {
                    list.get(position).setGuanzhu("互相关注");
                    //互相关注
                } else {
                    list.get(position).setGuanzhu("关注");
                    //关注
                }
                holder.mine_attention.setText(list.get(position).getGuanzhu());
                attention.attention(holder.mine_attention,position,list.get(position).getGuanzhu().equals("关注")?false:true);
            }
        });

        holder.mine_circle_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("userId", list.get(position).getUserid());
                context.startActivity(intent);
            }
        });
    }
    //关注的监听
    private attention attention;
    public interface attention{
        void attention(View view,int i,boolean isAttention);
    }
    public void setAttention(attention attention) {
        this.attention = attention;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView mine_circle_icon;
        private final TextView mine_name;
        private final TextView mine_lighten;
        private final TextView mine_attention;

        public ViewHolder(View itemView) {
            super(itemView);
            mine_circle_icon = itemView.findViewById(R.id.mine_circle_icon);
            mine_name = itemView.findViewById(R.id.mine_name);
            mine_lighten = itemView.findViewById(R.id.mine_lighten);
            mine_attention = itemView.findViewById(R.id.mine_attention);
        }
    }
}
