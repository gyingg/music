package com.future_melody.receiver;

/**
 * Author WZL
 * Date：2018/8/10 16
 * Notes:
 */
public class ToNewFragmentEventBus {
    private int isShow;

    public ToNewFragmentEventBus(int isShow) {
        this.isShow = isShow;
    }

    public int getIsShow() {
        return isShow;
    }
}
