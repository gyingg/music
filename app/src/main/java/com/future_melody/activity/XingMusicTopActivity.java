package com.future_melody.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.future_melody.R;
import com.future_melody.adapter.XingMusicHandler;
import com.future_melody.base.BaseActivity;
import com.future_melody.mode.XingMusicTopModel;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.HttpUtil;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.XingMusicTopRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.XingMusicTopRespone;
import com.future_melody.receiver.ListenXingMusicEventBus;
import com.future_melody.widget.cardview.CardViewPager;
import com.lzx.musiclibrary.manager.MusicManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Author WZL
 * Date：2018/8/20 21
 * Notes:
 */
public class XingMusicTopActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ph_title_back;
    private ImageView ph_title_right_img;
    private ImageView xing_music_top_bg;
    private Animation animation;
    private List<XingMusicTopModel> lists;
    private CardViewPager xing_music_cover_img;
    private TextView pager_number;
    private TextView pager_all_number;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_music_top_xing;
    }

    @Override
    protected void initView() {
        ph_title_back = findViewById(R.id.ph_title_back);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        xing_music_top_bg = findViewById(R.id.xing_music_top_bg);
        xing_music_cover_img = findViewById(R.id.xing_music_cover_img);
        pager_number = findViewById(R.id.pager_number);
        pager_all_number = findViewById(R.id.pager_all_number);
        ph_title_back.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        initAnim();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        lists = new LinkedList<>();
        getList();
        xing_music_cover_img.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pager_number.setText((position + 1) + "");
                    }
                });
                if (lists.get(position).list.size() > 0) {
                    String url = lists.get(position).list.get(0).coverUrl;
                    Glide.with(mActivity)
                            .load(url)
                            .apply(new RequestOptions()
                                    .transform(new BlurTransformation(13, 3)))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    xing_music_top_bg.setImageDrawable(resource);
                                }
                            });
                } else {
                    Glide.with(mActivity)
                            .load(R.mipmap.moren)
                            .apply(new RequestOptions()
                                    .transform(new BlurTransformation(13, 3)))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    xing_music_top_bg.setImageDrawable(resource);
                                }
                            });
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getList() {
        showLoadingDialog();
        addSubscribe(HttpUtil.getPHApis().getNewXingTop(new XingMusicTopRequest(userId(), 0, 0, 0))
                .compose(RxHttpUtil.<FutureHttpResponse<XingMusicTopRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<XingMusicTopRespone>handleResult())
                .subscribeWith(new HttpSubscriber<XingMusicTopRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        dismissLoadingDialog();
                        Toast.makeText(mActivity, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

                    @Override
                    public void onNext(XingMusicTopRespone respones) {
                        dismissLoadingDialog();
                        lists.addAll(respones.list);
                        pager_all_number.setText("/" + lists.size());
                        xing_music_cover_img.bind(getSupportFragmentManager(), new XingMusicHandler(), respones.list);
                        xing_music_cover_img.setCardTransformer(180, 0f);
                        xing_music_cover_img.setCardPadding(60);
                        xing_music_cover_img.setCardMargin(30);
                        xing_music_cover_img.notifyUI(CardViewPager.MODE_CARD);
                        if (lists.get(0).list.size() > 0) {
                            String url = lists.get(0).list.get(0).coverUrl;
                            Glide.with(mActivity)
                                    .load(url)
                                    .apply(new RequestOptions()
                                            .transform(new BlurTransformation(13, 3)))
                                    .into(new SimpleTarget<Drawable>() {
                                        @Override
                                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                            xing_music_top_bg.setImageDrawable(resource);
                                        }
                                    });
                        } else {
                            Glide.with(mActivity)
                                    .load(R.mipmap.moren)
                                    .apply(new RequestOptions()
                                            .transform(new BlurTransformation(13, 3)))
                                    .into(new SimpleTarget<Drawable>() {
                                        @Override
                                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                            xing_music_top_bg.setImageDrawable(resource);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                    }
                }));
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
    protected void onResume() {
        super.onResume();
        if (MusicManager.isPlaying()) {
            startAnmi();
        } else {
            stoptAnmi();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAnmi();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ph_title_back:
                finish();
                break;
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
        }
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

    private void destroyAnmi() {
        animation.cancel();
        animation = null;
    }
}
