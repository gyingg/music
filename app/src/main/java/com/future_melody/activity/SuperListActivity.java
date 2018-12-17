package com.future_melody.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.future_melody.fragment.RulersSuperuFragment;
import com.future_melody.fragment.TodaySuperuFragment;
import com.future_melody.fragment.WeekSuperuFragment;
import com.future_melody.music.PlayerActivity;
import com.future_melody.music.PlayerNewActivity;
import com.lzx.musiclibrary.manager.MusicManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author WZL
 * Date：2018/5/15 26
 * Notes: 达人榜
 */
public class SuperListActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.view_page)
    ViewPager viewPage;
    @BindView(R.id.sliding_tab_layout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.img_music)
    ImageView imgMusic;
    @BindView(R.id.back)
    ImageView back;
    private Animation animation;
    private ArrayList<Fragment> mFragments = new ArrayList<>();  //Fragment集合
    private String[] mTitles = {"今日之星", "一周最佳", "统治者"};   //标题的集合
    private TodaySuperuFragment TodaySuperuFragmen;
    private WeekSuperuFragment WeekSuperuFragment;
    private RulersSuperuFragment rulersSuperuFragment;
    private Bundle bundle;
    private String rulerUserId;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_super_list;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String planetId = intent.getStringExtra("planetId");
        rulerUserId = intent.getStringExtra("rulerUserId");
        bundle = new Bundle();
        bundle.putString("planetId", planetId);  //给Fragment传值
        bundle.putString("rulerUserId", rulerUserId);  //给Fragment传值
        initAnim();
        back.setOnClickListener(this);
        imgMusic.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        TodaySuperuFragmen = new TodaySuperuFragment();
        WeekSuperuFragment = new WeekSuperuFragment();
        rulersSuperuFragment = new RulersSuperuFragment();
        mFragments.add(TodaySuperuFragmen);
        mFragments.add(WeekSuperuFragment);
        mFragments.add(rulersSuperuFragment);
        TodaySuperuFragmen.setArguments(bundle);
        WeekSuperuFragment.setArguments(bundle);
        rulersSuperuFragment.setArguments(bundle);
        slidingTabLayout.setViewPager(viewPage, mTitles, mActivity, mFragments);
        slidingTabLayout.setCurrentTab(1);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_music:
                intent = new Intent(mActivity, PlayerNewActivity.class);
                startActivity(intent);
                break;
        }
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
        imgMusic.startAnimation(animation);
    }

    private void stoptAnmi() {
        imgMusic.clearAnimation();
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
}
