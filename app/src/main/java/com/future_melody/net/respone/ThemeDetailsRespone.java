package com.future_melody.net.respone;

import com.future_melody.mode.ThemeDetailsMusicBean;

import java.util.List;

/**
 * Author WZL
 * Date：2018/5/12 15
 * Notes:主题详情
 */
public class ThemeDetailsRespone extends FutureHttpResponse {
    public String asteroidName;// (string, optional): 小行星名称 ,
    public int commentCount;// (integer, optional): 评论数量 ,
    public int likeCount ;// (integer, optional): 评论数量 ,
    public int isLike  ;// (integer, optional): 评论数量 ,
    public int shareCount  ;// (integer, optional): 评论数量 ,
    public int isAttention;//  (integer, optional): 是否关注 0未关注 大于0已关注 ,
    public int musicCount;//  (integer, optional): 包含的音乐数量 ,
    public String nickname;// (string, optional): 昵称 ,
    public String planetName;// (string, optional): 行星名称 ,
    public String specialDescription;// (string, optional): 主题描述 ,
    public String specialId;// (string, optional): 主题id ,
    public String specialPictureUrl;// (string, optional): 主题url ,
    public String userHeadUrl;// (string, optional): 用户头像url ,
    public String userId;// (string, optional): 用户id
    public String specialTitle;// (string, optional):
    public String createTime;// (string, optional):
    public List<ThemeDetailsMusicBean> musicVoList;
}
