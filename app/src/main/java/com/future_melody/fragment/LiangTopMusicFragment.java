package com.future_melody.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.base.BaseFragment;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.UmengUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Author WZL
 * Date：2018/5/24 45
 * Notes:日榜 周榜 月榜
 */
public class LiangTopMusicFragment extends BaseFragment {
    private static final String DAY = "dayFragment";
    private static final String WEEK = "weekFragment";
    private static final String MONTH = "monthFragment";
    @BindView(R.id.top_music_day)
    FrameLayout topMusicDay;
    @BindView(R.id.top_music_week)
    FrameLayout topMusicWeek;
    @BindView(R.id.top_music_month)
    FrameLayout topMusicMonth;
    @BindView(R.id.top_music_fragment)
    FrameLayout topMusicFragment;
    @BindView(R.id.day_text)
    TextView dayText;
    @BindView(R.id.text_week)
    TextView dayWeek;
    @BindView(R.id.text_month)
    TextView dayMonth;
    private FragmentManager manager;
    private LiangDayTopcFragment dayFragment;
    private LiangWeekTopcFragment weekFragment;
    private LiangMonthTopcFragment monthFragment;
    private FragmentTransaction transaction;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_top_music;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    public boolean getUserVisibleHint() {
        LogUtil.e("1", "1");
        return super.getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            weekFragment.setUserVisibleHint(true);
        }
    }

    @Override
    protected void initData() {
        LogUtil.e("initData", "Liang");
        manager = getChildFragmentManager();
        transaction = manager.beginTransaction();
        dayFragment = new LiangDayTopcFragment();
        weekFragment = new LiangWeekTopcFragment();
        monthFragment = new LiangMonthTopcFragment();
        transaction.show(weekFragment);
        transaction.hide(dayFragment);
        transaction.hide(monthFragment);
        topMusicWeek.setBackgroundResource(R.color.F5A623);
        transaction.add(R.id.top_music_fragment, dayFragment);
        transaction.add(R.id.top_music_fragment, weekFragment);
        transaction.add(R.id.top_music_fragment, monthFragment);
        transaction.commit();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengUtils.onResumeToFragment("LiangTopMusicFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("LiangTopMusicFragment");
    }
    @OnClick({R.id.top_music_day, R.id.top_music_week, R.id.top_music_month})
    public void onViewClicked(View view) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(dayFragment);
        transaction.hide(weekFragment);
        transaction.hide(monthFragment);
        switch (view.getId()) {
            case R.id.top_music_day:
                transaction.show(dayFragment);
                topMusicDay.setBackgroundResource(R.color.F5A623);
                topMusicWeek.setBackgroundResource(R.color.color_f6f6f6);
                topMusicMonth.setBackgroundResource(R.color.color_f6f6f6);
                dayText.setTextSize(16);
                dayWeek.setTextSize(12);
                dayMonth.setTextSize(12);
                dayText.setTextColor(mActivity.getResources().getColor(R.color.white));
                dayWeek.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                dayMonth.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                break;
            case R.id.top_music_week:
                transaction.show(weekFragment);
                dayWeek.setTextSize(16);
                dayText.setTextSize(12);
                dayMonth.setTextSize(12);
                topMusicWeek.setBackgroundResource(R.color.F5A623);
                topMusicDay.setBackgroundResource(R.color.color_f6f6f6);
                topMusicMonth.setBackgroundResource(R.color.color_f6f6f6);
                dayWeek.setTextColor(mActivity.getResources().getColor(R.color.white));
                dayText.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                dayMonth.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                break;
            case R.id.top_music_month:
                transaction.show(monthFragment);
                dayMonth.setTextSize(16);
                dayWeek.setTextSize(12);
                dayText.setTextSize(12);
                topMusicMonth.setBackgroundResource(R.color.F5A623);
                topMusicWeek.setBackgroundResource(R.color.color_f6f6f6);
                topMusicDay.setBackgroundResource(R.color.color_f6f6f6);
                dayMonth.setTextColor(mActivity.getResources().getColor(R.color.white));
                dayWeek.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                dayText.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                break;
        }
        transaction.commit();
    }
}
