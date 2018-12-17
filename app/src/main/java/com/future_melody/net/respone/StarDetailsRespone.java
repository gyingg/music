package com.future_melody.net.respone;

import com.future_melody.mode.StarDetailsUserModel;

import java.util.List;

/**
 * Created by Y on 2018/5/16.
 * 查询行星信息
 */

public class StarDetailsRespone extends FutureHttpResponse {
    public List<StarDetailsUserModel> asteroidList;//(Array[守护小行星简单包装类], optional):守护小行星list ,
    public String backgroundUrl;//(string, optional):行星背景url ,
    public String planetId;//(string, optional):行星id ,
    public String planetName;//(string, optional):行星名称 ,
    public String planetUrl;//(string, optional):行星头像url ,
    public String rulerHeadUrl;//(string, optional):统治者头像url ,
    public String rulerUserId;//(string, optional):统治者id ,
    public String rulerUserName;//(string, optional):统治者昵称 ,
    public String signature;//(string, optional):个性签名
    public int isJoin; //是否加入星球
    public int musiciantype; //1 ,不是音乐人 ，2是音乐
    public String huodongurl; //1 ,不是音乐人 ，2是音乐
    public int activeCount;  //活动数量
    public int specialCount;  //专题数量
    public int userCount;  //居民数量
}
