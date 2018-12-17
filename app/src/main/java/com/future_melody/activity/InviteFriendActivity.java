package com.future_melody.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.bean.ShareItemBean;
import com.future_melody.js.ToStartDetailsJs;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.api.FutrueApis;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.SharFriendsRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.SharFriendsRespone;
import com.future_melody.utils.AdtailPopWindow;
import com.future_melody.utils.ImgUtils;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.TipLinearUtil;
import com.future_melody.view.X5WebView;
import com.gyf.barlibrary.ImmersionBar;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请好友
 */
public class InviteFriendActivity extends BaseActivity implements View.OnClickListener, AdtailPopWindow.OnItemClickListener {


    private TextView invite;
    private DrawerLayout draw;
    private TextView tv_rule;
    private AdtailPopWindow adtailPopWindow;
    private ImgUtils imgUtils;
    private List<ShareItemBean> list;
    private X5WebView webView;
    private X5WebView webView2;
    private FrameLayout web_fragment;
    private FrameLayout web_fment;
    private ImageView right_back;
    private ImageView back;
    private String shareUrl;
    private String pictureUrl;
    private TextView cope;
    private TextView code_friend;
    private String my_invitationcode;
    private String title;
    private String summary;
    // private UMWeb web;
    private UMImage image;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_invite_friend;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        adtailPopWindow = new AdtailPopWindow(this);//创建popwindow
        imgUtils = new ImgUtils(this);//图片保存相册工具类
        invite = findViewById(R.id.invite);
        tv_rule = findViewById(R.id.tv_rule);
        draw = findViewById(R.id.draw);
        back = findViewById(R.id.back);
        cope = findViewById(R.id.cope);
        right_back = findViewById(R.id.right_back);
        code_friend = findViewById(R.id.code_friend);
        //h5
        web_fragment = findViewById(R.id.web_fragment);
        webView = new X5WebView(mActivity);
        web_fragment.addView(webView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        //侧滑h5
        web_fment = findViewById(R.id.web_fment);
        webView2 = new X5WebView(mActivity);
        web_fment.addView(webView2, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }


    @Override
    protected void initData() {
        my_invitationcode = getIntent().getStringExtra("code");
        code_friend.setText(my_invitationcode);
        list = new ArrayList<>();
        list.add(new ShareItemBean(R.mipmap.locel_img, "保存图片"));
        list.add(new ShareItemBean(R.mipmap.weixin, "微信"));
        list.add(new ShareItemBean(R.mipmap.friends, "朋友圈"));
        list.add(new ShareItemBean(R.mipmap.weibo, "微博"));
        list.add(new ShareItemBean(R.mipmap.qq, "QQ"));
        list.add(new ShareItemBean(R.mipmap.lianjie, "复制链接"));

        adtailPopWindow.setList(list);
        adtailPopWindow.setOnItemClickListener(this);
        invite.setOnClickListener(this);
        back.setOnClickListener(this);
        tv_rule.setOnClickListener(this);
        right_back.setOnClickListener(this);
        cope.setOnClickListener(this);
        //h5
        loadUrl(FutrueApis.HOST + "/h5page/share/index.html?id=" + userId());
        //侧滑h5
        loadUrl2(FutrueApis.HOST + "/h5page/h5All/rule.html");
    }


    private void loadUrl2(String url) {
        WebSettings webSettings = webView2.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //自适应
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //JS
        webSettings.setJavaScriptEnabled(true);
        webView2.addJavascriptInterface(new ToStartDetailsJs(mActivity), "JavascriptInterface");
        webSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView2.loadUrl(url);
        webView2.setWebViewClient(new WebViewClient() {
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
        if (webView2 != null) webView2.destroy();
    }


    private void initShare(String url) {
/*        //分享的图片
        UMImage image = new UMImage(this, pictureUrl);//网络图片
        //分享链接 必须以http开头
        web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(summary);//描述*/
        //网络图片
        image = new UMImage(mActivity, pictureUrl);
        image.compressStyle = UMImage.CompressStyle.SCALE;
        image.compressStyle = UMImage.CompressStyle.QUALITY;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invite:
                sharFriend(userId(), "1");
                adtailPopWindow.showAtLocation(getWindow().getDecorView(), Gravity.TOP, 0, 0);
                // shareAction.open(shareBoardConfig);
                break;
            case R.id.tv_rule:
                draw.openDrawer(Gravity.END);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.right_back:
                draw.closeDrawer(Gravity.END);
                break;
            case R.id.cope:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(my_invitationcode);
                TipLinearUtil.create(mActivity).showTipMessage("已复制到剪切版");
                break;
        }
    }

    private void sharFriend(final String userid, String type) {
        addSubscribe(apis.sharFriends(new SharFriendsRequest(userid, type))
                .compose(RxHttpUtil.<FutureHttpResponse<SharFriendsRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<SharFriendsRespone>handleResult())
                .subscribeWith(new HttpSubscriber<SharFriendsRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {


                    @Override
                    public void onNext(SharFriendsRespone SharFriendsRespone) {
                        pictureUrl = SharFriendsRespone.getPictureUrl();
                        adtailPopWindow.setImg(pictureUrl);  //设置图片
                        shareUrl = SharFriendsRespone.getShareUrl();
                        title = SharFriendsRespone.getTitle();
                        summary = SharFriendsRespone.getSummary();
                        initShare(SharFriendsRespone.getShareUrl());
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            toast("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            toast("分享失败" + throwable.getMessage());

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            toast("分享取消");
        }
    };

    @Override
    public void setOnItemClickListener(int position, View v) {
        switch (position) {
            case 0:
                saveImg();
                break;
            case 1:
                shareOfPlatform(SHARE_MEDIA.WEIXIN);
                break;
            case 2:
                shareOfPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case 3:
                shareOfPlatform(SHARE_MEDIA.SINA);
                break;
            case 4:
                shareOfPlatform(SHARE_MEDIA.QQ);
                break;
            case 5:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(shareUrl);
                TipLinearUtil.create(mActivity).showTipMessage("已复制到剪切版");
                break;
        }
        adtailPopWindow.dismiss();
    }

    private void shareOfPlatform(SHARE_MEDIA media) {
        new ShareAction(InviteFriendActivity.this)
                .setPlatform(media)
                .withMedia(image) //web
                .setCallback(umShareListener)
                .share();
    }

    private void saveImg() {
        LogUtil.e("saveImg", "saveImg");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //在这里要判断是否拥有内存卡读写权限
                LogUtil.e("saveImg", pictureUrl + "===saveImg");
                String imgUrl = pictureUrl;
                imgUtils.saveBitmap(imgUtils.getHttpBitmap(imgUrl), imgUrl.substring(imgUrl.lastIndexOf("/")));
            }
        }).start();
    }
}
