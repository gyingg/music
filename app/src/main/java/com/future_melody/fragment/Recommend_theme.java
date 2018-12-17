package com.future_melody.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.future_melody.R;
import com.future_melody.activity.LoginActivity;
import com.future_melody.activity.ThemeDetailsActivity;
import com.future_melody.activity.ThemeDetilsNewActivity;
import com.future_melody.adapter.Recommend_themeAdapter;
import com.future_melody.base.BaseFragment;
import com.future_melody.common.CommonConst;
import com.future_melody.mode.Recommend_Theme_Bean;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.Recommend_theme_Request;
import com.future_melody.net.request.ShareTheme;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.Recommend_theme_Respne;
import com.future_melody.net.respone.ShareThemeRespone;
import com.future_melody.receiver.FollowsThemeEventBus;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.UmengUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
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
import butterknife.Unbinder;

/**
 * Created by Y on 2018/5/18.
 * 推荐主题
 */

public class Recommend_theme extends BaseFragment implements OnLoadMoreListener {
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    private Recommend_themeAdapter recommend_themeAdapter;
    private String beUserId;
    private int pageNum = 1;
    private int pageSize = 20;
    private List<Recommend_Theme_Bean> list;
    private NestedScrollView scrollView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend_them;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        scrollView = view.findViewById(R.id.scrollView);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        beUserId = bundle.getString("BeuserId");
        recommend_theme(pageNum, pageSize, beUserId);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // layoutManager.setAutoMeasureEnabled(true);  //图片自适应
        recycle.setLayoutManager(layoutManager);
        list = new LinkedList<>();
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FollowsThemeEventBus even) {
        LogUtil.e("关注主题：", even.position() + "");
        Recommend_Theme_Bean respone = list.get(even.position());
        respone = (Recommend_Theme_Bean) even.getRespone();
        list.set(even.position(), respone);
        if (recommend_themeAdapter != null) {
            recommend_themeAdapter.notifyDataSetChanged();
        }
    }

    private void recommend_theme(final int pageNum, int pageSize, String userId) {
        addSubscribe(apis.recommendTheme(new Recommend_theme_Request(pageNum, pageSize, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<Recommend_theme_Respne>>rxSchedulerHelper())
                .compose(RxHttpUtil.<Recommend_theme_Respne>handleResult())
                .subscribeWith(new HttpSubscriber<Recommend_theme_Respne>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(Recommend_theme_Respne recommend_theme_Respne) {
                        if (pageNum == 1 && recommend_theme_Respne.specialVoList.size() <= 0) {
                            scrollView.setVisibility(View.VISIBLE);
                            recycle.setVisibility(View.GONE);
                        } else {
                            scrollView.setVisibility(View.GONE);
                            recycle.setVisibility(View.VISIBLE);
                            if (recommend_theme_Respne.specialVoList != null && recommend_theme_Respne.specialVoList.size() > 0) {
                                list.addAll(recommend_theme_Respne.specialVoList);
                                recommend_themeAdapter = new Recommend_themeAdapter(mActivity, list);
                                recommend_themeAdapter.setRecitem(i -> {
                                    Intent intent = new Intent(mActivity, ThemeDetilsNewActivity.class);
                                    intent.putExtra("SpecialId", recommend_theme_Respne.specialVoList.get(i).specialId);
                                    intent.putExtra(CommonConst.USER_THEME, list.get(i));
                                    intent.putExtra("position", i);
                                    startActivity(intent);
                                });
                                recommend_themeAdapter.setShapitem(i -> {
                                    //分享
                                    if (isLogin()) {
                                        getShareInfo(recommend_theme_Respne.specialVoList.get(i).specialId);
                                    } else {
                                        Intent intent = new Intent(mActivity, LoginActivity.class);
                                        intent.putExtra(CommonConst.ISFINISH, 1);
                                        startActivity(intent);
                                    }
                                });
                                recycle.setAdapter(recommend_themeAdapter);
                                refreshLayout.finishLoadMoreWithNoMoreData();
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
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        recommend_theme(pageNum, pageSize, beUserId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengUtils.onResumeToFragment("Recommend_theme");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("Recommend_theme");
    }
}
