package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/5/16 01
 * Notes: 关注
 */
public class AddFollow {
    private String bg_userid;//被关注人的id
    private String g_userid;// 关注人的id

    public AddFollow(String bg_userid, String g_userid) {
        this.bg_userid = bg_userid;
        this.g_userid = g_userid;
    }
}
