package com.future_melody.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.future_melody.R;
import com.future_melody.net.respone.GetMusicLeaderRespone;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author WZL
 * Date：2018/5/16 36
 * Notes:
 */
public class TopDetailsMusicAdapter1 extends BaseAdapter {
    private Context mContext;
    private List<GetMusicLeaderRespone> lists;

    private ArrayList<SongInfo> songInfos;

    public TopDetailsMusicAdapter1(Context mContext, List<GetMusicLeaderRespone> lists, ArrayList<SongInfo> songInfos) {
        this.mContext = mContext;
        this.lists = lists;
        this.songInfos = songInfos;
    }


    @Override
    public int getCount() {
        return lists.size();
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
            view = View.inflate(mContext, R.layout.item_top_details_music, null);
            holder.top_music_num = view.findViewById(R.id.top_music_num);
            holder.top_music_img = view.findViewById(R.id.top_music_img);
            holder.star_music_song = view.findViewById(R.id.star_music_song);
            holder.star_music_sing = view.findViewById(R.id.star_music_sing);
            holder.top_music_zan_num = view.findViewById(R.id.top_music_zan_num);
            holder.gif_player = view.findViewById(R.id.gif_player);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        GetMusicLeaderRespone model = lists.get(i);
        Glide.with(mContext).load(model.coverUrl).into(holder.top_music_img);
        String string = (i + 1) + "";
        if (string.length() > 1) {
            holder.top_music_num.setText(string + "");
        } else {
            holder.top_music_num.setText("0" + string);
        }
        if (model.isCollection == 0) {
            holder.top_music_zan_num.setTextColor(mContext.getResources().getColor(R.color.color_999999));
        } else {
            holder.top_music_zan_num.setTextColor(mContext.getResources().getColor(R.color.F5A623));
        }
        holder.star_music_song.setText(model.musicName + " - " + model.singerName);
        holder.star_music_sing.setText(model.source);  //,,,
        holder.top_music_zan_num.setText(model.likeCount + "次点赞");
        SongInfo songInfo = songInfos.get(i);
        if (MusicManager.isCurrMusicIsPlayingMusic(songInfo)) {
            holder.gif_player.setVisibility(View.VISIBLE);
            holder.top_music_num.setVisibility(View.GONE);
            if (MusicManager.isPlaying()) {
                Glide.with(mContext).load(R.mipmap.gif_player).into(holder.gif_player);
            } else {
                holder.gif_player.setImageResource(R.mipmap.icon_player_gig_stop);
            }
        } else {
            holder.gif_player.setVisibility(View.GONE);
            holder.top_music_num.setVisibility(View.VISIBLE);
        }
        return view;
    }


    public static class ViewHolder {
        private TextView top_music_num;
        private RoundedImageView top_music_img;
        private TextView star_music_song;
        private TextView star_music_sing;
        private TextView top_music_zan_num;
        private ImageView gif_player;
    }
}

