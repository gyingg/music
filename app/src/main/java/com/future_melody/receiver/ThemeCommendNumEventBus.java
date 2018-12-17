package com.future_melody.receiver;

/**
 * Author WZL
 * Date：2018/7/25 31
 * Notes:
 */
public class ThemeCommendNumEventBus {
    public ThemeCommendNumEventBus(int commentCount) {
        this.commentCount = commentCount;
    }

    private int commentCount;

    public int getPosition() {
        return commentCount;
    }
}
