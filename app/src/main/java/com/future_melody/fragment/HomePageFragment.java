package com.future_melody.fragment;

import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.future_melody.R;
import com.future_melody.base.BaseFragment;
import com.future_melody.common.SPconst;
import com.future_melody.js.ToStartDetailsJs;
import com.future_melody.net.api.FutrueApis;
import com.future_melody.receiver.ListenXingMusicEventBus;
import com.future_melody.receiver.SendWebEventBus;
import com.future_melody.receiver.SendWebPlayerEventBus;
import com.future_melody.utils.DataCleanManager;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.UmengUtils;
import com.future_melody.view.X5WebView;
import com.lzx.musiclibrary.manager.MusicManager;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

public class HomePageFragment extends BaseFragment {

    private FrameLayout web_fragment;
    private X5WebView webView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_page;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        initImmersionBar();
        web_fragment = view.findViewById(R.id.web_fragment);
        if (webView == null) {
            webView = new X5WebView(mActivity ,null);
        }
        webView.setBackgroundColor(Color.BLACK);
        web_fragment.addView(webView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        mActivity.getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        LogUtil.e("开始走了吗", "initData");
        LogUtil.e("开始走了吗", "HomePageFragment");
        loadUrl(FutrueApis.HOST + "/h5page/solarstar/index.html");
        if (MusicManager.isPlaying()) {
            webView.loadUrl("javascript:audioPlay()");
        } else {
            webView.loadUrl("javascript:audioPause()");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SendWebEventBus even) {
        String isUpdate = even.getMessgae();
        LogUtil.e("isUpdate", isUpdate + "");
        if (isUpdate.equals("1")) {
            webView.loadUrl("javascript:changeData()");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ListenXingMusicEventBus even) {
        if (even.getPosition() == -1) {
            webView.loadUrl("javascript:audioPlay()");
        } else if (even.getPosition() == -2) {
            webView.loadUrl("javascript:audioPause()");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayerEvent(SendWebPlayerEventBus even) {
        boolean isPlayer = even.getMessgae();
        LogUtil.e("是否播放isPlayer", isPlayer + "");
        if (isPlayer) {
            webView.loadUrl("javascript:audioPlay()");
        } else {
            webView.loadUrl("javascript:audioPause()");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        loadUrl(FutrueApis.HOST + "/h5page/solarstar/index.html");
        if (!hidden) {
            LogUtil.e("走了吗", SPUtils.getInstance().getBoolean(SPconst.islOADINGMAINSTAR) + "1");
//            if (!SPUtils.getInstance().getBoolean(SPconst.islOADINGMAINSTAR)) {
//                loadUrl(FutrueApis.HOST + "/h5page/solarstar/index.html");
//            }
            if (MusicManager.isPlaying()) {
                webView.loadUrl("javascript:audioPlay()");
            } else {
                webView.loadUrl("javascript:audioPause()");
            }
            LogUtil.e("开始走了吗", "onHiddenChanged");
        }
    }

    private void loadUrl(String url) {
        SPUtils.getInstance().put(SPconst.islOADINGMAINSTAR, true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //自适应
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        //JS
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new ToStartDetailsJs(mActivity), "JavascriptInterface");
        webSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                LogUtil.e("走了吗", "shouldOverrideUrlLoading");
                return true;
            }

            @Override
            public void onReceivedError(WebView var1, int var2, String var3, String var4) {
                LogUtil.e("走了吗", "onReceivedError");
            }
        });
        webView.loadUrl("javascript:audioPlay()");

    }

    @Override
    public void onResume() {
        super.onResume();
        if (MusicManager.isPlaying()) {
            webView.loadUrl("javascript:audioPlay()");
        } else {
            webView.loadUrl("javascript:audioPause()");
        }
        UmengUtils.onResumeToFragment("HomePageFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("HomePageFragment");
    }

    @Override
    public void onDestroyView() {

        if (webView != null) {
            webView.destroy();
        }
        super.onDestroyView();
        LogUtil.e("HomeFragment", "onDestroyView");
        SPUtils.getInstance().put(SPconst.islOADINGMAINSTAR, false);

//        CookieSyncManager.createInstance(mActivity);  //Create a singleton CookieSyncManager within a context
//        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
//        cookieManager.removeAllCookie();// Removes all cookies.
//        CookieSyncManager.getInstance().sync(); // forces sync manager to sync now
//
//        webView.setWebChromeClient(null);
//        webView.setWebViewClient(null);
//        webView.getSettings().setJavaScriptEnabled(false);
//        webView.clearCache(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LogUtil.e("开始走了吗", "onDestroy");
    }
}
