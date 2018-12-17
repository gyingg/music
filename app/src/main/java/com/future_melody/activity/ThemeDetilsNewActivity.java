package com.future_melody.activity;

import android.content.Intent;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.future_melody.R;
import com.future_melody.adapter.ThemeNewDetailsMusicAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.CommonConst;
import com.future_melody.common.SPconst;
import com.future_melody.fragment.ThemeFragmentConment;
import com.future_melody.mode.ThemeDetailsMusicBean;
import com.future_melody.music.PlayerNewActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.AddFollowRequest;
import com.future_melody.net.request.CancelFollow;
import com.future_melody.net.request.ListerMusicRequest;
import com.future_melody.net.request.ShareSuccsecRequest;
import com.future_melody.net.request.ShareTheme;
import com.future_melody.net.request.ThemeDetails;
import com.future_melody.net.request.ThemePickRequest;
import com.future_melody.net.respone.AddFollowRespone;
import com.future_melody.net.respone.CancelFollowRespone;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.ListerMusicRespone;
import com.future_melody.net.respone.ShareSuccsecRespone;
import com.future_melody.net.respone.ShareThemeRespone;
import com.future_melody.net.respone.ThemeDetailsRespone;
import com.future_melody.net.respone.ThemePickRespone;
import com.future_melody.receiver.ListenXingMusicEventBus;
import com.future_melody.receiver.ThemeCommendNumEventBus;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.TipLinearUtil;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.aidl.model.TempInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author WZL
 * Date：2018/8/21 24
 * Notes:
 */
public class ThemeDetilsNewActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ph_title_back;
    private ImageView ph_title_right_img;
    private LinearLayout btn_layout_player_all;
    private TextView theme_details_context;
    private TextView theme_details_time;
    private TextView text_btn_follows;
    private RecyclerView theme_new_music;
    private CircleImageView theme_details_userimg;
    private LinearLayout btn_layout_add_follows;
    private ImageView theme_new_commend;
    private ImageView theme_details_new_bg;
    private TextView theme_new_zan_num;
    private TextView theme_new_share_num;
    private TextView theme_new_commend_num;
    private LinearLayout layout_theme_details_share;
    private ImageView all_player_img;
    private ImageView theme_one_music_zan;
    private ThemeNewDetailsMusicAdapter musicAdapter;
    private ArrayList<SongInfo> songInfos = new ArrayList<>();
    private List<ThemeDetailsMusicBean> musicBeanList;
    private String beUserId;
    private boolean isFollows;
    private String SpecialId;
    private int commentCount;
    private Animation animation;
    private ThemeFragmentConment fragmentConment;
    private int isLike;
    private int likeCount;
    private int shareCount;
    private LinearLayout layout_theme_details_zan;
    private LinearLayout layout_theme_details_commend;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_theme_new_details;
    }

    @Override
    protected void initView() {
        ph_title_back = findViewById(R.id.ph_title_back);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        btn_layout_player_all = findViewById(R.id.btn_layout_player_all);
        theme_details_context = findViewById(R.id.theme_details_context);
        theme_details_time = findViewById(R.id.theme_details_time);
        theme_new_music = findViewById(R.id.theme_new_music);
        theme_details_userimg = findViewById(R.id.theme_details_userimg);
        btn_layout_add_follows = findViewById(R.id.btn_layout_add_follows);
        theme_new_commend = findViewById(R.id.theme_new_commend);
        theme_details_new_bg = findViewById(R.id.theme_details_new_bg);
        text_btn_follows = findViewById(R.id.text_btn_follows);
        theme_new_zan_num = findViewById(R.id.theme_new_zan_num);
        theme_new_share_num = findViewById(R.id.theme_new_share_num);
        theme_new_commend_num = findViewById(R.id.theme_new_commend_num);
        layout_theme_details_share = findViewById(R.id.layout_theme_details_share);
        layout_theme_details_zan = findViewById(R.id.layout_theme_details_zan);
        layout_theme_details_commend = findViewById(R.id.layout_theme_details_commend);
        theme_one_music_zan = findViewById(R.id.theme_one_music_zan);
        all_player_img = findViewById(R.id.all_player_img);
        ph_title_back.setOnClickListener(this);
        btn_layout_add_follows.setOnClickListener(this);
        layout_theme_details_share.setOnClickListener(this);
        theme_details_userimg.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
        btn_layout_player_all.setOnClickListener(this);
        layout_theme_details_zan.setOnClickListener(this);
        layout_theme_details_commend.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ThemeCommendNumEventBus even) {
        theme_new_commend_num.setText(even.getPosition() + "");
    }

    @Override
    protected void initData() {
        musicBeanList = new LinkedList<>();
        Intent intent = getIntent();
        SpecialId = intent.getStringExtra("SpecialId");
        getDeatails(userId(), SpecialId);
        initAnim();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ListenXingMusicEventBus even) {
        if (even.getPosition() == -1) {
            startAnmi();
        } else if (even.getPosition() == -2) {
            stoptAnmi();
        }

    }

    private void getDeatails(String userId, String specialid) {
        showLoadingDialog();
        addSubscribe(apis.themeDetails(new ThemeDetails(specialid, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<ThemeDetailsRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ThemeDetailsRespone>handleResult())
                .subscribeWith(new HttpSubscriber<ThemeDetailsRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        dismissLoadingDialog();
                        toast(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(ThemeDetailsRespone themeDetailsRespone) {
                        dismissLoadingDialog();
                        musicBeanList.clear();
                        isLike = themeDetailsRespone.isLike;
                        likeCount = themeDetailsRespone.likeCount;
                        shareCount = themeDetailsRespone.shareCount;
                        commentCount = themeDetailsRespone.commentCount;
                        theme_new_zan_num.setText(themeDetailsRespone.likeCount + "");
                        theme_new_share_num.setText(themeDetailsRespone.shareCount + "");
                        theme_new_commend_num.setText(themeDetailsRespone.commentCount + "");
                        if (themeDetailsRespone.isLike == 0) {
                            theme_one_music_zan.setImageResource(R.mipmap.icon_theme_details_unzan);
                        } else {
                            theme_one_music_zan.setImageResource(R.mipmap.icon_theme_details_zan);
                        }
                        commentCount = themeDetailsRespone.commentCount;
                        Glide.with(mActivity).load(themeDetailsRespone.specialPictureUrl).into(theme_details_new_bg);
                        theme_details_context.setText(themeDetailsRespone.specialDescription);
                        Glide.with(mActivity).load(themeDetailsRespone.userHeadUrl).into(theme_details_userimg);
                        theme_details_time.setText(themeDetailsRespone.createTime);
                        theme_new_commend_num.setText(themeDetailsRespone.commentCount + "");
                        beUserId = themeDetailsRespone.userId;
                        if (userId().equals(beUserId)) {
                            btn_layout_add_follows.setVisibility(View.GONE);
                        } else {
                            btn_layout_add_follows.setVisibility(View.VISIBLE);
                        }
                        if (themeDetailsRespone.isAttention == 0) {
                            text_btn_follows.setText("关注");
                            isFollows = false;
                        } else {
                            text_btn_follows.setText("已关注");
                            isFollows = true;
                        }
                        if (themeDetailsRespone.musicVoList != null) {
                            songInfos.clear();
                            for (ThemeDetailsMusicBean respone : themeDetailsRespone.musicVoList) {
                                SongInfo model = new SongInfo();
                                TempInfo tempInfo = new TempInfo();
                                LogUtil.e("lyrics", respone.lyrics);
                                if (respone.lyrics != null && !TextUtils.isEmpty(respone.lyrics)) {
                                    tempInfo.setTemp_1(respone.lyrics);
                                }
                                LogUtil.e("url", respone.musicName);
                                tempInfo.setTemp_2(respone.isLike + "");
                                tempInfo.setTemp_3(respone.userId);
                                model.setTempInfo(tempInfo);
                                model.setSongUrl(respone.musicPath);
                                model.setSongCover(respone.coverUrl);
                                model.setSongId(respone.musicId);
                                model.setArtist(respone.singerName);
                                if (TextUtils.isEmpty(respone.musicName)) {
                                    model.setSongName("<未知>");
                                } else {
                                    model.setSongName(respone.musicName);
                                }
                                songInfos.add(model);
                            }

                            LogUtil.e("musicVoList", themeDetailsRespone.musicVoList.size() + "");
                            musicBeanList.addAll(themeDetailsRespone.musicVoList);
                            musicAdapter = new ThemeNewDetailsMusicAdapter(mActivity, musicBeanList);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
                            theme_new_music.setLayoutManager(layoutManager);
                            theme_new_music.setAdapter(musicAdapter);
                            musicAdapter.setItemClickListener(new ThemeNewDetailsMusicAdapter.ItemClickListener() {
                                @Override
                                public void set(int i) {
                                    SongInfo songInfo = songInfos.get(i);
                                    if (MusicManager.isCurrMusicIsPlayingMusic(songInfo)) {
                                        PlayerUitlis.player(mActivity, songInfos);
                                    } else {
                                        int isXingMusic;
                                        if (!TextUtils.isEmpty(MusicManager.get().getCurrPlayingMusic().getTempInfo().getTemp_4())) {
                                            isXingMusic = 1;
                                        } else {
                                            isXingMusic = 0;
                                        }
                                        if (MusicManager.get().getProgress() / 1000 > 4) {
                                            ListerMusic(CommonUtils.data(), 0, MusicManager.get().getCurrPlayingMusic().getSongId(), (int) (MusicManager.get().getProgress() / 1000), SPUtils.getInstance().getString(SPconst.starTime), userId(), isXingMusic);
                                        }
                                        MusicManager.get().playMusic(songInfos, i);
                                        PlayerNewActivity.launch(mActivity, songInfos, i);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                    }
                }));
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ph_title_back:
                finish();
                break;
            case R.id.btn_layout_player_all:
                if (songInfos.size() > 0) {
//                    MusicManager.get().playMusic(songInfos, 0);
                    MusicManager.get().playMusic(songInfos, 0);
                    PlayerNewActivity.launch(mActivity, songInfos, 0);
                }
//                if (MusicManager.isPlaying()) {
//                    all_player_img.setImageResource(R.mipmap.icon_theme_all_player_stop);
//                } else {
//                    all_player_img.setImageResource(R.mipmap.icon_theme_all_player);
//                }
                break;
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
            case R.id.btn_layout_add_follows:
                //关注
                LogUtil.e("是否登录", isLogin() + "");
                if (isLogin()) {
                    if (isFollows) {
                        text_btn_follows.setText("关注");
                        offtFollowList(beUserId, userId());
                    } else {
                        text_btn_follows.setText("已关注");
                        addFollowList(beUserId, userId());
                    }
                    isFollows = !isFollows;
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }
                break;
            case R.id.layout_theme_details_share:
                //分享
                if (isLogin()) {
                    getShareInfo();
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }
                break;
            case R.id.theme_details_userimg:
                intent = new Intent(mActivity, UserInfoActivity.class);
                intent.putExtra("userId", beUserId);
                startActivity(intent);
                break;
            case R.id.layout_theme_details_commend:
                showPopWindow();
                break;
            case R.id.layout_theme_details_zan:
                if (isLogin()) {
                    themePick(SpecialId);
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }
                break;
        }
    }


    //不要忘记重写分享成功后的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void getShareInfo() {
        addSubscribe(apis.sharTheme(new ShareTheme(SpecialId))
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
        UMImage thumb = new UMImage(this, img);
        LogUtil.e("url", url);
        LogUtil.e("img", img);
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(description);//描述
        new ShareAction(this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        toast("分享成功");
                        share_succes(SpecialId);
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

    //显示评论
    private void showPopWindow() {
        if (fragmentConment == null) {
            fragmentConment = new ThemeFragmentConment();
            Bundle bundle = new Bundle();
            bundle.putString("SpecialId", SpecialId);
            fragmentConment.setArguments(bundle);
        }
        LogUtil.e("高度：", 2 / CommonUtils.getWindowHeight(mActivity) + "hhh" + CommonUtils.getWindowHeight(mActivity));
        fragmentConment.setTopOffset((CommonUtils.getWindowHeight(mActivity) / 5) * 2);
        fragmentConment.show(getSupportFragmentManager(), "FRAGMENT");
    }

    //主题点赞
    public void themePick(String specialId) {
        addSubscribe(apis.theme_pick(new ThemePickRequest(specialId, userId()))
                .compose(RxHttpUtil.<FutureHttpResponse<ThemePickRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ThemePickRespone>handleResult())
                .subscribeWith(new HttpSubscriber<ThemePickRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(ThemePickRespone themePickRespone) {
                    }

                    @Override
                    public void onComplete() {
                        if (isLike == 0) {
                            theme_one_music_zan.setImageResource(R.mipmap.icon_theme_details_zan);
                            likeCount = likeCount + 1;
                            theme_new_zan_num.setText(likeCount + "");
                            isLike = 1;
                        } else {
                            theme_one_music_zan.setImageResource(R.mipmap.icon_theme_details_unzan);
                            likeCount = likeCount - 1;
                            theme_new_zan_num.setText(likeCount + "");
                            isLike = 0;
                        }
                        dismissLoadingDialog();
                    }
                }));
    }

    //分享成功回调
    public void share_succes(String specialId) {
        addSubscribe(apis.shareRespone(new ShareSuccsecRequest(userId(), specialId))
                .compose(RxHttpUtil.<FutureHttpResponse<ShareSuccsecRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ShareSuccsecRespone>handleResult())
                .subscribeWith(new HttpSubscriber<ShareSuccsecRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(ShareSuccsecRespone themePickRespone) {
                    }

                    @Override
                    public void onComplete() {
                        theme_new_share_num.setText((shareCount + 1) + "");
                        dismissLoadingDialog();
                    }
                }));
    }

    private void ListerMusic(String endTime, int isComplete, String musicId, int playTime, String startTime, String userId, int type) {
        addSubscribe(apis.listermusic(new ListerMusicRequest(endTime, isComplete, musicId, playTime, startTime, userId, type))
                .compose(RxHttpUtil.<FutureHttpResponse<ListerMusicRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ListerMusicRespone>handleResult())
                .subscribeWith(new HttpSubscriber<ListerMusicRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                    }
                }) {

                    @Override
                    public void onNext(ListerMusicRespone listenMusicRespone) {
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }
}
