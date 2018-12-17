package com.future_melody.receiver;

/**
 * Author WZL
 * Date：2018/7/5 41
 * Notes:
 */
public class SendWebPlayerEventBus {
    private boolean isPlayer;

    public SendWebPlayerEventBus(boolean isPlayer) {
        this.isPlayer = isPlayer;
    }

    public boolean getMessgae() {
        return isPlayer;
    }
}
