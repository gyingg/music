package com.future_melody.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.mode.XingTopModel;

import java.util.List;

/**
 * Author WZL
 * Date：2018/6/12 28
 * Notes:
 */
public class XingMusicTopNewAdapyer extends BaseAdapter {
    private Context mContext;
    private List<XingTopModel> list;

    public XingMusicTopNewAdapyer(Context mContext, List<XingTopModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list.size() > 4) {
            return 3;
        } else {
            return list.size();
        }

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
            view = View.inflate(mContext, R.layout.item_xingmusic_top_list, null);
            holder = new ViewHodler();
            holder.music_top_num = view.findViewById(R.id.music_top_num);
            holder.music_top_song_name = view.findViewById(R.id.music_top_song_name);
            view.setTag(holder);
        } else {
            holder = (ViewHodler) view.getTag();
        }
        XingTopModel respone = list.get(i);
        if (list.size() > 4) {
            if (i == 0) {
                holder.music_top_num.setTextColor(mContext.getResources().getColor(R.color.D0021B));
            } else if (i == 1) {
                holder.music_top_num.setTextColor(mContext.getResources().getColor(R.color.F5A623_1));
            } else if (i == 2) {
                holder.music_top_num.setTextColor(mContext.getResources().getColor(R.color.music01B0FE_1));
            }
        }
        if (list.size()>3){
            if (i == 0) {
                holder.music_top_num.setTextColor(mContext.getResources().getColor(R.color.D0021B));
            } else if (i == 1) {
                holder.music_top_num.setTextColor(mContext.getResources().getColor(R.color.F5A623_1));
            }
        }
        if (list.size()>2){
            if (i == 0) {
                holder.music_top_num.setTextColor(mContext.getResources().getColor(R.color.D0021B));
            }
        }
        holder.music_top_num.setText((i + 1) + "");
        holder.music_top_song_name.setText(respone.musicName + " - " + respone.singerName);
        return view;
    }

    public static class ViewHodler {
        private TextView music_top_num;
        private TextView music_top_song_name;
    }
}
