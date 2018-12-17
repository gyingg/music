package com.future_melody.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.future_melody.R;
import com.future_melody.adapter.MineMusicAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.MineReconmendMusic;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MineReconmendMusicRespone;
import com.future_melody.receiver.ListenXingMusicEventBus;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.TipLinearUtil;
import com.future_melody.view.RecyclerViewLayoutManager;
import com.gyf.barlibrary.ImmersionBar;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.aidl.model.TempInfo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Author WZL
 * Date：2018/5/16 04
 * Notes: 我的页面：推荐音乐
 */
public class MineRecommendMusicActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    private MineMusicAdapter adapter;
    private RecyclerView mine_music_listview;
    private SmartRefreshLayout refreshLayout;
    private ArrayList<SongInfo> songInfos = new ArrayList<>();
    private int pageNum = 1;
    private int PageSize = 20;
    private LinearLayout no_data;
    private ImageView no_img;
    private List<MineReconmendMusicRespone> respones;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_mine_music;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        mine_music_listview = findViewById(R.id.mine_music_listview);
        refreshLayout = findViewById(R.id.refreshLayout);
        no_data = findViewById(R.id.no_data);
        setBlackBackble();
        setTitle("我的音乐");
        setBarColor(R.color.white, true);
        setTitleLayoutColor(mActivity, R.color.white);
        setTitleColor(R.color.color_333333);
        mine_music_listview.setLayoutManager(new RecyclerViewLayoutManager(mActivity));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
//        refreshLayout.setEnableRefresh(false);
    }

    @Override
    protected void initData() {
        respones = new LinkedList<>();
        getMusicList(pageNum, PageSize, userId());
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ListenXingMusicEventBus even) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getMusicList(int pageNum, int PageSize, String userId) {
        addSubscribe(apis.mineMusic(new MineReconmendMusic(userId, pageNum, PageSize))
                .compose(RxHttpUtil.<FutureHttpResponse<List<MineReconmendMusicRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<MineReconmendMusicRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<MineReconmendMusicRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        if (exception.getMessage() != null) {
                            new Runnable() {
                                @Override
                                public void run() {
                                    refreshLayout.finishRefresh(false);
                                    refreshLayout.finishLoadMore(false);
                                    TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                                }
                            };
                        }
                    }
                }) {
                    @Override
                    public void onNext(List<MineReconmendMusicRespone> mineReconmendMusicRespone) {
                        if (pageNum == 1 && mineReconmendMusicRespone.size() <= 0) {
                            no_data.setVisibility(View.VISIBLE);
                            mine_music_listview.setVisibility(View.GONE);
                        } else {
                            no_data.setVisibility(View.GONE);
                            mine_music_listview.setVisibility(View.VISIBLE);
                            if (mineReconmendMusicRespone != null && mineReconmendMusicRespone.size() > 0) {
                                for (MineReconmendMusicRespone respone : mineReconmendMusicRespone) {
                                    SongInfo model = new SongInfo();
                                    TempInfo tempInfo = new TempInfo();
                                    LogUtil.e("lyrics", respone.lyrics);
                                    if (respone.lyrics != null && !TextUtils.isEmpty(respone.lyrics)) {
                                        tempInfo.setTemp_1(respone.lyrics);
                                    }
                                    LogUtil.e("url", respone.music_name);
                                    LogUtil.e("lrc:", tempInfo.getTemp_1() + "");
                                    tempInfo.setTemp_2(respone.isLike + "");
                                    tempInfo.setTemp_3(respone.userid);
                                    model.setTempInfo(tempInfo);
                                    model.setSongUrl(respone.music_path);
                                    model.setSongCover(respone.music_picture);
                                    model.setSongId(respone.musicid);
                                    model.setArtist(respone.singer_name);
                                    if (TextUtils.isEmpty(respone.music_name)) {
                                        model.setSongName("<未知>");
                                    } else {
                                        model.setSongName(respone.music_name);
                                    }
                                    songInfos.add(model);
                                }
                                respones.addAll(mineReconmendMusicRespone);
                                adapter = new MineMusicAdapter(mActivity, respones, songInfos);
                                mine_music_listview.setAdapter(adapter);
                            } else {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            }
                            LogUtil.e("TAG", songInfos.size() + "");
                        }
//                        LogUtil.e("TAG", songInfos.get(0).getSongName() + "");
//                        LogUtil.e("TAG", songInfos.get(3).getSongName() + "");
//                        LogUtil.e("TAG", songInfos.get(0).getTempInfo().getTemp_1() + "");
                    }

                    @Override
                    public void onComplete() {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                }));
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNum = 1;
        songInfos.clear();
        respones.clear();
        refreshLayout.setNoMoreData(false);
        getMusicList(pageNum, PageSize, userId());

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        getMusicList(pageNum, PageSize, userId());
    }
}
