package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/7/5 25
 * Notes:
 */
public class PlayerIsLikeRequest {
    private String userId;
    private String musicId;

    public PlayerIsLikeRequest(String userId, String musicId) {
        this.userId = userId;
        this.musicId = musicId;
    }
}
