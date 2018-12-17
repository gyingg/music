package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/5/29 44
 * Notes:
 */
public class SetMusicTop {
    private int  rownum  ;
    private String userid;
    private String music_id1;
    private String music_id2;
    private String music_id3;

    public SetMusicTop(int rownum, String userid, String music_id1, String music_id2, String music_id3) {
        this.rownum = rownum;
        this.userid = userid;
        this.music_id1 = music_id1;
        this.music_id2 = music_id2;
        this.music_id3 = music_id3;
    }
}
