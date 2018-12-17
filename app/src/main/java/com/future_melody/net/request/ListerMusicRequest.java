package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/9/19 04
 * Notes:
 */
public class ListerMusicRequest {
    private String endTime;
    private int isComplete;
    private String musicId;
    private int playTime;
    private String startTime;
    private String userId;
    private int type;

    public ListerMusicRequest(String endTime, int isComplete, String musicId, int playTime, String startTime, String userId, int type) {
        this.endTime = endTime;
        this.isComplete = isComplete;
        this.musicId = musicId;
        this.playTime = playTime;
        this.startTime = startTime;
        this.userId = userId;
        this.type = type;
    }
}
