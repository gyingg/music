package com.future_melody.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * .
 * Created by Administrator on 2018/8/17/017.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = dp2px(parent.getContext(),8f);

        if (parent.getChildAdapterPosition(view) %2== 0) {
            outRect.bottom = dp2px(parent.getContext(),8f);
        }else
            outRect.bottom = 0;

    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
