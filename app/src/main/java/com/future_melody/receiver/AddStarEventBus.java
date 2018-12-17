package com.future_melody.receiver;

/**
 * Author WZL
 * Date：2018/7/17 29
 * Notes:EventBus定义消息事件类
 */
public class AddStarEventBus {
    private String messgae;
    private String planet_name;

    public AddStarEventBus(String messgae, String planet_name) {
        this.messgae = messgae;
        this.planet_name = planet_name;
    }

    public String getMessgae() {
        return messgae;
    }

    public String getPlanet_name() {
        return planet_name;
    }
}
