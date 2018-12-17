package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/5/16 38
 * Notes:
 */
public class PlanetTheme {
    private String planetId;
    private int pageNum;
    private int pageSize;

    public PlanetTheme(String planetId, String userId, int pageNum, int pageSize) {
        this.planetId = planetId;
        this.userId = userId;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    private String userId;
}
