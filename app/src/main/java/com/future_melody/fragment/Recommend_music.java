package com.future_melody.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.future_melody.R;
import com.future_melody.activity.LoginActivity;
import com.future_melody.adapter.Recommend_musicAdapter;
import com.future_melody.base.BaseFragment;
import com.future_melody.common.CommonConst;
import com.future_melody.common.SPconst;
import com.future_melody.mode.Recommend_Music_Bean;
import com.future_melody.music.PlayerNewActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.DotPraiseRequest;
import com.future_melody.net.request.ListerMusicRequest;
import com.future_melody.net.request.Recommend_music_Request;
import com.future_melody.net.respone.DotPraiseResponse;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.ListerMusicRespone;
import com.future_melody.net.respone.Recommend_music_Respne;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.TipLinearUtil;
import com.future_melody.utils.UmengUtils;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.aidl.model.TempInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * Created by Y on 2018/5/18.
 * 推荐音乐
 */

public class Recommend_music extends BaseFragment implements OnLoadMoreListener {
    @BindView(R.id.recycle)
    RecyclerView recycle;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder1;
    private Recommend_musicAdapter recommend_musicAdapter;
    private String beUserId;
    private int pageNum = 1;
    private int pageSize = 20;
    private ArrayList<SongInfo> songInfos = new ArrayList<>();
    private List<Recommend_Music_Bean> list;
    private NestedScrollView scrollView;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend_music;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        scrollView = view.findViewById(R.id.scrollView);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        beUserId = bundle.getString("BeuserId");
        recommend_music(pageNum, pageSize, userId(), beUserId);
        recycle.setLayoutManager(new LinearLayoutManager(mActivity));
        list = new LinkedList<>();
        list.clear();
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(this);
        //setAdapter  setListener
        recommend_musicAdapter = new Recommend_musicAdapter(mActivity, list, songInfos);
        recommend_musicAdapter.setThemeClickListener(i -> {
            if (isLogin()) {
                Recommend_Music_Bean bean = list.get(i);
                if (bean.isLike == 0) {
                    bean.isLike = 1;
                } else {
                    bean.isLike = 0;
                }
                recommend_musicAdapter.notifyDataSetChanged();
                dotpraise(beUserId, 0, list.get(i).musicId, list.get(i).musicName, list.get(i).coverUrl, "", userId());
            } else {
                Intent intent = new Intent(mActivity, LoginActivity.class);
                intent.putExtra(CommonConst.ISFINISH, 1);
                startActivity(intent);
            }
        });
        recycle.setAdapter(recommend_musicAdapter);
        recommend_musicAdapter.setItemClickListener(new Recommend_musicAdapter.ThemeClickListener() {
            @Override
            public void GetInfo(int position) {
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
                    MusicManager.get().playMusic(songInfos, position);
                    PlayerNewActivity.launch(mActivity, songInfos, position);
                }
            }
        });
    }

    private void recommend_music(final int pageNum, int pageSize, String userId, String beUserId) {
        addSubscribe(apis.recommendMusic(new Recommend_music_Request(pageNum, pageSize, userId, beUserId))
                .compose(RxHttpUtil.<FutureHttpResponse<Recommend_music_Respne>>rxSchedulerHelper())
                .compose(RxHttpUtil.<Recommend_music_Respne>handleResult())
                .subscribeWith(new HttpSubscriber<Recommend_music_Respne>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {


                    @Override
                    public void onNext(Recommend_music_Respne recommend_music_Respne) {
                        if (pageNum == 1 && recommend_music_Respne.musicVoList.size() <= 0) {
                            scrollView.setVisibility(View.VISIBLE);
                            recycle.setVisibility(View.GONE);
                        } else {
                            scrollView.setVisibility(View.GONE);
                            recycle.setVisibility(View.VISIBLE);
                            if (recommend_music_Respne.musicVoList != null && recommend_music_Respne.musicVoList.size() > 0) {
                                refreshLayout.finishLoadMore();
                                for (Recommend_Music_Bean respone : recommend_music_Respne.musicVoList) {
                                    SongInfo model = new SongInfo();
                                    TempInfo tempInfo = new TempInfo();
                                    LogUtil.e("lyrics", respone.lyrics);
                                    if (respone.lyrics != null && !TextUtils.isEmpty(respone.lyrics)) {
                                        tempInfo.setTemp_1(respone.lyrics);
                                    }
                                    tempInfo.setTemp_2(respone.isLike + "");
                                    tempInfo.setTemp_3(beUserId);
                                    model.setTempInfo(tempInfo);
                                    model.setSongUrl(respone.musicPath);
                                    model.setSongCover(respone.coverUrl);
                                    model.setSongId(respone.musicId);
                                    model.setArtist(respone.musicName);
                                    if (TextUtils.isEmpty(respone.musicName)) {
                                        model.setSongName("<未知>");
                                    } else {
                                        model.setSongName(respone.musicName);
                                    }
                                    songInfos.add(model);
                                }
                                LogUtil.e("songInfos", songInfos.size() + "");
                                list.addAll(recommend_music_Respne.musicVoList);
                                recommend_musicAdapter.notifyDataSetChanged();
                            } else {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            }
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
        if (recommend_musicAdapter != null) {
            recommend_musicAdapter.notifyDataSetChanged();
        }
        UmengUtils.onResumeToFragment("Recommend_music");
    }

    //点赞
    private void dotpraise(final String beingUserId, int flag, String musicId, String musicName, String musicPicture, String specialId, String userId) {
        addSubscribe(apis.dotpraise(new DotPraiseRequest(beingUserId, flag, musicId, musicName, musicPicture, specialId, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<DotPraiseResponse>>rxSchedulerHelper())
                .compose(RxHttpUtil.<DotPraiseResponse>handleResult())
                .subscribeWith(new HttpSubscriber<DotPraiseResponse>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(DotPraiseResponse DotPraiseResponse) {

                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        recommend_music(pageNum, pageSize, userId(), beUserId);
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("Recommend_music");
    }

    private void ListerMusic(String endTime, int isComplete, String musicId, int playTime, String startTime, String userId, int type) {
        addSubscribe(apis.listermusic(new ListerMusicRequest(endTime, isComplete, musicId, playTime, startTime, userId, type))
                .compose(RxHttpUtil.<FutureHttpResponse<ListerMusicRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ListerMusicRespone>handleResult())
                .subscribeWith(new HttpSubscriber<ListerMusicRespone>(exception -> {
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
