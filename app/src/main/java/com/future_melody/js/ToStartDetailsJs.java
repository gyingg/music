package com.future_melody.js;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.future_melody.activity.StarDetailsActivity;
import com.future_melody.activity.UserInfoActivity;
import com.future_melody.base.FutureApplication;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.receiver.SendWebPlayerEventBus;
import com.future_melody.utils.LogUtil;
import com.lzx.musiclibrary.manager.MusicManager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;


/**
 * Author WZL
 * Date：2018/6/1 23
 * Notes:
 */
public class ToStartDetailsJs {
    private Context mContext;

    public ToStartDetailsJs(Context mContext) {
        this.mContext = mContext;
    }

    @JavascriptInterface
    public void btntoStarDetails(String id) {
        LogUtil.e("JS", id + "");
        try {
            Intent intent = new Intent(mContext, StarDetailsActivity.class);
            intent.putExtra("planetId", id);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @JavascriptInterface
    public void btntoPlayer() {
        try {
            PlayerUitlis.player(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @JavascriptInterface
    public void btntoUserInfo(String userId) {
        try {
            Intent intent = new Intent(mContext, UserInfoActivity.class);
            intent.putExtra("userId", userId);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void btnPauseOrResume() {
        try {
            if (MusicManager.get().getCurrPlayingMusic() != null) {
                if (MusicManager.isPlaying()) {
                    MusicManager.get().pauseMusic();
                    sendEvent(false);
                } else {
                    MusicManager.get().resumeMusic();
                    sendEvent(true);
                }
            } else {
                Toast.makeText(FutureApplication.getContext(), "去找点音乐听听吧", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEvent(boolean isplayer) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new SendWebPlayerEventBus(isplayer));
            }
        }).start();
    }

    @JavascriptInterface
    public void btntoShare(String title, String img, String description, String Url) {

        try {
            share(title, description, Url, img);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 分享
     */
    private void share(String title, String description, String url, String img) {

        UMImage thumb = new UMImage(mContext, img);

        LogUtil.e("url", url);
        LogUtil.e("img", img);
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(description);//描述
        new ShareAction((Activity) mContext)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        Toast.makeText(mContext, "分享取消", Toast.LENGTH_SHORT).show();
                    }
                }).open();
    }
}
