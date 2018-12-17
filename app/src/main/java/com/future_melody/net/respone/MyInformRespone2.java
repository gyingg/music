package com.future_melody.net.respone;

/**
 * Created by Y on 2018/5/21.
 */

public class MyInformRespone2 extends FutureHttpResponse {

        /**
         * type : 2
         * fromUserId : bca371fa283b4de2891c87f0b3de27ea
         * fromNickname : 李四(误删)
         * fromUserHeadUrl : http://ant-image.oss-cn-shanghai.aliyuncs.com/ant2.0/shop/2018-05-17/940423dd-fac3-4d24-ae24-c2aa46a2e2f1.jpg
         * content : null
         * transferUrl : null
         * createTime : 2018-05-17 11:37:59.0
         * fromThingId : dca03830c0f3499abf1d54a14c3c0f7d
         * fromThingContent : 相当之好
         * toThingId : ebd6f5488c994139ba353ecdda0b1e98
         * toThingContent : 牵着时光衣襟,走进芳菲五月
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

        public Object getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Object getTransferUrl() {
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
