package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/5/8 50
 * Notes: 忘记密码
 */
public class FindPassword {
    public String user_name;
    public String password;
    public String usercode;
    public String code;

    public FindPassword(String user_name, String password, String usercode ,String code) {
        this.user_name = user_name;
        this.password = password;
        this.usercode = usercode;
        this.code = code;
    }
}
