package com.future_melody.net.respone;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Y on 2018/5/24.
 */

public class UserInforRespne  extends FutureHttpResponse {

        /**
         * userId : 514cca2f1ea9404b89e4bfe2b69aa945
         * nickname : 张三(误删)
         * headUrl : http://ant-image.oss-cn-shanghai.aliyuncs.com/ant2.0/shop/2018-05-17/9014c45a-8af1-4d64-8f5f-37fd79d602f3.jpg
         * planetId : f75cbf2ddefe43efb43e1963369d2e3f
         * planetName : 地球
         * asteroidName : 张三的小行星
         * isAttention : 1
         * identity : 统治者
         * attentionCount : 1
         * fansCount : 7
         * specialCount : 4
         * musicCount : 3
         */

        private String userId;
        private String nickname;
        private String headUrl;
        private String planetId;
        private String planetName;
        private String asteroidName;
        private String isAttention;
        private String identity;
        private int attentionCount;
        private int fansCount;
        private int specialCount;
        private int musicCount;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

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

        public String getAsteroidName() {
            return asteroidName;
        }

        public void setAsteroidName(String asteroidName) {
            this.asteroidName = asteroidName;
        }

        public String getIsAttention() {
            return isAttention;
        }

        public void setIsAttention(String isAttention) {
            this.isAttention = isAttention;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public int getAttentionCount() {
            return attentionCount;
        }

        public void setAttentionCount(int attentionCount) {
            this.attentionCount = attentionCount;
        }

        public int getFansCount() {
            return fansCount;
        }

        public void setFansCount(int fansCount) {
            this.fansCount = fansCount;
        }

        public int getSpecialCount() {
            return specialCount;
        }

        public void setSpecialCount(int specialCount) {
            this.specialCount = specialCount;
        }

        public int getMusicCount() {
            return musicCount;
        }

        public void setMusicCount(int musicCount) {
            this.musicCount = musicCount;
        }

}
