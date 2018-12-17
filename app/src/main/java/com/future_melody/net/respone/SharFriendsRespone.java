package com.future_melody.net.respone;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Y on 2018/6/7.
 */

public class SharFriendsRespone extends FutureHttpResponse  {

        /**
         * id : d1654ba80dcf4da6a40329bc6dd9f59d
         * type : 1
         * shareUrl : https://mbd.baidu.com/newspage/data/landingsuper?context=%7B%22nid%22%3A%22news_7145539940088002784%22%7D&n_type=0&p_from=1
         * pictureUrl : https://tfm.oss-cn-beijing.aliyuncs.com/futuremelody-procurement/2018-07-14/%E6%9C%AA%E6%9D%A5%E5%A3%B0%E9%9F%B3.jpg
         * title : 99.99%的人点开后都激动到失控
         * summary : 惊讶到说不出话来，感觉比中了100万还刺激。
         */

        private String id;
        private int type;
        private String shareUrl;
        private String pictureUrl;
        private String title;
        private String summary;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

}
