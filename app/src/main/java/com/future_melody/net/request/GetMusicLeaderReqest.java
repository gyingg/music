package com.future_melody.net.request;

/**
 * Created by Y on 2018/5/29.
 */

public class GetMusicLeaderReqest {
    public String userId;
    public int pageNum;
    public int pageSize;
    public int category ;

    public GetMusicLeaderReqest(String userId, int pageNum, int pageSize ,int category ) {
        this.userId = userId;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.category = category;
    }
}
