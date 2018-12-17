package com.future_melody.net.request;

/**
 * Created by Y on 2018/5/16.
 * 查询行星信息
 */

public class StarDetailsRequest {
    private String planetId;
    private String userId;

    public StarDetailsRequest(String planetId, String userId) {
        this.planetId = planetId;
        this.userId = userId;
    }
}
