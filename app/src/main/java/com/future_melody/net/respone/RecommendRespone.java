package com.future_melody.net.respone;

import com.future_melody.mode.RecommendSpecialVoListBean;

import java.util.List;

/**
 * Created by Y on 2018/5/11.
 * 推荐Fragment
 */

public class RecommendRespone extends FutureHttpResponse {

    /**
     * planetId : c8f8096558a946718b2dded88b6f204c
     * planetName : 孤独星球
     * picUrl : https://tfm.oss-cn-beijing.aliyuncs.com/future-sound/touxiang/gudu/3addb76bb1be57cb9ade542bc30dcf78.png
     * recommendSpecialVoList : [{"userId":"728ec7152cb94ab28f72472ab369e0fc","nickname":"张国良","userHeadUrl":"https://tfm.oss-cn-beijing.aliyuncs.com/future-sound/text/2018-06-23/2725faa1-4f6d-4194-8776-e37d4541d5c7.jpg","planetName":"热血星球","asteroidName":"\u2014","isAttention":0,"specialId":"80858313129f4575a49615010c8a5880","specialPictureUrl":"https://tfm.oss-cn-beijing.aliyuncs.com/future-sound/text/2018-06-05/e7186135-f46f-484f-81d8-547fcf2654d6.jpg","musicCount":10,"specialDescription":"张总测歌词","specialCreateTime":"06-22 发布","likeCount":20,"commentCount":5,"specialTitle":"张总测歌词"},{"userId":"bf16bfed26f34360a2c209ad3e4835ea","nickname":"打不过我吧","userHeadUrl":"https://tfm.oss-cn-beijing.aliyuncs.com/future-sound/text/2018-06-26/2d52e04d-a540-43e6-99e9-751a5fd5ff4f.jpg","planetName":"孤独星球","asteroidName":"\u2014","isAttention":1,"specialId":"3279eb5c784d434eae076f8312b5412f","specialPictureUrl":"https://tfm.oss-cn-beijing.aliyuncs.com/future-sound/text/2018-06-08/e33b4a12-5c96-4290-bead-32605e1a994f.jpg","musicCount":1,"specialDescription":"红红火火恍恍惚惚红红火火","specialCreateTime":"06-08 发布","likeCount":3,"commentCount":14,"specialTitle":"我们一起学猪叫"},{"userId":"bf16bfed26f34360a2c209ad3e4835ea","nickname":"打不过我吧","userHeadUrl":"https://tfm.oss-cn-beijing.aliyuncs.com/future-sound/text/2018-06-26/2d52e04d-a540-43e6-99e9-751a5fd5ff4f.jpg","planetName":"孤独星球","asteroidName":"\u2014","isAttention":1,"specialId":"4b8b6ec558ee4461b599e2eec7c15885","specialPictureUrl":"https://tfm.oss-cn-beijing.aliyuncs.com/future-sound/text/2018-05-30/68910d9e-40bc-48b5-8a5c-bf270986313c.jpg","musicCount":1,"specialDescription":"红红火火","specialCreateTime":"05-30 发布","likeCount":3,"commentCount":31,"specialTitle":"测试"}]
     */

    private String planetId;
    private String planetName;
    private String picUrl;

    public List<RecommendSpecialVoListBean> getRecommendSpecialVoList() {
        return recommendSpecialVoList;
    }

    public void setRecommendSpecialVoList(List<RecommendSpecialVoListBean> recommendSpecialVoList) {
        this.recommendSpecialVoList = recommendSpecialVoList;
    }

    private List<RecommendSpecialVoListBean> recommendSpecialVoList;

    public String getPlanetId() {
        return planetId;
    }

    public void setPlanetId(String planetId) {
        this.planetId = planetId;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
