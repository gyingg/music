package com.future_melody.net.respone;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Y on 2018/9/19.
 */

public class TelGiveRespone extends FutureHttpResponse implements Parcelable{
    public String nickname;
    public String head_portrait;
    public String user_name;

    protected TelGiveRespone(Parcel in) {
        nickname = in.readString();
        head_portrait = in.readString();
    }

    public static final Creator<TelGiveRespone> CREATOR = new Creator<TelGiveRespone>() {
        @Override
        public TelGiveRespone createFromParcel(Parcel in) {
            return new TelGiveRespone(in);
        }

        @Override
        public TelGiveRespone[] newArray(int size) {
            return new TelGiveRespone[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nickname);
        dest.writeString(head_portrait);
    }
}
