package com.future_melody.music;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.activity.LoginActivity;
import com.future_melody.adapter.MyCardHandler;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.CommonConst;
import com.future_melody.common.SPconst;
import com.future_melody.music.listener.SimpleSeekBarChangeListener;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.DotPraiseRequest;
import com.future_melody.net.request.ListerMusicRequest;
import com.future_melody.net.request.PlayerIsLikeRequest;
import com.future_melody.net.request.ShareMusicRequest;
import com.future_melody.net.respone.DotPraiseResponse;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.ListerMusicRespone;
import com.future_melody.net.respone.PlayerIsLikeRepone;
import com.future_melody.net.respone.ShareMusicRespone;
import com.future_melody.receiver.MusicIsLikeEventBus;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.DownloadUtils;
import com.future_melody.utils.FormatUtil;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.TipLinearUtil;
import com.future_melody.view.DownloadingListener;
import com.future_melody.widget.cardview.CardViewPager;
import com.lzx.musiclibrary.aidl.listener.OnPlayerEventListener;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.constans.PlayMode;
import com.lzx.musiclibrary.manager.MusicManager;
import com.lzx.musiclibrary.manager.TimerTaskManager;
import com.lzx.musiclibrary.utils.LogUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMusic;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import me.wcy.lrcview.LrcView;

public class PlayerNewActivity extends BaseActivity implements OnPlayerEventListener, LrcView.OnPlayClickListener {

    @BindView(R.id.player_img_bg)
    ImageView playerImgBg;
    @BindView(R.id.player_title_back)
    ImageView playerTitleBack;
    @BindView(R.id.player_title_share)
    ImageView playerTitleShare;
    @BindView(R.id.sing_info)
    TextView singInfo;
    @BindView(R.id.song_name)
    TextView songName;
    @BindView(R.id.lrc_view)
    LrcView lrcView;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.start_time)
    TextView startTime;
    @BindView(R.id.total_time)
    TextView totalTime;
    @BindView(R.id.btn_zan)
    ImageView btnZan;
    @BindView(R.id.btn_music_type)
    ImageView btnMusicType;
    @BindView(R.id.btn_play_pause)
    ImageView btnPlayPause;
    @BindView(R.id.btn_pre)
    ImageView btnPre;
    @BindView(R.id.btn_next)
    ImageView btnNext;
    @BindView(R.id.btn_plaer_new_include)
    RelativeLayout btnPlaerNewInclude;
    private List<SongInfo> songInfos;
    private TimerTaskManager mTimerTaskManager;
    private int postion;
    private SongInfo mSoninfo;
    private int type;
    private int isLike;
    private RelativeLayout btn_player_layout_top;
    private ImageView layout_lrc_top_bg;
    private FrameLayout layout_btn_lrc;
    private CardViewPager music_cover_img;
    private TextView music_name_top;
    private TextView music_sing_top;
    private int isXingMusic;

    public static void launch(Context context, List<SongInfo> songInfos, int position) {
        Intent intent = new Intent(context, PlayerNewActivity.class);
        intent.putParcelableArrayListExtra(CommonConst.SONGINFO, (ArrayList<? extends Parcelable>) songInfos);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_player_two;
    }

    @Override
    protected void initView() {
        if (isLogin()) {
            if (MusicManager.get().getCurrPlayingMusic() != null) {
                isLike(MusicManager.get().getCurrPlayingMusic().getSongId());
            }
        }
        setBarColor();
        if (MusicManager.get().getCurrPlayingMusic().getTempInfo().getTemp_4() != null) {
            if (MusicManager.get().getCurrPlayingMusic().getTempInfo().getTemp_4().equals(CommonConst.ISXING_MUSIC)) {
                btnZan.setVisibility(View.GONE);
                seekBar.setEnabled(false);
                lrcView.setOnPlayClickListener(null);
            } else {
                seekBar.setEnabled(true);
                btnZan.setVisibility(View.VISIBLE);
                lrcView.setOnPlayClickListener(this);
            }
        }
        btn_player_layout_top = findViewById(R.id.btn_player_layout_top);
        layout_lrc_top_bg = findViewById(R.id.layout_lrc_top_bg);
        layout_btn_lrc = findViewById(R.id.layout_btn_lrc);
        music_cover_img = findViewById(R.id.music_cover_img);
        music_name_top = findViewById(R.id.music_name_top);
        music_sing_top = findViewById(R.id.music_sing_top);
        MyCardHandler myCardHandler = new MyCardHandler();
        myCardHandler.setItemClickListener(new MyCardHandler.ItemClickListener() {
            @Override
            public void set(int i) {
                btn_player_layout_top.setVisibility(View.GONE);
                music_name_top.setVisibility(View.VISIBLE);
                music_sing_top.setVisibility(View.VISIBLE);
                playerImgBg.setVisibility(View.VISIBLE);
                layout_btn_lrc.setVisibility(View.VISIBLE);
                layout_lrc_top_bg.setVisibility(View.VISIBLE);
            }
        });
        initOnclick();
        music_cover_img.bind(getSupportFragmentManager(), myCardHandler, MusicManager.get().getPlayList());
        music_cover_img.setCardTransformer(180, 0.1f);
        music_cover_img.setCardPadding(50);
        music_cover_img.setCardMargin(30);
        music_cover_img.notifyUI(CardViewPager.MODE_CARD);
        music_cover_img.setCurrentItem(MusicManager.get().getCurrPlayingIndex());
        music_cover_img.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("MainActivity", "position:" + position + " pos:" + (position % MusicManager.get().getPlayList().size()));
                LogUtil.e("正在播放歌曲", (position % MusicManager.get().getPlayList().size()) + "");
                if ((position % MusicManager.get().getPlayList().size()) > MusicManager.get().getCurrPlayingIndex()) {
                    //上一首
                    LogUtil.e("滑动播放", "下一首");
                    if (MusicManager.get().hasNext()) {
                        lrcView.loadLrc("");
                        if (MusicManager.get().getCurrPlayingIndex() == MusicManager.get().getPlayList().size() - 1) {

                        } else {
                            MusicManager.get().playMusic(MusicManager.get().getPlayList(), MusicManager.get().getCurrPlayingIndex() + 1);
                        }
                    } else {
                        Toast.makeText(mActivity, "没有下一首了", Toast.LENGTH_SHORT).show();
                    }
                } else if ((position % MusicManager.get().getPlayList().size()) < MusicManager.get().getCurrPlayingIndex()) {
                    //下一首
                    LogUtil.e("滑动播放", "上一首");
                    if (MusicManager.get().hasPre()) {
                        lrcView.loadLrc("");
                        if (MusicManager.get().getCurrPlayingIndex() == 0) {

                        } else {
                            MusicManager.get().playMusic(MusicManager.get().getPlayList(), MusicManager.get().getCurrPlayingIndex() - 1);
                        }
                    } else {
                        Toast.makeText(mActivity, "没有上一首了", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        viewPager.bind(getSupportFragmentManager(), new MyCardHandler(), Arrays.asList(imageArray));
    }

    private void initOnclick() {
        layout_btn_lrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_player_layout_top.setVisibility(View.VISIBLE);
                music_name_top.setVisibility(View.GONE);
                music_sing_top.setVisibility(View.GONE);
                playerImgBg.setVisibility(View.GONE);
                layout_btn_lrc.setVisibility(View.GONE);
                layout_lrc_top_bg.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void initData() {
        LogUtil.e("当前播放列表", MusicManager.get().getPlayList().size() + "");
        LogUtil.e("当前播放模式", MusicManager.get().getPlayMode() + "");
        mTimerTaskManager = new TimerTaskManager();
        type = SPUtils.getInstance().getInt(SPconst.PLAYER_TYPE);
        playerType(type);
        mSoninfo = new SongInfo();
        if (MusicManager.get().getPlayList() != null && MusicManager.get().getPlayList().size() > 0) {
            songInfos = MusicManager.get().getPlayList();
            mSoninfo = songInfos.get(MusicManager.get().getCurrPlayingIndex());
        }
        if (MusicManager.isIdea()) {
            MusicManager.get().playMusicByInfo(mSoninfo);
        }
        if (MusicManager.isPlaying()) {
            mTimerTaskManager.scheduleSeekBarUpdate();
        }
        if (MusicManager.isPaused()) {
            mTimerTaskManager.scheduleSeekBarUpdate();
        }
        postion = getIntent().getIntExtra("position", -1);
        if (postion != -1) {
            MusicManager.get().setPlayMode(PlayMode.PLAY_IN_ORDER);
        }
        seekBar.setOnSeekBarChangeListener(new SimpleSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                LogUtil.e("seekBar.getProgress()", seekBar.getProgress() + "");
                MusicManager.get().seekTo(seekBar.getProgress());
            }
        });
        updateUI(mSoninfo);
        MusicManager.get().addPlayerEventListener(this);
        mTimerTaskManager.setUpdateProgressTask(this::updateProgress);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(MusicIsLikeEventBus even) {
        String isliKE = even.getMessgae();
        if (isliKE.equals("0")) {
            isLike = 0;
            btnZan.setImageResource(R.mipmap.icon_star_music_unplayer);
        } else {
            isLike = 1;
            btnZan.setImageResource(R.mipmap.icon_star_music_zan);
        }
        EventBus.getDefault().removeStickyEvent(even);
    }


    private void updateUI(SongInfo songInfo) {
        if (songInfo == null) {
            return;
        }
        if (MusicManager.get().isPlaying()) {
            btnPlayPause.setImageResource(R.mipmap.ic_pause);
        } else {
            btnPlayPause.setImageResource(R.mipmap.icon_player_stop);
        }
//        if (isLogin()) {
//            if (songInfo.getTempInfo() != null) {
//                if (songInfo.getTempInfo().getTemp_2().equals("0")) {
//                    isLike = 0;
//                    btnZan.setImageResource(R.mipmap.icon_star_music_unplayer);
//                } else {
//                    isLike = 1;
//                    btnZan.setImageResource(R.mipmap.icon_star_music_zan);
//                }
//            }
//        }
        LogUtil.e("正在播放：", MusicManager.get().getCurrPlayingIndex() + "");
        music_cover_img.setCurrentItem(MusicManager.get().getCurrPlayingIndex());
        songName.setText(songInfo.getSongName());
        music_name_top.setText(songInfo.getSongName());
        singInfo.setText(songInfo.getArtist());
        music_sing_top.setText(songInfo.getArtist());
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.mine_background);
        options.transform(new BlurTransformation(13, 3));
        Glide.with(mActivity).load(songInfo.getSongCover()).apply(options).into(playerImgBg);
        if (songInfo.getTempInfo() != null) {
            if (!TextUtils.isEmpty(MusicManager.get().getCurrPlayingMusic().getTempInfo().getTemp_1())) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DownloadUtils.downloadLrc(MusicManager.get().getCurrPlayingMusic().getSongId(), MusicManager.get().getCurrPlayingMusic().getTempInfo().getTemp_1(), new DownloadingListener() {
                            @Override
                            public void onDownloadSuccess(File file) {
                                if (file != null) {
                                    lrcView.loadLrc(file);
                                }
//                                lrcView.loadLrc("R.raw.xuemaojiao");
//                                LogUtil.e("歌词url：", songInfo.getTempInfo().getTemp_1());
                                LogUtil.e("歌词：", "下载完成");
                            }

                            @Override
                            public void onDownloading(int progress) {

                            }

                            @Override
                            public void onDownloadFailed() {

                            }

                        });

                    }
                });
            }
        }
    }

    /**
     * 更新进度
     */
    private void updateProgress() {
        if (seekBar.getMax() != MusicManager.get().getDuration()) {
            seekBar.setMax(MusicManager.get().getDuration());
            totalTime.setText(FormatUtil.formatMusicTime(MusicManager.get().getDuration()));
            LogUtil.e("音乐时长 = ", MusicManager.get().getDuration() + "");
        }
        long progress = MusicManager.get().getProgress();
        long bufferProgress = MusicManager.get().getBufferedPosition();
        seekBar.setProgress((int) progress);
        seekBar.setSecondaryProgress((int) bufferProgress);

        startTime.setText(FormatUtil.formatMusicTime(progress));
        if (lrcView.hasLrc()) {
            lrcView.updateTime(progress);
        }
    }

    @OnClick({R.id.btn_play_pause, R.id.btn_pre, R.id.btn_next, R.id.btn_music_type, R.id.player_title_back, R.id.player_title_share, R.id.btn_zan})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_play_pause:
                if (MusicManager.isPlaying()) {
                    MusicManager.get().pauseMusic();
                } else {
                    MusicManager.get().resumeMusic();
                }
                break;
            case R.id.btn_pre:
                LogUtil.e("播放器", "上一首当前时长:" + FormatUtil.formatMusicTime(MusicManager.get().getProgress()));
                if (!TextUtils.isEmpty(MusicManager.get().getCurrPlayingMusic().getTempInfo().getTemp_4())) {
                    isXingMusic = 1;
                } else {
                    isXingMusic = 0;
                }
                if (MusicManager.get().getProgress() / 1000 > 4) {
                    ListerMusic(CommonUtils.data(), 0, MusicManager.get().getCurrPlayingMusic().getSongId(), (int) (MusicManager.get().getProgress() / 1000), SPUtils.getInstance().getString(SPconst.starTime), userId(), isXingMusic);
                }
                if (MusicManager.get().hasPre()) {
                    lrcView.loadLrc("");
                    MusicManager.get().playPre();
                } else {
                    Toast.makeText(mActivity, "没有上一首了", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_next:
                LogUtil.e("播放器", "下一首当前时长:" + FormatUtil.formatMusicTime(MusicManager.get().getProgress()));
                if (!TextUtils.isEmpty(MusicManager.get().getCurrPlayingMusic().getTempInfo().getTemp_4())) {
                    isXingMusic = 1;
                } else {
                    isXingMusic = 0;
                }
                if (MusicManager.get().getProgress() / 1000 > 4) {
                    ListerMusic(CommonUtils.data(), 0, MusicManager.get().getCurrPlayingMusic().getSongId(), (int) (MusicManager.get().getProgress() / 1000), SPUtils.getInstance().getString(SPconst.starTime), userId(), isXingMusic);
                }
                if (MusicManager.get().hasNext()) {
                    lrcView.loadLrc("");
                    MusicManager.get().playNext();
                } else {
                    Toast.makeText(mActivity, "没有下一首了", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_music_type:
                type = SPUtils.getInstance().getInt(SPconst.PLAYER_TYPE);
                LogUtil.e("type", type + "");
                if (type == 3) {
                    type = 1;
                    SPUtils.getInstance().put(SPconst.PLAYER_TYPE, 1);
                } else if (type == 1) {
                    type = 2;
                    SPUtils.getInstance().put(SPconst.PLAYER_TYPE, 2);
                } else if (type == 2) {
                    type = 3;
                    SPUtils.getInstance().put(SPconst.PLAYER_TYPE, 3);
                }
                playerType(type);
                break;
            case R.id.player_title_back:
                finish();
                break;
            case R.id.player_title_share:
                if (isLogin()) {
                    if (MusicManager.get().getCurrPlayingMusic() != null) {
                        shareInfo(userId(), MusicManager.get().getCurrPlayingMusic().getSongId());
                    } else {
                        toast("嘤嘤嘤~数据不见了");
                    }
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }
                break;
            case R.id.btn_zan:
                if (isLogin()) {
                    dotpraise(mSoninfo.getTempInfo().getTemp_3(), 0, mSoninfo.getSongId(), mSoninfo.getSongName(), mSoninfo.getSongCover(), "", userId());
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }
                break;
        }
    }

    private void playerType(int type) {
        LogUtil.e("type2", SPUtils.getInstance().getInt(SPconst.PLAYER_TYPE) + "");
        switch (type) {
            case 1:
                //单曲
                btnMusicType.setImageResource(R.mipmap.ic_play_gourp);
                MusicManager.get().setPlayMode(PlayMode.PLAY_IN_SINGLE_LOOP);
                LogUtil.e("当前播放模式1", MusicManager.get().getPlayMode() + "");
                break;
            case 2:
                //随机
                btnMusicType.setImageResource(R.mipmap.ic_suiji);
                MusicManager.get().setPlayMode(PlayMode.PLAY_IN_ORDER);
                LogUtil.e("当前播放模式2", MusicManager.get().getPlayMode() + "");
                break;
            case 3:
                //顺序
                btnMusicType.setImageResource(R.mipmap.ic_type_suiji);
                MusicManager.get().setPlayMode(PlayMode.PLAY_IN_ORDER);
                LogUtil.e("当前播放模式3", MusicManager.get().getPlayMode() + "");
                break;

        }
    }

    @Override
    public void onMusicSwitch(SongInfo songInfo) {
        mSoninfo = songInfo;
        btnPlayPause.setImageResource(R.mipmap.icon_player_stop);
        updateUI(mSoninfo);
        LogUtil.e("播放器", "当前时长:" + (int) MusicManager.get().getProgress() + "");
    }

    @Override
    public void onPlayerStart() {
        btnPlayPause.setImageResource(R.mipmap.ic_pause);
        LogUtil.e("onPlayerStart", "");
        if (MusicManager.isPlaying()) {
            mTimerTaskManager.scheduleSeekBarUpdate();
        }
    }

    @Override
    public void onPlayerPause() {
        btnPlayPause.setImageResource(R.mipmap.icon_player_stop);
        mTimerTaskManager.stopSeekBarUpdate();

    }

    @Override
    public void onPlayCompletion() {
        btnPlayPause.setImageResource(R.mipmap.icon_player_stop);
        seekBar.setProgress(0);
        startTime.setText("00:00");
        lrcView.loadLrc("");
    }

    @Override
    public void onPlayerStop() {
    }

    @Override
    public void onError(String s) {

    }

    @Override
    public void onAsyncLoading(boolean b) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("onDestroy", "onDestroy");
        mTimerTaskManager.onRemoveUpdateProgressTask();
        MusicManager.get().removePlayerEventListener(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onPlayClick(long time) {
        if (MusicManager.get().isPlaying() || MusicManager.get().isPaused()) {
            MusicManager.get().seekTo((int) time);
            if (MusicManager.get().isPaused()) {
                MusicManager.get().isPaused();
            }
            return true;
        }
        return false;
    }

    /**
     * 分享
     */
    private void share(String title, String description, String url, String img, String musicUrl) {
        UMusic music = new UMusic(musicUrl);//音乐的播放链接
        UMImage thumb = new UMImage(this, img);
        music.setTitle(title);//音乐的标题
        music.setThumb(thumb);//音乐的缩略图
        music.setDescription(description);//音乐的描述
        music.setmTargetUrl(url);//音乐的跳转链接
        new ShareAction(this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA)
                .withMedia(music)
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

    //不要忘记重写分享成功后的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void shareInfo(String userId, String musicId) {
        addSubscribe(apis.shareMusic(new ShareMusicRequest(musicId, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<ShareMusicRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ShareMusicRespone>handleResult())
                .subscribeWith(new HttpSubscriber<ShareMusicRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(ShareMusicRespone shareMusicRespone) {
                        share(shareMusicRespone.musicName, shareMusicRespone.singerName, shareMusicRespone.h5Url, shareMusicRespone.musicPicture, shareMusicRespone.musicPath);
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    //点赞
    private void dotpraise(final String beingUserId, int flag, String musicId, String musicName, String musicPicture, String specialId, String userId) {
        showLoadingDialog();
        addSubscribe(apis.dotpraise(new DotPraiseRequest(beingUserId, flag, musicId, musicName, musicPicture, specialId, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<DotPraiseResponse>>rxSchedulerHelper())
                .compose(RxHttpUtil.<DotPraiseResponse>handleResult())
                .subscribeWith(new HttpSubscriber<DotPraiseResponse>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        dismissLoadingDialog();
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(DotPraiseResponse DotPraiseResponse) {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isLike == 0) {
                                    btnZan.setImageResource(R.mipmap.icon_star_music_zan);
                                    isLike = 1;
                                } else {
                                    btnZan.setImageResource(R.mipmap.icon_star_music_unplayer);
                                    isLike = 0;
                                }
                            }
                        });
                    }
                }));
    }

    //是否收藏
    private void isLike(String musicId) {
        addSubscribe(apis.islikeMusic(new PlayerIsLikeRequest(userId(), musicId))
                .compose(RxHttpUtil.<FutureHttpResponse<PlayerIsLikeRepone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<PlayerIsLikeRepone>handleResult())
                .subscribeWith(new HttpSubscriber<PlayerIsLikeRepone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(PlayerIsLikeRepone isLikeRepone) {
                        LogUtil.e("PlayerIsLikeRepone", isLikeRepone.isLike + "");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isLikeRepone.isLike == 0) {
                                    isLike = 0;
                                    btnZan.setImageResource(R.mipmap.icon_star_music_unplayer);
                                } else {
                                    isLike = 1;
                                    btnZan.setImageResource(R.mipmap.icon_star_music_zan);
                                }
                            }
                        });

                    }

                    @Override
                    public void onComplete() {
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
