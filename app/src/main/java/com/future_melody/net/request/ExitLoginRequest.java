package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/6/28 56
 * Notes:
 */
public class ExitLoginRequest {
    private String userid;
    private String token;

    public ExitLoginRequest(String userid, String token) {
        this.userid = userid;
        this.token = token;
    }
}
