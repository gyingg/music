package com.future_melody.net.request;

/**
 * Created by Y on 2018/5/12.
 */

public class JoinStarRequest {
    public String userid;
    public String starrysky_id; //星球id
    public String  planet_name; //星球名称
    public String  asteroid_name   ; //小行星名字

    public JoinStarRequest(String userid, String starrysky_id, String planet_name ,String asteroid_name) {
        this.userid = userid;
        this.starrysky_id = starrysky_id;
        this.planet_name = planet_name;
        this.asteroid_name = asteroid_name;
    }
}