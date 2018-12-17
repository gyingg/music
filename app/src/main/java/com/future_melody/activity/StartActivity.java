package com.future_melody.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.utils.SPUtils;

/**
 * Author WZL
 * Date：2018/7/14 35
 * Notes:启动页
 */
public class StartActivity extends BaseActivity {
    //    private ImageView start_image;
    private CountDownTimer countDownTimer;
//    private GifDrawable gifDrawable;

    @Override
    protected int getContentViewId() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_start;
    }

    @Override
    protected void initView() {
//        start_image = findViewById(R.id.start_image);
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
//        Glide.with(mActivity).load(R.mipmap.shou_news).apply(requestOptions).into(start_image);
//        try {
//            gifDrawable = new GifDrawable(getResources(), R.drawable.shou_news);
//            gifDrawable.setSpeed(1.8f);
//            gifDrawable.setLoopCount(1);
//            start_image.setImageDrawable(gifDrawable);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void initData() {
        if (!SPUtils.getInstance().getBoolean("isFirst", true)) {
            countDownTimer = new CountDownTimer(100, 100) {
                @Override
                public void onTick(long millisUntilFinished) {
//                    time.setText(millisUntilFinished / 1000 + "跳过");
                }

                @Override
                public void onFinish() {
                    startActivity(new Intent(mActivity, MainNewActivity.class));
                    finish();
                }
            };
            countDownTimer.start();
        } else {
            startActivity(new Intent(mActivity, GuideActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (gifDrawable != null) {
//            if (!gifDrawable.isRecycled()) {
//                gifDrawable.recycle();
//            }
//        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
