package com.future_melody.net.request;

/**
 * Created by Y on 2018/5/16.
 * 查询关注人的主题
 */

public class AttentionThemeRequest {
    public int pageNum;
    public int pageSize;
    public String userId;

    public AttentionThemeRequest(int pageNum, int pageSize, String userId) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.userId = userId;
    }
}
