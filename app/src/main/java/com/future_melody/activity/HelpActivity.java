package com.future_melody.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.js.ToStartDetailsJs;
import com.future_melody.net.api.FutrueApis;
import com.future_melody.view.X5WebView;
import com.gyf.barlibrary.ImmersionBar;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * 帮助界面
 */
public class HelpActivity extends BaseActivity {

    private FrameLayout web_fragment;
    private X5WebView webView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_help;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        web_fragment = findViewById(R.id.web_fragment);
        webView = new X5WebView(mActivity);
        web_fragment.addView(webView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void initData() {
        loadUrl(FutrueApis.HOST + "/h5page/h5All/help.html");
    }


    //h5
    private void loadUrl(String url) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //自适应
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        //JS
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new ToStartDetailsJs(mActivity), "JavascriptInterface");
        webSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(com.tencent.smtt.sdk.WebView var1, int var2, String var3, String var4) {
            }
        });
    }

    //h5
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) webView.destroy();
    }
}
