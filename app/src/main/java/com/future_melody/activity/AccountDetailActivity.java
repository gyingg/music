package com.future_melody.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

import com.future_melody.R;
import com.future_melody.adapter.DecibelAdapyer;
import com.future_melody.base.BaseActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.DecibelRequest;
import com.future_melody.net.respone.DecibelRespone;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.utils.TipLinearUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.lzx.musiclibrary.manager.MusicManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.LinkedList;
import java.util.List;

/**
 * 账户明细
 */
public class AccountDetailActivity extends BaseActivity implements OnLoadMoreListener,OnRefreshListener, View.OnClickListener {

    private DecibelAdapyer adapyer;
    private ListView award_listview;
    private SmartRefreshLayout refresh;
    private int pageSize = 20;
    private int startRecord = 1;
    private List<DecibelRespone> list;
    private Animation animation;
    private ImageView ph_title_right_img;
    private ImageView back;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_account_detail;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        award_listview = findViewById(R.id.award_listview);
        refresh = findViewById(R.id.refresh);
        back = findViewById(R.id.back);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        ph_title_right_img.setOnClickListener(this);
        back.setOnClickListener(this);
        list = new LinkedList<>();
    }

    @Override
    protected void initData() {
        decibel(startRecord, pageSize, userId());
        refresh.setOnLoadMoreListener(this);
        refresh.setEnableRefresh(false);
    }


    private void decibel(final int pageNum, int pageSize, String userId) {
        addSubscribe(apis.decibel(new DecibelRequest(pageNum, pageSize, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<List<DecibelRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<DecibelRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<DecibelRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        refresh.finishLoadMore(false);
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(List<DecibelRespone> decibelRespones) {
                        refresh.finishLoadMore();
                        if (decibelRespones != null && decibelRespones.size() > 0) {
                            list.addAll(decibelRespones);
                            adapyer = new DecibelAdapyer(mActivity, list);
                            award_listview.setAdapter(adapyer);
                        } else {
                            refresh.finishLoadMoreWithNoMoreData();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        startRecord++;
        refreshLayout.setNoMoreData(false);
        decibel(startRecord, pageSize, userId());
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        startRecord = 1;
        list.clear();
       // refreshLayout.setNoMoreData(false);
        decibel(startRecord, pageSize, userId());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
