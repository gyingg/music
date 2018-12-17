package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/5/16 01
 * Notes: 取消关注
 */
public class CancelFollow {
    private String bg_userid;// 被关注人的id
    private String  g_userid;// 关注人的id

    public CancelFollow(String bg_userid, String g_userid) {
        this.bg_userid = bg_userid;
        this.g_userid = g_userid;
    }
}
