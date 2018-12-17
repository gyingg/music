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

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.SPconst;
import com.future_melody.music.PlayerActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.InvitationCodeRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.InvitationCodeRespone;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.TipLinearUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.lzx.musiclibrary.manager.MusicManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 输入邀请码
 */
public class InviteCodeActivity extends BaseActivity {

    @BindView(R.id.et_invitation_code)
    EditText etInvitationCode;
    @BindView(R.id.but_baocun)
    Button butBaocun;
    @BindView(R.id.ph_title_right_img)
    ImageView phTitleRightImg;
    private Animation animation;
    private ImageView ph_title_back;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_invite_code;
    }

    @Override
    protected void initView() {
        setBlackBackble();
    }

    @Override
    protected void initData() {
        etInvitationCode.setHint(SPUtils.getInstance().getString(SPconst.isInviteRegister));
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        setTitle("输入邀请码");
        setTitleColor(R.color.color_333333);
        setBarColor(R.color.white, true);
        phTitleRightImg.setVisibility(View.VISIBLE);
        phTitleRightImg.setImageResource(R.mipmap.back_music);
        ph_title_back = findViewById(R.id.ph_title_back);
        ph_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                hideSoftInputView();
            }
        });

    }


    //  隐藏软键盘
    public void hideSoftInputView() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    private void setConde(String s) {
        showLoadingDialog();
        addSubscribe(apis.invitationCode(new InvitationCodeRequest(userId(), s))
                .compose(RxHttpUtil.<FutureHttpResponse<InvitationCodeRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<InvitationCodeRespone>handleResult())
                .subscribeWith(new HttpSubscriber<InvitationCodeRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        dismissLoadingDialog();
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(InvitationCodeRespone invitationCodeRespone) {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                        finish();
                    }
                }));
    }

    @OnClick({R.id.but_baocun, R.id.ph_title_right_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.but_baocun:
                if (TextUtils.isEmpty(etInvitationCode.getText().toString())) {
                    TipLinearUtil.create(mActivity).showTipMessage("请输入邀请码");
                } else {
                    setConde(etInvitationCode.getText().toString());
                }
                break;
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
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
        phTitleRightImg.startAnimation(animation);
    }

    private void stoptAnmi() {
        phTitleRightImg.clearAnimation();
    }

    private void destroyAnmi() {
        animation.cancel();
        animation = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAnmi();
    }
}
