package com.future_melody.net.request;

/**
 * Created by Y on 2018/8/24.
 */

public class MainActivityFragmentRequest {
    public String planetId;
    public String userId ;

    public MainActivityFragmentRequest(String planetId, String userId) {
        this.planetId = planetId;
        this.userId = userId;
    }
}
