package com.future_melody.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.future_melody.R;
import com.future_melody.adapter.StarDetailsMusicAdapter;
import com.future_melody.adapter.TodaySuperuAdapter;
import com.future_melody.base.BaseFragment;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.DotPraiseRequest;
import com.future_melody.net.request.PlanetMusic;
import com.future_melody.net.respone.DotPraiseResponse;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.PlanetMusicResone;
import com.future_melody.receiver.ListenXingMusicEventBus;
import com.future_melody.utils.TipLinearUtil;
import com.future_melody.utils.UmengUtils;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.aidl.model.TempInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

/**
 * Author WZL
 * Date：2018/5/16 24
 * Notes: 星球详情：推荐音乐
 */
public class StarInfoMusicFragment extends BaseFragment {

    @BindView(R.id.star_details_music_list)
    RecyclerView starDetailsMusicList;
    private StarDetailsMusicAdapter adapter;
    private String planetId;
    private List<PlanetMusicResone> list;
    private ArrayList<SongInfo> songInfos = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_star_details_music;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
        starDetailsMusicList.setLayoutManager(new LinearLayoutManager(mActivity));
        Bundle bundle = getArguments();
        planetId = bundle.getString("planetId");
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
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        list = new LinkedList<>();
        getList(userId(), planetId);
        // starDetailsMusicList.setNestedScrollingEnabled(false);  RecyclerView不能滑动
        UmengUtils.onResumeToFragment("StarInfoMusicFragment");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    private void getList(String userId, String planetId) {
        addSubscribe(apis.planeMusic(new PlanetMusic(userId, planetId))
                .compose(RxHttpUtil.<FutureHttpResponse<List<PlanetMusicResone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<PlanetMusicResone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<PlanetMusicResone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(List<PlanetMusicResone> planetMusicResones) {
                        if (planetMusicResones != null) {
                            list.clear();
                            list.addAll(planetMusicResones);
                            for (PlanetMusicResone respone : planetMusicResones) {
                                SongInfo model = new SongInfo();
                                TempInfo tempInfo = new TempInfo();
                                if (respone.lyrics != null && !TextUtils.isEmpty(respone.lyrics)) {
                                    tempInfo.setTemp_1(respone.lyrics);
                                }
                                tempInfo.setTemp_2(respone.isLike + "");
                                tempInfo.setTemp_3(respone.userId);
                                model.setTempInfo(tempInfo);
                                model.setSongUrl(respone.musicPath);
                                model.setSongCover(respone.coverUrl);
                                model.setSongId(respone.musicId);
                                model.setArtist(respone.asteroidName);
                                if (TextUtils.isEmpty(respone.musicName)) {
                                    model.setSongName("<未知>");
                                } else {
                                    model.setSongName(respone.musicName);
                                }
                                songInfos.add(model);
                            }
                            adapter = new StarDetailsMusicAdapter(mActivity, planetMusicResones, songInfos);
                            adapter.setAttention(new TodaySuperuAdapter.attention() {

                                private String userI;
                                private String musicId;
                                private String musicName;
                                private String musicPath;

                                @Override
                                public void attention(boolean b, int i) {
                                    userI = planetMusicResones.get(i).userId;
                                    musicId = planetMusicResones.get(i).musicId;
                                    musicName = planetMusicResones.get(i).musicName;
                                    musicPath = planetMusicResones.get(i).musicPath;
                                    if (b == true) {
                                        //点赞
                                        dotpraise(userI, 0, musicId, musicName, musicPath, planetId, userId);
                                    } else {
                                        //取消点赞
                                        dotpraise(userI, 0, musicId, musicName, musicPath, planetId, userId);
                                    }
                                }
                            });
                            starDetailsMusicList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
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
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("StarInfoMusicFragment");
    }

}
