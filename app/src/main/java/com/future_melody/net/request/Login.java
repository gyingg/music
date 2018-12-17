package com.future_melody.net.request;

/**
 * 请求参数
 */
public class Login {
    public String user_name;
    public String password;
    public String token;

    public Login(String user_name, String password, String token) {
        this.user_name = user_name;
        this.password = password;
        this.token = token;
    }
}
