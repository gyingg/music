package com.future_melody.net.respone;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author WZL
 * Date：2018/5/16 49
 * Notes:
 */
public class MineReconmendThemeRespone extends FutureHttpResponse implements Parcelable {
    public String specialid;// 专题id
    public String special_picture;//专题图片
    public String special_describe;//专题描述
    public String recommend_time;// 上传时间
    public int musicnumber;//音乐数量
    public int likesnumber;//点赞数量S
    public int commentsnumber;//评论数量
    public String special_title;  //专题名字
    public String create_times;  //发布时间

    protected MineReconmendThemeRespone(Parcel in) {
        specialid = in.readString();
        special_picture = in.readString();
        special_describe = in.readString();
        recommend_time = in.readString();
        musicnumber = in.readInt();
        likesnumber = in.readInt();
        commentsnumber = in.readInt();
        special_title = in.readString();
        create_times = in.readString();
    }

    public static final Creator<MineReconmendThemeRespone> CREATOR = new Creator<MineReconmendThemeRespone>() {
        @Override
        public MineReconmendThemeRespone createFromParcel(Parcel in) {
            return new MineReconmendThemeRespone(in);
        }

        @Override
        public MineReconmendThemeRespone[] newArray(int size) {
            return new MineReconmendThemeRespone[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(specialid);
        parcel.writeString(special_picture);
        parcel.writeString(special_describe);
        parcel.writeString(recommend_time);
        parcel.writeInt(musicnumber);
        parcel.writeInt(likesnumber);
        parcel.writeInt(commentsnumber);
        parcel.writeString(special_title);
        parcel.writeString(create_times);
    }
}
