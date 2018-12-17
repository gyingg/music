package com.future_melody.adapter.xiaowei;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.activity.xiaowei.XiaoWeiPlayerActivity;
import com.future_melody.net.respone.MineXiaoWeiListRespone;

import java.util.List;


public class RelevanceXiaoWeiAdapter extends RecyclerView.Adapter<RelevanceXiaoWeiAdapter.ViewHolder> {

    private Context context;
    private List<MineXiaoWeiListRespone> respones;

    public RelevanceXiaoWeiAdapter(Context context, List<MineXiaoWeiListRespone> mineXiaoWeiListRespones) {
        this.context = context;
        this.respones = mineXiaoWeiListRespones;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_relevance_xiaowei, null);
        RelevanceXiaoWeiAdapter.ViewHolder viewHolder = new RelevanceXiaoWeiAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MineXiaoWeiListRespone respone = respones.get(position);
        holder.hei_money_num.setText(respone.bp + " 黑珍珠");
        holder.text_xiaowei_name.setText(respone.deviceName + "");
        if (relieveClickListener != null) {
            holder.btn_relieve_xiaowei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relieveClickListener.relieve(position);
                }
            });
        }
        if (resetClickListener != null) {
            holder.btn_reset_xiaowei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetClickListener.relieve(position);
                }
            });
        }
        holder.btn_relieve_xiaowei_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, XiaoWeiPlayerActivity.class);
                intent.putExtra("deviceId" ,respone.snCode);
                intent.putExtra("deviceName" ,respone.deviceName);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return respones.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView btn_relieve_xiaowei;
        private TextView hei_money_num;
        private TextView btn_reset_xiaowei;
        private TextView text_xiaowei_name;
        private TextView btn_relieve_xiaowei_type;

        public ViewHolder(View itemView) {
            super(itemView);
            btn_relieve_xiaowei = itemView.findViewById(R.id.btn_relieve_xiaowei);
            hei_money_num = itemView.findViewById(R.id.hei_money_num);
            btn_reset_xiaowei = itemView.findViewById(R.id.btn_reset_xiaowei);
            text_xiaowei_name = itemView.findViewById(R.id.text_xiaowei_name);
            btn_relieve_xiaowei_type = itemView.findViewById(R.id.btn_relieve_xiaowei_type);
        }
    }

    private RelieveClickListener relieveClickListener;
    private RelieveClickListener resetClickListener;

    public interface RelieveClickListener {
        void relieve(int i);
    }

    public void setRelieveClickListener(RelieveClickListener relieveClickListener) {
        this.relieveClickListener = relieveClickListener;
    }

    public void setResetClickListener(RelieveClickListener relieveClickListener) {
        this.resetClickListener = relieveClickListener;
    }
}
