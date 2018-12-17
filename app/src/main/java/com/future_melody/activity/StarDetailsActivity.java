package com.future_melody.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.future_melody.R;
import com.future_melody.adapter.StarDetailsUserAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.SPconst;
import com.future_melody.fragment.ActivityFragment;
import com.future_melody.fragment.StarInfoThemeFragment;
import com.future_melody.mode.StarDetailsUserModel;
import com.future_melody.music.PlayerActivity;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.api.FutrueApis;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.JoinStarRequest;
import com.future_melody.net.request.StarDetailsRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.JoinStarRespone;
import com.future_melody.net.respone.StarDetailsRespone;
import com.future_melody.receiver.AddStarEventBus;
import com.future_melody.receiver.SendWebEventBus;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.SpaceItemDecoration;
import com.future_melody.utils.TipLinearUtil;
import com.future_melody.widget.AddStartDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.lzx.musiclibrary.manager.MusicManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

;

/**
 * Author WZL
 * Date：2018/5/15 21
 * Notes: 星球详情
 */
public class StarDetailsActivity extends BaseActivity implements OnTabSelectListener {
    private static final String MUSIC = "musicFragment";
    private static final String THEME = "themeFragment";
    @BindView(R.id.star_img)
    ImageView starImg;
    @BindView(R.id.star_name)
    TextView starName;
    @BindView(R.id.star_info)
    TextView starInfo;
    @BindView(R.id.star_rv_user)
    RecyclerView starRvUser;
    @BindView(R.id.star_details_fragment)
    FrameLayout starDetailsFragment;
    @BindView(R.id.star_details_top_bg)
    ImageView starDetailsTopBg;
    @BindView(R.id.star_man_img)
    ImageView starManImg;
    @BindView(R.id.activity_finsh)
    ImageView activityFinsh;
    @BindView(R.id.music)
    ImageView music;
    @BindView(R.id.isadd_start)
    TextView isaddStart;
    @BindView(R.id.send_theme)
    ImageView sendTheme;
    @BindView(R.id.start_tab)
    CommonTabLayout commonTabLayout;
    @BindView(R.id.star_inhabitant)
    TextView starInhabitant;
    @BindView(R.id.star_activities)
    TextView starActivities;
    @BindView(R.id.star_special)
    TextView starSpecial;
    @BindView(R.id.ruleruser)
    ImageView ruleruser;
    private StarDetailsUserAdapter userAdapter;
    // private StarInfoMusicFragment musicFragment;
    private ActivityFragment activityFragment;
    private StarInfoThemeFragment themeFragment;
    private String planetId;
    private String planetName;
    private List<StarDetailsUserModel> asteroidList;
    private String rulerUserId;
    private int isShow = 1;
    private int isJoin = 1;
    private AddStartDialog dialog;
    private Animation animation;
    private Toolbar toolbar;
    //private X5WebView webView;
    private AppBarLayout mAppBarLayout;
    private String toH5Url;
    ArrayList<CustomTabEntity> titleList = new ArrayList<CustomTabEntity>();
    ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();


    @Override
    protected int getContentViewId() {
        return R.layout.activity_new_star_details;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).init();
        initAnim();
        // webView = new X5WebView(mActivity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.getBackground().setAlpha(0);//toolbar透明度初始化为0
        commonTabLayout.setOnTabSelectListener(this);
        starRvUser.addItemDecoration(new SpaceItemDecoration());
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        planetId = intent.getStringExtra("planetId");
        initTab();
        initFragment();
        commonTabLayout.setTabData(titleList, this, R.id.star_details_fragment, fragmentList);
        commonTabLayout.getTitleView(1).setPadding(20, 0, 0, 0);  //距离
        for (int i = 0; i < commonTabLayout.getChildCount(); i++) {
            if (0 == i) {
                commonTabLayout.getTitleView(i).setTextSize(24);
                commonTabLayout.getTitleView(i).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                commonTabLayout.getTitleView(i).setTextSize(13);
                commonTabLayout.getTitleView(i).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
        asteroidList = new LinkedList<>();
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                toolbar.setBackgroundColor(changeAlpha(getResources().getColor(android.R.color.white), Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange()));
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    activityFinsh.setImageResource(R.mipmap.back_back);
                    music.setImageResource(R.mipmap.back_music);
                    ImmersionBar.with(mActivity).statusBarDarkFont(true).init();
                }
                //展开
                else {
                    activityFinsh.setImageResource(R.mipmap.back);
                    music.setImageResource(R.mipmap.music);
                    ImmersionBar.with(mActivity).statusBarDarkFont(false).init();
                }
            }
        });
        starManImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, UserInfoActivity.class);
                intent.putExtra("userId", rulerUserId);
                mActivity.startActivity(intent);
            }
        });
        startdetails(planetId);
    }

    private void initFragment() {
        // musicFragment = new StarInfoMusicFragment();
        activityFragment = new ActivityFragment();
        themeFragment = new StarInfoThemeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("planetId", planetId);//这里的values就是我们要传的值
        activityFragment.setArguments(bundle);
        themeFragment.setArguments(bundle);
        fragmentList.add(themeFragment);
        fragmentList.add(activityFragment);
    }

    private void initTab() {
        titleList.add(new CustomTabEntity() {
            @Override
            public String getTabTitle() {
                return "专题";
            }

            @Override
            public int getTabSelectedIcon() {
                return 0;
            }

            @Override
            public int getTabUnselectedIcon() {
                return 0;
            }
        });
        titleList.add(new CustomTabEntity() {
            @Override
            public String getTabTitle() {
                return "活动";
            }

            @Override
            public int getTabSelectedIcon() {
                return 0;
            }

            @Override
            public int getTabUnselectedIcon() {
                return 0;
            }
        });
    }

    /**
     * 根据百分比改变颜色透明度
     */
    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
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
        music.startAnimation(animation);
    }

    private void stoptAnmi() {
        music.clearAnimation();
    }

    private void destroyAnmi() {
        animation.cancel();
        animation = null;
    }

    private void startdetails(String planetId) {
        addSubscribe(apis.stardetails(new StarDetailsRequest(planetId, userId()))
                .compose(RxHttpUtil.<FutureHttpResponse<StarDetailsRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<StarDetailsRespone>handleResult())
                .subscribeWith(new HttpSubscriber<StarDetailsRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {

                        if (exception.getMessage() != null) {
                            TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                        }
                    }
                }) {

                    @Override
                    public void onNext(StarDetailsRespone starDetailsRespone) {
                        if (starDetailsRespone.asteroidList != null) {
                            asteroidList.addAll(starDetailsRespone.asteroidList);
                            userAdapter = new StarDetailsUserAdapter(mActivity, starDetailsRespone.asteroidList);
                            GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 2);
                            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            starRvUser.setLayoutManager(layoutManager);
                            starRvUser.setAdapter(userAdapter);
                        }
//                        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
//                            @Override
//                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//                                starDetailsTopBg.setBackground(resource);
//                            }

//                        };
                        RequestOptions options = new RequestOptions();
                        options.centerCrop()
                                .placeholder(R.mipmap.icon_diqiu)
                                .error(R.mipmap.icon_diqiu)
                                .fallback(R.mipmap.icon_diqiu);
                        Glide.with(mActivity).load(starDetailsRespone.backgroundUrl).into(starDetailsTopBg);
                        RequestOptions options1 = new RequestOptions();
                        options1.placeholder(R.mipmap.moren);
                        Glide.with(mActivity).load(starDetailsRespone.rulerHeadUrl).apply(options1).into(starManImg);
                        RequestOptions RequestOptions = new RequestOptions();
                        RequestOptions.placeholder(R.mipmap.moren);
                        Glide.with(mActivity).load(starDetailsRespone.planetUrl).apply(RequestOptions).into(starImg);
                        starName.setText(starDetailsRespone.planetName);
                        starInhabitant.setText(starDetailsRespone.userCount + "");
                        starActivities.setText(starDetailsRespone.activeCount + "");
                        starSpecial.setText(starDetailsRespone.specialCount + "");
                        planetName = starDetailsRespone.planetName;
                        starInfo.setText(starDetailsRespone.signature);
                        rulerUserId = starDetailsRespone.rulerUserId;
                        if (starDetailsRespone.isJoin == 0) {
                            sendTheme.setVisibility(View.GONE);
                        } else {
                            if (starDetailsRespone.musiciantype == 1) {
                                if (TextUtils.isEmpty(starDetailsRespone.huodongurl)) {
                                    sendTheme.setVisibility(View.GONE);
                                } else {
                                    sendTheme.setVisibility(View.VISIBLE);
                                    toH5Url = starDetailsRespone.huodongurl;
                                }
                            } else {
                                sendTheme.setVisibility(View.VISIBLE);
                            }
                        }
                        isJoin = starDetailsRespone.isJoin;
                        if (starDetailsRespone.isJoin == 0) {
                            if (SPUtils.getInstance().getBoolean(SPconst.ISJOIN)) {
                                isaddStart.setVisibility(View.GONE);
                            } else {
                                if (isLogin()) {
                                    isaddStart.setVisibility(View.VISIBLE);
                                } else {
                                    isaddStart.setVisibility(View.GONE);
                                }
                            }
                            isaddStart.setText("加入");
                        } else {
                            isaddStart.setText("已加入");
                        }
                        if (userId().equals(rulerUserId)) {
                            //是否是统治者
                            isaddStart.setVisibility(View.GONE);
                            ruleruser.setVisibility(View.VISIBLE);
                        } else {
                            if (isLogin()) {
                                isaddStart.setVisibility(View.VISIBLE);
                            } else {
                                isaddStart.setVisibility(View.GONE);
                            }
                            ruleruser.setVisibility(View.GONE);
                        }
                       /* if (userId().equals(starDetailsRespone.rulerUserId)) {
                            //是否是统治者
                            btnAppointment.setVisibility(View.VISIBLE);
                            starManQuanli.setText("管理");
                        } else {
                            btnAppointment.setVisibility(View.GONE);
                            starManQuanli.setText("统治者权利");
                        }*/
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }


    @OnClick({R.id.send_theme, R.id.music, R.id.activity_finsh, R.id.isadd_start, R.id.ruleruser})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.send_theme:
                if (!TextUtils.isEmpty(toH5Url)) {
                    X5WebviewNoTitleActivity.startPHLoanWebActivity(mActivity, toH5Url);
                } else {
                    intent = new Intent(mActivity, ReleaseActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.music:
                intent = new Intent(mActivity, PlayerActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_finsh:
                finish();
                break;
          /*  case R.id.star_details_btn_super:
                intent = new Intent(mActivity, SuperListActivity.class);
                intent.putExtra("planetId", planetId);
                intent.putExtra("rulerUserId", rulerUserId);
                startActivity(intent);
                break;*/
            case R.id.star_man_quanli:
                if (userId().equals(rulerUserId)) {
                    //是否是统治者
                    intent = new Intent(mActivity, AdministrationActivity.class);
                    startActivity(intent);
                } else {
                    LogUtil.e("rulerUserId", rulerUserId + "");
                    X5WebviewNoTitleActivity.startPHLoanWebActivity(mActivity, FutrueApis.HOST + "/h5page/h5All/Rulers.html?id=" + rulerUserId, "统治者权力");
                }
                break;
            case R.id.ruleruser:
                intent = new Intent(mActivity, AdministrationActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_appointment:
                //任命
                intent = new Intent(mActivity, AppointmentActivity.class);
                startActivity(intent);
                break;
            case R.id.isadd_start:
                //  webView.loadUrl("javascript:changeData()");
                if (isJoin == 0) {
                    dialog = new AddStartDialog(mActivity);
                    dialog.setTitle("你即将成为" + planetName + "居民," + "给自己的小行星取个名字吧");
                    dialog.setNegativeButton(view1 -> {
                        if (TextUtils.isEmpty(dialog.getMsg())) {
                            return;
                        } else {
                            joinstart(userId(), planetId, planetName, dialog.getMsg() + "小行星");
                            hideSoftInputView();
                        }
                    });
                }
                break;
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 加入星球
     */
    private void joinstart(final String userId, String starrysky_id, String planet_name, String asteroid_name) {
        addSubscribe(apis.joinstart(new JoinStarRequest(userId, starrysky_id, planet_name, asteroid_name))
                .compose(RxHttpUtil.<FutureHttpResponse<JoinStarRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<JoinStarRespone>handleResult())
                .subscribeWith(new HttpSubscriber<JoinStarRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(JoinStarRespone JoinStarRespone) {

                    }

                    @Override
                    public void onComplete() {
                        sendEvent("1");
                        AddEvent(starrysky_id, planet_name);
                        hideSoftInputView();
                        SPUtils.getInstance().put(SPconst.ISJOIN, true);
                        dialog.dismiss(mActivity);
                        Toast.makeText(mActivity, "加入星球成功", Toast.LENGTH_SHORT).show();
                        startdetails(planetId);

                    }
                }));
    }

    private void AddEvent(String isAdd, String planet_name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (planet_name != null) {
                    EventBus.getDefault().post(new AddStarEventBus(isAdd, planet_name));
                }
            }
        }).start();
    }

    private void sendEvent(String isUpdate) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isUpdate != null) {
                    EventBus.getDefault().post(new SendWebEventBus(isUpdate));
                }
            }
        }).start();
    }

    @Override
    public void onTabSelect(int position) {
        System.out.println("+++++++++++++" + commonTabLayout.getChildCount());
        System.out.println("position:" + position);
        for (int i = 0; i < commonTabLayout.getTabCount(); i++) {
            System.out.println("i:" + position);
            if (position == i) {
                System.out.println("加粗");
                commonTabLayout.getTitleView(i).setTextSize(24);
                commonTabLayout.getTitleView(i).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                System.out.println("正常");
                commonTabLayout.getTitleView(i).setTextSize(13);
                commonTabLayout.getTitleView(i).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
