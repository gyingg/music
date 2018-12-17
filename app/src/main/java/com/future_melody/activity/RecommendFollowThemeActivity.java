package com.future_melody.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.future_melody.R;
import com.future_melody.adapter.FollowThemeAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.CommonConst;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.AttentionThemeRequest;
import com.future_melody.net.respone.AttentionThemeRespone;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.receiver.FollowsThemeEventBus;
import com.future_melody.receiver.ThemeEventBus;
import com.future_melody.utils.LogUtil;
import com.lzx.musiclibrary.manager.MusicManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

/**
 * Author WZL
 * Date：2018/5/16 37
 * Notes: 推荐页：关注人的主题
 */
public class RecommendFollowThemeActivity extends BaseActivity implements AdapterView.OnItemClickListener, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.follow_theme_list)
    ListView followThemeList;
    @BindView(R.id.ph_title_right_img)
    ImageView phTitleRightImg;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.no_data)
    LinearLayout noData;
    private FollowThemeAdapter adapter;
    private List<AttentionThemeRespone> lists;
    private int pageNum = 1;
    private int pageSize = 10;
    private Animation animation;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_follow_theme;
    }

    @Override
    protected void initView() {
        setTitle("关注");
        initAnim();
        setTitleColor(R.color.color_333333);
        setTitleLayoutColor(mActivity, R.color.white);
        setBlackBackble();
        setBarColor(R.color.white, true);
        phTitleRightImg.setImageResource(R.mipmap.back_music);
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
        phTitleRightImg.startAnimation(animation);
    }

    private void stoptAnmi() {
        phTitleRightImg.clearAnimation();
    }


    private void destroyAnmi() {
        animation.cancel();
        animation = null;
    }

    @Override
    protected void initData() {
        attentiontheme(pageNum, pageSize, userId());
        lists = new LinkedList<>();
        followThemeList.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FollowsThemeEventBus even) {
        LogUtil.e("关注主题：", even.position() + "");
        AttentionThemeRespone respone = lists.get(even.position());
        respone = (AttentionThemeRespone) even.getRespone();
        lists.set(even.position(), respone);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAnmi();
        EventBus.getDefault().unregister(this);
    }

    private void attentiontheme(final int pageNum, int pageSize, String userId) {
        addSubscribe(apis.atttheme(new AttentionThemeRequest(pageNum, pageSize, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<List<AttentionThemeRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<AttentionThemeRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<AttentionThemeRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        refreshLayout.finishRefresh(false);
                        refreshLayout.finishLoadMore(false);
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(List<AttentionThemeRespone> attentionThemeRespones) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();

                        if (pageNum == 1 && attentionThemeRespones.size() <= 0) {
                            noData.setVisibility(View.VISIBLE);
                            followThemeList.setVisibility(View.GONE);
                        } else {
                            noData.setVisibility(View.GONE);
                            followThemeList.setVisibility(View.VISIBLE);
                            if (attentionThemeRespones != null && attentionThemeRespones.size() > 0) {
                                lists.addAll(attentionThemeRespones);
                                adapter = new FollowThemeAdapter(mActivity, lists);
                                followThemeList.setAdapter(adapter);
                            } else {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            }
                        }


                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (lists.size() > 0) {
            Intent intent = new Intent(mActivity, ThemeDetailsActivity.class);
            intent.putExtra("SpecialId", lists.get(i).specialId);
            intent.putExtra(CommonConst.FOLLOWS_THEME, lists.get(i));
            intent.putExtra("position", i);
            startActivity(intent);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        attentiontheme(pageNum, pageSize, userId());
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        lists.clear();
        attentiontheme(pageNum, pageSize, userId());
    }
}
