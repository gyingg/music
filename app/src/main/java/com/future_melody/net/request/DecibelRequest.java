package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/6/12 54
 * Notes:
 */
public class DecibelRequest {
    private String userid;
    private int pageSize;
    private int startRecord;

    public DecibelRequest(int startRecord, int pageSize, String userid) {
        this.userid = userid;
        this.pageSize = pageSize;
        this.startRecord = startRecord;
    }
}
