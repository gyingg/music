package com.future_melody.utils;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.future_melody.R;

/**
 * 回复对话框
 *
 * @author Jaiky
 * @date 2015-3-30 PS: Not easy to write code, please indicate.
 */
public class ReplyDialog extends Dialog {

    private EditText etContent;
    private TextView llBtnReply;

    private Context mContext;

    public ReplyDialog(Context context) {
        super(context, R.style.MyNoFrame_Dialog);
        mContext = context;
        init();
    }

    private ReplyDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_replyform);

        // 设置宽度
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        LayoutParams lp = window.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);


        etContent = (EditText) findViewById(R.id.et_content);
        llBtnReply = (TextView) findViewById(R.id.theme_details_send);

        InputFilter[] emoji = {CommonUtils.enmoji(mContext)};
        etContent.setFilters(emoji);
        // 弹出键盘
        etContent.setFocusable(true);
        etContent.setFocusableInTouchMode(true);
        etContent.requestFocus();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etContent, 0);
            }
        }, 200);

    }


    public ReplyDialog setContent(String content) {
        etContent.setText(content);
        return this;
    }

    public ReplyDialog setHintText(String hint) {
        etContent.setHint(hint);
        return this;
    }
    //获取文本内容
    public String getContent() {
        return etContent.getText().toString();
    }
    //发送点击事件
    public ReplyDialog setOnBtnCommitClickListener(
            android.view.View.OnClickListener onClickListener) {
        llBtnReply.setOnClickListener(onClickListener);
        return this;
    }
}
