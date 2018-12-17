package com.future_melody.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.flyco.tablayout.SlidingTabLayout;
import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.CommonConst;
import com.future_melody.fragment.Recommend_music;
import com.future_melody.fragment.Recommend_theme;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.AddFollowRequest;
import com.future_melody.net.request.CancelFollow;
import com.future_melody.net.request.UserInforRequet;
import com.future_melody.net.respone.AddFollowRespone;
import com.future_melody.net.respone.CancelFollowRespone;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.UserInforRespne;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.TipLinearUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.lzx.musiclibrary.manager.MusicManager;

import java.util.ArrayList;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.sliding_tab_layout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.view_page)
    ViewPager viewPage;
    @BindView(R.id.user_img_bg)
    ImageView userImgBg;
    private ArrayList<Fragment> mFragments = new ArrayList<>();  //Fragment集合
    private String[] mTitles = {"推荐主题", "推荐音乐"};   //标题的集合
    private TextView tv_fans;
    private TextView tv_name;
    private TextView tv_guanzhu;
    private TextView tv_governor;
    private CircleImageView circle_img_user_info;
    private ImageView back;
    private ImageView img_music;
    private String beUserId;
    //设置滑动定住
    private Toolbar toolbar;
    private SlidingTabLayout tb_toolbar;
    private AppBarLayout mAppBarLayout;
    private TextView ph_title_name;
    private Button attention;
    private Animation animation;
    private int fansNum;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initView() {
        initAnim();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        circle_img_user_info = findViewById(R.id.circle_img_user_info);
        tv_fans = findViewById(R.id.tv_fans);
        tv_name = findViewById(R.id.tv_name);
        ph_title_name = findViewById(R.id.ph_title_name);
        img_music = findViewById(R.id.img_music);
        tv_guanzhu = findViewById(R.id.tv_guanzhu);
        tv_governor = findViewById(R.id.tv_governor);
        back = findViewById(R.id.back);
        attention = findViewById(R.id.attention);
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
        img_music.startAnimation(animation);
    }

    private void stoptAnmi() {
        img_music.clearAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAnmi();
    }

    private void destroyAnmi() {
        animation.cancel();
        animation = null;
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
    protected void initData() {
        back.setOnClickListener(this);
        tv_fans.setOnClickListener(this);
        tv_guanzhu.setOnClickListener(this);
        img_music.setOnClickListener(this);
        Recommend_music music = new Recommend_music();
        Recommend_theme theme = new Recommend_theme();
        mFragments.add(theme);
        mFragments.add(music);
        slidingTabLayout.setViewPager(viewPage, mTitles, this, mFragments);
        Intent intent = getIntent();
        LogUtil.e("aas", intent.getStringExtra("userId") + "");
        if (intent.getStringExtra("userId") != null) {
            beUserId = intent.getStringExtra("userId");
            userinfo(intent.getStringExtra("userId"), userId());
        } else {
            userinfo(userId(), userId());
        }
        Bundle bundle = new Bundle();
        bundle.putString("BeuserId", beUserId);
        music.setArguments(bundle);
        theme.setArguments(bundle);
        // toolbar.getBackground().setAlpha(0);//toolbar透明度初始化为0
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                toolbar.setBackgroundColor(changeAlpha(getResources().getColor(android.R.color.white), Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange()));
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    back.setImageResource(R.mipmap.back_back);
                    ph_title_name.setTextColor(mActivity.getResources().getColor(R.color.back));
                    img_music.setBackgroundResource(R.mipmap.back_music);
                    ImmersionBar.with(mActivity).statusBarDarkFont(true).init();
                }
                //展开
                else {
                    back.setImageResource(R.mipmap.back);
                    ph_title_name.setTextColor(mActivity.getResources().getColor(R.color.white));
                    img_music.setBackgroundResource(R.mipmap.music);
                    ImmersionBar.with(mActivity).statusBarDarkFont(false).init();
                }
            }
        });
    }

    private void userinfo(final String beUserId, String userId) {
        addSubscribe(apis.userInfo(new UserInforRequet(beUserId, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<UserInforRespne>>rxSchedulerHelper())
                .compose(RxHttpUtil.<UserInforRespne>handleResult())
                .subscribeWith(new HttpSubscriber<UserInforRespne>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(UserInforRespne UserInforRespne) {
                        RequestOptions RequestOptions = new RequestOptions();
                        RequestOptions.placeholder(R.mipmap.moren);
                        Glide.with(mActivity).load(UserInforRespne.getHeadUrl()).apply(RequestOptions).into(circle_img_user_info);
                        RequestOptions options = new RequestOptions();
                        options.placeholder(R.mipmap.mine_background);
                        options.transform(new BlurTransformation(13, 3));
                        Glide.with(mActivity).load(UserInforRespne.getHeadUrl()).apply(options).into(userImgBg);
                        tv_fans.setText("粉丝:  " + UserInforRespne.getFansCount());
                        fansNum = UserInforRespne.getFansCount();
                        tv_name.setText(UserInforRespne.getNickname());
                        tv_guanzhu.setText("关注:  " + UserInforRespne.getAttentionCount() + "");
                        tv_governor.setText(UserInforRespne.getPlanetName() + UserInforRespne.getIdentity());
                        String model = "推荐主题 <font color='#A8A8A8'>(%1$s条)</font>";
                        String model2 = "推荐音乐 <font color='#A8A8A8'>(%1$s首)</font>";
                        String specialCount = String.format(model, UserInforRespne.getSpecialCount());
                        String musicCount = String.format(model2, UserInforRespne.getMusicCount());
                        slidingTabLayout.getTitleView(0).setText(Html.fromHtml(specialCount));
                        slidingTabLayout.getTitleView(1).setText(Html.fromHtml(musicCount));
                        if (userId().equals(UserInforRespne.getUserId())) {
                            attention.setVisibility(View.GONE);
                        } else {
                            attention.setVisibility(View.VISIBLE);
                        }
                        if (UserInforRespne.getIsAttention().equals("0")) {
                            attention.setText("关注");
                        } else {
                            attention.setText("已关注");
                        }
                        attention.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isLogin()) {
                                    if (userId != null) {
                                        if (UserInforRespne.getIsAttention().equals("0")) {
                                            attention.setText("已关注");
                                            UserInforRespne.setIsAttention("1");
                                            //这里接着做请求接口  关注接口
                                            addFollowList(UserInforRespne.getUserId(), userId);  //添加关注
                                            fansNum = fansNum + 1;
                                            tv_fans.setText("粉丝:  " + fansNum);
                                        } else {
                                            attention.setText("关注");
                                            UserInforRespne.setIsAttention("0");
                                            //这里接着做请求接口  取消关注
                                            offtFollowList(UserInforRespne.getUserId(), userId);   //取消关注
                                            fansNum = fansNum - 1;
                                            tv_fans.setText("粉丝:  " + fansNum);
                                        }
                                    }
                                } else {
                                    Intent intent = new Intent(mActivity, LoginActivity.class);
                                    intent.putExtra(CommonConst.ISFINISH, 1);
                                    startActivity(intent);
                                }

                            }
                        });
                    }

                    public void onComplete() {

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
            case R.id.tv_fans:
                intent = new Intent(mActivity, MineRecommendFansActivity.class);
                intent.putExtra("beUserId", beUserId);
                startActivity(intent);
                break;
            case R.id.tv_guanzhu:
                intent = new Intent(mActivity, MineRecommendFollowActivity.class);
                intent.putExtra("beUserId", beUserId);
                startActivity(intent);
                break;
            case R.id.img_music:
                PlayerUitlis.player(mActivity);
                break;
        }
    }
}
