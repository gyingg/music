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
public class XingTopMusicFragment extends BaseFragment {
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
    Unbinder unbinder;
    @BindView(R.id.day_text)
    TextView dayText;
    @BindView(R.id.text_week)
    TextView dayWeek;
    @BindView(R.id.text_month)
    TextView dayMonth;
    private FragmentManager manager;
    private DayTopDetailsMusicFragment dayFragment;
    private WeekTopDetailsMusicFragment weekFragment;
    private MonthDetailsMusicFragment monthFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_top_music;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
        manager = getChildFragmentManager();
        dayFragment = new DayTopDetailsMusicFragment();
        weekFragment = new WeekTopDetailsMusicFragment();
        monthFragment = new MonthDetailsMusicFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.top_music_fragment, dayFragment, DAY);
        transaction.add(R.id.top_music_fragment, weekFragment, WEEK);
        transaction.add(R.id.top_music_fragment, monthFragment, MONTH);
        transaction.hide(dayFragment);
        transaction.hide(monthFragment);
        transaction.show(weekFragment);
        transaction.commit();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        topMusicWeek.setBackgroundResource(R.color.color_01B0FE);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                topMusicDay.setBackgroundResource(R.color.color_01B0FE);
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
                topMusicWeek.setBackgroundResource(R.color.color_01B0FE);
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
                topMusicMonth.setBackgroundResource(R.color.color_01B0FE);
                topMusicWeek.setBackgroundResource(R.color.color_f6f6f6);
                topMusicDay.setBackgroundResource(R.color.color_f6f6f6);
                dayMonth.setTextColor(mActivity.getResources().getColor(R.color.white));
                dayWeek.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                dayText.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                break;
        }
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengUtils.onResumeToFragment("XingTopMusicFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("XingTopMusicFragment");
    }
}
