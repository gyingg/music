package com.future_melody.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.net.respone.MainActivityFragmentRespone;
import com.future_melody.utils.DateUtil;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Y on 2018/8/24.
 */

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {

    private Context context;
    private List<MainActivityFragmentRespone> list;

    public MainActivityAdapter(Context context, List<MainActivityFragmentRespone> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_mainadapter, null);
        MainActivityAdapter.ViewHolder viewHolder = new MainActivityAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestOptions RequestOption = new RequestOptions();
        RequestOption.placeholder(R.mipmap.moren);
        Glide.with(context)
                .load(list.get(position).activeCoverUrl)
                .apply(RequestOption)
                .into(holder.img_picture);
        holder.tv_title.setText(list.get(position).activeName);
        if (list.get(position).isOver == 0) {
            holder.tv_time.setVisibility(View.VISIBLE);
            holder.end_time.setVisibility(View.GONE);
            holder.tv_view.setVisibility(View.VISIBLE);
            holder.tv_time.setText("活动时间" +" : "+ list.get(position).startTimeStr + "-" + list.get(position).endTimeStr);
        } else {
            holder.tv_time.setVisibility(View.GONE);
            holder.end_time.setVisibility(View.VISIBLE);
            holder.tv_view.setVisibility(View.GONE);
            holder.end_time.setText("已结束");
        }
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
        return list == null ? 0 : list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_time;
        private final TextView end_time;
        private final TextView tv_title;
        private final ImageView img_picture;
        private final View tv_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_view = itemView.findViewById(R.id.tv_view);
            img_picture = itemView.findViewById(R.id.img_picture);
            tv_time = itemView.findViewById(R.id.tv_time);
            end_time = itemView.findViewById(R.id.end_time);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }

}
