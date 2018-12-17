package com.future_melody.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.js.ToStartDetailsJs;
import com.future_melody.net.api.FutrueApis;
import com.future_melody.view.X5WebView;
import com.gyf.barlibrary.ImmersionBar;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebViewClient;

//登陆界面中加入：登录即代表阅读并同意《服务条款》
public class UserClauseActivity extends BaseActivity implements View.OnClickListener {

    private X5WebView webView;
    private FrameLayout web_fragment;
    private ImageView back;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_clause;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true) .init();
        back=findViewById(R.id.back);
        web_fragment = findViewById(R.id.web_fragment);
        webView = new X5WebView(mActivity);
        web_fragment.addView(webView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void initData() {
        back.setOnClickListener(this);
        loadUrl(FutrueApis.HOST + "/h5page/h5All/register.html");
    }

    //h5
    private void loadUrl(String url) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //自适应
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
        }
    }
}
