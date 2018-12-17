package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/6/12 06
 * Notes:
 */
public class ShareMusicRequest {
    private String musicId;// (string, optional): 音乐id ,
    private String userId;// (string, optional): 当前登录人id

    public ShareMusicRequest(String musicId, String userId) {
        this.musicId = musicId;
        this.userId = userId;
    }
}
