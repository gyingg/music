package com.future_melody.receiver;

/**
 * Author WZL
 * Date：2018/7/5 21
 * Notes:
 */
public class LikeEventBus {
    private int TopisLike;
    private int position;

    public LikeEventBus(int TopisLike, int position) {
        this.TopisLike = TopisLike;
        this.position = position;
    }

    public int getLike() {
        return TopisLike;
    }

    public int getPosition() {
        return position;
    }
}
