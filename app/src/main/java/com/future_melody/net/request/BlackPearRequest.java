package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/6/12 54
 * Notes:
 */
public class BlackPearRequest {
    private String userid;
    private int pageSize;
    private int startRecord;

    public BlackPearRequest(String userid, int pageSize, int startRecord) {
        this.userid = userid;
        this.pageSize = pageSize;
        this.startRecord = startRecord;
    }
}
