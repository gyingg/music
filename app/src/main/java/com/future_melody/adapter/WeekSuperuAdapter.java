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
import com.future_melody.net.respone.WeekSuperuRespone;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Y on 2018/5/15.
 */

public class WeekSuperuAdapter extends RecyclerView.Adapter<WeekSuperuAdapter.ViewHolder> {

    private Context context;
    private List<WeekSuperuRespone> list;

    public WeekSuperuAdapter(Context context, List<WeekSuperuRespone> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_weeksuperu, null);
        WeekSuperuAdapter.ViewHolder viewHolder = new WeekSuperuAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RequestOptions RequestOption = new RequestOptions();
        RequestOption.placeholder(R.mipmap.moren);
        Glide.with(context)
                .load(list.get(position).headUrl)
                .apply(RequestOption)
                .into(holder.week_circle_icon);
        holder.week_name.setText(list.get(position).nickname);
        holder.week_lighten.setText("被点亮" + list.get(position).likeCount + "次");
        String string = (position + 1) + "";
        if (string.length() > 1) {
            holder.week_num.setText(string + "");
        } else {
            holder.week_num.setText("0" + string);
        }
        if (list.get(position).isAttention.equals("0"))
        {
            holder.week_attention.setText("+关注");
            holder.week_attention.setTextColor(context.getResources().getColor(R.color.white));
            holder.week_attention.setBackgroundResource(R.mipmap.white_back_daren);
        }else{
            holder.week_attention.setText("已关注");
            holder.week_attention.setTextColor(context.getResources().getColor(R.color.back));
            holder.week_attention.setBackgroundResource(R.mipmap.full_white_back_daren);
        }
        holder.week_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).isAttention.equals("0"))
                {
                    holder.week_attention.setText("已关注");
                    holder.week_attention.setTextColor(context.getResources().getColor(R.color.back));
                    holder.week_attention.setBackgroundResource(R.mipmap.full_white_back_daren);
                    list.get(position).isAttention="1";
                    //这里接着做请求接口  关注接口
                    attention.attention(true,position);
                }
                else{
                    holder.week_attention.setText("+关注");
                    holder.week_attention.setTextColor(context.getResources().getColor(R.color.white));
                    holder.week_attention.setBackgroundResource(R.mipmap.white_back_daren);
                    list.get(position).isAttention="0";
                    //这里接着做请求接口  取消关注
                    attention.attention(false,position);
                }
            }
        });
    }

    //关注的监听
    private attention attention;
    public interface attention{
        void attention(boolean b,int i);
    }
    public void setAttention(attention attention) {
        this.attention = attention;
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView week_num;
        private final CircleImageView week_circle_icon;
        private final TextView week_name;
        private final TextView week_lighten;
        private final TextView week_attention;

        public ViewHolder(View itemView) {
            super(itemView);
            week_num = itemView.findViewById(R.id.week_num);
            week_circle_icon = itemView.findViewById(R.id.week_circle_icon);
            week_name = itemView.findViewById(R.id.week_name);
            week_lighten = itemView.findViewById(R.id.week_lighten);
            week_attention = itemView.findViewById(R.id.week_attention);
        }
    }
}
