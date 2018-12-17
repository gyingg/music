package com.future_melody.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.future_melody.R;
import com.future_melody.activity.XingMusicTopDetalisActivity;
import com.future_melody.mode.XingMusicTopModel;
import com.future_melody.widget.cardview.CardHandler;
import com.future_melody.widget.cardview.CardViewPager;

import java.util.ArrayList;

@SuppressLint("ParcelCreator")
public class XingMusicHandler implements CardHandler<XingMusicTopModel> {

    private ItemClickListener itemClickListener;

    @Override
    public View onBind(final Context context, final XingMusicTopModel data, final int position, @CardViewPager.TransformerMode int mode) {
        View view = View.inflate(context, R.layout.item_xing_music_top, null);
        ImageView xing_top_img = view.findViewById(R.id.xing_top_img);
        ListView xing_top_listvew = view.findViewById(R.id.xing_top_listvew);
        TextView text_title = view.findViewById(R.id.text_title);
        RelativeLayout btn_layout_details = view.findViewById(R.id.btn_layout_details);
        XingMusicTopNewAdapyer newAdapyer = new XingMusicTopNewAdapyer(context, data.list);
        xing_top_listvew.setAdapter(newAdapyer);
//        ImageView imageView = (ImageView) view.findViewById(R.id.image);
//        ElasticCardView cardView = (ElasticCardView) view.findViewById(R.id.cardview);
//        final boolean isCard = mode == CardViewPager.MODE_CARD;
//        cardView.setPreventCornerOverlap(isCard);
//        cardView.setUseCompatPadding(isCard);
//        final String img = data.getSongCover();
//        Glide.with(context).load(img).into(imageView);
//        view.setOnClickListener(v -> {
//            itemClickListener.set(position);
//        });
        xing_top_img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        text_title.setText(data.name);
        if (data.list.size() > 0) {
            Glide.with(context).load(data.list.get(0).coverUrl).into(xing_top_img);
            btn_layout_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, XingMusicTopDetalisActivity.class);
                    intent.putParcelableArrayListExtra("xing", (ArrayList<? extends Parcelable>) data.list);
                    context.startActivity(intent);
                }
            });
        }
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
