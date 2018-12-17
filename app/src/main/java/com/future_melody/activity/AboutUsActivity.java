package com.future_melody.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.music.PlayerActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.utils.CommonUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.lzx.musiclibrary.manager.MusicManager;

/**
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout feedback_rel;
    private RelativeLayout what_voice;
    private ImageView back;
    private ImageView ph_title_right_img;
    private Animation animation;
    private TextView tv_code_version;




    @Override
    protected int getContentViewId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        back = findViewById(R.id.back);
        feedback_rel = findViewById(R.id.feedback_rel);
        tv_code_version = findViewById(R.id.tv_code_version);
        what_voice = findViewById(R.id.what_voice);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        feedback_rel.setOnClickListener(this);
        back.setOnClickListener(this);
        what_voice.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tv_code_version.setText(CommonUtils.getVersionName(mActivity));
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.feedback_rel:
                intent = new Intent(mActivity, FeedBackActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
            case R.id.what_voice:
                //官网跳转
                //http://www.futuremelody.cn/
                TencentX5WebviewActivity.startPHLoanWebActivity(mActivity, "http://www.futuremelody.cn/");
                break;
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
}
