package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/8/13 59
 * Notes:
 */
public class ThemeRecommendRequest {
    private String specialId;
    private String userId;

    public ThemeRecommendRequest(String specialId, String userId) {
        this.specialId = specialId;
        this.userId = userId;
    }
}
