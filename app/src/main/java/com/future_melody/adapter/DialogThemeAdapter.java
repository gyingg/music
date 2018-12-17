package com.future_melody.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.common.SPconst;
import com.future_melody.mode.RemendMusicNewModle;
import com.future_melody.mode.ThemeRecommendIMusic;
import com.future_melody.music.PlayerNewActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.SPUtils;
import com.future_melody.widget.IsWifiDialog;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.manager.MusicManager;

import java.util.List;

/**
 * Author WZL
 * Date：2018/6/12 28
 * Notes:
 */
public class DialogThemeAdapter extends BaseAdapter {
    private Context mContext;
    private List<RemendMusicNewModle> musicVoList;
    private List<SongInfo> songInfos;

    public DialogThemeAdapter(Context mContext, List<RemendMusicNewModle> musicVoList, List<SongInfo> songInfos) {
        this.mContext = mContext;
        this.musicVoList = musicVoList;
        this.songInfos = songInfos;
    }

    @Override
    public int getCount() {
        return musicVoList.size();
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
            view = View.inflate(mContext, R.layout.item_dialog_music, null);
            holder = new ViewHodler();
            holder.text_music_name = view.findViewById(R.id.text_music_name);
            holder.text_music_sing = view.findViewById(R.id.text_music_sing);
            holder.dialog_player_music = view.findViewById(R.id.dialog_player_music);
            view.setTag(holder);
        } else {
            holder = (ViewHodler) view.getTag();
        }
        RemendMusicNewModle respone = musicVoList.get(i);
        holder.text_music_name.setText(respone.musicName + "");
        holder.text_music_sing.setText(respone.singerName + "");
        SongInfo songInfo = songInfos.get(i);
        holder.dialog_player_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MusicManager.isCurrMusicIsPlayingMusic(songInfo)) {
                    PlayerUitlis.player(mContext);
                } else {
                    if (CommonUtils.isWifi(mContext)) {
                        MusicManager.get().playMusic(songInfos, i);
                        PlayerNewActivity.launch(mContext, songInfos, i);
                    } else {
                        if (SPUtils.getInstance().getBoolean(SPconst.ISWIFIPLAYER, false)) {
                            IsWifiDialog dialog = new IsWifiDialog(mContext);
                            dialog.setCancelButton(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SPUtils.getInstance().put(SPconst.ISWIFIPLAYER, false);
                                    MusicManager.get().playMusic(songInfos, i);
                                    PlayerNewActivity.launch(mContext, songInfos, i);
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            MusicManager.get().playMusic(songInfos, i);
                            PlayerNewActivity.launch(mContext, songInfos, i);
                        }
                    }
                }
            }
        });
        return view;
    }

    public static class ViewHodler {
        private TextView text_music_name;
        private TextView text_music_sing;
        private LinearLayout dialog_player_music;
    }
}
