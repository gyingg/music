package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/5/10 31
 * Notes:发布主题
 */
public class SendTheme {
    private String special_picture;//专题图片
    private String special_describe;//专题描述
    private String userid;//用户id
    private String specialid;//用户id
    private String special_title;//专题title

    public SendTheme(String special_title, String special_picture, String special_describe, String userid, String specialid) {
        this.special_picture = special_picture;
        this.special_describe = special_describe;
        this.userid = userid;
        this.specialid = specialid;
        this.special_title = special_title;
    }
}
