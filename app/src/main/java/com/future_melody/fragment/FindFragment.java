package com.future_melody.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.future_melody.R;
import com.future_melody.base.BaseFragment;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.receiver.ListenXingMusicEventBus;
import com.future_melody.receiver.ToNewFragmentEventBus;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.TipLinearUtil;
import com.future_melody.utils.UmengUtils;
import com.lzx.musiclibrary.manager.MusicManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 主页发现页面
 */
public class FindFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    @BindView(R.id.img_share)
    ImageView imgShare;
    private String TAG = "FindFragment";
    @BindView(R.id.view_page)
    ViewPager viewPage;
    @BindView(R.id.sliding_tab_layout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.img_music)
    ImageView imgMusic;
    @BindView(R.id.find_layout_bg)
    RelativeLayout findLayoutBg;
    Unbinder unbinder;
    private ArrayList<Fragment> mFragments = null;  //Fragment集合
    private String[] mTitles = {"星歌", "推荐"};   //标题的集合
    private NewMusicFragment musicFragment;
    private ThemeFragment recommendFragment;
    //private RankingFragment rankingFragment;
    private Animation animation;
    private int postion = 0;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        LogUtil.e(TAG, "initView");
        initImmersionBar();
        initAnim();
        LogUtil.e("开始走了吗", "FindFragment");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initImmersionBar();
            LogUtil.e(TAG, "onHiddenChanged");
//            setBarDarkFont();
            if (!isLogin()) {
                if (postion == 0) {
                    TipLinearUtil.create(mActivity).showSuccessTop("您当前未登录账号，为了避免您的收益损失，请前往登录", 1500);
                }
            }
        }
        initAnim();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.e(TAG, "onActivityCreated");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ListenXingMusicEventBus even) {
        LogUtil.e(TAG, even.getPosition() + "");
        if (even.getPosition() == -1) {
            startAnmi();
        } else if (even.getPosition() == -2) {
            stoptAnmi();
        }
    }

    @Override
    protected void initData() {
        mFragments = new ArrayList<>();
        musicFragment = new NewMusicFragment();
        recommendFragment = new ThemeFragment();
        //rankingFragment = new RankingFragment();
        mFragments.add(musicFragment);
        mFragments.add(recommendFragment);
        //mFragments.add(rankingFragment);
        LogUtil.e("viewPage", mTitles.length + "---" + mFragments.size());
        slidingTabLayout.setViewPager(viewPage, mTitles, getActivity(), mFragments);
        slidingTabLayout.setCurrentTab(1);
        slidingTabLayout.getTitleView(0).setTextSize(14);
        slidingTabLayout.getTitleView(1).setTextSize(20);
        slidingTabLayout.getTitleView(1).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD)); //正常
        //slidingTabLayout.getTitleView(2).setTextSize(14);
        findLayoutBg.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
        viewPage.addOnPageChangeListener(this);
        LogUtil.e("发现是否播放", MusicManager.isPlaying() + "");
        if (MusicManager.isPlaying()) {
            startAnmi();
        } else {
            stoptAnmi();
        }
    }

    private void initAnim() {
        animation = AnimationUtils.loadAnimation(mActivity, R.anim.player);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
    }

    private void startAnmi() {
        if (animation != null) {
            imgMusic.startAnimation(animation);
        }
    }

    private void stoptAnmi() {
        imgMusic.clearAnimation();
    }

    private void destroyAnmi() {
        if (animation != null) {
            animation.cancel();
            animation = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyAnmi();
        EventBus.getDefault().unregister(this);
        LogUtil.e("·134", "viewPage");
    }

    @OnClick({R.id.img_music, R.id.img_share})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.img_music:
                PlayerUitlis.player(mActivity);
                break;
            case R.id.img_share:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        LogUtil.e(TAG, position + "");
        postion = position;
        if (position == 0) {
            sendEvent(1);
            imgShare.setVisibility(View.GONE);
            imgMusic.setVisibility(View.VISIBLE);
            findLayoutBg.setBackgroundColor(mActivity.getResources().getColor(R.color.color_cfcfcf));
            findLayoutBg.setBackgroundColor(mActivity.getResources().getColor(R.color.white));  //背景
            slidingTabLayout.getTitleView(0).setTextSize(20);
            slidingTabLayout.getTitleView(0).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD)); //正常
            slidingTabLayout.getTitleView(1).setTextSize(14);
            //   slidingTabLayout.getTitleView(2).setTextSize(14);
            if (!isLogin()) {
                TipLinearUtil.create(mActivity).showSuccessTop("您当前未登录账号，为了避免您的收益损失，请前往登录", 1500);
            }
        } else if (position == 1) {
            imgShare.setVisibility(View.GONE);
            imgMusic.setVisibility(View.VISIBLE);
            findLayoutBg.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
            slidingTabLayout.getTitleView(0).setTextSize(14);
            slidingTabLayout.getTitleView(1).setTextSize(20);
            slidingTabLayout.getTitleView(1).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD)); //正常
            //slidingTabLayout.getTitleView(2).setTextSize(14);
        } /*else if (position == 2) {
            findLayoutBg.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
            slidingTabLayout.getTitleView(0).setTextSize(14);
            slidingTabLayout.getTitleView(1).setTextSize(14);
            slidingTabLayout.getTitleView(2).setTextSize(20);
            slidingTabLayout.getTitleView(2).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD)); //正常
        }*/
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    @Override
    public void onResume() {
        super.onResume();
        UmengUtils.onResumeToFragment("FindFragment");
        LogUtil.e(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("FindFragment");
    }

    private void sendEvent(int isShow) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new ToNewFragmentEventBus(isShow));
            }
        }).start();
    }


}
