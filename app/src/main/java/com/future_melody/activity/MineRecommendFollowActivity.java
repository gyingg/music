package com.future_melody.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.adapter.MineRecommendAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.AddFollowRequest;
import com.future_melody.net.request.CancelFollow;
import com.future_melody.net.request.MineReconmendFollow;
import com.future_melody.net.respone.AddFollowRespone;
import com.future_melody.net.respone.CancelFollowRespone;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MineReconmendFollowRespone;
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
 * Author WZL
 * Date??2018/5/16 04
 * Notes: 我的关注
 */
public class MineRecommendFollowActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener {
    private RecyclerView recycle;
    private MineRecommendAdapter mineRecommendAdapter;
    private ImageView back;
    private ImageView ph_title_right_img;
    private int pageNum = 1;
    private SmartRefreshLayout refreshLayout;
    private int PageSize = 20;
    private List<MineReconmendFollowRespone> list;
    private LinearLayout no_data;
    private Animation animation;
    private TextView ph_title_name;
    private TextView tv_data;
    private String user_Id;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_mine_follow;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        recycle = findViewById(R.id.recycle);
        back = findViewById(R.id.back);
        no_data = findViewById(R.id.no_data);
        ph_title_name = findViewById(R.id.ph_title_name);
        refreshLayout = findViewById(R.id.refreshLayout);
        tv_data = findViewById(R.id.tv_data);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        recycle.setLayoutManager(new LinearLayoutManager(mActivity));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
//        refreshLayout.setEnableRefresh(false);  //禁止下拉刷新
        //     refreshLayout.setEnableLoadMore(false);  //禁止上拉加载
        ph_title_right_img.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        list = new LinkedList<>();
        back.setOnClickListener(this);
        Intent intent = getIntent();
        String beUserId = intent.getStringExtra("beUserId");
        if (TextUtils.isEmpty(beUserId)) {
            ph_title_name.setText("我的关注");
            getFollowList(userId(), pageNum, PageSize);
            tv_data.setText("赶紧去关注别人吧~");
            user_Id = userId();
        } else {
            ph_title_name.setText("关注");
            getFollowList(beUserId, pageNum, PageSize);
            tv_data.setText("这位老铁还没关注别人哦~");
            user_Id = beUserId;
        }
        initAnim();
    }


    private void getFollowList(String userId, int startRecord, int pageSize) {
        addSubscribe(apis.mineFollow(new MineReconmendFollow(userId, startRecord, pageSize))
                .compose(RxHttpUtil.<FutureHttpResponse<List<MineReconmendFollowRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<MineReconmendFollowRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<MineReconmendFollowRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        if (exception.getMessage() != null) {
                            TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                        }
                    }
                }) {
                    @Override
                    public void onNext(final List<MineReconmendFollowRespone> mineReconmendFollowRespone) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        if (pageNum == 1 && mineReconmendFollowRespone.size() <= 0) {
                            no_data.setVisibility(View.VISIBLE);
                            recycle.setVisibility(View.GONE);
                        } else {
                            no_data.setVisibility(View.GONE);
                            recycle.setVisibility(View.VISIBLE);
                            if (mineReconmendFollowRespone != null && mineReconmendFollowRespone.size() > 0) {
                                list.addAll(mineReconmendFollowRespone);
                                mineRecommendAdapter = new MineRecommendAdapter(mActivity, list);
                                recycle.setAdapter(mineRecommendAdapter);
                                mineRecommendAdapter.notifyDataSetChanged();
                                //取消关注
                                mineRecommendAdapter.setAttention(new MineRecommendAdapter.attention() {
                                    private boolean isAttention = false;

                                    @Override
                                    public void attention(View view, int i, TextView tv, boolean isIstypes) {
                                        if (!isAttention) {
                                            tv.setText("关注");
                                            isAttention = true;
                                            offtFollowList(mineReconmendFollowRespone.get(i).getBg_userid(), userId);   //取消关注
                                        } else {
                                            tv.setText(isIstypes ? "互相关注" : "已关注");
                                            isAttention = false;
                                            addFollowList(mineReconmendFollowRespone.get(i).getBg_userid(), userId);  //添加关注

                                        }
                                    }
                                });
                            } else {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                }));
    }

    //添加关注
    private void addFollowList(final String bg_userid, String g_userid) {
        addSubscribe(apis.addlFollow(new AddFollowRequest(bg_userid, g_userid))
                .compose(RxHttpUtil.<FutureHttpResponse<AddFollowRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<AddFollowRespone>handleResult())
                .subscribeWith(new HttpSubscriber<AddFollowRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(AddFollowRespone AddFollowRespone) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    //取消关注
    private void offtFollowList(final String bg_userid, String g_userid) {
        addSubscribe(apis.cancelFollow(new CancelFollow(bg_userid, g_userid))
                .compose(RxHttpUtil.<FutureHttpResponse<CancelFollowRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<CancelFollowRespone>handleResult())
                .subscribeWith(new HttpSubscriber<CancelFollowRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(CancelFollowRespone CancelFollowRespone) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNum = 1;
        list.clear();
        getFollowList(user_Id, pageNum, PageSize);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        refreshLayout.setNoMoreData(false);
        getFollowList(user_Id, pageNum, PageSize);
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

