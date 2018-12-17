package com.future_melody.net.request;

/**
 * Created by Y on 2018/5/24.
 */

public class Recommend_theme_Request {
    private int pageNum;
    private int pageSize;
    private String userId;

    public Recommend_theme_Request(int pageNum, int pageSize, String userId) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.userId = userId;
    }
}
