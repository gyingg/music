package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/5/12 51
 * Notes: 评论列表
 */
public class CommentListRequest {
    private String userId;
    private String specialId;
    private int pageNum;
    private int pageSize;

    public CommentListRequest(String userId, String specialId, int pageNum, int pageSize) {
        this.userId = userId;
        this.specialId = specialId;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
