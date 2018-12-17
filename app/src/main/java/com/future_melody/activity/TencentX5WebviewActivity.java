package com.future_melody.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.music.PlayerActivity;
import com.future_melody.view.X5WebView;
import com.gyf.barlibrary.ImmersionBar;
import com.lzx.musiclibrary.manager.MusicManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Audhor: WZL
 * Time:  2018/1/30  9:31
 * Notes:
 */

public class TencentX5WebviewActivity extends BaseActivity {
    private X5WebView web_view;
    private String Url;
    private String title;
    private ImageView ph_title_right_img;
    private ViewGroup web_fragment;
    private Animation animation;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_tencent_x5;

    }

    @Override
    protected void initView() {
        setBarColor(R.color.white, true);
        setBlackBackble();
        setTitleColor(R.color.color_333333);
        setTitleLayoutColor(mActivity,R.color.white);
        web_fragment = findViewById(R.id.web_fragment);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        ph_title_right_img.setVisibility(View.VISIBLE);
        ph_title_right_img.setImageResource(R.mipmap.back_music);
        web_view = new X5WebView(mActivity);
        web_fragment.addView(web_view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void initData() {
        setBackble(true);
        initAnim();
        if (MusicManager.isPlaying()) {
            startAnmi();
        } else {
            stoptAnmi();
        }
        Intent intent = getIntent();
        if (intent != null) {
            Url = intent.getStringExtra("Url");
            title = intent.getStringExtra("title");
            setTitle(title);
        } else {
            setTitle("详情");
        }
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        loadUrl(Url);
        ph_title_right_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(mActivity, PlayerActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void loadUrl(String url) {
        WebSettings webSetting = web_view.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        long time = System.currentTimeMillis();
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
        web_view.loadUrl(url);
        web_view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView var1, int var2, String var3, String var4) {
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (web_view != null && web_view.canGoBack()) {
                web_view.goBack();
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAnmi();
        if (web_view != null) web_view.destroy();
    }

    public static void startPHLoanWebActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, TencentX5WebviewActivity.class);
        intent.putExtra("Url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    public static void startPHLoanWebActivity(Context context, String url) {
        Intent intent = new Intent(context, TencentX5WebviewActivity.class);
        intent.putExtra("Url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAnim();
        if (MusicManager.isPlaying()) {
            startAnmi();
        } else {
            stoptAnmi();
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
