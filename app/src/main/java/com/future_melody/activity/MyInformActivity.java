package com.future_melody.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.fragment.DiscussMyFragment;
import com.future_melody.fragment.PraiseMyFragment;
import com.future_melody.fragment.SystemMessagesFragment;
import com.future_melody.music.PlayerUitlis;
import com.gyf.barlibrary.ImmersionBar;
import com.lzx.musiclibrary.manager.MusicManager;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 我的通知
 */
public class MyInformActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.sliding_tab_layout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.view_page)
    ViewPager viewPage;
    private ArrayList<Fragment> mFragments = new ArrayList<>();  //Fragment集合
    private String[] mTitles = {"系统消息", "评论我的", "赞我的"};   //标题的集合
    private ImageView back;
    private ImageView ph_title_right_img;
    private Animation animation;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_inform;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        back = findViewById(R.id.back);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        back.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
        initAnim();
    }

    @Override
    protected void initData() {
        mFragments.add(new SystemMessagesFragment());
        mFragments.add(new DiscussMyFragment());
        mFragments.add(new PraiseMyFragment());
        slidingTabLayout.setViewPager(viewPage, mTitles, this, mFragments);
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
