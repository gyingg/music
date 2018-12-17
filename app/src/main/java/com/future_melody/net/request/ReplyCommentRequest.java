package com.future_melody.net.request;

/**
 * Created by Y on 2018/5/14.
 * 回复评论
 */

public class ReplyCommentRequest {
    public  String commentContent;  //评论内容
    public  String parentId;    //父评论id
    public  String spcialId;   //主题id
    public  String userId;    //用户id


    public ReplyCommentRequest(String commentContent, String parentId, String spcialId, String userId) {
        this.commentContent = commentContent;
        this.parentId = parentId;
        this.spcialId = spcialId;
        this.userId = userId;
    }
}
