package com.future_melody.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.CommonConst;
import com.future_melody.common.SPconst;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.Login;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.LoginResponse;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.TipLinearUtil;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;


/**
 * 登录界面 TextWatcher监听
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText phone_pwd;
    private ImageView back;
    private EditText phone_num;
    private TextView tv_register;
    private TextView forget_pass;
    private Button but_login;
    private TextView message_login;
    private int isFinish;
    private int screenHeight;
    private int keyHeight;
    private LinearLayout mContent;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        phone_pwd = findViewById(R.id.phone_pwd);
        back = findViewById(R.id.back);
        phone_num = findViewById(R.id.phone_num);
        tv_register = findViewById(R.id.tv_register);
        but_login = findViewById(R.id.but_login);
        message_login = findViewById(R.id.message_login);
        forget_pass = findViewById(R.id.forget_pass);
        back.setOnClickListener(this);
        phone_num.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        but_login.setOnClickListener(this);
        message_login.setOnClickListener(this);
        forget_pass.setOnClickListener(this);
        phone_pwd.setOnClickListener(this);
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


    @Override
    protected void initData() {
        ScrollViewlogin();
        Intent intent = getIntent();
        isFinish = intent.getIntExtra(CommonConst.ISFINISH, -1);
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
            case R.id.message_login:
                intent = new Intent(mActivity, LoginMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_register:
                intent = new Intent(mActivity, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.back:
                // 左上角退出
                finish();
                hideSoftInputView();
                break;
            case R.id.but_login:
                //登录
                if (CommonUtils.isMobileNO(phone_num.getText().toString())) {
                    if (TextUtils.isEmpty(phone_num.getText().toString())) {
                        TipLinearUtil.create(mActivity).showTipMessage("手机号不能为空");
                        return;
                    }
                    if (TextUtils.isEmpty(phone_pwd.getText().toString())) {
                        TipLinearUtil.create(mActivity).showTipMessage("请输入登录密码");
                        return;
                    } else {
                        login(phone_num.getText().toString(), phone_pwd.getText().toString());
                    }
                } else {
                    TipLinearUtil.create(mActivity).showTipMessage("请输入正确的手机号");
                }
                hideSoftInputView();
                break;
            case R.id.forget_pass:
                //忘记密码
                intent = new Intent(mActivity, FindPasswordActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 请求登录界面
     *
     * @param mobile
     * @param password
     */
    public void login(String mobile, String password) {
        showLoadingDialog();
        addSubscribe(apis.login(new Login(mobile, password, JPushInterface.getRegistrationID(this)))
                .compose(RxHttpUtil.<FutureHttpResponse<LoginResponse>>rxSchedulerHelper())
                .compose(RxHttpUtil.<LoginResponse>handleResult())
                .subscribeWith(new HttpSubscriber<LoginResponse>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        dismissLoadingDialog();
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        dismissLoadingDialog();
                        MobclickAgent.onEvent(mActivity, "10013");
                        SPUtils.getInstance().put(SPconst.USER_ID, loginResponse.getId());
                        SPUtils.getInstance().put(SPconst.ISlogin, true);
                        LogUtil.e("onNext", SPUtils.getInstance().getString(SPconst.USER_ID) + "");
                        Toast.makeText(mActivity, "登录成功", Toast.LENGTH_SHORT).show();
                        if (isFinish == 1) {
                            finish();
                        } else {
                            Intent intent = new Intent(mActivity, MainNewActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                        LogUtil.e("onComplete", SPUtils.getInstance().getString(SPconst.USER_ID) + "");
                    }
                }));
    }
}
