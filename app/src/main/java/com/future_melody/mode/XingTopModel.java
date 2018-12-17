package com.future_melody.mode;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author WZL
 * Date：2018/8/22 09
 * Notes:
 */
public class XingTopModel implements Parcelable{
    public String no;
    public String musicId;
    public String coverUrl;
    public String musicPath;
    public String musicName;
    public String singerName;
    public String likeCount;
    public int isLike;
    public int isCollection;
    public String userId;
    public String nickname;
    public String planetName;
    public String asteroidName;
    public String lyrics;
    public String source;

    protected XingTopModel(Parcel in) {
        no = in.readString();
        musicId = in.readString();
        coverUrl = in.readString();
        musicPath = in.readString();
        musicName = in.readString();
        singerName = in.readString();
        likeCount = in.readString();
        isLike = in.readInt();
        isCollection = in.readInt();
        userId = in.readString();
        nickname = in.readString();
        planetName = in.readString();
        asteroidName = in.readString();
        lyrics = in.readString();
        source = in.readString();
    }

    public static final Creator<XingTopModel> CREATOR = new Creator<XingTopModel>() {
        @Override
        public XingTopModel createFromParcel(Parcel in) {
            return new XingTopModel(in);
        }

        @Override
        public XingTopModel[] newArray(int size) {
            return new XingTopModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(no);
        parcel.writeString(musicId);
        parcel.writeString(coverUrl);
        parcel.writeString(musicPath);
        parcel.writeString(musicName);
        parcel.writeString(singerName);
        parcel.writeString(likeCount);
        parcel.writeInt(isLike);
        parcel.writeInt(isCollection);
        parcel.writeString(userId);
        parcel.writeString(nickname);
        parcel.writeString(planetName);
        parcel.writeString(asteroidName);
        parcel.writeString(lyrics);
        parcel.writeString(source);
    }
}
