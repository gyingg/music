package com.future_melody.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.future_melody.R;
import com.future_melody.activity.X5WebviewNoTitleActivity;
import com.future_melody.adapter.MainActivityAdapter;
import com.future_melody.base.BaseFragment;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.MainActivityFragmentRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MainActivityFragmentRespone;
import com.future_melody.receiver.ListenXingMusicEventBus;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.TipLinearUtil;
import com.lzx.musiclibrary.manager.MusicManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Y on 2018/8/20.
 */

public class MainActivityFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView rec;
    private MainActivityAdapter mainadapter;
    private ImageView ph_title_right_img;
    private Animation animation;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_activity_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        initAnim();
        rec = view.findViewById(R.id.rec);
        ph_title_right_img = view.findViewById(R.id.ph_title_right_img);
        ph_title_right_img.setOnClickListener(this);
        LogUtil.e("开始走了吗", "MainActivityFragment");
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
        if (even.getPosition() == -1) {
            startAnmi();
        } else if (even.getPosition() == -2) {
            stoptAnmi();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        initAnim();
        com.lzx.musiclibrary.utils.LogUtil.e("活动()", isLogin() + "");
        if (!hidden){
            initImmersionBar();
            setBarDarkFont();
        }
    }

    @Override
    protected void initData() {
        if (MusicManager.isPlaying()) {
            startAnmi();
        } else {
            stoptAnmi();
        }
        getActive("", userId());
        rec.setLayoutManager(new LinearLayoutManager(mActivity));
    }


    //获取活动
    private void getActive(final String planetId, String userId) {
        addSubscribe(apis.MainActivityFragment(new MainActivityFragmentRequest(planetId, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<List<MainActivityFragmentRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<MainActivityFragmentRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<MainActivityFragmentRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(List<MainActivityFragmentRespone> mainActivityFragmentRespone) {
                        mainadapter = new MainActivityAdapter(mActivity, mainActivityFragmentRespone);
                        mainadapter.setItemRec(new MainActivityAdapter.itemRec() {
                            @Override
                            public void itemRec(int i) {
                                X5WebviewNoTitleActivity.startPHLoanWebActivity(mActivity, mainActivityFragmentRespone.get(i).activeTransferUrl);
                            }
                        });
                        rec.setAdapter(mainadapter);
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroyAnmi();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyAnmi();
        EventBus.getDefault().unregister(this);
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


}
