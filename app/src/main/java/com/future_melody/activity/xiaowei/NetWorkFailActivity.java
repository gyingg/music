package com.future_melody.activity.xiaowei;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.CommonConst;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.XiaoWeiQRcodeRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.XiaoWeiQRcodeRespone;
import com.lzx.musiclibrary.manager.MusicManager;

public class NetWorkFailActivity extends BaseActivity implements View.OnClickListener {

    private Animation animation;
    private ImageView back;
    private ImageView ph_title_right_img;
    private String realContent;
    private TextView start_wifi;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_network_fail;
    }

    @Override
    protected void initView() {
        back = findViewById(R.id.back);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        start_wifi = findViewById(R.id.start_wifi);
        initAnim();
        back.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
        start_wifi.setOnClickListener(this);
    }

    private void initAnim() {
        animation = AnimationUtils.loadAnimation(this, R.anim.player);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
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


    private void startAnmi() {
        ph_title_right_img.startAnimation(animation);
    }

    private void stoptAnmi() {
        ph_title_right_img.clearAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAnmi();
    }

    private void destroyAnmi() {
        animation.cancel();
        animation = null;
    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        realContent = intent.getStringExtra(CommonConst.XIAOWEIMACSN);
    }

    private void bindXiaoWei(String s) {
        showLoadingDialog();
        addSubscribe(apis.getXiaoWeiInfo(new XiaoWeiQRcodeRequest("", s, userId(), 1))
                .compose(RxHttpUtil.<FutureHttpResponse<XiaoWeiQRcodeRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<XiaoWeiQRcodeRespone>handleResult())
                .subscribeWith(new HttpSubscriber<XiaoWeiQRcodeRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                        dismissLoadingDialog();
                        Intent intent = new Intent(mActivity, NetWorkFailActivity.class);
                        intent.putExtra(CommonConst.XIAOWEIMACSN, realContent);
                        startActivity(intent);
                        finish();
                    }
                }) {
                    @Override
                    public void onNext(XiaoWeiQRcodeRespone xiaoWeiQRcodeRespone) {
                    }

                    @Override
                    public void onComplete() {
                        Intent intent = new Intent(mActivity, RelationSuccessActivity.class);
                        startActivity(intent);
                    }
                }));
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.start_wifi:
                bindXiaoWei(realContent);
                break;
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
        }
    }


}
