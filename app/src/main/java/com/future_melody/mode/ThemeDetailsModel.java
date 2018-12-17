package com.future_melody.mode;

/**
 * Author WZL
 * Date：2018/5/10 05
 * Notes: 评论详情列表List model
 */
public class ThemeDetailsModel {
    public String asteroidName;//(string, optional): 小行星名称 ,
    public String commentContent;// (string, optional): 评论内容 ,
    public String commentId;// (string, optional): 评论id ,
    public String createTime;//(string, optional): 评论时间 ,
    public String headUrl;//(string, optional): 头像url ,
    public int isLike;//(integer, optional): 我是否已经点赞 ,
    public int likeCount;//(integer, optional): 点赞量 ,
    public String nickname;// (string, optional): 昵称 ,
    public String parentCommentContent;//(string, optional): 父用户评论内容 ,
    public String parentId;//(string, optional): 父id ,
    public String parentNickname;//(string, optional): 父用户昵称 ,
    public String parentUserId;//(string, optional): 父用户id ,
    public String planetName;//(string, optional):行星名称 ,
    public String userId;//(string, optional): 用户id
}
