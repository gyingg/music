package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/6/2 52
 * Notes:
 */
public class XingTopMusicRequest {
    public int category;//(integer, optional): 类别 0日榜 1周榜 2月榜 ,
    public int pageNum;//(integer, optional): 当前页码 ,
    public int pageSize;//(integer, optional): 每页显示条数 ,
    public String userId;//(string, optional): 当前用户id

    public XingTopMusicRequest(int category, int pageNum, int pageSize, String userId) {
        this.category = category;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.userId = userId;
    }
}
