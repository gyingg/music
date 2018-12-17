package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/5/16 46
 * Notes: 我的：粉丝
 */
public class MineReconmendFans {
    private String userid;
    private int startRecord;
    private int pageSize;

    public MineReconmendFans(String userid, int startRecord, int pageSize) {
        this.userid = userid;
        this.startRecord = startRecord;
        this.pageSize = pageSize;
    }
}
