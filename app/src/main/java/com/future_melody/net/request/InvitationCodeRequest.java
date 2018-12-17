package com.future_melody.net.request;

/**
 * Author WZL
 * Date：2018/6/4 12
 * Notes:
 */
public class InvitationCodeRequest {
    private String other_invitationcode;
    private String userid;

    public InvitationCodeRequest(String userid ,String other_invitationcode) {
        this.other_invitationcode = other_invitationcode;
        this.userid = userid;
    }
}
