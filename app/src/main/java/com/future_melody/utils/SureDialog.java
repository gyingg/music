package com.future_melody.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.future_melody.R;

public class SureDialog extends Dialog {

    private TextView mTvContent;
    private TextView mTvSure;

    public SureDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public SureDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public SureDialog(Context context) {
        super(context);
        initView();
    }




    public TextView getSureView() {
        return mTvSure;
    }

    public void setSureListener(View.OnClickListener listener) {
        mTvSure.setOnClickListener(listener);
    }

    public TextView getContentView() {
        return mTvContent;
    }


    public void setSure(String content) {
        mTvSure.setText(content);
    }

    public void setContent(String str) {
            mTvContent.setText(str);
    }

    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sure, null);
        setCanceledOnTouchOutside(false);
        mTvSure = (TextView) dialogView.findViewById(R.id.tv_sure);
        mTvContent = (TextView) dialogView.findViewById(R.id.tv_content);
        mTvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTvContent.setTextIsSelectable(true);
        setContentView(dialogView);
    }

}
