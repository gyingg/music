package com.future_melody.music;

import android.content.Context;
import android.widget.Toast;

import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.manager.MusicManager;

import java.util.List;

/**
 * Author WZL
 * Date：2018/6/19 23
 * Notes:
 */
public class PlayerUitlis {
    public static void player(Context context){
        if (MusicManager.get().getCurrPlayingMusic()!=null) {
            PlayerNewActivity.launch(context, MusicManager.get().getPlayList(), MusicManager.get().getCurrPlayingIndex());
        }else {
            Toast.makeText(context, "去找点音乐听听吧", Toast.LENGTH_SHORT).show();
        }
    }
    public static void player(Context context ,List<SongInfo> songInfos){
        PlayerNewActivity.launch(context, songInfos, MusicManager.get().getCurrPlayingIndex());
    }
}
