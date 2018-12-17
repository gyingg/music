package com.future_melody.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.adapter.LikeMusicAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.SPconst;
import com.future_melody.mode.LikeBean;
import com.future_melody.music.PlayerNewActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.DeletelikeRequest;
import com.future_melody.net.request.LikeRequest;
import com.future_melody.net.request.ListerMusicRequest;
import com.future_melody.net.respone.DeletelikeRespone;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.LikeRespone;
import com.future_melody.net.respone.ListerMusicRespone;
import com.future_melody.receiver.ListenXingMusicEventBus;
import com.future_melody.sideslip.SwipeMenuLayout;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.UmengUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.aidl.model.TempInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Notes: 我的页面：喜欢
 */
public class LikeActivity extends BaseActivity implements OnLoadMoreListener, View.OnClickListener {
    @BindView(R.id.like_top_image)
    ImageView likeTopImage;
    @BindView(R.id.like_name)
    TextView likeName;
    @BindView(R.id.like_music_size)
    TextView likeMusicSize;
    @BindView(R.id.like_music_player)
    ImageView likeMusicPlayer;
    @BindView(R.id.like_rv_music)
    RecyclerView likeRvMusic;
    @BindView(R.id.btn_player_all_music)
    LinearLayout btnPlayerAllMusic;
    private LikeMusicAdapter adapter;
    private SmartRefreshLayout refreshLayout;
    private ArrayList<SongInfo> songInfos = new ArrayList<>();
    private List<LikeBean> list;
    private int pageNum = 1;
    private int pageSize = 10;
    private Animation animation;
    private ImageView back;
    private ImageView ph_title_right_img;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_like;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        back = findViewById(R.id.back);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(this);
        back.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
        initAnim();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ListenXingMusicEventBus even) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (even.getPosition() == -1) {
            startAnmi();
        } else if (even.getPosition() == -2) {
            stoptAnmi();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MusicManager.isPlaying()) {
            startAnmi();
        } else {
            stoptAnmi();
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        pageNum = 1;
//        list.clear();
//        songInfos.clear();
        if (isLogin()) {
            like(userId(), pageNum, pageSize);
        }
        UmengUtils.onResumeToFragment("LikeFragment");
        LogUtil.e("这里", "45678");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("LikeFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyAnmi();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initData() {
        if (isLogin()) {
            pageNum = 1;
               /* if (list != null) {
                    list.clear();
                }
                songInfos.clear();*/
            like(userId(), pageNum, pageSize);
        }
        initAnim();
        LogUtil.e("这里", "1234");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        list = new LinkedList<>();
        pageNum = 1;
        //  list.clear();
        // songInfos.clear();
        likeRvMusic.setLayoutManager(new LinearLayoutManager(mActivity));
        likeRvMusic.setNestedScrollingEnabled(false);
        LogUtil.e("我的是否播放", MusicManager.isPlaying() + "");
        if (MusicManager.isPlaying()) {
            startAnmi();
        } else {
            stoptAnmi();
        }
    }

    private void initAnim() {
        animation = AnimationUtils.loadAnimation(mActivity, R.anim.player);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
    }

    private void startAnmi() {
        if (animation != null) {
            ph_title_right_img.startAnimation(animation);
        }
    }

    private void stoptAnmi() {
        ph_title_right_img.clearAnimation();
    }

    private void destroyAnmi() {
        if (animation != null) {
            animation.cancel();
            animation = null;
        }
    }

    @OnClick({R.id.ph_title_right_img, R.id.btn_player_all_music})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
            case R.id.btn_player_all_music:
                if (songInfos.size() > 0) {
                    MusicManager.get().playMusic(songInfos, 0);
                    PlayerNewActivity.launch(mActivity, songInfos, 0);
                }

                break;
        }
    }

    private void like(String userid, int pageNum, int pageSize) {
        addSubscribe(apis.like(new LikeRequest(userid, pageSize, pageNum))
                .compose(RxHttpUtil.<FutureHttpResponse<LikeRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<LikeRespone>handleResult())
                .subscribeWith(new HttpSubscriber<LikeRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
//                        toast(exception.getMessage());
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                    }
                }) {

                    @Override
                    public void onNext(LikeRespone likeRespone) {
                        list.clear();
                        songInfos.clear();
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        likeName.setText(likeRespone.usernickname);
                        likeMusicSize.setText(likeRespone.xinxi);
                        RequestOptions RequestOption = new RequestOptions();
                        RequestOption.placeholder(R.mipmap.moren);
                        Glide.with(mActivity)
                                .load(likeRespone.head_portrait)
                                .apply(RequestOption)
                                .into(likeTopImage);
                        if (likeRespone.listuser != null && likeRespone.listuser.size() > 0) {
                            for (LikeBean respone : likeRespone.listuser) {
                                SongInfo model = new SongInfo();
                                TempInfo tempInfo = new TempInfo();
                                LogUtil.e("lyrics", respone.lyrics);
                                if (respone.lyrics != null && !TextUtils.isEmpty(respone.lyrics)) {
                                    tempInfo.setTemp_1(respone.lyrics);
                                }
                                LogUtil.e("url", respone.music_name);
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
                            LogUtil.e("songInfos", songInfos.size() + "");
                            list.addAll(likeRespone.listuser);
                            adapter = new LikeMusicAdapter(mActivity, list, songInfos);
                            adapter.setOnDelListener(new LikeMusicAdapter.onSwipeListener() {
                                @Override
                                public void onDel(int position) {
                                    if (position >= 0 && position < list.size()) {
                                        delete(list.get(position).likesid);
                                        list.remove(position);
                                        songInfos.remove(position);
                                        adapter.notifyItemRemoved(position);//推荐用这个
                                        //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                                        //且如果想让侧滑菜单同时关闭，需要同时调用 ((SwipeMenuLayout) holder.itemView).quickClose();
                                        //mAdapter.notifyDataSetChanged();
                                    }
                                }
                            });
                            //给RecyclerView设置ontouch时间
                            likeRvMusic.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    if (event.getAction() == MotionEvent.ACTION_UP) {
                                        SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
                                        if (null != viewCache) {
                                            viewCache.smoothClose();
                                        }
                                    }
                                    return false;
                                }
                            });
                            likeRvMusic.setAdapter(adapter);
                            adapter.setItemClickListener(new LikeMusicAdapter.ItemClickListener() {
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
                                        MusicManager.get().playMusic(songInfos, position);
                                        PlayerNewActivity.launch(mActivity, songInfos, position);
                                    }
                                }
                            });
                        } else {
//                            refreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    private void delete(final String likesid) {
        addSubscribe(apis.deletelike(new DeletelikeRequest(likesid))
                .compose(RxHttpUtil.<FutureHttpResponse<List<DeletelikeRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<DeletelikeRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<DeletelikeRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(List<DeletelikeRespone> DeletelikeRespone) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        like(userId(), pageNum, pageSize);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
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
