package com.future_melody.receiver;

/**
 * Author WZL
 * Date：2018/7/5 41
 * Notes:
 */
public class SendWebEventBus {
    private String isUpdate;

    public SendWebEventBus(String isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getMessgae() {
        return isUpdate;
    }
}
