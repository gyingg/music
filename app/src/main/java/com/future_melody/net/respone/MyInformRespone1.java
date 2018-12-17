package com.future_melody.net.respone;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Y on 2018/5/21.
 */

public class MyInformRespone1 extends FutureHttpResponse {


        /**
         * type : 1
         * fromUserId : null
         * fromNickname : null
         * fromUserHeadUrl : null
         * content : 我是系统通知2
         * transferUrl : http:www.baidu.com
         * createTime : 24秒前
         * fromThingId : null
         * fromThingContent : null
         * toThingId : null
         * toThingContent : null
         */

        private int type;
        private String fromUserId;
        private String fromNickname;
        private String fromUserHeadUrl;
        private String content;
        private String transferUrl;
        private String createTime;
        private String fromThingId;
        private String fromThingContent;
        private String toThingId;
        private String toThingContent;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getFromUserId() {
            return fromUserId;
        }

        public void setFromUserId(String fromUserId) {
            this.fromUserId = fromUserId;
        }

        public String getFromNickname() {
            return fromNickname;
        }

        public void setFromNickname(String fromNickname) {
            this.fromNickname = fromNickname;
        }

        public String getFromUserHeadUrl() {
            return fromUserHeadUrl;
        }

        public void setFromUserHeadUrl(String fromUserHeadUrl) {
            this.fromUserHeadUrl = fromUserHeadUrl;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTransferUrl() {
            return transferUrl;
        }

        public void setTransferUrl(String transferUrl) {
            this.transferUrl = transferUrl;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getFromThingId() {
            return fromThingId;
        }

        public void setFromThingId(String fromThingId) {
            this.fromThingId = fromThingId;
        }

        public String getFromThingContent() {
            return fromThingContent;
        }

        public void setFromThingContent(String fromThingContent) {
            this.fromThingContent = fromThingContent;
        }

        public String getToThingId() {
            return toThingId;
        }

        public void setToThingId(String toThingId) {
            this.toThingId = toThingId;
        }

        public String getToThingContent() {
            return toThingContent;
        }

        public void setToThingContent(String toThingContent) {
            this.toThingContent = toThingContent;
        }
}
