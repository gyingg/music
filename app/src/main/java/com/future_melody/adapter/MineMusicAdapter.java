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
import com.future_melody.net.respone.MineReconmendMusicRespone;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.manager.MusicManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Author WZL
 * Date：2018/5/14 37
 * Notes: 星球详情：推荐音乐
 */
public class MineMusicAdapter extends RecyclerView.Adapter<MineMusicAdapter.StarDetailsMusicHodler> {
    private Context mContext;
    private List<MineReconmendMusicRespone> list;
    private ArrayList<SongInfo> songInfos;

    public MineMusicAdapter(Context mContext, List<MineReconmendMusicRespone> list, ArrayList<SongInfo> songInfos) {
        this.mContext = mContext;
        this.songInfos = songInfos;
        this.list = list;
    }

    @Override
    public StarDetailsMusicHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_mine_music, null);
        return new MineMusicAdapter.StarDetailsMusicHodler(view);
    }

    @Override
    public void onBindViewHolder(StarDetailsMusicHodler holder, int position) {
        final MineReconmendMusicRespone musicResone = list.get(position);
        holder.star_music_song.setText(musicResone.music_name + "-" + musicResone.singer_name);
        holder.star_music_zan_num.setText("被点亮" + musicResone.musicnumber + "次");
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
                .load(musicResone.music_picture)
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
        holder.btn_toPlayer.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class StarDetailsMusicHodler extends RecyclerView.ViewHolder {
        private TextView star_music_num;
        private TextView star_music_song;
        private TextView star_music_zan_num;
        private ImageView star_music_img;
        private ImageView star_music_zan;
        private ImageView gif_player;
        private RelativeLayout btn_toPlayer;

        public StarDetailsMusicHodler(View itemView) {
            super(itemView);
            star_music_num = itemView.findViewById(R.id.star_music_num);
            star_music_song = itemView.findViewById(R.id.star_music_song);
            star_music_zan_num = itemView.findViewById(R.id.star_music_zan_num);
            star_music_img = itemView.findViewById(R.id.star_music_img);
            star_music_zan = itemView.findViewById(R.id.star_music_zan);
            btn_toPlayer = itemView.findViewById(R.id.btn_toPlayer);
            gif_player = itemView.findViewById(R.id.gif_player);
        }
    }
}
