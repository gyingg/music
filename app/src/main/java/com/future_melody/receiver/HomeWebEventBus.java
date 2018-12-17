package com.future_melody.receiver;

/**
 * Author WZL
 * Date：2018/7/5 41
 * Notes:
 */
public class HomeWebEventBus {
    private String isUpdate;

    public HomeWebEventBus(String isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getMessgae() {
        return isUpdate;
    }
}
