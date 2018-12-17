package com.future_melody.net.respone;

import com.future_melody.mode.XingMusicSetTomModel;

import java.util.List;

/**
 * Author WZL
 * Date：2018/5/29 45
 * Notes:
 */
public class XingMusicSetTopResopone extends FutureHttpResponse {
    public int rownum;
    public int type;
    public List<XingMusicSetTomModel> users;
}
