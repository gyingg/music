package com.future_melody.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 修改登陆密码
 */
import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.music.PlayerActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.FindPassword;
import com.future_melody.net.request.MsgCode;
import com.future_melody.net.respone.FindPswResponse;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MsgCodeResponse;
import com.future_melody.utils.TipLinearUtil;
import com.lzx.musiclibrary.manager.MusicManager;

/**
 * 修改登录密码
 */
public class UpdataloginMessageActivity extends BaseActivity implements View.OnClickListener {

    private EditText updata_login_tel;
    private EditText updata_login_password;
    private TextView updata_login_password_yzm;
    private EditText new_login_password;
    private Button updata_login;
    private ImageView back;
    private String code;
    private Animation animation;
    private ImageView ph_title_right_img;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_updatalogin_message;
    }

    @Override
    protected void initView() {
        updata_login_tel = findViewById(R.id.updata_login_tel);
        updata_login_password = findViewById(R.id.updata_login_password);
        updata_login_password_yzm = findViewById(R.id.updata_login_password_yzm);
        new_login_password = findViewById(R.id.new_login_password);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        back = findViewById(R.id.back);
    }

    @Override
    protected void initData() {
        updata_login = findViewById(R.id.updata_login);
        updata_login_tel.setOnClickListener(this);
        updata_login_password.setOnClickListener(this);
        updata_login_password_yzm.setOnClickListener(this);
        new_login_password.setOnClickListener(this);
        updata_login.setOnClickListener(this);
        back.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.updata_login_password_yzm:
                if (TextUtils.isEmpty(updata_login_tel.getText().toString())) {
                    TipLinearUtil.create(mActivity).showTipMessage("请输入手机号");
                } else {
                    getCode(updata_login_tel.getText().toString());
                }
                break;
            case R.id.updata_login:
                if (TextUtils.isEmpty(updata_login_tel.getText().toString())) {
                    TipLinearUtil.create(mActivity).showTipMessage("请输入手机号");
                } else if (TextUtils.isEmpty(updata_login_password.getText().toString())) {
                    TipLinearUtil.create(mActivity).showTipMessage("请输入验证码");
                } else if (TextUtils.isEmpty(new_login_password.getText().toString())) {
                    TipLinearUtil.create(mActivity).showTipMessage("请输入密码");
                } else {
                    initFind(updata_login_tel.getText().toString(), updata_login_password.getText().toString(), new_login_password.getText().toString(), code);
                }
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
        updata_login_password_yzm.setEnabled(false);
        CountDownTimer countDownTimer = new CountDownTimer(1000 * 60 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updata_login_password_yzm.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                updata_login_password_yzm.setText("重新获取验证");
                updata_login_password_yzm.setEnabled(true);
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAnmi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAnim();
        if (MusicManager.isPlaying()) {
            startAnmi();
        } else {
            stoptAnmi();
        }
    }

    private void initAnim() {
        animation = AnimationUtils.loadAnimation(this, R.anim.player);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
    }

    private void startAnmi() {
        ph_title_right_img.startAnimation(animation);
    }

    private void stoptAnmi() {
        ph_title_right_img.clearAnimation();
    }

    private void destroyAnmi() {
        animation.cancel();
        animation = null;
    }
}
