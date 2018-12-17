package com.future_melody.net.respone;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Y on 2018/5/16.
 * 查询关注人的主题
 */

public class AttentionThemeRespone extends FutureHttpResponse implements Parcelable {
    public String userId;
    public String nickname;
    public String userHeadUrl;
    public String planetName;
    public String asteroidName;
    public int isAttention;
    public String specialId;
    public String specialPictureUrl;
    public int musicCount;
    public String specialDescription;
    public String specialCreateTime;
    public int likeCount;
    public int commentCount;

    protected AttentionThemeRespone(Parcel in) {
        userId = in.readString();
        nickname = in.readString();
        userHeadUrl = in.readString();
        planetName = in.readString();
        asteroidName = in.readString();
        isAttention = in.readInt();
        specialId = in.readString();
        specialPictureUrl = in.readString();
        musicCount = in.readInt();
        specialDescription = in.readString();
        specialCreateTime = in.readString();
        likeCount = in.readInt();
        commentCount = in.readInt();
    }

    public static final Creator<AttentionThemeRespone> CREATOR = new Creator<AttentionThemeRespone>() {
        @Override
        public AttentionThemeRespone createFromParcel(Parcel in) {
            return new AttentionThemeRespone(in);
        }

        @Override
        public AttentionThemeRespone[] newArray(int size) {
            return new AttentionThemeRespone[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(nickname);
        parcel.writeString(userHeadUrl);
        parcel.writeString(planetName);
        parcel.writeString(asteroidName);
        parcel.writeInt(isAttention);
        parcel.writeString(specialId);
        parcel.writeString(specialPictureUrl);
        parcel.writeInt(musicCount);
        parcel.writeString(specialDescription);
        parcel.writeString(specialCreateTime);
        parcel.writeInt(likeCount);
        parcel.writeInt(commentCount);
    }
}
