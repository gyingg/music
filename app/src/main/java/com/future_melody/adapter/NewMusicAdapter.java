package com.future_melody.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.SPconst;
import com.future_melody.mode.XingMusicModel;
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
 * Date：2018/5/14 37
 * Notes: 新歌
 */
public class NewMusicAdapter extends BaseAdapter {
    private Context mContext;
    private List<XingMusicModel> musicModelList;
    private ArrayList<SongInfo> songInfos;
    private ZanClickListener zanClickListener;
    private ZanClickListener itemClickListener;

    public NewMusicAdapter(Context mContext, List<XingMusicModel> musicModelList, ArrayList<SongInfo> songInfos) {
        this.mContext = mContext;
        this.musicModelList = musicModelList;
        this.songInfos = songInfos;
    }

    @Override
    public int getCount() {
        return musicModelList.size();
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        NewMusicViewHodler holder = null;
        if (view == null) {
            holder = new NewMusicViewHodler();
            view = View.inflate(viewGroup.getContext(), R.layout.item_new_music, null);
            holder.new_music_num = view.findViewById(R.id.new_music_num);
            holder.new_music_songname = view.findViewById(R.id.new_music_songname);
            holder.new_music_frome = view.findViewById(R.id.new_music_frome);
            holder.btn_new_music_zan = view.findViewById(R.id.btn_new_music_zan);
            holder.new_music_img = view.findViewById(R.id.new_music_img);
            holder.btn_toplayer = view.findViewById(R.id.btn_toplayer);
            holder.gif_player = view.findViewById(R.id.gif_player);
            holder.new_music_img_over = view.findViewById(R.id.new_music_img_over);
            view.setTag(holder);
        } else {
            holder = (NewMusicViewHodler) view.getTag();
        }
        XingMusicModel musicModel = musicModelList.get(position);
       /* String string = (position + 1) + "";
        if (string.length() > 1) {
            holder.new_music_num.setText(string + "");
        } else {
            holder.new_music_num.setText("0" + string);
        }*/
        holder.new_music_num.setText(musicModel.no + "");
        holder.new_music_songname.setText(musicModel.musicName + " - " + musicModel.singerName);
        holder.new_music_frome.setText(musicModel.source);
        RequestOptions RequestOptions = new RequestOptions();
        RequestOptions.placeholder(R.mipmap.moren);
        Glide.with(mContext).load(musicModel.musicCoverUrl).apply(RequestOptions).into(holder.new_music_img);
        if (musicModel.isLike == 1) {
            holder.btn_new_music_zan.setImageResource(R.mipmap.blue_good);
        } else {
            holder.btn_new_music_zan.setImageResource(R.mipmap.back_good);
        }
        holder.btn_new_music_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zanClickListener.set(position);
            }
        });
        if (musicModel.isListen == 1) {
            holder.new_music_img_over.setVisibility(View.VISIBLE);
        } else {
            holder.new_music_img_over.setVisibility(View.GONE);
        }
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
        if (itemClickListener!=null){
            holder.btn_toplayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.set(position);
                }
            });
        }

        return view;
    }

    public static class NewMusicViewHodler {
        private TextView new_music_num;
        private TextView new_music_songname;
        private TextView new_music_frome;
        private ImageView btn_new_music_zan;
        private RoundedImageView new_music_img;
        private RelativeLayout btn_toplayer;
        private ImageView gif_player;
        private ImageView new_music_img_over;
    }

    public interface ZanClickListener {
        void set(int i);
    }

    public void setZanClickListener(ZanClickListener zanClickListener) {
        this.zanClickListener = zanClickListener;
    }

    public void itemClickListener(ZanClickListener zanClickListener) {
        this.itemClickListener = zanClickListener;
    }
}
