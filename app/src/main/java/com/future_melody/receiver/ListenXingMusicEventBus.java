package com.future_melody.receiver;

/**
 * Author WZL
 * Date：2018/7/25 31
 * Notes:
 */
public class ListenXingMusicEventBus {
    public ListenXingMusicEventBus(int position) {
        this.position = position;
    }

    private int position;

    public int getPosition() {
        return position;
    }
}
