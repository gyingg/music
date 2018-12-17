package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/8/22 44
 * Notes:
 */
public class XingMusicTopRequest {
    private String userId;
    private int category;
    private int pageNum;
    private int pageSize;

    public XingMusicTopRequest(String userId, int category, int pageNum, int pageSize) {
        this.userId = userId;
        this.category = category;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

}
