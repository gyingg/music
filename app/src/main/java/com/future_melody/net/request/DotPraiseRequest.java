package com.future_melody.net.request;

/**
 * Created by Y on 2018/5/14.
 * 点赞  根据flag判断  + （0）歌曲 musicId musicName musicName +specialId和userId
 *                       （1）评论  commentId
 */

public class DotPraiseRequest {
    public String beingUserId; //被点赞的用户id ,
    public String commentId;  //评论id
    public int flag;  //0:歌曲点赞 1:评论点赞 ,
    public String musicId;   //音乐id
    public String musicName;   //音乐名称
    public String musicPicture;    //音乐封面url
    public String specialId;    //主题id
    public String userId;    //当前用户id

    public DotPraiseRequest(String beingUserId,int flag, String musicId, String musicName, String musicPicture, String specialId, String userId) {
        this.beingUserId=beingUserId;
        this.flag = flag;
        this.musicId = musicId;
        this.musicName = musicName;
        this.musicPicture = musicPicture;
        this.specialId = specialId;
        this.userId = userId;
    }


    public DotPraiseRequest(String beingUserId,String commentId, int flag, String specialId, String userId) {
        this.beingUserId=beingUserId;
        this.commentId = commentId;
        this.flag = flag;
        this.specialId = specialId;
        this.userId = userId;
    }
}
