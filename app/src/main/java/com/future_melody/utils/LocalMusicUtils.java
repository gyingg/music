package com.future_melody.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.future_melody.mode.LocalMusicModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Author WZL
 * Date：2018/6/5 30
 * Notes:
 */
public class LocalMusicUtils {

    public static List<LocalMusicModel> getMusicData(Context context) {
        List<LocalMusicModel> list = new ArrayList<LocalMusicModel>();
        // 媒体库查询语句（写一个工具类MusicUtils）
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                LocalMusicModel song = new LocalMusicModel();
                song.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                song.setSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                song.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                if (song.getSize() > 1000 * 800) {
                    // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
                    if (song.getName()!=null) {
                        if (song.getName().contains("-")) {
                            String[] str = song.getName().split("-");
                            song.setSinger(str[0]);
                            song.setName(str[1]);
                        }
                    }

                    list.add(song);
                }
            }
            // 释放资源
            cursor.close();
        }

        return list;
    }

    /**
     * 定义一个方法用来格式化获取到的时间
     */
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;

        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }
    }
}
