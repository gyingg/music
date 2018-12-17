package com.future_melody.activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.music.PlayerUitlis;
import com.gyf.barlibrary.ImmersionBar;
import com.lzx.musiclibrary.manager.MusicManager;

/**
 * 转出黑珍珠
 */
public class AccountRollOutActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private ImageView ph_title_right_img;
    private Animation animation;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_account_roll_out;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).init();
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        back = findViewById(R.id.back);
    }

    @Override
    protected void initData() {
        back.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back:
                finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAnmi();
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
