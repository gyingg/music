package com.future_melody.net.respone;

/**
 * Author WZL
 * Date：2018/5/8 40
 * Notes: 短信验证码登录
 */
public class SendLoginResponse extends FutureHttpResponse {

    /**
     * create_time : null
     * head_portrait :
     * id : 7
     * nickname :
     * password :
     * phone :
     * planet_name :
     * starrysky_id : 0
     * user_name : 13693656131
     * usercode :
     */

    private String create_time;
    private String head_portrait;
    private String userid;
    private String nickname;
    private String password;
    private String phone;
    private String planet_name;
    private String starrysky_id;
    private String user_name;
    private String usercode;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    public String getId() {
        return userid;
    }

    public void setId(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlanet_name() {
        return planet_name;
    }

    public void setPlanet_name(String planet_name) {
        this.planet_name = planet_name;
    }

    public String getStarrysky_id() {
        return starrysky_id;
    }

    public void setStarrysky_id(String starrysky_id) {
        this.starrysky_id = starrysky_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }
}
