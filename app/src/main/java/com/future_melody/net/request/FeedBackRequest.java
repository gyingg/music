package com.future_melody.net.request;

/**
 * Created by Y on 2018/5/21.
 */

public class FeedBackRequest {
    private String userid;
    private String opinion;

    public FeedBackRequest(String userid, String opinion) {
        this.userid = userid;
        this.opinion = opinion;
    }
}
