package com.future_melody.receiver;

/**
 * Author WZL
 * Date：2018/7/5 21
 * Notes:
 */
public class MusicIsLikeEventBus {
    private String isLike;
    private int TopisLike;

    public MusicIsLikeEventBus(String isLike) {
        this.isLike = isLike;
    }

    public MusicIsLikeEventBus(int TopisLike) {
        this.TopisLike = TopisLike;
    }

    public String getMessgae() {
        return isLike;
    }
    public int getLike() {
        return TopisLike;
    }
}
