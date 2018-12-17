package com.future_melody.net.request;

public class Register {
    public String user_name;
    public String password;
    public String usercode;
    public String other_invitationcode;
    public String equipment_token;

    public Register(String user_name, String password, String usercode, String other_invitationcode, String equipment_token) {
        this.user_name = user_name;
        this.password = password;
        this.usercode = usercode;
        this.equipment_token = equipment_token;
        this.other_invitationcode = other_invitationcode;
    }
}
