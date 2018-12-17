package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/5/10 24
 * Notes:
 */
public class EditMusicInfo {
    private String music_path;//音乐路径
    private String music_picture;//音乐图片
    private String music_name;//音乐名字
    private String singer_name;//歌手名字
    private String biaoqian_id;//类型
    private String userid;//用户id
    private String specialid;//专题id
    private int duration;//专题id

    public EditMusicInfo(String userid, String music_path, String music_picture, String music_name, String singer_name, String biaoqian_id, String specialid ,int duration) {
        this.music_path = music_path;
        this.music_picture = music_picture;
        this.music_name = music_name;
        this.singer_name = singer_name;
        this.biaoqian_id = biaoqian_id;
        this.specialid = specialid;
        this.userid = userid;
        this.duration = duration;
    }
}
