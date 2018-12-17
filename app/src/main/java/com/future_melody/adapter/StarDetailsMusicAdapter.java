package com.future_melody.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.music.PlayerActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.respone.PlanetMusicResone;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.manager.MusicManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Author WZL
 * Date：2018/5/14 37
 * Notes: 星球详情：推荐音乐
 */
public class StarDetailsMusicAdapter extends RecyclerView.Adapter<StarDetailsMusicAdapter.StarDetailsMusicHodler> {
    private Context mContext;
    private List<PlanetMusicResone> list;
    private ArrayList<SongInfo> songInfos;

    public StarDetailsMusicAdapter(Context mContext, List<PlanetMusicResone> list, ArrayList<SongInfo> songInfos) {
        this.mContext = mContext;
        this.list = list;
        this.songInfos = songInfos;
    }

    @Override
    public StarDetailsMusicHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_star_details_music, null);
        return new StarDetailsMusicAdapter.StarDetailsMusicHodler(view);
    }

    @Override
    public void onBindViewHolder(StarDetailsMusicHodler holder, int position) {
        final PlanetMusicResone musicResone = list.get(position);
        holder.star_music_song.setText(musicResone.musicName);
        holder.star_music_singer.setText(musicResone.singerName);
        holder.star_music_sing.setText("来自" + musicResone.nickname + "的推荐");
        holder.star_music_zan_num.setText(musicResone.likeCount);
        if (musicResone.isLike == 0) {
            holder.star_music_zan.setImageResource(R.mipmap.icon_star_music_unzan);
        } else {
            holder.star_music_zan.setImageResource(R.mipmap.icon_star_music_zan);
        }
        holder.star_music_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点赞
                if (musicResone.isLike == 0) {
                    holder.star_music_zan.setImageResource(R.mipmap.icon_star_music_zan);
                    musicResone.isLike = 1;
                    attention.attention(true, position);
                    holder.star_music_zan_num.setText((Integer.valueOf(musicResone.likeCount) + 1) + "");
                } else {
                    holder.star_music_zan.setImageResource(R.mipmap.icon_star_music_unzan);
                    musicResone.isLike = 0;
                    attention.attention(false, position);
                    holder.star_music_zan_num.setText((Integer.valueOf(musicResone.likeCount) - 1) + "");
                    holder.star_music_zan_num.setText(musicResone.likeCount);
                }
            }
        });
        String string = (position + 1) + "";
        if (string.length() > 1) {
            holder.star_music_num.setText(string + "");
        } else {
            holder.star_music_num.setText("0" + string);
        }
        RequestOptions options = new RequestOptions();
        options.centerCrop()
                .placeholder(R.mipmap.moren)
                .error(R.mipmap.moren)
                .fallback(R.mipmap.moren);
        Glide.with(mContext)
                .load(musicResone.coverUrl)
                .apply(options)
                .into(holder.star_music_img);
        SongInfo songInfo = songInfos.get(position);
        if (MusicManager.isCurrMusicIsPlayingMusic(songInfo)) {
            holder.gif_player.setVisibility(View.VISIBLE);
            holder.star_music_num.setVisibility(View.GONE);
            if (MusicManager.isPlaying()) {
                Glide.with(mContext).load(R.mipmap.gif_player).into(holder.gif_player);
            } else {
                holder.gif_player.setImageResource(R.mipmap.icon_player_gig_stop);
            }
        } else {
            holder.gif_player.setVisibility(View.GONE);
            holder.star_music_num.setVisibility(View.VISIBLE);
        }
        holder.item_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MusicManager.isCurrMusicIsPlayingMusic(songInfo)) {
                    PlayerUitlis.player(mContext);
                } else {
                    MusicManager.get().playMusic(songInfos, position);
                    PlayerActivity.launch(mContext, songInfos, position);
                }
            }
        });
    }

    //关注的监听
    private TodaySuperuAdapter.attention attention;

    public interface attention {
        void attention(boolean b, int i);
    }

    public void setAttention(TodaySuperuAdapter.attention attention) {
        this.attention = attention;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class StarDetailsMusicHodler extends RecyclerView.ViewHolder {
        private TextView star_music_num;
        private TextView star_music_song;
        private TextView star_music_sing;
        private TextView star_music_zan_num;
        private TextView star_music_singer;
        private ImageView star_music_img;
        private ImageView star_music_zan;
        private ImageView gif_player;
        private RelativeLayout item_btn;

        public StarDetailsMusicHodler(View itemView) {
            super(itemView);
            star_music_num = itemView.findViewById(R.id.star_music_num);
            star_music_song = itemView.findViewById(R.id.star_music_song);
            star_music_sing = itemView.findViewById(R.id.star_music_sing);
            star_music_zan_num = itemView.findViewById(R.id.star_music_zan_num);
            star_music_img = itemView.findViewById(R.id.star_music_img);
            star_music_zan = itemView.findViewById(R.id.star_music_zan);
            item_btn = itemView.findViewById(R.id.item_btn);
            gif_player = itemView.findViewById(R.id.gif_player);
            star_music_singer = itemView.findViewById(R.id.star_music_singer);
        }
    }
}
