package com.future_melody.activity.xiaowei;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.future_melody.R;
import com.future_melody.adapter.xiaowei.RelevanceXiaoWeiAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.CommonConst;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.MineXiaoWeiListRequest;
import com.future_melody.net.request.XiaoWeiQRcodeRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MineXiaoWeiListRespone;
import com.future_melody.net.respone.XiaoWeiQRcodeRespone;
import com.future_melody.widget.CommonDialog;
import com.lzx.musiclibrary.manager.MusicManager;

import java.util.List;

public class RelevanceXiaoWeiActivity extends BaseActivity implements View.OnClickListener {

    private Animation animation;
    private ImageView back;
    private ImageView ph_title_right_img;
    private RecyclerView rev_mine_xiaowei;
    private RelevanceXiaoWeiAdapter adapter;
    private LinearLayout add_xiaowei;
    private String scanPic;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_relevance_xiao_wei;
    }

    @Override
    protected void initView() {
        back = findViewById(R.id.back);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        rev_mine_xiaowei = findViewById(R.id.rev_mine_xiaowei);
        add_xiaowei = findViewById(R.id.add_xiaowei);
        initAnim();
        back.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
        add_xiaowei.setOnClickListener(this);
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
        scanPic = getIntent().getStringExtra("scanPic");
    }

    private void getData() {
        addSubscribe(apis.getMineXiaoWeiList(new MineXiaoWeiListRequest(userId()))
                .compose(RxHttpUtil.<FutureHttpResponse<List<MineXiaoWeiListRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<MineXiaoWeiListRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<MineXiaoWeiListRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(List<MineXiaoWeiListRespone> mineXiaoWeiListRespones) {
                        dismissLoadingDialog();
                        adapter = new RelevanceXiaoWeiAdapter(mActivity, mineXiaoWeiListRespones);
                        rev_mine_xiaowei.setLayoutManager(new LinearLayoutManager(mActivity));
                        rev_mine_xiaowei.setAdapter(adapter);
                        adapter.setRelieveClickListener(new RelevanceXiaoWeiAdapter.RelieveClickListener() {
                            @Override
                            public void relieve(int i) {
                                MineXiaoWeiListRespone respone = mineXiaoWeiListRespones.get(i);
                                CommonDialog commonDialog = new CommonDialog(mActivity);
                                commonDialog.setMsg("是否要解除绑定");
                                commonDialog.setCancelButton(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        commonDialog.dismiss();
                                    }
                                });
                                commonDialog.setDetermineButton(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        relevance(respone.mac);
                                        commonDialog.dismiss();
                                    }
                                });

                            }
                        });
                        adapter.setResetClickListener(new RelevanceXiaoWeiAdapter.RelieveClickListener() {
                            @Override
                            public void relieve(int i) {
                                Intent intent = new Intent(mActivity, AddXiaoweiQRcodeActivity.class);
                                intent.putExtra(CommonConst.XIAOWEI_IS_NET, 1);
                                intent.putExtra("scanPic", scanPic);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    private void relevance(String mac) {
        showLoadingDialog();
        addSubscribe(apis.getXiaoWeiInfo(new XiaoWeiQRcodeRequest(mac, "", userId(), 2))
                .compose(RxHttpUtil.<FutureHttpResponse<XiaoWeiQRcodeRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<XiaoWeiQRcodeRespone>handleResult())
                .subscribeWith(new HttpSubscriber<XiaoWeiQRcodeRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                        dismissLoadingDialog();
                    }
                }) {
                    @Override
                    public void onNext(XiaoWeiQRcodeRespone xiaoWeiQRcodeRespone) {
                    }

                    @Override
                    public void onComplete() {
                        getData();
                        toast("小未解绑成功");
                    }
                }));
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add_xiaowei:
                intent = new Intent(mActivity, AddXiaoweiQRcodeActivity.class);
                intent.putExtra("scanPic", scanPic);
                startActivity(intent);
                finish();
                break;
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
        }
    }
}
