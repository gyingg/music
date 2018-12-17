package com.future_melody.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.future_melody.R;
import com.future_melody.activity.LoginActivity;
import com.future_melody.activity.ThemeDetailsActivity;
import com.future_melody.activity.ThemeDetilsNewActivity;
import com.future_melody.adapter.RecommendThemeAdapter;
import com.future_melody.adapter.RecommendThemesAdapter;
import com.future_melody.adapter.ThemeNewDetailsMusicAdapter;
import com.future_melody.base.BaseFragment;
import com.future_melody.common.CommonConst;
import com.future_melody.mode.RecommendSpecialVoListBean;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.AddFollowRequest;
import com.future_melody.net.request.CancelFollow;
import com.future_melody.net.request.PlanetTheme;
import com.future_melody.net.request.ShareTheme;
import com.future_melody.net.request.ThemeSetTop;
import com.future_melody.net.respone.AddFollowRespone;
import com.future_melody.net.respone.CancelFollowRespone;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.ShareThemeRespone;
import com.future_melody.net.respone.ThemeSetTopRespone;
import com.future_melody.receiver.ThemeEventBus;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.TipLinearUtil;
import com.future_melody.utils.UmengUtils;
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
import butterknife.Unbinder;

/**
 * Author WZL
 * Date：2018/5/16 24
 * Notes: 星球详情：推荐主题
 */
public class StarInfoThemeFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    private RecyclerView recycle;
    private SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    private RecommendThemeAdapter adapter;
    private String planetId;
    private List<RecommendSpecialVoListBean> listBeans;
    private int pageNum = 1;
    private int PageSize = 10;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_star_details_theme;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        recycle = view.findViewById(R.id.recycle);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setEnableRefresh(false);
    }

    @Override
    protected void initData() {
        listBeans = new LinkedList<>();
        recycle.setLayoutManager(new LinearLayoutManager(mActivity));
        Bundle bundle = getArguments();
        planetId = bundle.getString("planetId");
        getThemeList(planetId, userId(), pageNum, PageSize);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ThemeEventBus even) {
        int position = even.position();
        LogUtil.e("position", position + "");
        RecommendSpecialVoListBean respone = listBeans.get(position);
        respone = (RecommendSpecialVoListBean) even.getRespone();
        listBeans.set(position, respone);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        LogUtil.e("StarInfoThemeFragment", "走？");
        super.onHiddenChanged(hidden);
    }

    private void getThemeList(String planetId, String userId, int pageNum, int PageSize) {
        addSubscribe(apis.planeTheme(new PlanetTheme(planetId, userId, pageNum, PageSize))
                .compose(RxHttpUtil.<FutureHttpResponse<List<RecommendSpecialVoListBean>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<RecommendSpecialVoListBean>>handleResult())
                .subscribeWith(new HttpSubscriber<List<RecommendSpecialVoListBean>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        if (exception.getMessage() != null) {
                            TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                        }
                    }
                }) {
                    @Override
                    public void onNext(List<RecommendSpecialVoListBean> respone) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        if (respone != null && respone.size() > 0) {
                            listBeans.addAll(respone);
                            adapter = new RecommendThemeAdapter(mActivity, listBeans);
                            adapter.setItemRec(new RecommendThemeAdapter.itemRec() {
                                @Override
                                public void itemRec(int i) {
                                    Intent intent = new Intent(mActivity, ThemeDetilsNewActivity.class);
                                    intent.putExtra("SpecialId", listBeans.get(i).getSpecialId());
                                    intent.putExtra(CommonConst.FIND_THEME, listBeans.get(i));
                                    intent.putExtra("position", i);
                                    startActivity(intent);
                                }
                            });
                            recycle.setAdapter(adapter);
                            adapter.setShareClickListener(new RecommendThemesAdapter.ThemeClickListener() {
                                @Override
                                public void onClick(int i) {
                                    //分享
                                    if (isLogin()) {
                                        getShareInfo(listBeans.get(i).getSpecialId());
                                    } else {
                                        Intent intent = new Intent(mActivity, LoginActivity.class);
                                        intent.putExtra(CommonConst.ISFINISH, 1);
                                        startActivity(intent);
                                    }
                                }
                            });
                            adapter.setFollowsClickListener(new RecommendThemesAdapter.ThemeClickListener() {
                                @Override
                                public void onClick(int i) {
                                    RecommendSpecialVoListBean bean = listBeans.get(i);
                                    if (isLogin()) {
                                        if (bean.getIsAttention() == 0) {
                                            bean.setIsAttention(1);
                                            addFollowList(bean.getUserId(), userId());
                                        } else {
                                            bean.setIsAttention(0);
                                            offtFollowList(bean.getUserId(), userId());
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Intent intent = new Intent(mActivity, LoginActivity.class);
                                        intent.putExtra(CommonConst.ISFINISH, 1);
                                        startActivity(intent);
                                    }
                                }
                            });
                            adapter.setTopClickListener(new RecommendThemesAdapter.ThemeClickListener() {
                                @Override
                                public void onClick(int i) {
                                    RecommendSpecialVoListBean bean = listBeans.get(i);
                                    setThemeTop(planetId, bean.getSpecialId());
                                }
                            });
                        } else {
                            refreshLayout.finishLoadMoreWithNoMoreData();
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

    //主题置顶
    private void setThemeTop(String planetId, String specialId) {
        addSubscribe(apis.setTop(new ThemeSetTop(planetId, specialId))
                .compose(RxHttpUtil.<FutureHttpResponse<ThemeSetTopRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ThemeSetTopRespone>handleResult())
                .subscribeWith(new HttpSubscriber<ThemeSetTopRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(ThemeSetTopRespone setTopRespone) {

                    }

                    @Override
                    public void onComplete() {
                        toast("置顶成功");
                        pageNum = 1;
                        listBeans.clear();
                        getThemeList(planetId, userId(), pageNum, PageSize);
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengUtils.onResumeToFragment("StarInfoThemeFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("StarInfoThemeFragment");
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNum = 1;
        listBeans.clear();
        getThemeList(planetId, userId(), pageNum, PageSize);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        getThemeList(planetId, userId(), pageNum, PageSize);
    }

}
