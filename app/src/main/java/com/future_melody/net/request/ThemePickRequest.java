package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/9/20 35
 * Notes:
 */
public class ThemePickRequest {
    public String specialId;
    public String userId ;

    public ThemePickRequest(String specialId, String userId) {
        this.specialId = specialId;
        this.userId = userId;
    }
}
