package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/6/4 20
 * Notes:
 */
public class MusicZanRequest {
    private String musicId;//(string, optional):音乐id ,
    private String musicName;//(string, optional):音乐名称 ,
    private String userId;//(string, optional):当前登录人id

    public MusicZanRequest(String musicId, String musicName, String userId) {
        this.musicId = musicId;
        this.musicName = musicName;
        this.userId = userId;
    }
}
