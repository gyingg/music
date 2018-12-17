package com.future_melody.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.FindPassword;
import com.future_melody.net.request.MsgCode;
import com.future_melody.net.respone.FindPswResponse;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MsgCodeResponse;
import com.future_melody.utils.TipLinearUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author WZL
 * Date：2018/5/9 46
 * Notes:忘记密码
 */
public class FindPasswordActivity extends BaseActivity {

    @BindView(R.id.btn_find_back)
    ImageView btnFindBack;
    @BindView(R.id.et_find_phone)
    EditText etFindPhone;
    @BindView(R.id.et_find_msg)
    EditText etFindMsg;
    @BindView(R.id.btn_get_msg)
    TextView btnGetMsg;
    @BindView(R.id.et_find_psw)
    EditText etFindPsw;
    @BindView(R.id.btn_find_send)
    Button btnFindSend;
    private String code;
    private int screenHeight;
    private int keyHeight;
    private LinearLayout mContent;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_finf_password;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        ScrollViewlogin();
    }

    private void ScrollViewlogin() {
        screenHeight = this.getResources().getDisplayMetrics().heightPixels; //获取屏幕高度
        keyHeight = screenHeight / 2;//弹起高度为屏幕高度的1/2
        ScrollView scrollView = findViewById(R.id.scrollView);
        final LinearLayout pwd_text = findViewById(R.id.pwd_text);
        mContent = findViewById(R.id.content);
        /**
         * 禁止键盘弹起的时候可以滚动
         */
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        scrollView.addOnLayoutChangeListener(new ViewGroup.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
              /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
              现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    Log.e("wenzhihao", "up------>" + (oldBottom - bottom));
                    int dist = mContent.getBottom() - bottom;
                    if (dist > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", 0.0f, -dist);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                    }
                    pwd_text.setVisibility(View.GONE);
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    Log.e("wenzhihao", "down------>" + (bottom - oldBottom));
                    if ((mContent.getBottom() - oldBottom) > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", mContent.getTranslationY(), 0);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                    }
                    pwd_text.setVisibility(View.VISIBLE);
                }
            }
        });
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


    @OnClick({R.id.btn_find_back, R.id.btn_get_msg, R.id.btn_find_send})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_find_back:
                finish();
                hideSoftInputView();
                break;
            case R.id.btn_get_msg:
                if (TextUtils.isEmpty(etFindPhone.getText().toString())) {
                    TipLinearUtil.create(mActivity).showTipMessage("请输入手机号");
                } else {
                    getCode(etFindPhone.getText().toString());
                }
                break;
            case R.id.btn_find_send:
                if (TextUtils.isEmpty(etFindPhone.getText().toString())) {
                    TipLinearUtil.create(mActivity).showTipMessage("请输入手机号");
                } else if (TextUtils.isEmpty(etFindMsg.getText().toString())) {
                    TipLinearUtil.create(mActivity).showTipMessage("请输入验证码");
                } else if (TextUtils.isEmpty(etFindPsw.getText().toString())) {
                    TipLinearUtil.create(mActivity).showTipMessage("请输入密码");
                } else {
                    initFind(etFindPhone.getText().toString(), etFindMsg.getText().toString(), etFindPsw.getText().toString(), code);
                }
                hideSoftInputView();
                break;
        }
    }

    private void initFind(String phone, String msg, String psw, String code) {
        FindPassword findPassword = new FindPassword(phone, psw, msg, code);
        addSubscribe(apis.findPsw(findPassword)
                .compose(RxHttpUtil.<FutureHttpResponse<FindPswResponse>>rxSchedulerHelper())
                .compose(RxHttpUtil.<FindPswResponse>handleResult())
                .subscribeWith(new HttpSubscriber<FindPswResponse>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(FindPswResponse findPswResponse) {

                    }

                    @Override
                    public void onComplete() {
                        Intent intent = new Intent(mActivity, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }));
    }

    private void getCode(String phone) {
        MsgCode msgCode = new MsgCode(phone);
        addSubscribe(apis.msgCode(msgCode)
                .compose(RxHttpUtil.<FutureHttpResponse<MsgCodeResponse>>rxSchedulerHelper())
                .compose(RxHttpUtil.<MsgCodeResponse>handleResult())
                .subscribeWith(new HttpSubscriber<MsgCodeResponse>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(MsgCodeResponse msgCodeResponse) {
                    }

                    @Override
                    public void onComplete() {
                        getVerificationCode();
                    }
                }));
    }

    /**
     * 倒计时方法
     * setEnabled  不可点击
     */
    private void getVerificationCode() {
        btnGetMsg.setEnabled(false);
        CountDownTimer countDownTimer = new CountDownTimer(1000 * 60 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnGetMsg.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                btnGetMsg.setText("重新获取验证");
                btnGetMsg.setEnabled(true);
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}