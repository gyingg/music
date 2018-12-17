package com.future_melody.net.respone;

import com.future_melody.mode.BannerModel;
import com.future_melody.mode.XingMusicModel;

import java.util.List;

/**
 * Author WZL
 * Date：2018/5/24 46
 * Notes:
 */
public class XingMusicRespone extends FutureHttpResponse{
    public List<BannerModel> activeList;
    public List<XingMusicModel> starMusicVoList;
    public int starMusicCount;
    public int isStarMusicList;
    public String word;
    public String word2;
}
