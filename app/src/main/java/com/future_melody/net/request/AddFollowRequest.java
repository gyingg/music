package com.future_melody.net.request;

/**
 * Created by Y on 2018/5/30.
 */

public class AddFollowRequest {
    private String bg_userid;// 被关注人的id
    private String  g_userid;// 关注人的id

    public AddFollowRequest(String bg_userid, String g_userid) {
        this.bg_userid = bg_userid;
        this.g_userid = g_userid;
    }
}
