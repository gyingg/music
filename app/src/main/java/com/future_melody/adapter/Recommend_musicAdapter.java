package com.future_melody.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.mode.Recommend_Music_Bean;
import com.future_melody.mode.Recommend_Theme_Bean;
import com.future_melody.music.PlayerActivity;
import com.future_melody.music.PlayerNewActivity;
import com.future_melody.music.PlayerUitlis;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Y on 2018/5/24.
 */

public class Recommend_musicAdapter extends RecyclerView.Adapter<Recommend_musicAdapter.ViewHolder> {

    private Context context;
    private List<Recommend_Music_Bean> list;
    private ThemeClickListener themeClicklistener;
    private ThemeClickListener itemThemeClicklistener;
    private ArrayList<SongInfo> songInfos;

    public Recommend_musicAdapter(Context context, List<Recommend_Music_Bean> list, ArrayList<SongInfo> songInfos) {
        this.context = context;
        this.list = list;
        this.songInfos = songInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_musicfragment, null);
        Recommend_musicAdapter.ViewHolder viewHolder = new Recommend_musicAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recommend_Music_Bean recommend_music_bean = list.get(position);
        RequestOptions RequestOption = new RequestOptions();
        RequestOption.placeholder(R.mipmap.moren);
        Glide.with(context)
                .load(recommend_music_bean.coverUrl)
                .apply(RequestOption)
                .into(holder.like_music_img);
        // holder.tv_count.setText("已有" + recommend_music_bean.listenCount + "人听过");
        holder.tv_count.setText(recommend_music_bean.singerName);
        holder.tv_name.setText(recommend_music_bean.musicName);
        String string = (position + 1) + "";
        if (string.length() > 1) {
            holder.like_music_num.setText(string + "");
        } else {
            holder.like_music_num.setText("0" + string);
        }
        //holder.like_music_num.setText(recommend_music_bean.no + "");
        if (recommend_music_bean.isLike == 0) {
            holder.img_xing.setImageResource(R.mipmap.icon_star_music_unzan);
        } else {
            holder.img_xing.setImageResource(R.mipmap.icon_star_music_zan);
        }
        if (themeClicklistener != null) {
            holder.img_xing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    themeClicklistener.GetInfo(position);
                }
            });
        }

        SongInfo songInfo = songInfos.get(position);
        if (MusicManager.isCurrMusicIsPlayingMusic(songInfo)) {
            holder.gif_player.setVisibility(View.VISIBLE);
            holder.like_music_num.setVisibility(View.GONE);
            if (MusicManager.isPlaying()) {
                Glide.with(context).load(R.mipmap.gif_player).into(holder.gif_player);
            } else {
                holder.gif_player.setImageResource(R.mipmap.icon_player_gig_stop);
            }
        } else {
            holder.gif_player.setVisibility(View.GONE);
            holder.like_music_num.setVisibility(View.VISIBLE);
        }
        if (itemThemeClicklistener != null) {
            holder.btn_toplayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemThemeClicklistener.GetInfo(position);
                }
            });
        }
       /* if (holder.like_music_num != null) {
             holder.singer_name.setText("—  " + recommend_music_bean.singerName );
        }*/
    }

    public interface ThemeClickListener {
        void GetInfo(int i);
    }

    public void setThemeClickListener(ThemeClickListener themeClickListener) {
        this.themeClicklistener = themeClickListener;
    }

    public void setItemClickListener(ThemeClickListener themeClickListener) {
        this.itemThemeClicklistener = themeClickListener;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final RoundedImageView like_music_img;
        private final TextView tv_name;
        private final TextView tv_count;
        private final TextView like_music_num;
        private final TextView singer_name;
        private final ImageView img_xing;
        private final RelativeLayout btn_toplayer;
        private ImageView gif_player;

        public ViewHolder(View itemView) {
            super(itemView);
            like_music_img = itemView.findViewById(R.id.like_music_img);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_count = itemView.findViewById(R.id.tv_count);
            singer_name = itemView.findViewById(R.id.singer_name);
            like_music_num = itemView.findViewById(R.id.like_music_num);
            img_xing = itemView.findViewById(R.id.img_xing);
            btn_toplayer = itemView.findViewById(R.id.btn_toplayer);
            gif_player = itemView.findViewById(R.id.gif_player);
        }
    }
}
