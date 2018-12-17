package com.future_melody.activity;

import android.content.Context;
import android.content.Intent;
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
import com.future_melody.common.SPconst;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.FindMoneyPswRequest;
import com.future_melody.net.request.SetMoneyPswRequest;
import com.future_melody.net.respone.FindMoneyPswRespone;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.SetMoneyPswRespone;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.TipLinearUtil;
import com.future_melody.utils.passwordview.MD5Tool;
import com.lzx.musiclibrary.manager.MusicManager;

/**
 * 设置资金密码
 */
public class FundPasswordActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private ImageView ph_title_right_img;
    private Animation animation;
    private TextView text_title;
    private EditText et_one;
    private EditText et_two;
    private Button btn_ok;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_fund_password;
    }

    @Override
    protected void initView() {
        back = findViewById(R.id.back);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        text_title = findViewById(R.id.text_title);
        et_one = findViewById(R.id.et_one);
        et_two = findViewById(R.id.et_two);
        btn_ok = findViewById(R.id.btn_ok);
        //  资金密码绑定成功后显示 找回资金密码（FindFundPasswordActivity）
    }

    @Override
    protected void initData() {
        back.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back:
                finish();
                hideSoftInputView();
                break;
            case R.id.btn_ok:
                String md5_et_one = MD5Tool.encryptMD5ToString(et_one.getText().toString());
                String md5_et_two = MD5Tool.encryptMD5ToString(et_two.getText().toString());
                if (TextUtils.isEmpty(md5_et_one)) {
                    toast("请输入资金密码");
                } else if (TextUtils.isEmpty(md5_et_two)) {
                    toast("请再次输入资金密码");
                } else if (!md5_et_one.equals(md5_et_two)) {
                    toast("您输入的密码不一致");
                } else if (md5_et_two.length() < 7) {
                    toast("密码长度只能是6位");
                } else {
                    setPassworld(userId(), md5_et_two);
                }
                hideSoftInputView();
                break;
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
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

    private void setPassworld(String userid, String password) {
        addSubscribe(apis.setMoney(new SetMoneyPswRequest(userid, password))
                .compose(RxHttpUtil.<FutureHttpResponse<SetMoneyPswRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<SetMoneyPswRespone>handleResult())
                .subscribeWith(new HttpSubscriber<SetMoneyPswRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(SetMoneyPswRespone setMoneyPswRespone) {

                    }

                    @Override
                    public void onComplete() {
                        toast("设置成功");
                        SPUtils.getInstance().put(SPconst.ISMONEYPSW, 1);
                        finish();
                    }
                }));
    }
}
