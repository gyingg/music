package com.future_melody.net.respone;

import com.future_melody.mode.RemendMusicNewModle;
import com.future_melody.mode.RemendThemeNewModle;

import java.util.List;

/**
 * Author WZL
 * Date：2018/9/20 54
 * Notes:
 */
public class RemmendNewThemeRespone extends FutureHttpResponse {
    public int commentCount;
    public int isComment;
    public int isLike;
    public int isShare;
    public int likeCount;
    public int shareCount;
    public RemendThemeNewModle special;
    public List<RemendMusicNewModle> musicList;
}
