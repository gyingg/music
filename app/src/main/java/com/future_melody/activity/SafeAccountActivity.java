package com.future_melody.activity;

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
import com.future_melody.common.SPconst;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.utils.SPUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.lzx.musiclibrary.manager.MusicManager;

/**
 * 安全中心
 */
public class SafeAccountActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout updata_login_message;
    private RelativeLayout fund_password;
    private ImageView back;
    private ImageView ph_title_right_img;
    private TextView text_psw_hini;
    private TextView text_psw;
    private Animation animation;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_safe_account;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        updata_login_message = findViewById(R.id.updata_login_message);
        fund_password = findViewById(R.id.fund_password);
        updata_login_message.setOnClickListener(this);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        text_psw = findViewById(R.id.text_psw);
        text_psw_hini = findViewById(R.id.text_psw_hini);
        back = findViewById(R.id.back);


    }

    @Override
    protected void initData() {
        fund_password.setOnClickListener(this);
        back.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.updata_login_message:
                intent = new Intent(this, UpdataloginMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.fund_password:
                if (SPUtils.getInstance().getInt(SPconst.ISMONEYPSW) == 0) {
                    intent = new Intent(this, FundPasswordActivity.class);
                } else {
                    intent = new Intent(this, FindFundPasswordActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
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
        if (SPUtils.getInstance().getInt(SPconst.ISMONEYPSW) == 0) {
            text_psw.setText("资金密码");
            text_psw_hini.setText("去设置");
        } else {
            text_psw.setText("找回资金密码");
            text_psw_hini.setText("");
        }
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
