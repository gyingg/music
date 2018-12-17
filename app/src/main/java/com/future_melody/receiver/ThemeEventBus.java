package com.future_melody.receiver;

import com.future_melody.net.respone.MineReconmendThemeRespone;

/**
 * Author WZL
 * Date：2018/7/4 15
 * Notes:
 */
public class ThemeEventBus {
    private String messgae;
    private Object respone;
    private int position;

    public ThemeEventBus(Object respone, int position) {
        this.respone = respone;
        this.position = position;
    }


    public ThemeEventBus(String messgae) {
        this.messgae = messgae;
    }

    public String getMessgae() {
        return messgae;
    }

    public Object getRespone() {
        return respone;
    }

    public int position() {
        return position;
    }
}
