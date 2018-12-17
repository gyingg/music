package com.future_melody.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.future_melody.R;
import com.future_melody.net.respone.BlackPearRespone;

import java.util.List;

/**
 * Created by Y on 2018/9/5.
 */

public class BlackPearAdapyer extends BaseAdapter {

    private Context mContext;
    private List<BlackPearRespone> list;
    private BlackPearRespone respone;

    public BlackPearAdapyer(Context mContext, List<BlackPearRespone> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHodler holder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_decibel_list, null);
            holder = new ViewHodler();
            holder.decibel_forme = view.findViewById(R.id.decibel_forme);
            holder.decibel_time = view.findViewById(R.id.decibel_time);
            holder.decibel_num = view.findViewById(R.id.decibel_num);
            view.setTag(holder);
        } else {
            holder = (ViewHodler) view.getTag();
        }
        respone = list.get(i);
        holder.decibel_forme.setText(respone.xinxi + "");
        holder.decibel_time.setText(respone.date_time + "");
        holder.decibel_num.setText(respone.codeing + "");
        return view;
    }

    public static class ViewHodler {
        private TextView decibel_forme;
        private TextView decibel_time;
        private TextView decibel_num;
    }
}
