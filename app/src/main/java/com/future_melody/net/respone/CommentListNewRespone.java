package com.future_melody.net.respone;

import com.future_melody.mode.CommentModel;

import java.util.List;

/**
 * Author WZL
 * Date：2018/8/22 28
 * Notes:
 */
public class CommentListNewRespone extends FutureHttpResponse {
    public List<CommentModel> commentVoList;
    public int count;
}
