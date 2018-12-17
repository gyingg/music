package com.future_melody.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.future_melody.R;
import com.future_melody.activity.LoginActivity;
import com.future_melody.base.BaseFragment;
import com.future_melody.bean.ShareItemBean;
import com.future_melody.common.CommonConst;
import com.future_melody.common.SPconst;
import com.future_melody.mode.RemendMusicNewModle;
import com.future_melody.music.PlayerNewActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.RemendThemeShare;
import com.future_melody.net.request.RemmendNewThemeRequest;
import com.future_melody.net.request.ShareSuccsecRequest;
import com.future_melody.net.request.ThemePickRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.RemendThemeRespone;
import com.future_melody.net.respone.RemmendNewThemeRespone;
import com.future_melody.net.respone.ShareSuccsecRespone;
import com.future_melody.net.respone.ThemePickRespone;
import com.future_melody.receiver.ListenXingMusicEventBus;
import com.future_melody.receiver.ThemeCommendNumEventBus;
import com.future_melody.utils.AdtailPopWindow;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.ImgUtils;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.TipLinearUtil;
import com.future_melody.widget.IsWifiDialog;
import com.future_melody.widget.RemomendThemwMusicPp;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.aidl.model.TempInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Author WZL
 * Date：2018/8/8 31
 * Notes:
 */
public class ThemeFragment extends BaseFragment implements View.OnClickListener, AdtailPopWindow.OnItemClickListener {
    private ImageView layout_btn_bg;
    private ImageView music_cover;
    private TextView theme_one_music_name;
    private TextView theme_one_music_sing;
    private ImageView theme_one_music_other;
    private ImageView music_player_type;
    private RelativeLayout layout_bottom;
    private List<RemendMusicNewModle> musicVoList;
    private ArrayList<SongInfo> songInfos = new ArrayList<>();
    private RemomendThemwMusicPp themwMusicPp;
    private LinearLayout layout_commend;
    private LinearLayout layout_share;
    private LinearLayout layout_zan;
    private ImageView theme_one_music_zan;
    private TextView zan_num;
    private TextView share_num;
    private TextView commend_num;
    private ThemeFragmentConment fragmentConment;
    private String SpecialId;

    private AdtailPopWindow adtailPopWindow;
    private List<ShareItemBean> list;
    private String shareurl;
    private UMImage image;
    private int isLike;
    private int likeCount;
    private int shareCount;
    private int commentCount;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_theme;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        layout_btn_bg = view.findViewById(R.id.layout_btn_bg);
        music_cover = view.findViewById(R.id.music_cover);
        theme_one_music_name = view.findViewById(R.id.theme_one_music_name);
        theme_one_music_sing = view.findViewById(R.id.theme_one_music_sing);
        theme_one_music_other = view.findViewById(R.id.theme_one_music_other);
        layout_bottom = view.findViewById(R.id.layout_bottom);
        music_player_type = view.findViewById(R.id.music_player_type);
        layout_commend = view.findViewById(R.id.layout_commend);
        layout_share = view.findViewById(R.id.layout_share);
        layout_zan = view.findViewById(R.id.layout_zan);
        theme_one_music_zan = view.findViewById(R.id.theme_one_music_zan);
        zan_num = view.findViewById(R.id.zan_num);
        share_num = view.findViewById(R.id.share_num);
        commend_num = view.findViewById(R.id.commend_num);
        theme_one_music_other.setOnClickListener(this);
        layout_bottom.setOnClickListener(this);
        music_player_type.setOnClickListener(this);
        layout_commend.setOnClickListener(this);
        layout_share.setOnClickListener(this);
        layout_zan.setOnClickListener(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void initData() {
        list = new ArrayList<>();
        list.add(new ShareItemBean(R.mipmap.locel_img, "保存图片"));
        list.add(new ShareItemBean(R.mipmap.weixin, "微信"));
        list.add(new ShareItemBean(R.mipmap.friends, "朋友圈"));
        list.add(new ShareItemBean(R.mipmap.weibo, "微博"));
        list.add(new ShareItemBean(R.mipmap.qq, "QQ"));
        musicVoList = new LinkedList<>();
        adtailPopWindow = new AdtailPopWindow(mActivity);//创建popwindow
        adtailPopWindow.setTitle("分享到");
        adtailPopWindow.setList(list);
        adtailPopWindow.setOnItemClickListener(this);
        getInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MusicManager.isPlaying()) {
            if (MusicManager.get().getCurrPlayingMusic().getTempInfo().getTemp_7().equals("themeMusic")) {
                music_player_type.setImageResource(R.mipmap.icon_theme_music_stop);
            } else {
                music_player_type.setImageResource(R.mipmap.icon_theme_song_cover);
            }

        } else {
            music_player_type.setImageResource(R.mipmap.icon_theme_song_cover);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ThemeCommendNumEventBus even) {
        commend_num.setText(even.getPosition() + "");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ListenXingMusicEventBus even) {
        if (even.getPosition() == -1) {
            if (MusicManager.get().getCurrPlayingMusic().getTempInfo().getTemp_7().equals("themeMusic")) {
                music_player_type.setImageResource(R.mipmap.icon_theme_music_stop);
            }
        } else if (even.getPosition() == -2) {
            music_player_type.setImageResource(R.mipmap.icon_theme_song_cover);
        }
    }

    private void getInfo() {
        addSubscribe(apis.remend_new_theme(new RemmendNewThemeRequest(userId()))
                .compose(RxHttpUtil.<FutureHttpResponse<List<RemmendNewThemeRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<RemmendNewThemeRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<RemmendNewThemeRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                    }
                }) {
                    @Override
                    public void onNext(List<RemmendNewThemeRespone> remmendNewThemeRespone) {
                        if (remmendNewThemeRespone.size() > 0) {
                            RemmendNewThemeRespone respone = remmendNewThemeRespone.get(0);
                            isLike = respone.isLike;
                            likeCount = respone.likeCount;
                            shareCount = respone.shareCount;
                            commentCount = respone.commentCount;
                            SpecialId = respone.special.specialid;
                            zan_num.setText(respone.likeCount + "");
                            share_num.setText(respone.shareCount + "");
                            commend_num.setText(respone.commentCount + "");
                            shareurl = respone.special.shareSpecialPicture;
                            if (respone.isLike == 0) {
                                theme_one_music_zan.setImageResource(R.mipmap.icon_theme_details_unzan);
                            } else {
                                theme_one_music_zan.setImageResource(R.mipmap.icon_theme_details_zan);
                            }
                            Glide.with(mActivity).load(respone.special.showPicture).into(layout_btn_bg);
                            Glide.with(mActivity).load(respone.musicList.get(0).coverUrl).into(music_cover);
                            if (respone.musicList.size() > 0) {
                                musicVoList.addAll(respone.musicList);
                                theme_one_music_name.setText(respone.musicList.get(0).musicName);
                                theme_one_music_sing.setText(respone.musicList.get(0).singerName);
                            }
                            if (respone.musicList.size() > 1) {
                                theme_one_music_other.setVisibility(View.VISIBLE);
                            } else {
                                theme_one_music_other.setVisibility(View.GONE);
                            }
                            songInfos.clear();
                            for (RemendMusicNewModle music : respone.musicList) {
                                SongInfo model = new SongInfo();
                                TempInfo tempInfo = new TempInfo();
                                LogUtil.e("lyrics", music.lyrics);
                                if (music.lyrics != null && !TextUtils.isEmpty(music.lyrics)) {
                                    tempInfo.setTemp_1(music.lyrics);
                                }
                                LogUtil.e("url", music.musicName);
                                tempInfo.setTemp_2(music.isLike + "");
                                tempInfo.setTemp_3(music.userId);
                                tempInfo.setTemp_7("themeMusic");
                                model.setTempInfo(tempInfo);
                                model.setSongUrl(music.musicPath);
                                model.setSongCover(music.coverUrl);
                                model.setSongId(music.musicId);
                                model.setArtist(music.singerName);
                                if (TextUtils.isEmpty(music.musicName)) {
                                    model.setSongName("<未知>");
                                } else {
                                    model.setSongName(music.musicName);
                                }
                                songInfos.add(model);
                                if (!MusicManager.isPlaying()) {
                                    MusicManager.get().playMusic(songInfos, 0);
                                }
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        themwMusicPp.dismiss();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.theme_one_music_other:
                LogUtil.e("点击了吗", "hhhhh");
                themwMusicPp = new RemomendThemwMusicPp(mActivity, musicVoList, songInfos);
                themwMusicPp.show(theme_one_music_other);
                break;
            case R.id.layout_bottom:
                if (MusicManager.isCurrMusicIsPlayingMusic(songInfos.get(0))) {
                    PlayerUitlis.player(mActivity);
                } else {
                    if (CommonUtils.isWifi(mActivity)) {
                        MusicManager.get().playMusic(songInfos, 0);
                        PlayerNewActivity.launch(mActivity, songInfos, 0);
                    } else {
                        if (SPUtils.getInstance().getBoolean(SPconst.ISWIFIPLAYER, false)) {
                            IsWifiDialog dialog = new IsWifiDialog(mActivity);
                            dialog.setCancelButton(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SPUtils.getInstance().put(SPconst.ISWIFIPLAYER, false);
                                    MusicManager.get().playMusic(songInfos, 0);
                                    PlayerNewActivity.launch(mActivity, songInfos, 0);
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            MusicManager.get().playMusic(songInfos, 0);
                            PlayerNewActivity.launch(mActivity, songInfos, 0);
                        }
                    }
                }
                break;
            case R.id.music_player_type:
                if (MusicManager.get().getPlayList() != null) {
                    if (MusicManager.get().getCurrPlayingMusic().getTempInfo().getTemp_7().equals("themeMusic")) {
                        if (MusicManager.isPlaying()) {
                            MusicManager.get().pauseMusic();
                            music_player_type.setImageResource(R.mipmap.icon_theme_song_cover);
                        } else {
                            MusicManager.get().playMusic(songInfos, 0);
                            music_player_type.setImageResource(R.mipmap.icon_theme_music_stop);
                        }

                    } else {
                        MusicManager.get().playMusic(songInfos, 0);
                        music_player_type.setImageResource(R.mipmap.icon_theme_music_stop);
                    }
                }

//                if (songInfos.get(0).getTempInfo().getTemp_7().equals("themeMusic")) {
//                    if (MusicManager.isPlaying()) {
//                        MusicManager.get().pauseMusic();
//                        music_player_type.setImageResource(R.mipmap.icon_theme_song_cover);
//                    } else {
//                        MusicManager.get().playMusic(songInfos, 0);
//                        music_player_type.setImageResource(R.mipmap.icon_theme_music_stop);
//                    }
//                }
                break;
            case R.id.layout_commend:
                showPopWindow();
                break;
            case R.id.layout_share:
                if (isLogin()) {
//                    getShareInfo();
                    adtailPopWindow.setImg(shareurl);  //设置图片
                    initShare(shareurl);
                    adtailPopWindow.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.TOP, 0, 0);
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }
                break;
            case R.id.layout_zan:
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
        fragmentConment.show(getFragmentManager(), "FRAGMENT");
    }

    private void getShareInfo() {
        addSubscribe(apis.getThemeShare(new RemendThemeShare(userId()))
                .compose(RxHttpUtil.<FutureHttpResponse<RemendThemeRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<RemendThemeRespone>handleResult())
                .subscribeWith(new HttpSubscriber<RemendThemeRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(RemendThemeRespone remendThemeRespone) {
//                        share(remendThemeRespone.string);
                        shareurl = remendThemeRespone.string;
                        adtailPopWindow.setImg(shareurl);  //设置图片
                        initShare(remendThemeRespone.string);
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                    }
                }));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(mActivity).onActivityResult(requestCode, resultCode, data);
    }

    private void initShare(String url) {
/*        //分享的图片
        UMImage image = new UMImage(this, pictureUrl);//网络图片
        //分享链接 必须以http开头
        web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(summary);//描述*/
        //网络图片
        image = new UMImage(mActivity, url);
        image.compressStyle = UMImage.CompressStyle.SCALE;
        image.compressStyle = UMImage.CompressStyle.QUALITY;

    }

    @Override
    public void setOnItemClickListener(int position, View v) {
        switch (position) {
            case 0:
                saveImg();
                break;
            case 1:
                shareOfPlatform(SHARE_MEDIA.WEIXIN);
                break;
            case 2:
                shareOfPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case 3:
                shareOfPlatform(SHARE_MEDIA.SINA);
                break;
            case 4:
                shareOfPlatform(SHARE_MEDIA.QQ);
                break;
        }
        adtailPopWindow.dismiss();
    }

    private void shareOfPlatform(SHARE_MEDIA media) {
        new ShareAction(mActivity)
                .setPlatform(media)
                .withMedia(image) //web
                .setCallback(umShareListener)
                .share();
    }

    private void saveImg() {
        ImgUtils imgUtils = new ImgUtils(mActivity);//图片保存相册工具类
        new Thread(new Runnable() {
            @Override
            public void run() {
                //在这里要判断是否拥有内存卡读写权限
                LogUtil.e("保存图片", shareurl + "1234567890-");
                String imgUrl = shareurl;
                imgUtils.saveBitmap(imgUtils.getHttpBitmap(imgUrl), imgUrl.substring(imgUrl.lastIndexOf("/")));
            }
        }).start();
    }

    UMShareListener umShareListener = new UMShareListener() {
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
            toast("分享失败" + throwable.getMessage());

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            toast("分享取消");
        }
    };

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
                            zan_num.setText(likeCount + "");
                            isLike = 1;

                        } else {
                            theme_one_music_zan.setImageResource(R.mipmap.icon_theme_details_unzan);
                            likeCount = likeCount - 1;
                            zan_num.setText(likeCount + "");
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
                        share_num.setText((shareCount + 1) + "");
                        dismissLoadingDialog();
                    }
                }));
    }

}
