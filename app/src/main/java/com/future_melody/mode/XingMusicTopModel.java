package com.future_melody.mode;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Author WZL
 * Date：2018/8/22 39
 * Notes:
 */
public class XingMusicTopModel implements Parcelable{
    public List<XingTopModel> list;
    public String name;

    protected XingMusicTopModel(Parcel in) {
        list = in.createTypedArrayList(XingTopModel.CREATOR);
        name = in.readString();
    }

    public static final Creator<XingMusicTopModel> CREATOR = new Creator<XingMusicTopModel>() {
        @Override
        public XingMusicTopModel createFromParcel(Parcel in) {
            return new XingMusicTopModel(in);
        }

        @Override
        public XingMusicTopModel[] newArray(int size) {
            return new XingMusicTopModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(list);
        parcel.writeString(name);
    }
}
