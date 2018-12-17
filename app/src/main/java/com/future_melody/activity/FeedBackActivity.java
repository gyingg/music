package com.future_melody.activity;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.FeedBackRequest;
import com.future_melody.net.respone.FeedBackRespone;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.TipLinearUtil;
import com.gyf.barlibrary.ImmersionBar;

/**
 * 意见反馈
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener {

    private EditText feedback_content;
    private TextView feedbackContentSize;
    private TextView ensure;
    private TextView abolish;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        feedback_content = (EditText) findViewById(R.id.feedback_content);
        feedbackContentSize = (TextView) findViewById(R.id.feedback_content_size);
        ensure = findViewById(R.id.ensure);
        abolish = findViewById(R.id.abolish);
        ensure.setOnClickListener(this);
        abolish.setOnClickListener(this);
        feedback_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String len = feedback_content.getText().toString().trim();
                if (!TextUtils.isEmpty(len)) {
                    int lenSize = len.length();
                    feedbackContentSize.setText(lenSize + "/500");
                    if (lenSize == 500)
                        TipLinearUtil.create(mActivity).showTipMessage("输入字数已达上限");
                    return;
                } else feedbackContentSize.setText(0 + "/500");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {
        InputFilter[] emoji = {CommonUtils.enmoji(mActivity)};
        feedback_content.setFilters(emoji);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ensure:
                String a = feedback_content.getText().toString().trim();
                if (!TextUtils.isEmpty(a) && a.length() > 0) {
                    addfeedback(userId(), a);  //请求接口
                    hideSoftInputView();
                    toast("意见反馈提交成功");
                } else
                    TipLinearUtil.create(mActivity).showTipMessage("反馈内容不能为空");
                break;
            case R.id.abolish:
                finish();
                break;
        }
    }


    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }
    private void addfeedback(String userid, String opinion) {
        addSubscribe(apis.myinform(new FeedBackRequest(userId(), opinion))
                .compose(RxHttpUtil.<FutureHttpResponse<FeedBackRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<FeedBackRespone>handleResult())
                .subscribeWith(new HttpSubscriber<FeedBackRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(FeedBackRespone FeedBackRespone) {

                    }

                    @Override
                    public void onComplete() {
                        finish();
                    }
                }));
    }

}
