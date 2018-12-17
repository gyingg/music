package com.future_melody.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Author WZL
 * Date：2018/7/25 20
 * Notes:友盟统计
 */
public class UmengUtils {
    /**
     * 在Application中做的初始化
     */
    public static void initUmeng() {
        MobclickAgent.setDebugMode(true);//开启调试模式（如果不开启debug运行不会上传umeng统计）
        MobclickAgent.openActivityDurationTrack(false);
//        AnalyticsConfig.setChannel(Common.getChannel());
    }

    /**
     * 在BaseActivity跟BaseFragmentActivity中的onResume加入
     *
     * @param context
     */
    public static void onResumeToActivity(Context context) {
        MobclickAgent.onPageStart(context.getClass().getSimpleName());
        MobclickAgent.onResume(context);
    }

    /**
     * 在BaseActivity跟BaseFragmentActivity中的onPause加入
     *
     * @param context
     */
    public static void onPauseToActivity(Context context) {
        MobclickAgent.onPageEnd(context.getClass().getSimpleName());
        MobclickAgent.onPause(context);
    }

    /**
     * 在BaseFragment中的onResume加入
     *
     * @param fragmrent
     */
    public static void onResumeToFragment(String fragmrent) {
        MobclickAgent.onPageStart(fragmrent);
    }

    /**
     * 在BaseFragment中的onPause加入
     *
     * @param fragmrent
     */
    public static void onPauseToFragment(String fragmrent) {
        MobclickAgent.onPageEnd(fragmrent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Fragment getVisibleFragment(Activity context) {
        FragmentManager fragmentManager = context.getFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

}
