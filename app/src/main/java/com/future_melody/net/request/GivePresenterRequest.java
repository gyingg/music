package com.future_melody.net.request;

/**
 * Created by Y on 2018/9/19.
 */

public class GivePresenterRequest {
    private int count;
    private String user_name;
    private String userid;
    private int type;
    private String explains;

    public GivePresenterRequest(int count, String user_name, String userid, int type, String explains) {
        this.count = count;
        this.user_name = user_name;
        this.userid = userid;
        this.type = type;
        this.explains = explains;
    }
}
