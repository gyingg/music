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
import com.future_melody.common.SPconst;
import com.future_melody.interfaces.ActivityCollector;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.Login;
import com.future_melody.net.request.MsgCode;
import com.future_melody.net.request.Register;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.LoginResponse;
import com.future_melody.net.respone.MsgCodeResponse;
import com.future_melody.net.respone.RegisterResponse;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.TipLinearUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 注册界面
 */
public class RegisterActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.et_register_tel)
    EditText etRegisterTel;
    @BindView(R.id.tv_getmsg_mode)
    TextView tvGetmsgMode;
    @BindView(R.id.et_register_pas)
    EditText etRegisterPas;
    @BindView(R.id.but_register)
    Button butRegister;
    @BindView(R.id.tv_ready_logn)
    TextView tvReadyLogn;
    @BindView(R.id.et_phone_code)
    EditText etPhoneCode;
    @BindView(R.id.et_invitation)
    EditText etInvitation;
    @BindView(R.id.tv_invite_register)
    TextView tvInvite;
    private String code;
    private int screenHeight;
    private int keyHeight;
    private LinearLayout mContent;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        String getinvite = SPUtils.getInstance().getString(SPconst.isInviteRegister);
        tvInvite.setText(getinvite);
    }

    @Override
    protected void initData() {
        ScrollViewlogin();
    }

    private void ScrollViewlogin() {
        screenHeight = this.getResources().getDisplayMetrics().heightPixels; //获取屏幕高度
        keyHeight = screenHeight / 2;//弹起高度为屏幕高度的1/3
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
                    //pwd_text.setVisibility(View.GONE);
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


    @OnClick({R.id.back, R.id.tv_getmsg_mode, R.id.but_register, R.id.tv_ready_logn, R.id.use_clause})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.use_clause:
                intent = new Intent(this, UserClauseActivity.class);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                hideSoftInputView();
                break;
            case R.id.tv_getmsg_mode:
                if (CommonUtils.isMobileNO(etRegisterTel.getText().toString())) {
                    if (TextUtils.isEmpty(etRegisterTel.getText().toString())) {
                        TipLinearUtil.create(mActivity).showTipMessage("请输入手机号");
                    } else {
                        getCode(etRegisterTel.getText().toString());
                    }
                } else {
                    TipLinearUtil.create(mActivity).showTipMessage("请输入正确的手机号");
                }

                break;
            case R.id.but_register:
                if (CommonUtils.isMobileNO(etRegisterTel.getText().toString())) {
                    if (TextUtils.isEmpty(etRegisterTel.getText().toString())) {
                        TipLinearUtil.create(mActivity).showTipMessage("请输入手机号");
                    } else if (TextUtils.isEmpty(etPhoneCode.getText().toString())) {
                        TipLinearUtil.create(mActivity).showTipMessage("请输入验证码");
                    } else if (TextUtils.isEmpty(etRegisterPas.getText().toString())) {
                        TipLinearUtil.create(mActivity).showTipMessage("请输入密码");
                    } else {
                        initRegister(etRegisterTel.getText().toString(), etRegisterPas.getText().toString(), etPhoneCode.getText().toString(), etInvitation.getText().toString(), JPushInterface.getRegistrationID(this));
                    }
                } else {
                    TipLinearUtil.create(mActivity).showTipMessage("请输入正确的手机号");
                }
                hideSoftInputView();
                break;
            case R.id.tv_ready_logn:
                intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getCode(String phone) {
        MsgCode msgCode = new MsgCode(phone);
        addSubscribe(apis.msgCode(msgCode)
                .compose(RxHttpUtil.<FutureHttpResponse<MsgCodeResponse>>rxSchedulerHelper())
                .compose(RxHttpUtil.<MsgCodeResponse>handleResult())
                .subscribeWith(new HttpSubscriber<MsgCodeResponse>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
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
     * 注册
     *
     * @param mobile
     * @param password
     * @param usercode
     */
    private void initRegister(String mobile, String password, String usercode, String other_invitationcode, String equipment_token) {
        showLoadingDialog();
        Register register = new Register(mobile, password, usercode, other_invitationcode, equipment_token);
        addSubscribe(apis.register(register)
                .compose(RxHttpUtil.<FutureHttpResponse<RegisterResponse>>rxSchedulerHelper())
                .compose(RxHttpUtil.<RegisterResponse>handleResult())
                .subscribeWith(new HttpSubscriber<RegisterResponse>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        dismissLoadingDialog();
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(RegisterResponse registerModel) {
                        dismissLoadingDialog();

                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                        if (TextUtils.isEmpty(SPUtils.getInstance().getString(SPconst.isRegister))) {
                            SPUtils.getInstance().put(SPconst.isRegister, "0");
                        }
                        login(mobile, password);
                    }
                }));
    }

    /**
     * 倒计时方法
     * setEnabled  不可点击
     */
    private void getVerificationCode() {
        tvGetmsgMode.setEnabled(false);
        CountDownTimer countDownTimer = new CountDownTimer(1000 * 60 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvGetmsgMode.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                tvGetmsgMode.setText("重新获取验证");
                tvGetmsgMode.setEnabled(true);
            }
        };
        countDownTimer.start();
    }

    /**
     * 登录
     *
     * @param mobile
     * @param password
     */
    private void login(final String mobile, String password) {
        addSubscribe(apis.login(new Login(mobile, password, JPushInterface.getRegistrationID(this)))
                .compose(RxHttpUtil.<FutureHttpResponse<LoginResponse>>rxSchedulerHelper())
                .compose(RxHttpUtil.<LoginResponse>handleResult())
                .subscribeWith(new HttpSubscriber<LoginResponse>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        SPUtils.getInstance().put(SPconst.USER_ID, loginResponse.getId());
                        SPUtils.getInstance().put(SPconst.NICKNAME, loginResponse.getNickname());
                        SPUtils.getInstance().put(SPconst.ISlogin, true);
                    }

                    @Override
                    public void onComplete() {
                        ActivityCollector.finishAll();
                        MobclickAgent.onEvent(mActivity, "10013");
                        toast("登录成功");
                        Intent intent = new Intent(mActivity, MainNewActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
