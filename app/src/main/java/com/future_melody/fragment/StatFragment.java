package com.future_melody.fragment;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebViewFragment;
import android.widget.FrameLayout;

import com.future_melody.R;
import com.future_melody.base.BaseFragment;
import com.future_melody.js.ToStartDetailsJs;
import com.future_melody.net.api.FutrueApis;
import com.future_melody.view.X5WebView;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Author WZL
 * Date：2018/8/30 14
 * Notes:
 */
public class StatFragment extends BaseFragment {
    private FrameLayout web_fragment;
    private X5WebView x5WebView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_page;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        web_fragment = view.findViewById(R.id.web_fragment);
        x5WebView = new X5WebView(mActivity);
        web_fragment.addView(x5WebView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void initData() {
        mActivity.getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        loadUrl(FutrueApis.HOST + "/h5page/solarstar/index.html");
    }

    private void loadUrl(String url) {
        WebSettings webSetting = x5WebView.getSettings();
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
        webSetting.setAppCachePath(mActivity.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(mActivity.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(mActivity.getDir("geolocation", 0)
                .getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //JS
        webSetting.setJavaScriptEnabled(true);
        x5WebView.addJavascriptInterface(new ToStartDetailsJs(mActivity), "JavascriptInterface");
        webSetting.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        long time = System.currentTimeMillis();
        CookieSyncManager.createInstance(mActivity);
        CookieSyncManager.getInstance().sync();
        x5WebView.loadUrl(url);
        x5WebView.setWebViewClient(new WebViewClient() {
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
    public void onDestroyView() {
        super.onDestroyView();
        if (x5WebView != null) {
            x5WebView.removeAllViews();
            x5WebView.destroy();
        }
    }
}
