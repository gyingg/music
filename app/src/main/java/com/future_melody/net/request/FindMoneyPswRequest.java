package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/6/19 51
 * Notes:
 */
public class FindMoneyPswRequest {
    private String userid;// 用户ID
    private String password;// 密码
    private String code;// 密码
    private String phone;// 密码

    public FindMoneyPswRequest(String userid, String password, String code, String phone) {
        this.userid = userid;
        this.password = password;
        this.code = code;
        this.phone = phone;
    }
}
