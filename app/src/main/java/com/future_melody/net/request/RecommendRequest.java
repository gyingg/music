package com.future_melody.net.request;

/**
 * Created by Y on 2018/5/11.
 * 推荐Fragment
 */

public class RecommendRequest {
    public String userId;
    public int pageNum;
    public int pageSize;

    public RecommendRequest(String userId, int pageNum, int pageSize) {
        this.userId = userId;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
