package com.future_melody.net.respone;

import com.future_melody.mode.Recommend_Music_Bean;
import com.future_melody.mode.Recommend_Theme_Bean;

import java.util.List;

/**
 * Created by Y on 2018/5/24.
 */

public class Recommend_music_Respne  extends FutureHttpResponse {
    public String musicCount;
    public List<Recommend_Music_Bean> musicVoList;
}

