package com.future_melody.net.respone;

import com.future_melody.fragment.Recommend_theme;
import com.future_melody.mode.LikeBean;
import com.future_melody.mode.Recommend_Theme_Bean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Y on 2018/5/24.
 */

public class Recommend_theme_Respne extends FutureHttpResponse{
    public String specialCount;
    public List<Recommend_Theme_Bean> specialVoList;
}
