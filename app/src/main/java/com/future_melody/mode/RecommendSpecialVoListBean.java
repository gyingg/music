package com.future_melody.mode;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author WZL
 * Date：2018/7/4 14
 * Notes:
 */
public class RecommendSpecialVoListBean implements Parcelable {
    private String userId;
    private String nickname;
    private String userHeadUrl;
    private String planetName;
    private String asteroidName;
    private int isAttention;
    private String specialId;
    private String specialPictureUrl;
    private int musicCount;
    private String specialDescription;
    private String specialCreateTime;
    private int likeCount;
    private int commentCount;
    private String specialTitle;
    public String rulerUserId;

    protected RecommendSpecialVoListBean(Parcel in) {
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
        specialTitle = in.readString();
    }

    public static final Creator<RecommendSpecialVoListBean> CREATOR = new Creator<RecommendSpecialVoListBean>() {
        @Override
        public RecommendSpecialVoListBean createFromParcel(Parcel in) {
            return new RecommendSpecialVoListBean(in);
        }

        @Override
        public RecommendSpecialVoListBean[] newArray(int size) {
            return new RecommendSpecialVoListBean[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserHeadUrl() {
        return userHeadUrl;
    }

    public void setUserHeadUrl(String userHeadUrl) {
        this.userHeadUrl = userHeadUrl;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }

    public String getAsteroidName() {
        return asteroidName;
    }

    public void setAsteroidName(String asteroidName) {
        this.asteroidName = asteroidName;
    }

    public int getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(int isAttention) {
        this.isAttention = isAttention;
    }

    public String getSpecialId() {
        return specialId;
    }

    public void setSpecialId(String specialId) {
        this.specialId = specialId;
    }

    public String getSpecialPictureUrl() {
        return specialPictureUrl;
    }

    public void setSpecialPictureUrl(String specialPictureUrl) {
        this.specialPictureUrl = specialPictureUrl;
    }

    public int getMusicCount() {
        return musicCount;
    }

    public void setMusicCount(int musicCount) {
        this.musicCount = musicCount;
    }

    public String getSpecialDescription() {
        return specialDescription;
    }

    public void setSpecialDescription(String specialDescription) {
        this.specialDescription = specialDescription;
    }

    public String getSpecialCreateTime() {
        return specialCreateTime;
    }

    public void setSpecialCreateTime(String specialCreateTime) {
        this.specialCreateTime = specialCreateTime;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getSpecialTitle() {
        return specialTitle;
    }

    public void setSpecialTitle(String specialTitle) {
        this.specialTitle = specialTitle;
    }

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
        parcel.writeString(specialTitle);
    }
}
