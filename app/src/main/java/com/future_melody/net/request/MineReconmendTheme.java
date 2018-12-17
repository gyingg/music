package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/5/16 46
 * Notes: 我的：推荐主题
 */
public class MineReconmendTheme {
    private String userid;
    private int startRecord;
    private int pageSize;

    public MineReconmendTheme(String userid, int startRecord, int pageSize) {
        this.userid = userid;
        this.startRecord = startRecord;
        this.pageSize = pageSize;
    }
}
