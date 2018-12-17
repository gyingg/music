package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/5/9 25
 * Notes:设置个人资料
 */
public class SetUserInfo {
    private int userid;//用户ID
    private int sex;//性别
    private int isupdate;//修改过昵称就传1  没有修改就传 0
    private String nickname;//用户昵称
    private String head_portrait;//用户头像

    public SetUserInfo(int id, int sex, String nickname, String head_portrait, int isupdate) {
        this.userid = id;
        this.sex = sex;
        this.nickname = nickname;
        this.head_portrait = head_portrait;
        this.isupdate = isupdate;
    }

}
