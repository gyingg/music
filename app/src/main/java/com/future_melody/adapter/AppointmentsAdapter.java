package com.future_melody.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.future_melody.R;
import com.future_melody.net.respone.StarAppointmentRespone;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author WZL
 * Date：2018/5/14 37
 * Notes: 喜欢页面
 */
public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.AppointmentsViewHodler> {
    private Context mContext;
    private List<StarAppointmentRespone> list;
    private AddClickListener addClickListener;

    public AppointmentsAdapter(Context mContext, List<StarAppointmentRespone> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public AppointmentsViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_appointment, null);
        return new AppointmentsAdapter.AppointmentsViewHodler(view);
    }

    @Override
    public void onBindViewHolder(AppointmentsViewHodler holder, int position) {
        StarAppointmentRespone model = list.get(position);
        Glide.with(mContext).load(model.head_portrait).into(holder.appointment_user_img);
        holder.appointment_user_name.setText(model.nickname);
        holder.appointment_user_theme_num.setText("主题数:  " + model.shuliang);
        if (model.guardian_asteroid == 1) {
            holder.appointment_add.setBackgroundResource(R.drawable.appointment_add);
            holder.appointment_add.setText("已添加");
        } else {
            holder.appointment_add.setBackgroundResource(R.drawable.appointment_un_add);
            holder.appointment_add.setText("添加");
        }
        if (addClickListener != null) {
            holder.appointment_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addClickListener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class AppointmentsViewHodler extends RecyclerView.ViewHolder {

        private CircleImageView appointment_user_img;
        private TextView appointment_user_name;
        private TextView appointment_user_theme_num;
        private TextView appointment_add;

        public AppointmentsViewHodler(View itemView) {
            super(itemView);
            appointment_user_img = itemView.findViewById(R.id.appointment_user_img);
            appointment_user_name = itemView.findViewById(R.id.appointment_user_name);
            appointment_user_theme_num = itemView.findViewById(R.id.appointment_user_theme_num);
            appointment_add = itemView.findViewById(R.id.appointment_add);

        }
    }

    public static class ViewHolder {

    }

    public interface AddClickListener {
        void onClick(int i);
    }

    public void setAddClickListener(AddClickListener addClickListener) {
        this.addClickListener = addClickListener;
    }
}
