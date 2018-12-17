package com.future_melody.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.SPconst;
import com.future_melody.interfaces.CountDownTimerListener;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.ExitLoginRequest;
import com.future_melody.net.respone.ExitLoginRespone;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.services.CountDownTimerService;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.DataCleanUtil;
import com.future_melody.utils.EaseSwitchButton;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.RxSeekBar;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.SureDialog;
import com.future_melody.utils.TipLinearUtil;
import com.future_melody.widget.CommonDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.lzx.musiclibrary.manager.MusicManager;
import com.umeng.analytics.MobclickAgent;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout about_us;
    private RelativeLayout versions_updata;
    private RelativeLayout user_cleanfile;
    private AlertDialog mDialog;
    private TextView dialog_message;
    private EaseSwitchButton open_switch_button;
    private EaseSwitchButton switch_button_voice;
    private EaseSwitchButton switch_button_wifi;
    private RxSeekBar seekbar;
    private Button exit_login;
    String[] strings = {"15分钟", "30分钟", "45分钟", "60分钟"};
    private ImageView back;
    private RelativeLayout rel_service;
    private ImageView ph_title_right_img;
    private TextView tv_time;
    private TextView versions_code;
    private Animation animation;
    private int service_distination_total = 1000 * 60 * 15;
    private CountDownTimerService countDownTimerService;
    private MyCountDownLisener myCountDownLisener;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    tv_time.setText(formateTimer(countDownTimerService.getCountingTime()));
//                    if (countDownTimerService.getTimerStatus() == CountDownTimerUtil.PREPARE) {
//                        tv_time.setVisibility(View.GONE);
//                        SPUtils.getInstance().put(SPconst.ISMUSIC_SLEEP, 0);
//                        seekbar.setVisibility(View.GONE);
//                        open_switch_button.closeSwitch();
//                    }
                    //设置完关闭
                    if (countDownTimerService.getCountingTime() == 0) {
                        switch_button_voice.closeSwitch();
                        seekbar.setVisibility(View.GONE);
                        tv_time.setVisibility(View.GONE);
                    }
                    LogUtil.e("mHandler2", tv_time.getText().toString());
                    break;
            }
        }
    };


    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        back = findViewById(R.id.back);
        versions_code = findViewById(R.id.versions_code);
        about_us = findViewById(R.id.about_us);
        seekbar = findViewById(R.id.seekbar);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        rel_service = findViewById(R.id.rel_service);
        user_cleanfile = findViewById(R.id.user_cleanfile);
        open_switch_button = findViewById(R.id.open_switch_button);
        switch_button_voice = findViewById(R.id.switch_button_voice);
        tv_time = findViewById(R.id.tv_time);
        exit_login = findViewById(R.id.exit_login);
        if (isLogin()) {
            exit_login.setVisibility(View.VISIBLE);
        } else {
            exit_login.setVisibility(View.GONE);
        }
        versions_updata = findViewById(R.id.versions_updata);
        Boolean Voice = SPUtils.getInstance().getBoolean(SPconst.ISWIFIPLAYER, false);
        if (Voice) {
            switch_button_voice.openSwitch();
        } else {
            switch_button_voice.closeSwitch();
        }
        switch_button_wifi = findViewById(R.id.switch_button_wifi);
        Boolean WiFi = (Boolean) SPUtils.getInstance().getBoolean(SPconst.WiFi, false);
        if (WiFi) {
            switch_button_wifi.openSwitch();
        } else {
            switch_button_wifi.closeSwitch();
        }
        if (SPUtils.getInstance().getInt(SPconst.ISMUSIC_SLEEP) == 1) {
            open_switch_button.openSwitch();
            seekbar.setVisibility(View.VISIBLE);
            tv_time.setVisibility(View.VISIBLE);
            seekbar.setValue(SPUtils.getInstance().getFloat(SPconst.SEEKBARSCALE, 1f));
        } else {
            open_switch_button.closeSwitch();
            seekbar.setVisibility(View.GONE);
            tv_time.setVisibility(View.GONE);
        }
        about_us.setOnClickListener(this);
        user_cleanfile.setOnClickListener(this);
        open_switch_button.setOnClickListener(this);
        switch_button_voice.setOnClickListener(this);
        switch_button_wifi.setOnClickListener(this);
        exit_login.setOnClickListener(this);
        rel_service.setOnClickListener(this);
        back.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
        versions_updata.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        seekbar.setmTextArray(strings);
        versions_code.setText(CommonUtils.getVersionName(mActivity));
        myCountDownLisener = new MyCountDownLisener();
        /**
         * 滑动的时候获取到你滑动到的值  获取到这个刻度值之后才能设置倒计时关闭
         */
        countDownTimerService = CountDownTimerService.getInstance(myCountDownLisener, service_distination_total);
        tv_time.setText(formateTimer(countDownTimerService.getCountingTime()));
        LogUtil.e("tag", countDownTimerService.getCountingTime() + "");
        seekbar.setOnRangeChangedListener(new RxSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RxSeekBar view, float min, float max, boolean isFromUser) {
                if (!isFromUser) {
                    if (min != 0) {
                        int time = service_distination_total * (int) (min == 0 ? 1 : min);
                        if (countDownTimerService != null) {
                            LogUtil.e("Min" + min + ":", formateTimer(time) + "");
                            countDownTimerService.stopCountDown();//停止服务
                        }
                        SPUtils.getInstance().put(SPconst.SEEKBARSCALE, min);
                        countDownTimerService = CountDownTimerService.getInstance(myCountDownLisener, time);//重新获取服务
                        MusicManager.get().pausePlayInMillis(time);//设置音乐暂停时间
                        countDownTimerService.startCountDown();//开始服务
                    }
                }
            }
        });
    }

    private class MyCountDownLisener implements CountDownTimerListener {

        @Override
        public void onChange() {
            mHandler.sendEmptyMessage(2);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.versions_updata:
                toast("已是最新版本");
                break;
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
            case R.id.rel_service:
                intent = new Intent(this, ServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.about_us:
                intent = new Intent(this, AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.open_switch_button:
                if (open_switch_button.isSwitchOpen()) {
                    seekbar.setVisibility(View.GONE);
                    tv_time.setVisibility(View.GONE);
                    open_switch_button.closeSwitch();
                    SPUtils.getInstance().put(SPconst.ISMUSIC_SLEEP, 0);
                    countDownTimerService.stopCountDown();
                } else {
                    countDownTimerService.startCountDown();
                    MusicManager.get().pausePlayInMillis(service_distination_total);
                    SPUtils.getInstance().put(SPconst.ISMUSIC_SLEEP, 1);
                    tv_time.setText(formateTimer(service_distination_total));
                    tv_time.setVisibility(View.VISIBLE);
                    seekbar.setVisibility(View.VISIBLE);
                    seekbar.setValue(1);//初始化时间为第一个
                    open_switch_button.openSwitch();
                }
                break;
            case R.id.switch_button_voice:
                if (switch_button_voice.isSwitchOpen()) {
                    switch_button_voice.closeSwitch();
                    voiceclose();
                } else {
                    switch_button_voice.openSwitch();
                }
                LogUtil.e("是否打开", switch_button_voice.isSwitchOpen() + "");
                SPUtils.getInstance().put(SPconst.ISWIFIPLAYER, switch_button_voice.isSwitchOpen());
                break;
            case R.id.switch_button_wifi:
                if (switch_button_wifi.isSwitchOpen()) {
                    switch_button_wifi.closeSwitch();
                    wificlose();
                } else {
                    switch_button_wifi.openSwitch();
                }
                SPUtils.getInstance().put(SPconst.WiFi, switch_button_wifi.isSwitchOpen());
                break;
            case R.id.user_cleanfile:
                clean();
                break;
            case R.id.exit_login:
                CommonDialog dialog = new CommonDialog(mActivity);
                dialog.setMsg("是否退出登录");
                dialog.setDetermineButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        eixtLogin(dialog);
                    }
                });
                dialog.setCancelButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    private void voiceclose() {
        SureDialog SureDialog = new SureDialog(mActivity);
        SureDialog.setContent("已关闭流量提醒，在非Wi-Fi环境下在线播放／上传，将不再提示消费流量");
        SureDialog.setSure("我知道了");
        SureDialog.getSureView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...保存联网状态
                SureDialog.cancel();
            }
        });
        SureDialog.show();
    }

    private void wificlose() {
        SureDialog SureDialog = new SureDialog(mActivity);
        SureDialog.setContent("已关闭仅Wi-Fi播放音乐，2G/3G/4G网络请关注流量消耗");
        SureDialog.setSure("我知道了");
        SureDialog.getSureView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ...保存联网状态
                SureDialog.cancel();
            }
        });
        SureDialog.show();
    }

    /**
     * 清除缓存
     */
    private void clean() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        View vv = View.inflate(SettingActivity.this, R.layout.delete_history,
                null);
        mDialog = builder.create();
        dialog_message = (TextView) vv.findViewById(R.id.alog_message);
        dialog_message.setText("您确定要清除缓存么？");
        Button btn_cencal = (Button) vv.findViewById(R.id.delete_cencal);
        Button btn_true = (Button) vv.findViewById(R.id.delete_true);
        btn_cencal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.cancel();
            }
        });
        btn_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataCleanUtil.clearAllCache(SettingActivity.this);
                mDialog.cancel();
                Toast.makeText(mActivity, "清除缓存完成!", Toast.LENGTH_SHORT).show();
            }
        });
        mDialog.show();
        mDialog.getWindow().setContentView(vv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAnim();
        if (isLogin()) {
            exit_login.setVisibility(View.VISIBLE);
        } else {
            exit_login.setVisibility(View.GONE);
        }
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

    /**
     * formate timer shown in textview
     *
     * @param time
     * @return
     */
    private String formateTimer(long time) {
        String str = "00:00:00";
        int hour = 0;
        if (time >= 1000 * 3600) {
            hour = (int) (time / (1000 * 3600));
            time -= hour * 1000 * 3600;
        }
        int minute = 0;
        if (time >= 1000 * 60) {
            minute = (int) (time / (1000 * 60));
            time -= minute * 1000 * 60;
        }
        int sec = (int) (time / 1000);
        str = formateNumber(hour) + ":" + formateNumber(minute) + ":" + formateNumber(sec);
        return str;
    }

    /**
     * formate time number with two numbers auto add 0
     *
     * @param time
     * @return
     */
    private String formateNumber(int time) {
        return String.format("%02d", time);
    }


    private void eixtLogin(CommonDialog dialog) {
        addSubscribe(apis.exitLogin(new ExitLoginRequest(userId(), ""))
                .compose(RxHttpUtil.<FutureHttpResponse<ExitLoginRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ExitLoginRespone>handleResult())
                .subscribeWith(new HttpSubscriber<ExitLoginRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(ExitLoginRespone exitLoginRespone) {

                    }

                    @Override
                    public void onComplete() {
                        MobclickAgent.onEvent(mActivity, "10014");
                        SPUtils.getInstance().remove(SPconst.USER_ID);
                        SPUtils.getInstance().put(SPconst.ISlogin, false);
                        //  ActivityCollector.finishAll();  退出全部Activity
                        dialog.dismiss();
                        finish();
                    }
                }));
    }


}
