package com.future_melody.mode;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Y on 2018/5/24.
 */

public class Recommend_Theme_Bean implements Parcelable {
    public int commentCount;
    public int likeCount;
    public int musicCount;
    public String specialCreateTime;
    public String specialId;
    public String specialPictureUrl;
    public String specialTitle;
    public int width;
    public int height;

    protected Recommend_Theme_Bean(Parcel in) {
        commentCount = in.readInt();
        likeCount = in.readInt();
        musicCount = in.readInt();
        specialCreateTime = in.readString();
        specialId = in.readString();
        specialPictureUrl = in.readString();
        specialTitle = in.readString();
        width = in.readInt();
        height = in.readInt();
    }

    public static final Creator<Recommend_Theme_Bean> CREATOR = new Creator<Recommend_Theme_Bean>() {
        @Override
        public Recommend_Theme_Bean createFromParcel(Parcel in) {
            return new Recommend_Theme_Bean(in);
        }

        @Override
        public Recommend_Theme_Bean[] newArray(int size) {
            return new Recommend_Theme_Bean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(commentCount);
        parcel.writeInt(likeCount);
        parcel.writeInt(musicCount);
        parcel.writeString(specialCreateTime);
        parcel.writeString(specialId);
        parcel.writeString(specialPictureUrl);
        parcel.writeString(specialTitle);
        parcel.writeInt(width);
        parcel.writeInt(height);
    }
}
