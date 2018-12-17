package com.future_melody.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
 * 分贝
 */
public class DecibelActivity extends BaseActivity implements View.OnClickListener, OnLoadMoreListener, OnRefreshListener {


    private ImageView back;
    private TextView tv_decibel;
    private LinearLayout tv_accoundt;
    private int pageSize = 10;
    private int startRecord = 1;
    private List<DecibelRespone> list;
    private DecibelAdapyer adapyer;
    private ListView award_listview;
    private SmartRefreshLayout refresh;
    private Animation animation;
    private ImageView ph_title_right_img;
    private LinearLayout width_asset;
    private LinearLayout give_out;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_decibel;
    }

    @Override
    protected void initView() {
        award_listview = findViewById(R.id.award_listview);
        refresh = findViewById(R.id.refresh);
        tv_decibel = findViewById(R.id.tv_decibel);
        give_out = findViewById(R.id.give_out);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        width_asset = findViewById(R.id.width_asset);
        tv_accoundt = findViewById(R.id.tv_accoundt);
        ImmersionBar.with(this).init();
        back = findViewById(R.id.back);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            tv_decibel.setText(intent.getStringExtra("jifen"));
        } else {
            tv_decibel.setText("0");
        }
        list = new LinkedList<>();
        refresh.setOnLoadMoreListener(this);
        refresh.setEnableRefresh(false);
        tv_accoundt.setOnClickListener(this);
        back.setOnClickListener(this);
        width_asset.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
        give_out.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.tv_accoundt:
                intent = new Intent(mActivity, AccountDetailActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.width_asset:
                toast("兑换功能正在抓紧研发中..");
                break;
            case R.id.give_out:
                intent = new Intent(mActivity, GiveDecibelActivity.class);
                startActivity(intent);
                finish();
                break;
        }
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
                        refresh.finishRefresh();
                        refresh.finishLoadMore();
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
        refreshLayout.setNoMoreData(false);
        decibel(startRecord, pageSize, userId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        decibel(startRecord, pageSize, userId());
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
