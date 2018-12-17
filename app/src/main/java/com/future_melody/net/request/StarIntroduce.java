package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/5/23 12
 * Notes: 修改星球介绍
 */
public class StarIntroduce {
    public String userid;//用户id
    public String background_url;// 背景图片
    public String signature;// 星球介绍

    public StarIntroduce(String userid, String background_url, String signature) {
        this.userid = userid;
        this.background_url = background_url;
        this.signature = signature;
    }
}
