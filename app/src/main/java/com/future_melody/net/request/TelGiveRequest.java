package com.future_melody.net.request;

/**
 * Created by Y on 2018/9/19.
 */

public class TelGiveRequest {
    private String user_name;
    private int startRecord;
    private int pageSize;

    public TelGiveRequest(String user_name, int startRecord, int pageSize) {
        this.user_name = user_name;
        this.startRecord = startRecord;
        this.pageSize = pageSize;
    }
}
