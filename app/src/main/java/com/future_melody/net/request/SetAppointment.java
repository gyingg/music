package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/5/22 52
 * Notes: 任命小行星：添加和取消
 */
public class SetAppointment {
    public String userid;// 用户id
    public int guardian_asteroid;//是否是守护小行星(1是 0 不是)
    public String xiaoxingxing;

    public SetAppointment(String userid, int guardian_asteroid, String xiaoxingxing) {
        this.userid = userid;
        this.guardian_asteroid = guardian_asteroid;
        this.xiaoxingxing = xiaoxingxing;
    }
}
