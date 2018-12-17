package com.future_melody.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.future_melody.R;
import com.future_melody.base.BaseFragment;
import com.future_melody.js.ToStartDetailsJs;
import com.future_melody.net.api.FutrueApis;
import com.future_melody.utils.UmengUtils;
import com.future_melody.view.X5WebView;
import com.lzx.musiclibrary.utils.LogUtil;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Author WZL
 * Date：2018/5/15 32
 * Notes: 统治者
 */
public class RulersSuperuFragment extends BaseFragment {

    private X5WebView webView;
    private FrameLayout web_fragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rulers_super;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        web_fragment = view.findViewById(R.id.web_fragment);
        webView = new X5WebView(mActivity);
        web_fragment.addView(webView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        String rulerUserId = bundle.getString("rulerUserId");
        LogUtil.e("rulerUserId", rulerUserId);
        loadUrl(FutrueApis.HOST + "/h5page/h5All/Rulers.html?id=" + rulerUserId);
    }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (webView != null) webView.destroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengUtils.onResumeToFragment("RulersSuperuFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("RulersSuperuFragment");
    }
}
