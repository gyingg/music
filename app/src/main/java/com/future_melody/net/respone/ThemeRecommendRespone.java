package com.future_melody.net.respone;

import com.future_melody.mode.ThemeRecommendIMusic;
import com.future_melody.mode.ThemeRecommendInfo;
import com.future_melody.mode.ThemeRecommendTheme;

import java.util.List;

/**
 * Author WZL
 * Date：2018/8/13 00
 * Notes:
 */
public class ThemeRecommendRespone extends FutureHttpResponse {
    public ThemeRecommendInfo special;
    public List<ThemeRecommendIMusic> musicVoList;
    public String recommendTxt;
    public List<ThemeRecommendTheme> specialList;
}
