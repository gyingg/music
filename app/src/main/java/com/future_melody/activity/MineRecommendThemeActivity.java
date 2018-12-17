package com.future_melody.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.adapter.MineThemeAdapter;
import com.future_melody.adapter.RecommendThemesAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.CommonConst;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.MineReconmendTheme;
import com.future_melody.net.request.ShareTheme;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MineReconmendThemeRespone;
import com.future_melody.net.respone.ShareThemeRespone;
import com.future_melody.receiver.ThemeEventBus;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.TipLinearUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.lzx.musiclibrary.manager.MusicManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author WZL
 * Date：2018/5/16 04
 * Notes: 我的页面：推荐主题
 */
public class MineRecommendThemeActivity extends BaseActivity implements AdapterView.OnItemClickListener, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.ph_title_right_img)
    ImageView phTitleRightImg;
    @BindView(R.id.mine_theme_list)
    ListView mineThemeList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.no_data)
    LinearLayout noData;

    private MineThemeAdapter adapter;
    private List<MineReconmendThemeRespone> list;
    private int pageNum = 1;
    private int pageSize = 10;
    private Animation animation;
    private ImageView ph_title_right_img;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_mine_theme;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        mineThemeList.setOnItemClickListener(this);
        setTitle("我的主题");
        setBarColor(R.color.white, true);
        setTitleLayoutColor(mActivity, R.color.white);
        setTitleColor(R.color.color_333333);
        phTitleRightImg.setImageResource(R.mipmap.back_music);
        setBlackBackble();
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        initAnim();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
//        refreshLayout.setEnableRefresh(false);  //禁止下拉刷新
        ph_title_right_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerUitlis.player(mActivity);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MusicManager.isPlaying()) {
            startAnmi();
        } else {
            stoptAnmi();
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ThemeEventBus even) {
        int position = even.position();
        MineReconmendThemeRespone respone = list.get(position);
        respone = (MineReconmendThemeRespone) even.getRespone();
        list.set(position, respone);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAnmi();
        EventBus.getDefault().unregister(mActivity);
    }

    private void destroyAnmi() {
        animation.cancel();
        animation = null;
    }

    @Override
    protected void initData() {
        list = new LinkedList<>();
        getThemeList(userId(), pageNum, pageSize);
        EventBus.getDefault().register(mActivity);
    }

    private void getThemeList(String userId, int pageNum, int pageSize) {
        addSubscribe(apis.mineTheme(new MineReconmendTheme(userId, pageNum, pageSize))
                .compose(RxHttpUtil.<FutureHttpResponse<List<MineReconmendThemeRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<MineReconmendThemeRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<MineReconmendThemeRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(List<MineReconmendThemeRespone> mineReconmendThemeRespone) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        if (pageNum == 1 && mineReconmendThemeRespone.size() <= 0) {
                            noData.setVisibility(View.VISIBLE);
                            mineThemeList.setVisibility(View.GONE);
                        } else {
                            noData.setVisibility(View.GONE);
                            mineThemeList.setVisibility(View.VISIBLE);
                            if (mineReconmendThemeRespone != null && mineReconmendThemeRespone.size() > 0) {
                                list.addAll(mineReconmendThemeRespone);
                                adapter = new MineThemeAdapter(mActivity, list);
                                adapter.setShareClickListener(new MineThemeAdapter.ThemeClickListener() {
                                    @Override
                                    public void onClick(int i) {
                                        //分享
                                        if (isLogin()) {
                                            getShareInfo(mineReconmendThemeRespone.get(i).specialid);
                                        } else {
                                            Intent intent = new Intent(mActivity, LoginActivity.class);
                                            intent.putExtra(CommonConst.ISFINISH, 1);
                                            startActivity(intent);
                                        }
                                    }
                                });
                                mineThemeList.setAdapter(adapter);
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


    private void getShareInfo(final String specialId) {
        addSubscribe(apis.sharTheme(new ShareTheme(specialId))
                .compose(RxHttpUtil.<FutureHttpResponse<ShareThemeRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ShareThemeRespone>handleResult())
                .subscribeWith(new HttpSubscriber<ShareThemeRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(ShareThemeRespone shareThemeRespone) {
                        share(shareThemeRespone.specialTitle, shareThemeRespone.specialContent, shareThemeRespone.specialShareUrl, shareThemeRespone.pictureUrl);
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    /**
     * 分享
     */
    private void share(String title, String description, String url, String img) {
//        //分享的图片
        UMImage thumb = new UMImage(mActivity, img);
//        //分享链接
        LogUtil.e("url", url);
        LogUtil.e("img", img);
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(description);//描述
        new ShareAction(mActivity)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        toast("分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        toast("分享失败");
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        toast("分享失败");
                    }
                }).open();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MineReconmendThemeRespone respone = list.get(i);
        Intent intent = new Intent(mActivity, ThemeDetilsNewActivity.class);
        intent.putExtra("SpecialId", list.get(i).specialid);
        intent.putExtra(CommonConst.MINE_THEME, respone);
        intent.putExtra("position", i);
        startActivity(intent);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        ++pageNum;
        getThemeList(userId(), pageNum, pageSize);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNum = 1;
        list.clear();
        refreshLayout.setNoMoreData(false);
        getThemeList(userId(), pageNum, pageSize);
    }
}
