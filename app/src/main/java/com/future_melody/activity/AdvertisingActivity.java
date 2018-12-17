package com.future_melody.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.adapter.VpAdapter;
import com.future_melody.common.SPconst;
import com.future_melody.utils.SPUtils;
import com.lzx.musiclibrary.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 */
public class AdvertisingActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    //网络改成String类型
    private List<Integer> list;
    private Button into;
    private TextView time;
    private boolean isFirst;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SPUtils.getInstance().put(SPconst.ISMUSIC_SLEEP, 0);//初始化刻度条状态
//        Uri uridata = this.getIntent().getData();
//        String mydata = uridata.getQueryParameter("data");
        //设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_advertising);
        isFirst = SPUtils.getInstance().getBoolean("isFirst", true);
        initView();
        initData();
        initListener();
        if (!isFirst)
            startMainActivity();
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdvertisingActivity.this, MainNewActivity.class));
                finish();
            }
        });
    }

    private void startMainActivity() {
        time.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(9000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText(millisUntilFinished / 1000 + "跳过");
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(AdvertisingActivity.this, MainNewActivity.class));
                finish();
            }
        };
        countDownTimer.start();
    }

    private void initListener() {
        viewPager.setAdapter(new VpAdapter(list, this));
        viewPager.addOnPageChangeListener(this);
        into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdvertisingActivity.this, MainNewActivity.class));
                SPUtils.getInstance().put("isFirst", false);
                finish();
            }
        });
    }

    private void initData() {
        list = new ArrayList<>();
        //第一次加载时   加载三张引导页图片
        if (isFirst) {
            list.add(R.mipmap.page1);
            list.add(R.mipmap.page2);
            list.add(R.mipmap.page3);
        } else {//加载一张广告页
            list.add(R.drawable.shou_news);
        }

    }


    private void initView() {
        viewPager = findViewById(R.id.vp_guide);
        into = findViewById(R.id.into);
        time = findViewById(R.id.time);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position != 0) {//position不是0时说明是引导页  引导页图片数量大于1
            if (position == list.size() - 1)
                into.setVisibility(View.VISIBLE);
            else into.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("onDestroy", "引导页");
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


}
