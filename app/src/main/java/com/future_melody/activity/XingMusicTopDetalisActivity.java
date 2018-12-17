package com.future_melody.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.future_melody.R;
import com.future_melody.adapter.XingMusicDetailsAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.SPconst;
import com.future_melody.mode.XingTopModel;
import com.future_melody.music.PlayerNewActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.ListerMusicRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.ListerMusicRespone;
import com.future_melody.receiver.ListenXingMusicEventBus;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.SPUtils;
import com.future_melody.widget.IsWifiDialog;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.aidl.model.TempInfo;
import com.lzx.musiclibrary.manager.MusicManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Author WZL
 * Date：2018/8/20 02
 * Notes:
 */
public class XingMusicTopDetalisActivity extends BaseActivity implements View.OnClickListener {
    private ImageView top_img;
    private ImageView img_all_player;
    private TextView top_music_name;
    private TextView top_music_user;
    private TextView img_all_text_player;
    private FrameLayout player_all;
    private RecyclerView details_recyclerview;
    private ArrayList<SongInfo> songInfos = new ArrayList<>();
    private XingMusicDetailsAdapter adapter;
    private ImageView ph_title_right_img;
    private List<XingTopModel> xingTopModels;
    private Animation animation;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_xing_music_top_details;
    }

    @Override
    protected void initView() {
        top_img = findViewById(R.id.top_img);
        top_music_name = findViewById(R.id.top_music_name);
        top_music_user = findViewById(R.id.top_music_user);
        player_all = findViewById(R.id.player_all);
        details_recyclerview = findViewById(R.id.details_recyclerview);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        img_all_player = findViewById(R.id.img_all_player);
        img_all_text_player = findViewById(R.id.img_all_text_player);
        details_recyclerview.setOnClickListener(this);
        player_all.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
        img_all_player.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        xingTopModels = new LinkedList<>();
        xingTopModels = intent.getParcelableArrayListExtra("xing");
        top_img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mActivity).load(xingTopModels.get(0).coverUrl).into(top_img);
        top_music_name.setText(xingTopModels.get(0).musicName);
        top_music_user.setText(xingTopModels.get(0).singerName);
        LogUtil.e("传过来了吗", xingTopModels.size() + "h");
        songInfos.clear();
        for (XingTopModel respone : xingTopModels) {
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

        adapter = new XingMusicDetailsAdapter(mActivity, xingTopModels, songInfos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        details_recyclerview.setLayoutManager(linearLayoutManager);
        details_recyclerview.setAdapter(adapter);
        adapter.setItemClickListener(new XingMusicDetailsAdapter.ZanClickListener() {
            @Override
            public void set(int position) {
                SongInfo songInfo = songInfos.get(position);
                if (MusicManager.isCurrMusicIsPlayingMusic(songInfo)) {
                    PlayerUitlis.player(mActivity);
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
                    if (CommonUtils.isWifi(mActivity)) {
                        MusicManager.get().playMusic(songInfos, position);
                        PlayerNewActivity.launch(mActivity, songInfos, position);
                    } else {
                        if (SPUtils.getInstance().getBoolean(SPconst.ISWIFIPLAYER, false)) {
                            IsWifiDialog dialog = new IsWifiDialog(mActivity);
                            dialog.setCancelButton(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SPUtils.getInstance().put(SPconst.ISWIFIPLAYER, false);
                                    MusicManager.get().playMusic(songInfos, position);
                                    PlayerNewActivity.launch(mActivity, songInfos, position);
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            MusicManager.get().playMusic(songInfos, position);
                            PlayerNewActivity.launch(mActivity, songInfos, position);
                        }
                    }
                }
            }
        });
        details_recyclerview.setHasFixedSize(true);
        details_recyclerview.setNestedScrollingEnabled(false);
        setBarColor(R.color.white, true);
        setTitleLayoutColor(mActivity, R.color.white);
        ph_title_right_img.setImageResource(R.mipmap.back_music);
        setBlackBackble();
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
    protected void onResume() {
        super.onResume();
        initAnim();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (MusicManager.isPlaying()) {
            startAnmi();
        } else {
            stoptAnmi();
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
    protected void onDestroy() {
        super.onDestroy();
        destroyAnmi();
        EventBus.getDefault().unregister(this);
    }

    private void destroyAnmi() {
        animation.cancel();
        animation = null;
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.details_recyclerview:

                break;
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
            case R.id.player_all:
                if (songInfos.size() > 0) {
//                    MusicManager.get().playMusic(songInfos, 0);
//                    if (MusicManager.isPlaying()) {
//                        img_all_player.setImageResource(R.mipmap.icon_top_details_all_player_stop);
//                        img_all_text_player.setText("点击暂停");
//                    } else {
//                        img_all_player.setImageResource(R.mipmap.icon_top_details_all_player);
//                        img_all_text_player.setText("全部播放");
//                    }
                    MusicManager.get().playMusic(songInfos, 0);
                    PlayerNewActivity.launch(mActivity, songInfos, 0);
                }
                break;
        }
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
