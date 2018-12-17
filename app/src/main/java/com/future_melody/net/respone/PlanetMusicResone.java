package com.future_melody.net.respone;

/**
 * Author WZL
 * Date：2018/5/16 42
 * Notes: 行星推荐音乐
 */
public class PlanetMusicResone extends FutureHttpResponse {
    public String asteroidName;//(string, optional): 小行星名称 ,
    public String coverUrl;// (string, optional): 封面url ,
    public int isLike;// (string, optional): 本人是否点赞 ,
    public String likeCount;// (string, optional): 点赞数量 ,
    public String musicId;//(string, optional): 音乐id ,
    public String musicName;// (string, optional): 音乐名称 ,
    public String planetName;//(string, optional): 行星名称 ,
    public String userId;//(string, optional): 用户id
    public String musicPath; //音乐封面url
    public String lyrics;
    public String singerName;
    public String nickname;
}
