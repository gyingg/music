package com.future_melody.net.request;

/**
 * Created by Y on 2018/5/24.
 */

public class Recommend_music_Request {
    private int pageNum;
    private int pageSize;
    private String userId;
    private  String beUserId;

    public Recommend_music_Request(int pageNum, int pageSize, String userId, String beUserId) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.userId = userId;
        this.beUserId = beUserId;
    }
}
