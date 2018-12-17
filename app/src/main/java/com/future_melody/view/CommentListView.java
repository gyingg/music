package com.future_melody.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.future_melody.utils.MyListView;

/**
 * Created by guianxiang on 16-9-26.
 */

public class CommentListView extends MyListView{

    public void setmListener(OnResizeListener mListener) {
        this.mListener = mListener;
    }

    private OnResizeListener mListener;



    public CommentListView(Context context) {
        super(context);
    }

    public CommentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mListener.OnResize(w, h, oldw, oldh);
    }
}
