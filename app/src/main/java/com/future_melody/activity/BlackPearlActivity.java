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
import com.future_melody.adapter.BlackPearAdapyer;
import com.future_melody.adapter.DecibelAdapyer;
import com.future_melody.base.BaseActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.BlackPearRequest;
import com.future_melody.net.request.DecibelRequest;
import com.future_melody.net.respone.BlackPearRespone;
import com.future_melody.net.respone.DecibelRespone;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.utils.TipLinearUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.lzx.musiclibrary.manager.MusicManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.LinkedList;
import java.util.List;

/**
 * 黑珍珠
 */
public class BlackPearlActivity extends BaseActivity implements View.OnClickListener, OnLoadMoreListener {


    private ImageView back;
    private ListView award_listview;
    private Animation animation;
    private LinearLayout tv_accoundt;
    private SmartRefreshLayout refresh;
    private ImageView ph_title_right_img;
    private LinearLayout roll_out;
    private LinearLayout roll_up;
    private TextView tv_back_pearl;
    private int pageSize = 10;
    private int startRecord = 1;
    private List<BlackPearRespone> list;
    private BlackPearAdapyer adapter;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_black_pearl;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).init();
        refresh = findViewById(R.id.refresh);
        roll_out = findViewById(R.id.roll_out);
        roll_up = findViewById(R.id.roll_up);
        tv_back_pearl = findViewById(R.id.tv_back_pearl);
        tv_accoundt = findViewById(R.id.tv_accoundt);
        award_listview = findViewById(R.id.award_listview);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        back = findViewById(R.id.back);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            tv_back_pearl.setText(intent.getStringExtra("heizhenzhu"));
        } else {
            tv_back_pearl.setText("0");
        }
        list = new LinkedList<>();
        blackpearl(userId(), startRecord, pageSize);
        back.setOnClickListener(this);
        refresh.setOnLoadMoreListener(this);
        refresh.setEnableRefresh(false);
        tv_accoundt.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
        roll_out.setOnClickListener(this);
        roll_up.setOnClickListener(this);
    }


    private void blackpearl(final String userid, int startRecord, int pageSize) {
        addSubscribe(apis.blackpear(new BlackPearRequest(userid, pageSize,startRecord))
                .compose(RxHttpUtil.<FutureHttpResponse<List<BlackPearRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<BlackPearRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<BlackPearRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        refresh.finishLoadMore(false);
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(List<BlackPearRespone> blackPearRespone) {
                        refresh.finishLoadMore();
                        if (blackPearRespone != null && blackPearRespone.size() > 0) {
                            list.addAll(blackPearRespone);
                            adapter = new BlackPearAdapyer(mActivity, blackPearRespone);
                            award_listview.setAdapter(adapter);
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
        blackpearl(userId(), startRecord, pageSize);
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
            case R.id.tv_accoundt:
                intent = new Intent(mActivity, AccountBlackPrarlActivity.class);
                startActivity(intent);
                break;
            case R.id.roll_out:
                toast(" 转出黑珍珠正在加紧修建");
                /*intent = new Intent(mActivity, AccountRollOutActivity.class);
                startActivity(intent);*/
                break;
            case R.id.roll_up:
                toast(" 转入黑珍珠正在加紧修建");
              /*  intent = new Intent(mActivity, AccountRollUpActivity.class);
                startActivity(intent);*/
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
