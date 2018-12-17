package com.future_melody.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.activity.BlackPearlActivity;
import com.future_melody.activity.DecibelActivity;
import com.future_melody.activity.InviteCodeActivity;
import com.future_melody.activity.InviteFriendActivity;
import com.future_melody.activity.LikeActivity;
import com.future_melody.activity.LoginActivity;
import com.future_melody.activity.MineRecommendFansActivity;
import com.future_melody.activity.MineRecommendFollowActivity;
import com.future_melody.activity.MineRecommendThemeActivity;
import com.future_melody.activity.MyInformActivity;
import com.future_melody.activity.PersonaalDataActivity;
import com.future_melody.activity.SafeAccountActivity;
import com.future_melody.activity.SettingActivity;
import com.future_melody.activity.TencentX5WebviewActivity;
import com.future_melody.activity.xiaowei.MyXiaoWeiActivity;
import com.future_melody.base.BaseFragment;
import com.future_melody.common.CommonConst;
import com.future_melody.common.SPconst;
import com.future_melody.mode.ThemeRecommendIMusic;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.api.FutrueApis;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.MineInfo;
import com.future_melody.net.request.ThemeRecommendRequest;
import com.future_melody.net.request.XingMusicRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MineInfoRespone;
import com.future_melody.net.respone.ThemeRecommendRespone;
import com.future_melody.net.respone.XingMusicRespone;
import com.future_melody.receiver.ListenXingMusicEventBus;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.TipLinearUtil;
import com.future_melody.utils.UmengUtils;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.aidl.model.TempInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.lzx.musiclibrary.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class MineFragment extends BaseFragment {
    @BindView(R.id.ph_title_right_img)
    ImageView phTitleRightImg;
    @BindView(R.id.mine_user_img)
    CircleImageView mineUserImg;
    @BindView(R.id.mine_btn_login)
    TextView mineBtnLogin;
    @BindView(R.id.btn_mine_follow)
    LinearLayout btnMineFollow;
    @BindView(R.id.btn_mine_fans)
    LinearLayout btnMineFans;
    @BindView(R.id.btn_mine_hei)
    LinearLayout btnMineHei;
    @BindView(R.id.btn_mine_fen)
    LinearLayout btnMineFen;
    @BindView(R.id.btn_mine_xiaowei)
    RelativeLayout btnMineXiaowei;
    @BindView(R.id.btn_mine_msg)
    RelativeLayout btnMineMsg;
    @BindView(R.id.btn_mine_security)
    RelativeLayout btnMineSecurity;
    @BindView(R.id.btn_mine_help)
    RelativeLayout btnMineHelp;
    @BindView(R.id.btn_mine_yaoqing)
    RelativeLayout btnMineYaoqing;
    @BindView(R.id.btn_mine_setting)
    RelativeLayout btnMineSetting;
    @BindView(R.id.mine_layout_user_info)
    LinearLayout mineLayoutUserInfo;
    @BindView(R.id.text_mine_follow)
    TextView textMineFollow;
    @BindView(R.id.text_mine_fans)
    TextView textMineFans;
    @BindView(R.id.text_mine_fansh)
    TextView textMineFansh;
    @BindView(R.id.view_mine_hei_num)
    View viewMineHeiNum;
    @BindView(R.id.view_mine_ji_num)
    View viewMineJiNum;
    @BindView(R.id.mine_user_name)
    TextView mineUserName;
    @BindView(R.id.mine_user_sex)
    ImageView mineUserSex;
    @BindView(R.id.img_text)
    ImageView img_text;
    @BindView(R.id.mine_user_address)
    TextView mineUserAddress;
    @BindView(R.id.text_mine_hei_num)
    TextView textMineHeiNum;
    @BindView(R.id.text_mine_ji_num)
    TextView textMineJiNum;
    @BindView(R.id.back_zhenzhu)
    TextView BackZhenZhu;
    @BindView(R.id.fenbei)
    TextView FenBei;
    @BindView(R.id.Rel_login)
    RelativeLayout RelLogin;
    @BindView(R.id.rel_invitation_code)
    RelativeLayout rel_invitation_code;
    @BindView(R.id.user_img_bg)
    ImageView userImgBg;
    @BindView(R.id.tv_invite)
    TextView tvInvite;
    @BindView(R.id.text_mine_like)
    TextView textMineLike;
    @BindView(R.id.text_mine_theme)
    TextView textMineTheme;
    @BindView(R.id.btn_mine_like)
    LinearLayout btnMineLike;
    @BindView(R.id.btn_mine_theme)
    LinearLayout btnMineTheme;
    @BindView(R.id.xiaowei_type)
    TextView xiaoweiType;
    private String jifen;
    private String heizhenzhu;
    private String my_invitationcode;
    private Animation animation;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        com.future_melody.utils.LogUtil.e("开始走了吗", "MineFragment");
    }

    @Override
    protected void initData() {
        initAnim();
        //是否登录
        getInfo();
//        getInfo_theme();
        if (MusicManager.isPlaying()) {
            startAnmi();
        } else {
            stoptAnmi();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ListenXingMusicEventBus even) {
        if (even.getPosition() == -1) {
            startAnmi();
        } else if (even.getPosition() == -2) {
            stoptAnmi();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.e("我的()", isLogin() + "");
        initAnim();
        if (!hidden) {
            LogUtil.e("我的()", isLogin() + "");
            initImmersionBar();
            setBarDarkFont();
            if (isLogin()) {
                LogUtil.e("userid", userId());
                initInfo(userId());
            }
        }

        String getinvite = SPUtils.getInstance().getString(SPconst.isInvite);
        tvInvite.setText(getinvite);
    }

    private void initAnim() {
        animation = AnimationUtils.loadAnimation(mActivity, R.anim.player);
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
        if (animation != null) {
            animation.cancel();
            animation = null;
        }
    }

    private void initInfo(String userid) {
        addSubscribe(apis.mineInfo(new MineInfo(userid))
                .compose(RxHttpUtil.<FutureHttpResponse<MineInfoRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<MineInfoRespone>handleResult())
                .subscribeWith(new HttpSubscriber<MineInfoRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                    }
                }) {
                    @Override
                    public void onNext(final MineInfoRespone mineInfoRespone) {
                        RequestOptions RequestOptions = new RequestOptions();
                        RequestOptions.placeholder(R.mipmap.icon_user_touxiang);
//                       options(new BlurTransformation(mActivity ,14,3));
                        Glide.with(getActivity()).load(mineInfoRespone.getHead_portrait()).apply(RequestOptions).into(mineUserImg);
                        RequestOptions options = new RequestOptions();
                        options.placeholder(R.mipmap.mine_background);
                        options.transform(new BlurTransformation(13, 3));
                        Glide.with(getActivity()).load(mineInfoRespone.getHead_portrait()).apply(options).into(userImgBg);
                        mineUserName.setText(mineInfoRespone.getNickname());
                        if (mineInfoRespone.getSex() == 1) {
                            mineUserSex.setVisibility(View.VISIBLE);
                            Glide.with(getActivity()).load(R.mipmap.man).apply(RequestOptions).into(mineUserSex);
                        } else if (mineInfoRespone.getSex() == 2) {
                            mineUserSex.setVisibility(View.VISIBLE);
                            Glide.with(getActivity()).load(R.mipmap.woman).apply(RequestOptions).into(mineUserSex);
                        } else if (mineInfoRespone.getSex() == 0) {
                            mineUserSex.setVisibility(View.GONE);
                        }
                        if (TextUtils.isEmpty(mineInfoRespone.getPlanet_name())) {
                            SPUtils.getInstance().put(SPconst.ISJOIN, false);
                        } else {
                            SPUtils.getInstance().put(SPconst.ISJOIN, true);
                        }
                        if (TextUtils.isEmpty(mineInfoRespone.getXinxi())) {
                            img_text.setVisibility(View.GONE);
                            mineUserAddress.setText("还没有签名");
                        } else {
                            img_text.setVisibility(View.VISIBLE);
                            mineUserAddress.setText(mineInfoRespone.getXinxi());
                        }
                        xiaoweiType.setText(mineInfoRespone.bangding + "");
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textMineTheme.setText(mineInfoRespone.getZhuantinumber() + "");   //主题
                                textMineLike.setText(mineInfoRespone.getLikecount() + "");  //喜欢
                                textMineFollow.setText(mineInfoRespone.getGuanzhunumber() + "");   //关注
                                textMineFansh.setText(mineInfoRespone.getFensinumber() + "");    //粉丝
                            }
                        });
                        LogUtil.e("123", mineInfoRespone.getIntegral() + "");
                        my_invitationcode = mineInfoRespone.getMy_invitationcode() + "";
                        textMineJiNum.setText(mineInfoRespone.getIntegral() + "");   //分贝
                        jifen = mineInfoRespone.getIntegral() + "";
                        textMineHeiNum.setText(mineInfoRespone.getAssets_bp() + "");   //黑珍珠
                        heizhenzhu = mineInfoRespone.getAssets_bp() + "";
                        SPUtils.getInstance().put(SPconst.ISMONEYPSW, mineInfoRespone.getCapitaltype());
                        if (TextUtils.isEmpty(mineInfoRespone.getOther_invitationcode())) {
                            rel_invitation_code.setVisibility(View.VISIBLE);
                        } else {
                            rel_invitation_code.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLogin()) {
            //登录成功
            mineBtnLogin.setVisibility(View.GONE);
            btnMineHei.setBackgroundResource(R.mipmap.fenbei);
            btnMineFen.setBackgroundResource(R.mipmap.backzhenzhu);
            viewMineHeiNum.setVisibility(View.GONE);
            viewMineJiNum.setVisibility(View.GONE);
            BackZhenZhu.setTextColor(Color.WHITE);
            textMineHeiNum.setVisibility(View.VISIBLE);
            textMineJiNum.setVisibility(View.VISIBLE);
            FenBei.setTextColor(Color.WHITE);
            img_text.setVisibility(View.VISIBLE);

        } else {
            mineBtnLogin.setVisibility(View.VISIBLE);
            viewMineHeiNum.setVisibility(View.VISIBLE);
            viewMineJiNum.setVisibility(View.VISIBLE);
            rel_invitation_code.setVisibility(View.VISIBLE);
            btnMineHei.setBackgroundResource(R.mipmap.blue_kuang);
            btnMineFen.setBackgroundResource(R.mipmap.blue_kuang);
            textMineHeiNum.setVisibility(View.GONE);
            textMineJiNum.setVisibility(View.GONE);
            BackZhenZhu.setTextColor(Color.BLACK);
            img_text.setVisibility(View.GONE);
            FenBei.setTextColor(Color.BLACK);
            userImgBg.setImageResource(R.mipmap.mine_background_no_login);
            textMineFollow.setText("0");
            textMineLike.setText("0");
            textMineFansh.setText("0");
            textMineTheme.setText("0");
            Glide.with(getActivity()).load(R.mipmap.icon_user_touxiang).into(mineUserImg);
            mineUserName.setText("——");
            mineUserAddress.setText("还没有签名");
            mineUserSex.setVisibility(View.GONE);
        }
        initInfo(userId());
        if (MusicManager.isPlaying()) {
            startAnmi();
        } else {
            stoptAnmi();
        }
        UmengUtils.onResumeToFragment("MineFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("MineFragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroyAnmi();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyAnmi();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.ph_title_right_img, R.id.img_text, R.id.btn_mine_like, R.id.rel_invitation_code, R.id.btn_mine_theme, R.id.btn_mine_yaoqing, R.id.btn_mine_follow, R.id.btn_mine_fans, R.id.btn_mine_hei, R.id.btn_mine_fen, R.id.btn_mine_xiaowei, R.id.btn_mine_msg, R.id.btn_mine_security, R.id.btn_mine_help, R.id.btn_mine_setting, R.id.mine_btn_login, R.id.mine_user_img})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_mine_theme:
                LogUtil.e("当前下标：", MusicManager.get().getCurrPlayingIndex() + "=====" + MusicManager.get().getCurrPlayingMusic().getSongName());
                if (isLogin()) {
                    intent = new Intent(mActivity, MineRecommendThemeActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }
                break;
            case R.id.mine_user_img:
                if (isLogin()) {
                    intent = new Intent(mActivity, PersonaalDataActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }

                break;
            case R.id.img_text:
                if (isLogin()) {
                    intent = new Intent(mActivity, PersonaalDataActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }

                break;
            case R.id.btn_mine_like:
                if (isLogin()) {
                    intent = new Intent(mActivity, LikeActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }

                break;
            case R.id.rel_invitation_code:
                if (isLogin()) {
                    intent = new Intent(mActivity, InviteCodeActivity.class);
                    startActivity(intent);

                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }
                break;
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
            case R.id.btn_mine_yaoqing:
                if (isLogin()) {
                    intent = new Intent(mActivity, InviteFriendActivity.class);
                    intent.putExtra("code", my_invitationcode);
                    startActivity(intent);

                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }
                break;
            case R.id.btn_mine_follow:
                if (isLogin()) {
                    intent = new Intent(mActivity, MineRecommendFollowActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }

                break;
            case R.id.btn_mine_fans:
                if (isLogin()) {
                    intent = new Intent(mActivity, MineRecommendFansActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }

                break;
            case R.id.btn_mine_hei:
                if (isLogin()) {
                    //toast("黑珍珠系统正在加紧修建");
                    intent = new Intent(mActivity, BlackPearlActivity.class);
                    intent.putExtra("heizhenzhu", heizhenzhu);
                    startActivity(intent);
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }

                break;
            case R.id.btn_mine_fen:
                if (isLogin()) {
                    intent = new Intent(mActivity, DecibelActivity.class);
                    intent.putExtra("jifen", jifen);
                    startActivity(intent);
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }
                break;
            case R.id.btn_mine_xiaowei:
                if (isLogin()) {
                    intent = new Intent(mActivity, MyXiaoWeiActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }
                break;
            case R.id.btn_mine_msg:
                if (isLogin()) {
                    intent = new Intent(mActivity, MyInformActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }

                break;
            case R.id.btn_mine_security:
                if (isLogin()) {
                    intent = new Intent(mActivity, SafeAccountActivity.class);
                    startActivity(intent);

                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }

                break;
            case R.id.btn_mine_help:
                TencentX5WebviewActivity.startPHLoanWebActivity(mActivity, FutrueApis.HOST + "/h5page/h5All/help.html", "帮助");
                break;
            case R.id.btn_mine_setting:
                intent = new Intent(mActivity, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_btn_login:
                intent = new Intent(mActivity, LoginActivity.class);
                intent.putExtra(CommonConst.ISFINISH, 1);
                startActivity(intent);
                break;
        }
    }

    private ArrayList<SongInfo> songInfos = new ArrayList<>();

    private void getInfo() {
        addSubscribe(apis.xingMusic(new XingMusicRequest(userId()))
                .compose(RxHttpUtil.<FutureHttpResponse<XingMusicRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<XingMusicRespone>handleResult())
                .subscribeWith(new HttpSubscriber<XingMusicRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(XingMusicRespone xingMusicRespone) {
//                        for (XingMusicModel respone : xingMusicRespone.starMusicVoList) {
//                            SongInfo model = new SongInfo();
//                            TempInfo tempInfo = new TempInfo();
//                            com.future_melody.utils.LogUtil.e("lyrics", respone.lyrics);
//                            if (respone.lyrics != null && !TextUtils.isEmpty(respone.lyrics)) {
//                                tempInfo.setTemp_1(respone.lyrics);
//                            }
//                            com.future_melody.utils.LogUtil.e("url", respone.musicName);
//                            tempInfo.setTemp_2(respone.isLike + "");
//                            tempInfo.setTemp_3(respone.userId);
//                            tempInfo.setTemp_4(CommonConst.ISXING_MUSIC);
//                            model.setTempInfo(tempInfo);
//                            model.setSongUrl(respone.musicPath);
//                            model.setSongCover(respone.musicCoverUrl);
//                            model.setSongId(respone.musicId);
//                            model.setArtist(respone.singerName);
//                            if (TextUtils.isEmpty(respone.musicName)) {
//                                model.setSongName("<未知>");
//                            } else {
//                                model.setSongName(respone.musicName);
//                            }
//                            songInfos.add(model);
//                            if (!MusicManager.isPlaying()) {
//                                MusicManager.get().playMusic(songInfos, 0);
//                            }
//                        }
                        SPUtils.getInstance().put(SPconst.isInviteRegister, xingMusicRespone.word);
                        SPUtils.getInstance().put(SPconst.isInvite, xingMusicRespone.word2);
                        String getinvite = SPUtils.getInstance().getString(SPconst.isInvite);
                        tvInvite.setText(getinvite);
                    }

                    @Override
                    public void onComplete() {

                    }
                }));

    }

//    private void getInfo_theme() {
//        addSubscribe(apis.getRecommendTheme(new ThemeRecommendRequest("", userId()))
//                .compose(RxHttpUtil.<FutureHttpResponse<ThemeRecommendRespone>>rxSchedulerHelper())
//                .compose(RxHttpUtil.<ThemeRecommendRespone>handleResult())
//                .subscribeWith(new HttpSubscriber<ThemeRecommendRespone>(new HttpSubscriber.ErrorListener() {
//                    @Override
//                    public void onError(ApiException exception) {
//                    }
//                }) {
//                    @Override
//                    public void onNext(ThemeRecommendRespone respone) {
//                        songInfos.clear();
//                        for (ThemeRecommendIMusic music : respone.musicVoList) {
//                            SongInfo model = new SongInfo();
//                            TempInfo tempInfo = new TempInfo();
//                            com.future_melody.utils.LogUtil.e("lyrics", music.lyrics);
//                            if (music.lyrics != null && !TextUtils.isEmpty(music.lyrics)) {
//                                tempInfo.setTemp_1(music.lyrics);
//                            }
//                            com.future_melody.utils.LogUtil.e("url", music.musicName);
//                            tempInfo.setTemp_2(music.isLike + "");
//                            tempInfo.setTemp_3(music.userId);
//                            tempInfo.setTemp_7("themeMusic");
//                            model.setTempInfo(tempInfo);
//                            model.setSongUrl(music.musicPath);
//                            model.setSongCover(music.coverUrl);
//                            model.setSongId(music.musicId);
//                            model.setArtist(music.singerName);
//                            if (TextUtils.isEmpty(music.musicName)) {
//                                model.setSongName("<未知>");
//                            } else {
//                                model.setSongName(music.musicName);
//                            }
//                            songInfos.add(model);
//                            if (!MusicManager.isPlaying()) {
//                                MusicManager.get().playMusic(songInfos, 0);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                }));
//    }

}
