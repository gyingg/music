package com.future_melody.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.mode.ThemeDetailsMusicBean;
import com.future_melody.music.PlayerActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.utils.LogUtil;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author WZL
 * Date：2018/5/10 57
 * Notes: 动态详情 推荐音乐列表
 */
public class ThemeDetailsMusicAdapter extends RecyclerView.Adapter<ThemeDetailsMusicAdapter.ThemeDetailsMusicViewHodler> {
    private Context context;
    private List<ThemeDetailsMusicBean> list;
    private ArrayList<SongInfo> songInfos;
    private MusicClickListener musicClicklistener;

    public ThemeDetailsMusicAdapter(Context context, List<ThemeDetailsMusicBean> list, ArrayList<SongInfo> songInfos) {
        if (context != null) {
            this.context = context;
            this.list = list;
            this.songInfos = songInfos;
        } else {
            throw new IllegalStateException("Context must nut be null");
        }
    }

    @Override
    public ThemeDetailsMusicViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme_music, null);
        return new ThemeDetailsMusicAdapter.ThemeDetailsMusicViewHodler(view);
    }

    @Override
    public void onBindViewHolder(ThemeDetailsMusicViewHodler holder, int position) {
        LogUtil.e("musicVoList2", list.size() + "");
        ThemeDetailsMusicBean bean = list.get(position);
        RequestOptions requestOptions=new RequestOptions();
        requestOptions.placeholder(R.mipmap.icon_user_touxiang);
        Glide.with(context).load(bean.coverUrl).apply(requestOptions).into(holder.like_music_img);
        holder.theme_music_song.setText(bean.musicName);
        holder.theme_music_sing.setText(bean.singerName + "");
        holder.details_music_zan_num.setText(bean.likeCount + "");
        if (bean.isLike == 0) {
            holder.details_music_zan.setImageResource(R.mipmap.icon_star_music_unzan);
            holder.details_music_zan_num.setTextColor(context.getResources().getColor(R.color.color_999999));
        } else {
            holder.details_music_zan.setImageResource(R.mipmap.icon_star_music_zan);
            holder.details_music_zan_num.setTextColor(context.getResources().getColor(R.color.F5A623));
        }
        SongInfo songInfo = songInfos.get(position);
        LogUtil.e("喜欢", songInfo.getTempInfo().getTemp_2() + "");
        if (MusicManager.isCurrMusicIsPlayingMusic(songInfo)) {
            holder.gif_player.setVisibility(View.VISIBLE);
            if (MusicManager.isPlaying()) {
                Glide.with(context).load(R.mipmap.gif_player).into(holder.gif_player);
            } else {
                holder.gif_player.setImageResource(R.mipmap.icon_player_gig_stop);
            }
        } else {
            holder.gif_player.setVisibility(View.GONE);
        }
        holder.btn_toplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MusicManager.isCurrMusicIsPlayingMusic(songInfo)) {
                    PlayerUitlis.player(context, songInfos);
                } else {
                    MusicManager.get().playMusic(songInfos, position);
                    PlayerActivity.launch(context, songInfos, position);
                }
            }
        });
        if (musicClicklistener != null) {
            holder.btn_layout_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    musicClicklistener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ThemeDetailsMusicViewHodler extends RecyclerView.ViewHolder {
        private RoundedImageView like_music_img;
        private ImageView details_music_zan;
        private ImageView gif_player;
        private TextView theme_music_song;
        private TextView theme_music_sing;
        private TextView details_music_zan_num;
        private FrameLayout btn_toplayer;
        private LinearLayout btn_layout_zan;

        public ThemeDetailsMusicViewHodler(View itemView) {
            super(itemView);
            like_music_img = itemView.findViewById(R.id.like_music_img);
            details_music_zan = itemView.findViewById(R.id.details_music_zan);
            theme_music_song = itemView.findViewById(R.id.theme_music_song);
            theme_music_sing = itemView.findViewById(R.id.theme_music_sing);
            details_music_zan_num = itemView.findViewById(R.id.details_music_zan_num);
            btn_toplayer = itemView.findViewById(R.id.btn_toplayer);
            btn_layout_zan = itemView.findViewById(R.id.btn_layout_zan);
            gif_player = itemView.findViewById(R.id.gif_player);
        }
    }

    public interface MusicClickListener {
        void onClick(int i);
    }

    public void setMusicClickListener(MusicClickListener musicClicklistener) {
        this.musicClicklistener = musicClicklistener;
    }
}
