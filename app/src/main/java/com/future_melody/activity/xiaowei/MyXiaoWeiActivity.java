package com.future_melody.activity.xiaowei;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.MineXiaoWeiRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MineXiaoWeiRespone;
import com.lzx.musiclibrary.manager.MusicManager;

import butterknife.BindView;

public class MyXiaoWeiActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ph_title_right_img)
    ImageView phTitleRightImg;
    @BindView(R.id.btn_to_all_profit)
    RelativeLayout btnToAllProfit;
    @BindView(R.id.xiaowei_dministration)
    RelativeLayout xiaoweiDministration;
    @BindView(R.id.text_perle_num)
    TextView textPerleNum;
    @BindView(R.id.text_xiaowei_num)
    TextView textXiaoweiNum;
    private Animation animation;
    private int type;
    private String scanPic;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_xiao_wei;
    }

    @Override
    protected void initView() {
        setTitle("我的小未");
        setBlackBackble();
        setBarColor(R.color.white, true);
        setTitleLayoutColor(mActivity, R.color.white);
        setTitleColor(R.color.color_333333);
        initAnim();
        phTitleRightImg.setImageResource(R.mipmap.back_music);
        btnToAllProfit.setOnClickListener(this);
        xiaoweiDministration.setOnClickListener(this);
        phTitleRightImg.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (MusicManager.isPlaying()) {
            startAnmi();
        } else {
            stoptAnmi();
        }
        getData();
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
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_to_all_profit:
                intent = new Intent(mActivity, NoirPerleActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.xiaowei_dministration:
                if (type > 0) {
                    intent = new Intent(mActivity, RelevanceXiaoWeiActivity.class);
                    intent.putExtra("scanPic", scanPic);
                    startActivity(intent);
                } else {
                    intent = new Intent(mActivity, AddXiaoweiQRcodeActivity.class);
                    intent.putExtra("scanPic", scanPic);
                    startActivity(intent);
                }
                break;
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
        }
    }

    private void getData() {
        showLoadingDialog();
        addSubscribe(apis.getMineXiaoWei(new MineXiaoWeiRequest(userId()))
                .compose(RxHttpUtil.<FutureHttpResponse<MineXiaoWeiRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<MineXiaoWeiRespone>handleResult())
                .subscribeWith(new HttpSubscriber<MineXiaoWeiRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                        dismissLoadingDialog();
                    }
                }) {
                    @Override
                    public void onNext(MineXiaoWeiRespone mineXiaoWeiRespone) {
                        dismissLoadingDialog();
                        textPerleNum.setText(mineXiaoWeiRespone.bp + "");
                        textXiaoweiNum.setText(mineXiaoWeiRespone.xwCount + "台");
                        type = mineXiaoWeiRespone.xwCount;
                        scanPic = mineXiaoWeiRespone.scanPic;
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                    }
                }));
    }
}
