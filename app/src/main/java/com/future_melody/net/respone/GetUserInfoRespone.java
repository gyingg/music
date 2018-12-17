package com.future_melody.net.respone;

/**
 * Author WZL
 * Date：2018/5/9 40
 * Notes:
 */
public class GetUserInfoRespone extends FutureHttpResponse {
    public String userid;
    public String nickname;
    public String planet_name;//小行星名字
    public String starrysky_name;//星球名字
    public String head_portrait;//头像Url
    public int sex;//1男 2女 0未修改过
    public int isupdate;// 是否修改过个人资料  0:没有修改过 1:修改过
}
