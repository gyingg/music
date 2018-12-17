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
import com.future_melody.js.ToStartDetailsJs;
import com.future_melody.music.PlayerActivity;
import com.future_melody.utils.LogUtil;
import com.future_melody.view.X5WebView;
import com.lzx.musiclibrary.manager.MusicManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.UMShareAPI;

/**
 * Audhor: WZL
 * Time:  2018/1/30  9:31
 * Notes:
 */

public class X5WebviewNoTitleActivity extends BaseActivity {
    private X5WebView web_view;
    private String Url;
    private String title;
    private ImageView ph_title_right_img;
    private ViewGroup web_fragment;
    private Animation animation;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_notitle_x5;

    }

    @Override
    protected void initView() {
        web_fragment = findViewById(R.id.web_fragment);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        ph_title_right_img.setVisibility(View.GONE);
        ph_title_right_img.setImageResource(R.mipmap.back_music);
        web_view = new X5WebView(mActivity);
        web_fragment.addView(web_view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        setBarColor();
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
            setTitle("");
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
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //JS
        webSetting.setJavaScriptEnabled(true);
        web_view.addJavascriptInterface(new ToStartDetailsJs(mActivity), "JavascriptInterface");
        webSetting.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
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
        if (web_view != null) {
            web_view.destroy();
        }
    }

    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();

    }

    public static void startPHLoanWebActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, X5WebviewNoTitleActivity.class);
        intent.putExtra("Url", url);
        intent.putExtra("title", title);
        LogUtil.e("打出来的链接：", url + "");
        context.startActivity(intent);
    }

    public static void startPHLoanWebActivity(Context context, String url) {
        Intent intent = new Intent(context, X5WebviewNoTitleActivity.class);
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

    //不要忘记重写分享成功后的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
