package com.future_melody.activity;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.FindMoneyPswRequest;
import com.future_melody.net.request.MsgCode;
import com.future_melody.net.respone.FindMoneyPswRespone;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MsgCodeResponse;
import com.future_melody.utils.TipLinearUtil;
import com.future_melody.utils.passwordview.MD5Tool;
import com.lzx.musiclibrary.manager.MusicManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 找回资金密码
 */
public class FindFundPasswordActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.btn_to_player)
    ImageView btnToPlayer;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_get_code)
    TextView btnGetCode;
    @BindView(R.id.et_new_psw)
    EditText etNewPsw;
    @BindView(R.id.btn_find)
    Button btnFind;
    private Animation animation;
    private CountDownTimer countDownTimer;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_find_fund_password;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
    }

    private void findPassworld(String userid, String password, String code, String phone) {
        addSubscribe(apis.findMoney(new FindMoneyPswRequest(userid, password, code, phone))
                .compose(RxHttpUtil.<FutureHttpResponse<FindMoneyPswRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<FindMoneyPswRespone>handleResult())
                .subscribeWith(new HttpSubscriber<FindMoneyPswRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(FindMoneyPswRespone findMoneyPswRespone) {

                    }

                    @Override
                    public void onComplete() {
                        toast("找回成功");
                        finish();
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAnmi();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
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
        btnToPlayer.startAnimation(animation);
    }

    private void stoptAnmi() {
        btnToPlayer.clearAnimation();
    }

    private void destroyAnmi() {
        animation.cancel();
        animation = null;
    }

    /**
     * 倒计时方法
     * setEnabled  不可点击
     */
    private void getVerificationCode() {
        btnGetCode.setEnabled(false);
        countDownTimer = new CountDownTimer(1000 * 60 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnGetCode.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                btnGetCode.setText("重新获取验证");
                btnGetCode.setEnabled(true);
            }
        };
        countDownTimer.start();
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

    @OnClick({R.id.back, R.id.btn_to_player, R.id.btn_get_code, R.id.btn_find})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                hideSoftInputView();
                break;
            case R.id.btn_to_player:
                PlayerUitlis.player(mActivity);
                break;
            case R.id.btn_get_code:
                if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    toast("请输入手机号");
                } else {
                    getCode(etPhone.getText().toString());
                }
                break;
            case R.id.btn_find:
                String psw = MD5Tool.encryptMD5ToString(etNewPsw.getText().toString());
                if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    toast("请输入手机号");
                } else if (TextUtils.isEmpty(etCode.getText().toString())) {
                    toast("请输入验证码");
                } else if (TextUtils.isEmpty(etNewPsw.getText().toString())) {
                    toast("请设置您的密码");
                } else if (etNewPsw.getText().toString().length() != 6) {
                    toast("密码长度只能是6位");
                } else {
                    findPassworld(userId(), psw, etCode.getText().toString(), etPhone.getText().toString());
                }
                break;
        }
    }


    //  隐藏软键盘
    public void hideSoftInputView() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
