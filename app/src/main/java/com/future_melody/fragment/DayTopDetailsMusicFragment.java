package com.future_melody.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.future_melody.R;
import com.future_melody.adapter.TopDetailsMusicAdapter1;
import com.future_melody.base.BaseFragment;
import com.future_melody.music.PlayerActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.XingTopMusicRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.GetMusicLeaderRespone;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.UmengUtils;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.aidl.model.TempInfo;
import com.lzx.musiclibrary.manager.MusicManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Author WZL
 * Date：2018/5/24 45
 * Notes:日榜 周榜 月榜
 */
public class DayTopDetailsMusicFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private ListView top_music_listView;
    private TopDetailsMusicAdapter1 adapter;
    private View headView;
    private List<GetMusicLeaderRespone> list;
    private ArrayList<SongInfo> songInfos =new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_top_music_details;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        top_music_listView = view.findViewById(R.id.top_music_listView);
        headView = View.inflate(mActivity, R.layout.headview_music_bg, null);
        top_music_listView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        list = new LinkedList<>();
        getList(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetInvalidated();
        }
        UmengUtils.onResumeToFragment("DayTopDetailsMusicFragment");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    private void getList(int category) {
        addSubscribe(apis.xingTop(new XingTopMusicRequest(category, 1, 100, userId()))
                .compose(RxHttpUtil.<FutureHttpResponse<List<GetMusicLeaderRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<GetMusicLeaderRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<GetMusicLeaderRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(List<GetMusicLeaderRespone> getMusicLeaderRespones) {
                        songInfos.clear();
                        for (GetMusicLeaderRespone respone : getMusicLeaderRespones) {
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
                        list.clear();
                        list.addAll(getMusicLeaderRespones);
                        adapter = new TopDetailsMusicAdapter1(mActivity, list, songInfos);
                        top_music_listView.setAdapter(adapter);
                        if (list.size() > 0) {
                            top_music_listView.addHeaderView(headView, null, false);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (MusicManager.isCurrMusicIsPlayingMusic(songInfos.get(position - 1))) {
            PlayerUitlis.player(mActivity);
        } else {
            MusicManager.get().playMusic(songInfos, position - 1);
            PlayerActivity.launch(mActivity, songInfos, position - 1);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onResumeToFragment("DayTopDetailsMusicFragment");
    }
}
