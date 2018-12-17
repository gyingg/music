package com.future_melody.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.ryan.rv_gallery.GalleryRecyclerView;

/**
 * Author WZL
 * Date：2018/8/14 14
 * Notes:
 */
public class ThemeRecycler extends GalleryRecyclerView {

    public ThemeRecycler(Context context) {
        super(context);
    }

    public ThemeRecycler(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ThemeRecycler(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (getAdapter()!=null)
            if (getAdapter().getItemCount() <= 0) {
                return;
        }
        // 获得焦点后滑动至第0项，避免第0项的margin不对
        smoothScrollToPosition(0);
    }
}
