package com.future_melody.net.request;

/**
 * Created by Y on 2018/5/15.
 */

public class WeekSuperuRequest {
    public int category;
    public String planetId;
    public String userId ;

    public WeekSuperuRequest(int category, String planetId, String userId) {
        this.category = category;
        this.planetId = planetId;
        this.userId = userId;
    }
}
