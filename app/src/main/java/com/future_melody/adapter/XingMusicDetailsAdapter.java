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
import com.future_melody.common.SPconst;
import com.future_melody.mode.XingTopModel;
import com.future_melody.music.PlayerNewActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.SPUtils;
import com.future_melody.widget.IsWifiDialog;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author WZL
 * Date：2018/5/15 24
 * Notes:
 */
public class XingMusicDetailsAdapter extends RecyclerView.Adapter<XingMusicDetailsAdapter.XingMusicViewHolder> {
    private Context mContext;
    private List<XingTopModel> lists;

    private ArrayList<SongInfo> songInfos;
    private ZanClickListener zanClickListener;
    private ZanClickListener itemClickListener;

    public XingMusicDetailsAdapter(Context mContext, List<XingTopModel> lists, ArrayList<SongInfo> songInfos) {
        this.mContext = mContext;
        this.lists = lists;
        this.songInfos = songInfos;
    }

    public XingMusicDetailsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public XingMusicDetailsAdapter.XingMusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_details_music, null);
        return new XingMusicDetailsAdapter.XingMusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(XingMusicDetailsAdapter.XingMusicViewHolder holder, int position) {
        XingTopModel musicModel = lists.get(position);
        String string = (position + 1) + "";
        holder.new_music_num.setText(string + "");
        holder.new_music_songname.setText(musicModel.musicName + " - " + musicModel.singerName);
        holder.new_music_frome.setText(musicModel.source);
        RequestOptions RequestOptions = new RequestOptions();
        RequestOptions.placeholder(R.mipmap.moren);
        Glide.with(mContext).load(musicModel.coverUrl).apply(RequestOptions).into(holder.new_music_img);
        if (musicModel.isLike == 1) {
            holder.btn_new_music_zan.setImageResource(R.mipmap.blue_good);
        } else {
            holder.btn_new_music_zan.setImageResource(R.mipmap.back_good);
        }
//        holder.btn_new_music_zan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                zanClickListener.set(position);
//            }
//        });
        SongInfo songInfo = songInfos.get(position);
        if (MusicManager.isCurrMusicIsPlayingMusic(songInfo)) {
            holder.gif_player.setVisibility(View.VISIBLE);
            holder.new_music_num.setVisibility(View.GONE);
            LogUtil.e("NewMusicAdapter", "ListenXingMusicEventBus");
            LogUtil.e("NewMusicAdapter", MusicManager.isIdea() + "");
            if (MusicManager.isPlaying()) {
                LogUtil.e("isPlaying", MusicManager.isPlaying() + "");
                Glide.with(mContext).load(R.mipmap.gif_player).into(holder.gif_player);
            } else {
                LogUtil.e("isPlaying", MusicManager.isPlaying() + "");
                holder.gif_player.setImageResource(R.mipmap.icon_player_gig_stop);
            }
        } else {
            holder.gif_player.setVisibility(View.GONE);
            holder.new_music_num.setVisibility(View.VISIBLE);
        }
        if (itemClickListener != null) {
            holder.btn_toplayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.set(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class XingMusicViewHolder extends RecyclerView.ViewHolder {
        private TextView new_music_num;
        private TextView new_music_songname;
        private TextView new_music_frome;
        private ImageView btn_new_music_zan;
        private RoundedImageView new_music_img;
        private RelativeLayout btn_toplayer;
        private ImageView gif_player;

        public XingMusicViewHolder(View view) {
            super(view);
            new_music_num = view.findViewById(R.id.new_music_num);
            new_music_songname = view.findViewById(R.id.new_music_songname);
            new_music_frome = view.findViewById(R.id.new_music_frome);
            btn_new_music_zan = view.findViewById(R.id.btn_new_music_zan);
            new_music_img = view.findViewById(R.id.new_music_img);
            btn_toplayer = view.findViewById(R.id.btn_toplayer);
            gif_player = view.findViewById(R.id.gif_player);
        }
    }

    public interface ZanClickListener {
        void set(int i);
    }

    public void setZanClickListener(ZanClickListener zanClickListener) {
        this.zanClickListener = zanClickListener;
    }

    public void setItemClickListener(ZanClickListener zanClickListener) {
        this.itemClickListener = zanClickListener;
    }
}
