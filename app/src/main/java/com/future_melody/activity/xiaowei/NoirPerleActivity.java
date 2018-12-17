package com.future_melody.activity.xiaowei;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.adapter.xiaowei.ProfitAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.mode.XiaoWeiInfo;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.XiaoWeiInfoRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.XiaoWeiInfoRespone;
import com.lzx.musiclibrary.manager.MusicManager;

/**
 * 黑珍珠收益
 */
public class NoirPerleActivity extends BaseActivity implements View.OnClickListener {
    private Animation animation;
    private ImageView back;
    private ImageView ph_title_right_img;
    private RecyclerView recyclerview_profit;
    private ProfitAdapter adapter;
    private TextView profit_title_name;
    private TextView profit_number;
    private String deviceId = "";
    private int type;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_noir_perle;
    }

    @Override
    protected void initView() {
        back = findViewById(R.id.back);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        recyclerview_profit = findViewById(R.id.recyclerview_profit);
        profit_title_name = findViewById(R.id.profit_title_name);
        profit_number = findViewById(R.id.profit_number);
        initAnim();
        back.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
    }

    private void initAnim() {
        animation = AnimationUtils.loadAnimation(this, R.anim.player);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MusicManager.isPlaying()) {
            startAnmi();
        } else {
            stoptAnmi();
        }
        getData();
    }

    private void getData() {
        showLoadingDialog();
        addSubscribe(apis.getXiaoWeiInfo(new XiaoWeiInfoRequest(userId(), deviceId))
                .compose(RxHttpUtil.<FutureHttpResponse<XiaoWeiInfoRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<XiaoWeiInfoRespone>handleResult())
                .subscribeWith(new HttpSubscriber<XiaoWeiInfoRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                        dismissLoadingDialog();
                    }
                }) {
                    @Override
                    public void onNext(XiaoWeiInfoRespone mineXiaoWeiRespone) {
                        profit_title_name.setText(mineXiaoWeiRespone.incomeDesc + "");
                        profit_number.setText(mineXiaoWeiRespone.allIncomeStr + "");
                        if (mineXiaoWeiRespone.trackList != null) {
                            recyclerview_profit.setLayoutManager(new LinearLayoutManager(mActivity));
                            adapter = new ProfitAdapter(mActivity, mineXiaoWeiRespone.trackList);
                            recyclerview_profit.setAdapter(adapter);
                            if (type == 1) {
                                adapter.setRelieveClickListener(new ProfitAdapter.RelieveClickListener() {
                                    @Override
                                    public void relieve(int i) {
                                        XiaoWeiInfo info = mineXiaoWeiRespone.trackList.get(i);
                                        Intent intent = new Intent(mActivity, NoirPerleActivity.class);
                                        intent.putExtra("deviceId", info.deviceId);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                    }
                }));
    }


    private void startAnmi() {
        ph_title_right_img.startAnimation(animation);
    }

    private void stoptAnmi() {
        ph_title_right_img.clearAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAnmi();
    }

    private void destroyAnmi() {
        animation.cancel();
        animation = null;
    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent.getStringExtra("deviceId") != null) {
            deviceId = intent.getStringExtra("deviceId");
        }
        type = intent.getIntExtra("type", -1);

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

}
