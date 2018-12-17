package com.future_melody.net.request;

/**
 * Created by Y on 2018/5/28.
 */

public class UpdataUserRequest {
    private String nickname;   //姓名
    private String head_portrait;  //头像路径
    private String  asteroid_name;  //小行星名字
    private int sex ;  //(男1  女 2  0代表没设置过)
    private String userid;

    public UpdataUserRequest(String nickname, String head_portrait, String asteroid_name, int sex, String userid) {
        this.nickname = nickname;
        this.head_portrait = head_portrait;
        this.asteroid_name = asteroid_name;
        this.sex = sex;
        this.userid = userid;
    }
}
