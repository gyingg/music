package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/6/19 51
 * Notes:
 */
public class SetMoneyPswRequest {
    private String userid;// 用户ID
    private String password;// 密码

    public SetMoneyPswRequest(String userid, String password) {
        this.userid = userid;
        this.password = password;
    }
}
