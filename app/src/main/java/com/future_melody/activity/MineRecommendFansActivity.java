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
import com.future_melody.adapter.MineRecommendFansAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.music.PlayerActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.AddFollowRequest;
import com.future_melody.net.request.CancelFollow;
import com.future_melody.net.request.MineReconmendFans;
import com.future_melody.net.respone.AddFollowRespone;
import com.future_melody.net.respone.CancelFollowRespone;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MineReconmendFansRespone;
import com.future_melody.net.respone.MineReconmendThemeRespone;
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
 * Date：2018/5/16 04
 * Notes: 我的页面：我的粉丝
 */
public class MineRecommendFansActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener {
    private RecyclerView recycle;
    private ImageView back;
    private ImageView no_img;
    private ImageView ph_title_right_img;
    private SmartRefreshLayout refreshLayout;
    private int pageNum = 1;
    private int PageSize = 20;
    private MineRecommendFansAdapter mineRecommendFansAdapter;
    private List<MineReconmendFansRespone> list;
    private LinearLayout no_data;
    private TextView tv_data;
    private Animation animation;
    private TextView ph_title_name;
    private String beUserId;
    private String user_Id;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_mine_fans;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        recycle = findViewById(R.id.recycle);
        back = findViewById(R.id.back);
        refreshLayout = findViewById(R.id.refreshLayout);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        ph_title_name = findViewById(R.id.ph_title_name);
        recycle.setLayoutManager(new LinearLayoutManager(mActivity));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        no_data = findViewById(R.id.no_data);
        tv_data = findViewById(R.id.tv_data);
        ph_title_right_img.setOnClickListener(this);
        back.setOnClickListener(this);
        initAnim();
    }

    @Override
    protected void initData() {
        list = new LinkedList<>();
        back.setOnClickListener(this);
        Intent intent = getIntent();
        beUserId = intent.getStringExtra("beUserId");
        if (TextUtils.isEmpty(beUserId)) {
            ph_title_name.setText("我的粉丝");
            getFansList(userId());
            tv_data.setText("糟糕，还没人关注我诶~");
            user_Id = userId();
        } else {
            ph_title_name.setText("粉丝");
            getFansList(beUserId);
            tv_data.setText("还没有人关注他哦~");
            user_Id = beUserId;
        }

    }

    private void getFansList(String userId) {
        addSubscribe(apis.mineFans(new MineReconmendFans(userId, pageNum, PageSize))
                .compose(RxHttpUtil.<FutureHttpResponse<List<MineReconmendFansRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<MineReconmendFansRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<MineReconmendFansRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {


                    @Override
                    public void onNext(List<MineReconmendFansRespone> mineReconmendFansRespone) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();

                        if (pageNum == 1 && mineReconmendFansRespone.size() <= 0) {
                            no_data.setVisibility(View.VISIBLE);
                            recycle.setVisibility(View.GONE);
                        } else {
                            no_data.setVisibility(View.GONE);
                            recycle.setVisibility(View.VISIBLE);
                            if (mineReconmendFansRespone != null && mineReconmendFansRespone.size() > 0) {
                                list.addAll(mineReconmendFansRespone);
                                mineRecommendFansAdapter = new MineRecommendFansAdapter(mActivity, list);
                                mineRecommendFansAdapter.setAttention(new MineRecommendFansAdapter.attention() {
                                    private boolean isAttention = false;

                                    @Override
                                    public void attention(View view, int i, boolean isAttention) {
                                        if (isAttention) {
                                            //关注     请求关注接口
                                            addFollowList(mineReconmendFansRespone.get(i).getG_userid(), userId);  //添加关注
                                        } else {
                                            //取消关注     请求取消关注接口
                                            offtFollowList(mineReconmendFansRespone.get(i).getG_userid(), userId);   //取消关注
                                        }

                                    }
                                });
                                recycle.setAdapter(mineRecommendFansAdapter);
                                mineRecommendFansAdapter.notifyDataSetChanged();
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
        list.clear();
        pageNum = 1;
        getFansList(user_Id);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        refreshLayout.setNoMoreData(false);
        getFansList(user_Id);
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
