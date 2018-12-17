package com.future_melody.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.future_melody.R;
import com.future_melody.base.BaseFragment;
import com.future_melody.utils.UmengUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Y on 2018/5/11.
 * 排行榜Fragment
 */

public class RankingFragment extends BaseFragment {
    private static final String XING = "xingFragment";
    private static final String LIANG = "liangFragment";
    @BindView(R.id.xing_top_bg)
    ImageView xingTopBg;
    @BindView(R.id.xing_top_text)
    ImageView xingTopText;
    @BindView(R.id.btn_ranking_xing)
    RelativeLayout btnRankingXing;
    @BindView(R.id.liang_top_bg)
    ImageView liangTopBg;
    @BindView(R.id.liang_top_text)
    ImageView liangTopText;
    @BindView(R.id.btn_ranking_liang)
    RelativeLayout btnRankingLiang;
    @BindView(R.id.ranking_fraglayout)
    FrameLayout rankingFraglayout;
    Unbinder unbinder;
    @BindView(R.id.xing_img_bg)
    View xingImgBg;
    @BindView(R.id.ling_img_bg)
    View lingImgBg;

    private XingTopMusicFragment xingFragment;
    private LiangTopMusicFragment liangFragment;
    private FragmentManager manager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ranking;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
    }

    @Override
    protected void initData() {
        manager = getChildFragmentManager();
        xingFragment = new XingTopMusicFragment();
        liangFragment = new LiangTopMusicFragment();
        xingImgBg.setVisibility(View.VISIBLE);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.ranking_fraglayout, xingFragment, XING);
        transaction.add(R.id.ranking_fraglayout, liangFragment, LIANG);
        transaction.hide(liangFragment);
        transaction.show(xingFragment);
        transaction.commit();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @OnClick({R.id.btn_ranking_xing, R.id.btn_ranking_liang})
    public void onViewClicked(View view) {
        FragmentTransaction transaction = manager.beginTransaction();
        switch (view.getId()) {
            case R.id.btn_ranking_xing:
                xingFragment.setUserVisibleHint(true);
                transaction.show(xingFragment);
                xingTopText.setImageResource(R.mipmap.icon_xing_top);
                liangTopText.setImageResource(R.mipmap.icon_liang_top_tv_un);
                transaction.hide(liangFragment);
                xingImgBg.setVisibility(View.VISIBLE);
                lingImgBg.setVisibility(View.GONE);
                break;
            case R.id.btn_ranking_liang:
                liangFragment.setUserVisibleHint(true);
                transaction.show(liangFragment);
                liangTopText.setImageResource(R.mipmap.icon_liang_top_tv);
                xingTopText.setImageResource(R.mipmap.icon_xing_top_un);
                transaction.hide(xingFragment);
                lingImgBg.setVisibility(View.VISIBLE);
                xingImgBg.setVisibility(View.GONE);
                break;
        }
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengUtils.onResumeToFragment("RankingFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("RankingFragment");
    }
}
