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

/**
 * Author WZL
 * Date：2018/9/3 17
 * Notes:
 */
public class NetworkSuccessActivity extends BaseActivity implements View.OnClickListener {
    private TextView qr_code_next;
    private Animation animation;
    private ImageView back;
    private ImageView ph_title_right_img;
    private String realContent;
    private int isNet;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_network_success;
    }

    @Override
    protected void initView() {
        qr_code_next = findViewById(R.id.qr_code_next);
        back = findViewById(R.id.back);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        initAnim();
    }

    @Override
    protected void initData() {
        qr_code_next.setOnClickListener(this);
        back.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
        Intent intent = getIntent();
        realContent = intent.getStringExtra(CommonConst.XIAOWEIMACSN);
        isNet = getIntent().getIntExtra(CommonConst.XIAOWEI_IS_NET, -1);
        if (isNet == 1) {
            qr_code_next.setText("完成");
        } else {
        }
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.qr_code_next:
                if (isNet == 1) {
                    Intent intent = new Intent(mActivity, RelevanceXiaoWeiActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    bindXiaoWei(realContent);
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
        }
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
                    }
                }) {
                    @Override
                    public void onNext(XiaoWeiQRcodeRespone xiaoWeiQRcodeRespone) {
                    }

                    @Override
                    public void onComplete() {
                        Intent intent = new Intent(mActivity, RelationSuccessActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }));
    }
}
