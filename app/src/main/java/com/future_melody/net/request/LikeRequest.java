package com.future_melody.net.request;

/**
 * Created by Y on 2018/5/21.
 */

public class LikeRequest {
    private  String userid;
    private int pageSize;//每页显示条数
    private int startRecord;//起始记录数

    public LikeRequest(String userid, int pageSize, int startRecord) {
        this.userid = userid;
        this.pageSize = pageSize;
        this.startRecord = startRecord;
    }
}
