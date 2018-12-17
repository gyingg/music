package com.future_melody.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
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
import com.future_melody.common.SPconst;
import com.future_melody.interfaces.ActivityCollector;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.LoginCode;
import com.future_melody.net.request.SendLogin;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.LoginCodeResponse;
import com.future_melody.net.respone.SendLoginResponse;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.TipLinearUtil;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

/**
 * 信息登录
 */
public class LoginMessageActivity extends BaseActivity implements View.OnClickListener {

    private EditText phone_num;
    private TextView getSms;
    private ImageView back;
    private EditText phone_verify;
    private TextView forget_pass;
    private TextView tv_register;
    private TextView pas_login;
    private Button but_send_login;
    private String code;
    private int screenHeight;
    private int keyHeight;
    private LinearLayout mContent;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login_message;
    }

    @Override
    protected void initView() {
        phone_num = findViewById(R.id.phone_num);
        getSms = findViewById(R.id.getSms);
        back = findViewById(R.id.back);
        tv_register = findViewById(R.id.tv_register);
        pas_login = findViewById(R.id.pas_login);
        phone_verify = findViewById(R.id.phone_verify);
        forget_pass = findViewById(R.id.forget_pass);
        forget_pass = findViewById(R.id.forget_pass);
        but_send_login = findViewById(R.id.but_send_login);
        phone_num.setOnClickListener(this);
        back.setOnClickListener(this);
        forget_pass.setOnClickListener(this);
        getSms.setOnClickListener(this);//弹出弹框
        but_send_login.setOnClickListener(this);//弹出弹框
        pas_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        ScrollViewlogin();
    }

    private void ScrollViewlogin() {
        screenHeight = this.getResources().getDisplayMetrics().heightPixels; //获取屏幕高度
        keyHeight = screenHeight / 3;//弹起高度为屏幕高度的1/3
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


    private void SendLogin(String phone, String usercode, String code, String equipment_token) {
        showLoadingDialog();
        addSubscribe(apis.sendLogin(new SendLogin(phone, usercode, code, JPushInterface.getRegistrationID(this), equipment_token))
                .compose(RxHttpUtil.<FutureHttpResponse<SendLoginResponse>>rxSchedulerHelper())
                .compose(RxHttpUtil.<SendLoginResponse>handleResult())
                .subscribeWith(new HttpSubscriber<SendLoginResponse>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        dismissLoadingDialog();
                        toast(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(SendLoginResponse sendLoginResponse) {
                        dismissLoadingDialog();
                        SPUtils.getInstance().put(SPconst.USER_ID, sendLoginResponse.getId());
                        SPUtils.getInstance().put(SPconst.ISlogin, true);
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                        ActivityCollector.finishAll();
                        LogUtil.e("是否第一次", SPUtils.getInstance().getString(SPconst.isRegister) + "");
                        if (TextUtils.isEmpty(SPUtils.getInstance().getString(SPconst.isRegister))) {
                            SPUtils.getInstance().put(SPconst.isRegister, "0");
                        }
                        MobclickAgent.onEvent(mActivity, "10013");
                        Intent intent = new Intent(mActivity, MainNewActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }));
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

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.getSms:
                //获取验证码
                if (TextUtils.isEmpty(phone_num.getText().toString())) {
                    TipLinearUtil.create(mActivity).showTipMessage("请输入手机号");
                } else {
                    initCode(phone_num.getText().toString());
                }
                break;
            case R.id.back:
                // 左上角退出
                finish();
                hideSoftInputView();
                break;
            case R.id.tv_register:
                intent = new Intent(mActivity, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.pas_login:
                intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.forget_pass:
                //忘记密码
                intent = new Intent(mActivity, FindPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.but_send_login:
                //登录
                if (TextUtils.isEmpty(phone_num.getText().toString())) {
                    TipLinearUtil.create(mActivity).showTipMessage("请输入手机号");
                } else if (TextUtils.isEmpty(phone_verify.getText().toString())) {
                    TipLinearUtil.create(mActivity).showTipMessage("请输入验证码");
                } else {
                    LogUtil.e("code", code + "");
                    SendLogin(phone_num.getText().toString(), phone_verify.getText().toString(), code, JPushInterface.getRegistrationID(this));
                }
                hideSoftInputView();
                break;
        }
    }

    /**
     * 请求获取登录验证码
     *
     * @param phone
     */
    private void initCode(String phone) {
        LoginCode loginCode = new LoginCode(phone);
        addSubscribe(apis.logincode(loginCode)
                .compose(RxHttpUtil.<FutureHttpResponse<LoginCodeResponse>>rxSchedulerHelper())
                .compose(RxHttpUtil.<LoginCodeResponse>handleResult())
                .subscribeWith(new HttpSubscriber<LoginCodeResponse>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(LoginCodeResponse loginCodeResponse) {
                        LogUtil.e("onNextcode", loginCodeResponse.yanzhengma + "");
                        code = loginCodeResponse.yanzhengma;
                    }

                    @Override
                    public void onComplete() {
                        getVerificationCode();
                        LogUtil.e("onNextcode", "onComplete");
                    }
                }));
    }

    /**
     * 倒计时方法
     * setEnabled  不可点击
     */
    private void getVerificationCode() {
        getSms.setEnabled(false);
        CountDownTimer countDownTimer = new CountDownTimer(1000 * 60 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getSms.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                getSms.setText("重新获取验证");
                getSms.setEnabled(true);
            }
        };
        countDownTimer.start();
    }
}
