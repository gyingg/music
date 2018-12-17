package com.future_melody.activity;
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
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.LinkedList;
import java.util.List;

/**
 * 黑珍珠账户明细
 */
public class AccountBlackPrarlActivity extends BaseActivity implements OnLoadMoreListener,OnRefreshListener, View.OnClickListener  {

    private ListView award_listview;
    private SmartRefreshLayout refresh;
    private int pageSize = 10;
    private int startRecord = 1;
    private BlackPearAdapyer adapter;
    private List<BlackPearRespone> list;
    private Animation animation;
    private ImageView ph_title_right_img;
    private ImageView back;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_account_black_prarl;
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
        blackpearl(userId(), startRecord, pageSize);
        refresh.setOnLoadMoreListener(this);
        refresh.setEnableRefresh(false);
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
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        startRecord = 1;
        list.clear();
        // refreshLayout.setNoMoreData(false);
        blackpearl(userId(), startRecord, pageSize);
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
