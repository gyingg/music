package com.future_melody.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.future_melody.R;
import com.future_melody.mode.RemendMusicNewModle;
import com.future_melody.net.respone.RemmendNewThemeRespone;
import com.future_melody.utils.LogUtil;
import com.lzx.musiclibrary.aidl.model.SongInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Y on 2018/5/16.
 */

public class RecommendThemeListAdapter extends RecyclerView.Adapter<RecommendThemeListAdapter.ViewHolder> {

    private Context context;
    private List<RemmendNewThemeRespone> list;
    private ArrayList<SongInfo> songInfos = new ArrayList<>();
    private ThemeClickListener shareClickListener;
    private ThemeClickListener commendClickListener;
    private ThemeClickListener pickClickListener;

    public RecommendThemeListAdapter(Context context, List<RemmendNewThemeRespone> list) {
        this.context = context;
        this.list = list;
    }

    public RecommendThemeListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommend_thme_list, parent, false);
        RecommendThemeListAdapter.ViewHolder viewHolder = new RecommendThemeListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LogUtil.e("推荐页：", "下标：" + position);
        RemmendNewThemeRespone respone = list.get(position);
        if (respone.musicList.size()>0){
            RemendMusicNewModle musicModel = respone.musicList.get(0);
            Glide.with(context).load(musicModel.coverUrl).into(holder.music_cover);
            holder.theme_one_music_name.setText(musicModel.musicName);
            holder.theme_one_music_sing.setText(musicModel.singerName);
        }
        holder.zan_num.setText(respone.likeCount + "");
        holder.share_num.setText(respone.shareCount + "");
        holder.commend_num.setText(respone.commentCount + "");
        if (respone.isLike == 0) {
            holder.theme_one_music_zan.setImageResource(R.mipmap.icon_theme_details_unzan);
        } else {
            holder.theme_one_music_zan.setImageResource(R.mipmap.icon_theme_details_zan);
        }
        Glide.with(context).load(respone.special.showPicture).into(holder.layout_btn_bg);
        if (shareClickListener != null) {
            holder.layout_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareClickListener.set(position);
                }
            });
        }
        if (commendClickListener != null) {
            holder.layout_commend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commendClickListener.set(position);
                }
            });
        }
        if (pickClickListener != null) {
            holder.layout_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pickClickListener.set(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView layout_btn_bg;
        private ImageView music_cover;
        private TextView theme_one_music_name;
        private TextView theme_one_music_sing;
        private ImageView theme_one_music_other;
        private ImageView music_player_type;
        private RelativeLayout layout_bottom;
        private LinearLayout layout_commend;
        private LinearLayout layout_share;
        private LinearLayout layout_zan;
        private ImageView theme_one_music_zan;
        private TextView zan_num;
        private TextView share_num;
        private TextView commend_num;

        public ViewHolder(View view) {
            super(view);
            layout_btn_bg = view.findViewById(R.id.layout_btn_bg);
            music_cover = view.findViewById(R.id.music_cover);
            theme_one_music_name = view.findViewById(R.id.theme_one_music_name);
            theme_one_music_sing = view.findViewById(R.id.theme_one_music_sing);
            theme_one_music_other = view.findViewById(R.id.theme_one_music_other);
            layout_bottom = view.findViewById(R.id.layout_bottom);
            music_player_type = view.findViewById(R.id.music_player_type);
            layout_commend = view.findViewById(R.id.layout_commend);
            layout_share = view.findViewById(R.id.layout_share);
            layout_zan = view.findViewById(R.id.layout_zan);
            theme_one_music_zan = view.findViewById(R.id.theme_one_music_zan);
            zan_num = view.findViewById(R.id.zan_num);
            share_num = view.findViewById(R.id.share_num);
            commend_num = view.findViewById(R.id.commend_num);
        }
    }

    public interface ThemeClickListener {
        void set(int i);
    }

    public void ShareClickListener(ThemeClickListener themeClickListener) {
        this.shareClickListener = themeClickListener;
    }

    public void PickClickListener(ThemeClickListener themeClickListener) {
        this.pickClickListener = themeClickListener;
    }

    public void CommendClickListener(ThemeClickListener themeClickListener) {
        this.commendClickListener = themeClickListener;
    }
}
