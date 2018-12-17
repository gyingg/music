package com.future_melody.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.future_melody.common.SPconst;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.HttpUtil;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.api.FutrueApis;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.ListenMusic;
import com.future_melody.net.request.ListerMusicRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.ListenMusicRespone;
import com.future_melody.net.respone.ListerMusicRespone;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.FormatUtil;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.SPUtils;
import com.lzx.musiclibrary.aidl.listener.OnPlayerEventListener;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.lzx.musiclibrary.manager.TimerTaskManager;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Author WZL
 * Date：2018/7/5 22
 * Notes:
 */
public class ListenrXingMusicReceiver extends BroadcastReceiver {
    private String TAG = "ListenrXingMusicReceiver";
    protected CompositeDisposable mCompositeDisposable;
    public FutrueApis apis = HttpUtil.getPHApis();
    private String musicId = null;
    private int position;
    private String endTime;
    private int isXingMusic;
    private int Duration;
    private SongInfo mSongInfo;
    TimerTaskManager mTimerTaskManager = new TimerTaskManager();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (!TextUtils.isEmpty(MusicManager.get().getCurrPlayingMusic().getTempInfo().getTemp_4())) {
                    isXingMusic = 1;
                } else {
                    isXingMusic = 0;
                }
                LogUtil.e(TAG, "当前时长3:" + msg.arg1);
                if (msg.arg1 > 4) {
                    try {
                        Thread.currentThread().sleep(100);
                        ListerMusic(CommonUtils.data(), 0, musicId, msg.arg1, SPUtils.getInstance().getString(SPconst.starTime), SPUtils.getInstance().getString(SPconst.USER_ID), isXingMusic);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        MusicManager.get().clearPlayerEventListener();
        LogUtil.e(TAG, "广播启动");
        MusicManager.get().addPlayerEventListener(new OnPlayerEventListener() {
            @Override
            public void onMusicSwitch(SongInfo music) {
                musicId = music.getSongId();
                mSongInfo = music;
                SPUtils.getInstance().put(SPconst.starTime, CommonUtils.data());
                LogUtil.e(TAG, "开始时间" + SPUtils.getInstance().getString(SPconst.starTime));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = 1;
                        msg.arg1 = (int) MusicManager.get().getProgress() / 1000;
                        handler.sendMessage(msg);
                    }
                }).start();

            }

            @Override
            public void onPlayerStart() {
                Duration = MusicManager.get().getDuration();
                if (MusicManager.isPlaying()) {
                    mTimerTaskManager.scheduleSeekBarUpdate();
                }
                LogUtil.e(TAG, "onPlayerStart:");
                sendListent(-1);
            }

            @Override
            public void onPlayerPause() {
                LogUtil.e(TAG, "onPlayerPause:");
                sendListent(-2);
                mTimerTaskManager.stopSeekBarUpdate();
            }

            @Override
            public void onPlayCompletion() {
                try {
                    Thread.currentThread().sleep(300);
                    LogUtil.e(TAG, "播放完成");
                    LogUtil.e(TAG, "getTemp_4:" + MusicManager.get().getCurrPlayingMusic().getTempInfo().getTemp_4());
                    LogUtil.e(TAG, "：时长" + FormatUtil.formatMusicTime(MusicManager.get().getDuration()));
                    if (!TextUtils.isEmpty(MusicManager.get().getCurrPlayingMusic().getTempInfo().getTemp_4())) {
                        LogUtil.e(TAG, "星歌");
                        LogUtil.e(TAG, SPUtils.getInstance().getBoolean(SPconst.ISlogin) + "");
                        LogUtil.e(TAG, "当前播放ID：" + musicId);
                        if (musicId != null) {
//                                ListenOver(musicId, SPUtils.getInstance().getString(SPconst.USER_ID));
                            if (!TextUtils.isEmpty(SPUtils.getInstance().getString(SPconst.starTime))){
                                ListerMusic(CommonUtils.data(), 1, musicId, Duration / 1000, SPUtils.getInstance().getString(SPconst.starTime), SPUtils.getInstance().getString(SPconst.USER_ID), 1);
                            }else {
                                ListerMusic(CommonUtils.data(), 1, musicId, Duration / 1000, CommonUtils.data(), SPUtils.getInstance().getString(SPconst.USER_ID), 1);
                            }

                        }
                    } else {
                        ListerMusic(CommonUtils.data(), 1, musicId, Duration / 1000, SPUtils.getInstance().getString(SPconst.starTime), SPUtils.getInstance().getString(SPconst.USER_ID), 0);
                    }
                    sendListent(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onPlayerStop() {
                LogUtil.e(TAG, "onPlayerStop:");
            }

            @Override
            public void onError(String errorMsg) {
                LogUtil.e(TAG, "onError:");
            }

            @Override
            public void onAsyncLoading(boolean isFinishLoading) {
                LogUtil.e(TAG, "onAsyncLoading:");
            }
        });
    }

    private void ListenOver(String musicId, String userId) {
        addSubscribe(apis.listen(new ListenMusic(musicId, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<ListenMusicRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ListenMusicRespone>handleResult())
                .subscribeWith(new HttpSubscriber<ListenMusicRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                    }
                }) {

                    @Override
                    public void onNext(ListenMusicRespone listenMusicRespone) {
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "拿到积分");
                        if (MusicManager.get().getCurrPlayingIndex() == 0) {
                            position = MusicManager.get().getPlayList().size() - 1;
                        } else {
                            position = MusicManager.get().getCurrPlayingIndex() - 1;
                        }
                        sendXListent(position);
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
                        if (type == 1) {
                            if (MusicManager.get().getCurrPlayingIndex() == 0) {
                                position = MusicManager.get().getPlayList().size() - 1;
                            } else {
                                position = MusicManager.get().getCurrPlayingIndex() - 1;
                            }
                            sendXListent(position);
                        }
                    }
                }));
    }

    public void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    private void sendListent(int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().postSticky(new ListenXingMusicEventBus(position));
            }
        }).start();
    }

    private void sendXListent(int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().postSticky(new ListenXingEventBus(position));
            }
        }).start();
    }
}
