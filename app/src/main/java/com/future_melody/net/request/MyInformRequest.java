package com.future_melody.net.request;

/**
 * Created by Y on 2018/5/21.
 */

public class MyInformRequest {
    private int pageNum;
    private int pageSize;
    private int type;
    private String userId;


    public MyInformRequest(int pageNum, int pageSize, int type, String userId) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.type = type;
        this.userId = userId;
    }
}
