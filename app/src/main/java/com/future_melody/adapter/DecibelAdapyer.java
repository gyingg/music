package com.future_melody.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.net.respone.DecibelRespone;

import java.util.List;

/**
 * Author WZL
 * Date：2018/6/12 28
 * Notes:
 */
public class DecibelAdapyer extends BaseAdapter {
    private Context mContext;
    private List<DecibelRespone> list;

    public DecibelAdapyer(Context mContext, List<DecibelRespone> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
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
        DecibelRespone respone = list.get(i);
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
