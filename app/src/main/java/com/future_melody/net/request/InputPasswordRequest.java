package com.future_melody.net.request;

/**
 * Created by Y on 2018/9/19.
 */

public class InputPasswordRequest {
    private String userid;
    private String zijingpassword;
    private int count;
    private String user_name;
    private int type;

    public InputPasswordRequest(String userid, String zijingpassword, int count, String user_name, int type) {
        this.userid = userid;
        this.zijingpassword = zijingpassword;
        this.count = count;
        this.user_name = user_name;
        this.type = type;
    }
}
