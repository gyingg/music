package com.future_melody.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.mode.LocalMusicModel;
import com.future_melody.utils.LocalMusicUtils;
import com.future_melody.utils.LogUtil;

import java.util.List;

/**
 * Author WZL
 * Date：2018/5/16 36
 * Notes:
 */
public class LocalMusicAdapter extends BaseAdapter {
    private Context mContext;
    private List<LocalMusicModel> musicModels;

    public LocalMusicAdapter(Context mContext, List<LocalMusicModel> musicModels) {
        this.mContext = mContext;
        this.musicModels = musicModels;
    }

    @Override
    public int getCount() {
        return musicModels.size();
    }

    public void updateList(List<LocalMusicModel> videos) {
        this.musicModels = videos;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.item_local_music, null);
            holder.local_song_name = view.findViewById(R.id.local_song_name);
            holder.local_song_info = view.findViewById(R.id.local_song_info);
            holder.local_song_time = view.findViewById(R.id.local_song_time);
            holder.local_btn_player = view.findViewById(R.id.local_btn_player);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        LocalMusicModel musicModel = musicModels.get(i);
        holder.local_song_name.setText(musicModel.getName());
        if (musicModel.getSinger() != null) {
            holder.local_song_info.setText(musicModel.getSinger());
        }
        holder.local_song_time.setText(LocalMusicUtils.formatTime(musicModel.getDuration()));
        LogUtil.e("地址", musicModel.getUrl() + "");
        return view;
    }

    public static class ViewHolder {
        private TextView local_song_name;
        private TextView local_song_info;
        private TextView local_song_time;
        private ImageView local_btn_player;
    }
}

