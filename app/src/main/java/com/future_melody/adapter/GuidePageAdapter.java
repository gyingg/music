package com.future_melody.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class GuidePageAdapter extends PagerAdapter {
    private List<View> imageViews;

    private ItemClickListener itemClickListener;

    public GuidePageAdapter(List<View> imageViews) {
        this.imageViews = imageViews;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getCount() {
        return imageViews.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        View imageView = imageViews.get(position);
        container.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.click(position);
                }
            }
        });
        return imageView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface ItemClickListener {
        void click(int position);
    }
}
