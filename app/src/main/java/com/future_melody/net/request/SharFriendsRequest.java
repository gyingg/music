package com.future_melody.net.request;

/**
 * Created by Y on 2018/6/7.
 */

public class SharFriendsRequest {
    public String userId;//用户ID
    public String type;//类别(1邀请好友分享)

    public SharFriendsRequest(String userId, String type) {
        this.userId = userId;
        this.type = type;
    }
}
