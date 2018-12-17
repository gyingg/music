package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/5/29 44
 * Notes:
 */
public class XingMusicSetTop {
    private String userid;
    public int startRecord;//起始页
    public int pageSize;// 每页显示条数

    public XingMusicSetTop(String userid, int startRecord, int pageSize) {
        this.userid = userid;
        this.startRecord = startRecord;
        this.pageSize = pageSize;
    }
}
