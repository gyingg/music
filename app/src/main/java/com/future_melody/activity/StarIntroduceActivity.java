package com.future_melody.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
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
import com.future_melody.net.request.StarIntroduce;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.StarIntroduceRespone;
import com.future_melody.utils.CommonUtils;
import com.lzx.musiclibrary.manager.MusicManager;
import com.umeng.socialize.media.Base;

//星球介绍
public class StarIntroduceActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private ImageView ph_title_right_img;
    private Button btn_send;
    private EditText feedback_content;
    private Animation animation;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_star_introduce;
    }

    @Override
    protected void initView() {
        back = findViewById(R.id.back);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        btn_send = findViewById(R.id.btn_send);
        feedback_content = findViewById(R.id.feedback_content);
    }

    @Override
    protected void initData() {
        back.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        initAnim();
        InputFilter[] emoji = {CommonUtils.enmoji(mActivity)};
        feedback_content.setFilters(emoji);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
            case R.id.btn_send:
                if (TextUtils.isEmpty(feedback_content.getText().toString())) {
                    toast("请输入星球介绍");
                } else {
                    setInfo("", feedback_content.getText().toString());
                }
                break;
        }
    }

    private void setInfo(String img, String et) {
        addSubscribe(apis.starIntroduce(new StarIntroduce(userId(), img, et))
                .compose(RxHttpUtil.<FutureHttpResponse<StarIntroduceRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<StarIntroduceRespone>handleResult())
                .subscribeWith(new HttpSubscriber<StarIntroduceRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                    }
                }) {
                    @Override
                    public void onNext(StarIntroduceRespone starIntroduceRespone) {
                    }

                    @Override
                    public void onComplete() {
                        finish();
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAnmi();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
