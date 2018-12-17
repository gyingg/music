package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/5/8 47
 * Notes: 短信验证码登录请求参数
 */
public class SendLogin {
    public String phone;
    public String usercode;
    public String code;
    public String token;
    public String equipment_token;

    public SendLogin(String phone, String usercode ,String code ,String token ,String equipment_token) {
        this.phone = phone;
        this.usercode = usercode;
        this.code = code;
        this.token = token;
        this.equipment_token = equipment_token;
    }
}
