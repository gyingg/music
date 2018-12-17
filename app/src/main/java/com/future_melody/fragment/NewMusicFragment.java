package com.future_melody.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.activity.X5WebviewNoTitleActivity;
import com.future_melody.activity.XingMusicTopActivity;
import com.future_melody.adapter.NewMusicAdapter;
import com.future_melody.adapter.ThemeNewDetailsMusicAdapter;
import com.future_melody.base.BaseFragment;
import com.future_melody.common.CommonConst;
import com.future_melody.common.SPconst;
import com.future_melody.mode.BannerModel;
import com.future_melody.mode.XingMusicModel;
import com.future_melody.music.PlayerNewActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.ListerMusicRequest;
import com.future_melody.net.request.MusicZanRequest;
import com.future_melody.net.request.XingMusicRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.ListerMusicRespone;
import com.future_melody.net.respone.MusicZanRespone;
import com.future_melody.net.respone.XingMusicRespone;
import com.future_melody.receiver.ListenXingEventBus;
import com.future_melody.receiver.ListenXingMusicEventBus;
import com.future_melody.receiver.ToNewFragmentEventBus;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.TipLinearUtil;
import com.future_melody.utils.UmengUtils;
import com.future_melody.widget.IsWifiDialog;
import com.future_melody.widget.XingMusicUNZanDialog;
import com.future_melody.widget.XingMusicZanDialog;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.aidl.model.TempInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.stx.xhb.xbanner.XBanner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * Created by Y on 2018/5/11.
 * 星歌Fragement
 */

public class NewMusicFragment extends BaseFragment implements View.OnClickListener {
    private final String TAG = "NewMusicFragment";
    @BindView(R.id.new_rv_music)
    ListView recyclerView;
    Unbinder unbinder;
    private List<BannerModel> bannerModelList;
    private List<XingMusicModel> musicModelList;
    private NewMusicAdapter newMusicAdapter;
    private View headView;
    private XBanner xbanner;
    private TextView new_music_size;
    private RelativeLayout btn_all_player;
    private ArrayList<SongInfo> songInfos = new ArrayList<>();
    private boolean isZan = false;
    private XingMusicUNZanDialog dialog;
    private XingMusicZanDialog zanDialog;
    private TextView new_music_btn_text;
    private boolean isCheck = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_music;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        headView = View.inflate(mActivity, R.layout.headview_xingmusic, null);
        xbanner = headView.findViewById(R.id.xbanner);
        new_music_size = headView.findViewById(R.id.new_music_size);
        btn_all_player = headView.findViewById(R.id.btn_all_player);
        new_music_btn_text = headView.findViewById(R.id.new_music_btn_text);
    }

    @Override
    protected void initData() {
        bannerModelList = new LinkedList<>();
        musicModelList = new LinkedList<>();
        getInfo();
        btn_all_player.setOnClickListener(this);
        new_music_btn_text.setOnClickListener(this);
        LogUtil.e(TAG, "initData");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.e(TAG, "onHiddenChanged");
        getInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ListenXingMusicEventBus even) {
        if (newMusicAdapter != null) {
            LogUtil.e(TAG, "onEvent");
            LogUtil.e(TAG, MusicManager.isPlaying() + "");
            newMusicAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXingEvent(ListenXingEventBus even) {
        int position = even.getPosition();
        LogUtil.e(TAG, position + "");
        XingMusicModel musicModel = musicModelList.get(position);
        musicModel.isListen = 1;
        if (newMusicAdapter != null) {
            newMusicAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTo(ToNewFragmentEventBus even) {
        if (even.getIsShow() == 1) {
            LogUtil.e(TAG, "点击了");
            getInfo();
            newMusicAdapter.notifyDataSetChanged();
        }
    }

    private void getInfo() {
        showLoadingDialog();
        addSubscribe(apis.xingMusic(new XingMusicRequest(userId()))
                .compose(RxHttpUtil.<FutureHttpResponse<XingMusicRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<XingMusicRespone>handleResult())
                .subscribeWith(new HttpSubscriber<XingMusicRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        dismissLoadingDialog();
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(XingMusicRespone xingMusicRespone) {
                        dismissLoadingDialog();
                        for (XingMusicModel respone : xingMusicRespone.starMusicVoList) {
                            SongInfo model = new SongInfo();
                            TempInfo tempInfo = new TempInfo();
                            LogUtil.e("lyrics", respone.lyrics);
                            if (respone.lyrics != null && !TextUtils.isEmpty(respone.lyrics)) {
                                tempInfo.setTemp_1(respone.lyrics);
                            }
                            LogUtil.e("url", respone.musicName);
                            tempInfo.setTemp_2(respone.isLike + "");
                            tempInfo.setTemp_3(respone.userId);
                            tempInfo.setTemp_4(CommonConst.ISXING_MUSIC);
                            model.setTempInfo(tempInfo);
                            model.setSongUrl(respone.musicPath);
                            model.setSongCover(respone.musicCoverUrl);
                            model.setSongId(respone.musicId);
                            model.setArtist(respone.singerName);
                            if (TextUtils.isEmpty(respone.musicName)) {
                                model.setSongName("<未知>");
                            } else {
                                model.setSongName(respone.musicName);
                            }
                            songInfos.add(model);
                            if (respone.isLike > 0) {
                                isZan = true;
                                //SPUtils.getInstance().put(SPconst.isZan, true);
                            }
                            /*else {
                                isZan = false;
                               SPUtils.getInstance().put(SPconst.isZan, false);
                            }*/
                        }
                        bannerModelList.addAll(xingMusicRespone.activeList);
                        xban(xingMusicRespone.activeList);
                        musicModelList.clear();
                        musicModelList.addAll(xingMusicRespone.starMusicVoList);
                        newMusicAdapter = new NewMusicAdapter(mActivity, musicModelList, songInfos);
                        recyclerView.setAdapter(newMusicAdapter);
                        newMusicAdapter.itemClickListener(new NewMusicAdapter.ZanClickListener() {
                            @Override
                            public void set(int position) {
                                SongInfo songInfo = songInfos.get(position);
                                if (MusicManager.isCurrMusicIsPlayingMusic(songInfo)) {
                                    PlayerUitlis.player(mActivity);
                                } else {
                                    LogUtil.e("星歌时长：", MusicManager.get().getProgress() / 1000 + "");
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
                        if (recyclerView.getHeaderViewsCount() > 0) {
                            recyclerView.removeHeaderView(headView);
                        }
                        recyclerView.addHeaderView(headView);
                        new_music_size.setText("(共" + xingMusicRespone.starMusicCount + "首)");
                        newMusicAdapter.setZanClickListener(new NewMusicAdapter.ZanClickListener() {
                            @Override
                            public void set(int i) {
                                // b = SPUtils.getInstance().getBoolean(SPconst.isZan);
                                if (isZan) {
                                    zanDialog = new XingMusicZanDialog(mActivity);
                                    zanDialog.setCancelButton(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            zanDialog.dismiss();
                                        }
                                    });
                                } else {
                                    if (!SPUtils.getInstance().getBoolean(SPconst.isXingZanShow)) {
                                        dialog = new XingMusicUNZanDialog(mActivity);
                                        dialog.setMsg("您每天只能给一首星歌点赞，点赞后将不可取消");
                                        dialog.setBtn_CheckBox(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                LogUtil.e("点赞是否选择", b + "");
                                                isCheck = b;
                                            }
                                        });
                                        dialog.setCancelButton(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.setDetermineButton(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                setZan(userId(), musicModelList.get(i).musicId, musicModelList.get(i).musicName, i);
                                            }
                                        });
                                    } else {
                                        setZan(userId(), musicModelList.get(i).musicId, musicModelList.get(i).musicName, i);
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                    }
                }));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
        if (zanDialog != null) {
            zanDialog.dismiss();
        }
        EventBus.getDefault().unregister(this);
    }

    /**
     * 设置轮播图
     */
    private void xban(List<BannerModel> banners) {
        //xbanner.setData(R.layout.fragment_xbanner_yuan, banners, null);  圆弧的xbanner
        xbanner.setData(banners, null);

        //监听广告 item 的单击事件
        xbanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, int position) {
                X5WebviewNoTitleActivity.startPHLoanWebActivity(mActivity, banners.get(position).activeTransferUrl);
            }
        });

        //加载广告图片
        xbanner.setmAdapter(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                //Glide占位图
                RequestOptions RequestOptions = new RequestOptions();
                RequestOptions.placeholder(R.mipmap.moren);
                Glide.with(getActivity()).load(banners.get(position).activeCoverUrl).apply(RequestOptions).into((ImageView) view);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (newMusicAdapter != null) {
            newMusicAdapter.notifyDataSetChanged();
        }
        xbanner.startAutoPlay();
        UmengUtils.onResumeToFragment("NewMusicFragment");
        LogUtil.e(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("NewMusicFragment");
        LogUtil.e(TAG, "onPause");
    }


    //xbanne stop
    @Override
    public void onStop() {
        super.onStop();
        xbanner.stopAutoPlay();
    }

    private void setZan(String userId, String musicId, String musicName, int i) {
        showLoadingDialog();
        addSubscribe(apis.musicZan(new MusicZanRequest(musicId, musicName, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<MusicZanRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<MusicZanRespone>handleResult())
                .subscribeWith(new HttpSubscriber<MusicZanRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                        dismissLoadingDialog();
                    }
                }) {

                    @Override
                    public void onNext(MusicZanRespone musicZanRespone) {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        XingMusicModel bean = musicModelList.get(i);
                        bean.isLike = bean.isLike + 1;
                        newMusicAdapter.notifyDataSetChanged();
                        isZan = true;
                        if (isCheck) {
                            SPUtils.getInstance().put(SPconst.isXingZanShow, true);
                        } else {
                            SPUtils.getInstance().put(SPconst.isXingZanShow, false);
                        }
                    }
                }));
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_all_player:
               /* if (songInfos.size() > 0) {
                    MusicManager.get().playMusic(songInfos, 0);
                    PlayerNewActivity.launch(mActivity, songInfos, 0);
                } else {
                    toast("暂无歌曲");
                }*/
                intent = new Intent(mActivity, ThemeNewDetailsMusicAdapter.class);
                startActivity(intent);
                break;
            case R.id.new_music_btn_text:
                intent = new Intent(mActivity, XingMusicTopActivity.class);
                startActivity(intent);
//                intent = new Intent(mActivity, ThemeDetilsNewActivity.class);
//                startActivity(intent);
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
