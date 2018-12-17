package com.future_melody.base;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.future_melody.R;
import com.future_melody.common.SPconst;
import com.future_melody.mode.LocalMusicModel;
import com.future_melody.utils.CrashHandler;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.UmengUtils;
import com.lzx.musiclibrary.cache.CacheConfig;
import com.lzx.musiclibrary.cache.CacheUtils;
import com.lzx.musiclibrary.manager.MusicLibrary;
import com.lzx.musiclibrary.notification.NotificationCreater;
import com.lzx.musiclibrary.notification.PendingIntentMode;
import com.lzx.musiclibrary.utils.BaseUtil;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


public class FutureApplication extends MultiDexApplication {

    private static Context sContext;
    private List<LocalMusicModel> musicModels = new ArrayList<>();
    private static FutureApplication futureApplication;

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.color_00AFFD, android.R.color.white);//全局设置主题颜色
                return new MaterialHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    public void setMusics(List<LocalMusicModel> musics) {
        this.musicModels = musics;
    }

    public boolean isFirstScanFile() {
        return isFirstScanFile;
    }

    public synchronized static FutureApplication getInstance() {
        return futureApplication;
    }

    public void setFirstScanFile(boolean firstScanFile) {
        isFirstScanFile = firstScanFile;
    }

    private boolean isFirstScanFile = true;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        futureApplication = this;
        if (SPUtils.getInstance().getInt(SPconst.PLAYER_TYPE) == -1) {
            SPUtils.getInstance().put(SPconst.PLAYER_TYPE, 3);
        }
        //友盟分享 5af3f97af29d981923000193
        UMConfigure.init(this, "5af3f97af29d981923000193"
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, ""); //58edcfeb310c93091c000be2 5965ee00734be40b580001a0
        PlatformConfig.setWeixin("wx7b691cf13525a868", "e7e626265f2d8d38d65ead7d3c610424");  //微信
        PlatformConfig.setQQZone("1106882248", "3mFzDDSDUMoyWaTr");  //qq
        PlatformConfig.setSinaWeibo("3319533871", "44741691822164a43878624a2427056a", "http://sns.whalecloud.com");  //微博

        if (BaseUtil.getCurProcessName(this).equals("com.future_melody")) {
            //边播边存配置
            CacheConfig cacheConfig = new CacheConfig.Builder()
                    .setOpenCacheWhenPlaying(true)
                    .setCachePath(CacheUtils.getStorageDirectoryPath() + "/Future/Music/")
                    .build();
            //通知栏配置
            NotificationCreater creater = new NotificationCreater.Builder()
                    .setTargetClass("com.future_melody.music.PlayerNewActivity")
                    .setCreateSystemNotification(true)
                    .setNotificationCanClearBySystemBtn(true)
                    .setSystemNotificationShowTime(true)
                    .setPendingIntentMode(PendingIntentMode.MODE_ACTIVITY)
                    .build();
            MusicLibrary musicLibrary = new MusicLibrary.Builder(this)
                    .setNotificationCreater(creater)
//                    .setCacheConfig(cacheConfig)
                    .setUseMediaPlayer(false)
                    .build();
            musicLibrary.init();
        }

        //x5内核初始化接口
        try {
            //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核
            QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
                @Override
                public void onViewInitFinished(boolean arg0) {
                    // TODO Auto-generated method stub
                    //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                    Log.d("app", " onViewInitFinished is " + arg0);
                }

                @Override
                public void onCoreInitFinished() {
                    // TODO Auto-generated method stub
                }
            };
            QbSdk.initX5Environment(getApplicationContext(), cb);
        } catch (Exception e) {

        }

        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        String regId = JPushInterface.getRegistrationID(this);
        LogUtil.e("别名", regId + "");
        MobclickAgent.setScenarioType(getContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        UmengUtils.initUmeng();
        try {
            CrashHandler catchHandler = CrashHandler.getInstance();
            catchHandler.init(getApplicationContext());
        } catch (Exception e) {
        }
    }

    public static Context getContext() {
        return sContext;
    }
}
