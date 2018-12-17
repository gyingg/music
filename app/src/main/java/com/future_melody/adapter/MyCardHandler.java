package com.future_melody.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcel;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.future_melody.R;
import com.future_melody.widget.cardview.CardHandler;
import com.future_melody.widget.cardview.CardViewPager;
import com.future_melody.widget.cardview.ElasticCardView;
import com.lzx.musiclibrary.aidl.model.SongInfo;

@SuppressLint("ParcelCreator")
public class MyCardHandler implements CardHandler<SongInfo> {

    private ItemClickListener itemClickListener;

    @Override
    public View onBind(final Context context, final SongInfo data, final int position, @CardViewPager.TransformerMode int mode) {
        View view = View.inflate(context, R.layout.item_palyer_song_cover, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        final String img = data.getSongCover();
        Glide.with(context).load(img).into(imageView);
        view.setOnClickListener(v -> {
            itemClickListener.set(position);
        });
        return view;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public interface ItemClickListener {
        void set(int i);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
