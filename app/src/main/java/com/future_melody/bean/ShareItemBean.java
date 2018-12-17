package com.future_melody.bean;

/**
 *
 */
public class ShareItemBean {
    private int imgId;
    private String shareTv;

    public ShareItemBean(int imgId, String shareTv) {
        this.imgId = imgId;
        this.shareTv = shareTv;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getShareTv() {
        return shareTv;
    }

    public void setShareTv(String shareTv) {
        this.shareTv = shareTv;
    }
}
