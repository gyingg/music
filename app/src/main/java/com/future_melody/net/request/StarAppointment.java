package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/5/22 45
 * Notes:
 */
public class StarAppointment {
    public String userid;//用户id
    public String nickname;//昵称
    public int startRecord;//起始页
    public int pageSize;// 每页显示条数

    public StarAppointment(String userid, String nickname, int startRecord, int pageSize) {
        this.userid = userid;
        this.nickname = nickname;
        this.startRecord = startRecord;
        this.pageSize = pageSize;
    }
}
