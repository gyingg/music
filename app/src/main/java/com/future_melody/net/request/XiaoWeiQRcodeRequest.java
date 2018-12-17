package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/9/3 19
 * Notes:
 * mac (string, optional): 真实的mac地址 ,
 * macSnCodeStr (string, optional): mac地址+sn机器码 前12位就是mac地址 ,
 * type (integer, optional): 0扫码获取小未名称 1扫码绑定小未 2解绑小未 ,
 * userId (string, optional): 绑定用户
 */
public class XiaoWeiQRcodeRequest {
    private String mac;
    private String macSnCodeStr;
    private String userId;
    private int type;

    public XiaoWeiQRcodeRequest(String mac, String macSnCodeStr, String userId, int type) {
        this.mac = mac;
        this.macSnCodeStr = macSnCodeStr;
        this.userId = userId;
        this.type = type;
    }
}
