package com.future_melody.receiver;

/**
 * Author WZL
 * Date：2018/7/4 15
 * Notes:
 */
public class FollowsThemeEventBus {
    private String messgae;
    private Object respone;
    private int position;

    public FollowsThemeEventBus(Object respone, int position) {
        this.respone = respone;
        this.position = position;
    }


    public FollowsThemeEventBus(String messgae) {
        this.messgae = messgae;
    }

    public String getMessgae() {
        return messgae;
    }

    public Object getRespone() {
        return respone;
    }

    public int position() {
        return position;
    }
}
