package com.future_melody.fragment;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.activity.LoginActivity;
import com.future_melody.activity.MainActivity;
import com.future_melody.activity.RecommendFollowThemeActivity;
import com.future_melody.activity.StarDetailsActivity;
import com.future_melody.activity.ThemeDetailsActivity;
import com.future_melody.adapter.RecommendThemesAdapter;
import com.future_melody.base.BaseFragment;
import com.future_melody.common.CommonConst;
import com.future_melody.mode.RecommendSpecialVoListBean;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.AddFollowRequest;
import com.future_melody.net.request.CancelFollow;
import com.future_melody.net.request.RecommendRequest;
import com.future_melody.net.request.ShareTheme;
import com.future_melody.net.respone.AddFollowRespone;
import com.future_melody.net.respone.CancelFollowRespone;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.RecommendRespone;
import com.future_melody.net.respone.ShareThemeRespone;
import com.future_melody.receiver.AddStarEventBus;
import com.future_melody.receiver.ThemeEventBus;
import com.future_melody.utils.CommonUtils;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Y on 2018/5/11.
 * 发现 推荐Fragment
 */

public class RecommendFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener, AdapterView.OnItemClickListener, View.OnClickListener {

    @BindView(R.id.recommend_rv_list)
    ListView recommendRvList;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private View headView;
    private LinearLayout btnToXingqiu;
    private LinearLayout btnToGuanzhu;
    private TextView themeStarName;
    private CircleImageView themeStarUrl;
    private RecommendThemesAdapter adapter;
    private int pageNum = 1;
    private int pageSize = 10;
    private String planetId;
    private int isAddStart = 0;
    private List<RecommendSpecialVoListBean> listBeans;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        headView = View.inflate(mActivity, R.layout.view_headview_recommend, null);
        btnToXingqiu = headView.findViewById(R.id.btn_to_xingqiu);
        btnToGuanzhu = headView.findViewById(R.id.btn_to_guanzhu);
        themeStarName = headView.findViewById(R.id.theme_star_name);
        themeStarUrl = headView.findViewById(R.id.theme_star_url);
    }

    @Override
    protected void initData() {
        recommend(userId(), pageNum, pageSize);
        listBeans = new ArrayList<>();
        adapter = new RecommendThemesAdapter(mActivity, listBeans);
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
        recommendRvList.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        recommendRvList.addHeaderView(headView);
        refreshLayout.setOnLoadMoreListener(this);
        recommendRvList.setOnItemClickListener(this);
        btnToXingqiu.setOnClickListener(this);
        btnToGuanzhu.setOnClickListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddEvent(AddStarEventBus even) {
        if (even != null) {
            isAddStart = 0;
            LogUtil.e("isAddStart", isAddStart + "2345");
            planetId = even.getMessgae();
            LogUtil.e("position", even.getPlanet_name() + "");
            themeStarName.setText(even.getPlanet_name());
        }
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

    /**
     * 获取推荐主题列表
     */
    private void recommend(final String userId, int pageNum, int pageSize) {
        addSubscribe(apis.recommend(new RecommendRequest(userId, pageNum, pageSize))
                .compose(RxHttpUtil.<FutureHttpResponse<RecommendRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<RecommendRespone>handleResult())
                .subscribeWith(new HttpSubscriber<RecommendRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                        refreshLayout.finishRefresh(false);
                        refreshLayout.finishLoadMore(false);
                    }
                }) {

                    @Override
                    public void onNext(RecommendRespone recommendRespone) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
//                        if (recommendRespone.getPicUrl() != null) {
//                            Glide.with(mActivity).load(recommendRespone.getPicUrl()).into(themeStarUrl);
//                        }
                        if (recommendRespone.getPlanetName() != null && !TextUtils.isEmpty(recommendRespone.getPlanetName())) {
                            isAddStart = 0;
                            themeStarName.setText(recommendRespone.getPlanetName());
                            adapter.notifyDataSetChanged();
                        } else {
                            themeStarName.setText("星球");
                            isAddStart = 1;
                        }
                        if (recommendRespone.getRecommendSpecialVoList() != null && recommendRespone.getRecommendSpecialVoList().size() > 0) {
                            listBeans.addAll(recommendRespone.getRecommendSpecialVoList());
                        } else {
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        }
                        planetId = (String) recommendRespone.getPlanetId();
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        recommend(userId(), pageNum, pageSize);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (CommonUtils.isNetworkAvalible(mActivity)) {
            pageNum = 1;
            listBeans.clear();
            recommend(userId(), pageNum, pageSize);
        } else {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (listBeans.size() > 0) {
            if (i > 0) {
                Intent intent = new Intent(mActivity, ThemeDetailsActivity.class);
                intent.putExtra("SpecialId", listBeans.get(i - 1).getSpecialId());
                intent.putExtra(CommonConst.FIND_THEME, listBeans.get(i - 1));
                intent.putExtra("position", i - 1);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_to_xingqiu:
                if (isLogin()) {
                    LogUtil.e("isAddStart", isAddStart + "");
                    if (isAddStart != 1) {
                        intent = new Intent(mActivity, StarDetailsActivity.class);
                        intent.putExtra("planetId", planetId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PendingIntent pendingIntent =
                                PendingIntent.getActivity(mActivity, 0, intent, 0);
                        try {
                            pendingIntent.send();
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // joinstart(userId, "5877dd5254df4eaebcb7df02402ec5a3", "金星");
                        ((MainActivity) getActivity()).showLoanFragment(0);
                    }
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }
                break;
            case R.id.btn_to_guanzhu:
                if (isLogin()) {
                    intent = new Intent(mActivity, RecommendFollowThemeActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengUtils.onResumeToFragment("RecommendFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("RecommendFragment");
    }
}
