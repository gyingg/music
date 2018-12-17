package com.future_melody.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.future_melody.R;

import java.lang.ref.WeakReference;

/**
 * ================================================
 * 描    述：顶部提示工具类
 * 创建日期：2017/3/3
 * 版    本：1.0
 * 修订历史：
 * 说    明：
 * isNullTop：提示内容是否判断空值，如果为true提示内容又为空将不会有提示
 * ================================================
 */
public class TipLinearUtil {

    //当前activity弱引用
    private static WeakReference<Activity> activityWeakReference;
    //提示控件
    private TipLinearLayout tiplinear;
    //提示内容是否判断空值，如果为true提示内容又为空将不会有提示
    private boolean isNullTop = true;

    private TipLinearUtil() {
    }

    /**
     * 清除提示
     *
     * @param activity 上下文
     */
    public static void clearCurrent(@NonNull final Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        try {
            final View alertView = activity.getWindow().getDecorView().findViewById(R.id.ll_tip);
            //检查是否添加到窗口的警报
            if (alertView == null || alertView.getWindowToken() == null) {
            } else {
                ViewCompat.animate(alertView).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        ((ViewGroup) alertView.getParent()).removeView(alertView);
                    }
                }).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 显示提示
     */
    private TipLinearLayout addTipView() {
        //这会给活动窗口的decorview
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //将提示布局添加到view
                    final ViewGroup decorView = getActivityDecorView();
                    if (decorView != null && tiplinear != null && tiplinear.getParent() == null) {
                        int height = getActivity().getResources().getDimensionPixelOffset(R.dimen.dp_48);
                        int barHeight = 0;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            barHeight = CommonUtils.getStatusBarHigh(getActivity());
                        }
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height + barHeight);
                        params.gravity = Gravity.TOP;
                        tiplinear.setPadding(tiplinear.getLeft(), barHeight, tiplinear.getRight(), tiplinear.getBottom());
                        decorView.addView(tiplinear, params);
                    }
                }
            });
        }
        return tiplinear;
    }

    /**
     * 获取当前根布局
     */
    private ViewGroup getActivityDecorView() {
        ViewGroup decorView = null;
        if (getActivity() != null) {
            decorView = (ViewGroup) getActivity().getWindow().getDecorView();
        }
        return decorView;
    }

    /**
     * 保存当前上下文
     *
     * @param activity 上下文
     */
    private void setActivity(@NonNull final Activity activity) {
        if (activityWeakReference != null) {
            activityWeakReference.clear();
            activityWeakReference = null;
        }
        activityWeakReference = new WeakReference<>(activity);
    }

    /**
     * 获取当前上下文
     */
    private Activity getActivity() {
        if (activityWeakReference != null && activityWeakReference.get() != null && !activityWeakReference.get().isFinishing()) {
            return activityWeakReference.get();
        }
        return null;
    }

    /**
     * 显示提示消息
     */
    private void showTipMessage() {
        if (tiplinear != null) {
            addTipView();
            tiplinear.showTipMessage();
        }
    }

    /**
     * 设置显示内容
     *
     * @param message 内容
     */
    public void showTipMessage(String message) {
        if (getActivity() != null)
            showTipMessage(message, ContextCompat.getColor(getActivity(), R.color.color_FF00AFFD));
    }

    public void showTipMessage(String message, int bgColor) {
        showTipMessage(message, -1, bgColor);
    }

    public void showTipMessage(String message, int iconResource, int bgColor) {
        showTipMessage(message, iconResource, bgColor, -1);
    }

    public void showTipMessage(String message, int iconResource, int bgColor, long showTimer) {
        showTipMessage(message, iconResource, bgColor, -1, showTimer);
    }

    /**
     * 显示提示
     *
     * @param message         提示内容
     * @param iconResource    图标
     * @param bgColor         背景颜色
     * @param bgResourceColor 背景图片
     * @param showTimer       显示时间
     */
    public void showTipMessage(String message, int iconResource, int bgColor, int bgResourceColor, long showTimer) {
        if (getActivity() != null) {
            if (isNullTop && (TextUtils.isEmpty(message) || TextUtils.isEmpty(message.trim()))) {
                return;
            }
            if (tiplinear != null) {
                tiplinear.setMessage(message)
                        .setImage(iconResource)
                        .setBgcolor(bgColor)
                        .setBgResourceColor(bgResourceColor)
                        .setShowTimer(showTimer);
                showTipMessage();
            }
        }
    }

    /**
     * 成功提示
     *
     * @param message 提示内容
     */
    public void showSuccessTop(String message) {
        if (getActivity() != null) {
            if (isNullTop && (TextUtils.isEmpty(message) || TextUtils.isEmpty(message.trim()))) {
                return;
            }
            if (tiplinear != null) {
                tiplinear.setMessage(message)
                        .setImage(R.mipmap.cd_ic_checked_success)
                        .setBgcolor(ContextCompat.getColor(getActivity(), R.color.color_FF00AFFD));
                showTipMessage();
            }
        }
    }

    /**
     * 成功提示
     *
     * @param message 提示内容
     */
    public void showSuccessTop(String message, long showTimer) {
        if (getActivity() != null) {
            if (isNullTop && (TextUtils.isEmpty(message) || TextUtils.isEmpty(message.trim()))) {
                return;
            }
            if (tiplinear != null) {
                tiplinear.setMessage(message)
                        .setImage(R.mipmap.cd_ic_checked_success)
                        .setBgcolor(ContextCompat.getColor(getActivity(), R.color.color_FF00AFFD))
                        .setShowTimer(showTimer);
                showTipMessage();
            }
        }
    }

    /**
     * 设置提示内容
     *
     * @param text 内容
     * @return TipLinearUtil
     */
    public TipLinearUtil setMessage(String text) {
        if (tiplinear != null) {
            tiplinear.setMessage(text);
        }
        return this;
    }

    /**
     * 设置提示的图标
     *
     * @param resources 图标
     */
    public TipLinearUtil setImage(int resources) {
        if (tiplinear != null) {
            this.tiplinear.setImage(resources);
        }
        return this;
    }

    /**
     * 设置提示的图标
     *
     * @param image 图标
     * @return TipLinearUtil
     */
    public TipLinearUtil setImage(Bitmap image) {
        if (tiplinear != null) {
            this.tiplinear.setImage(image);
        }
        return this;
    }

    /**
     * 设置提示时间
     *
     * @param showTimer 时间
     * @return TipLinearUtil
     */
    public TipLinearUtil setShowTimer(long showTimer) {
        if (tiplinear != null) {
            this.tiplinear.setShowTimer(showTimer);
        }
        return this;
    }

    /**
     * 设置背景
     *
     * @param bgcolor 背景颜色
     * @return TipLinearUtil
     */
    public TipLinearUtil setBgcolor(int bgcolor) {
        if (tiplinear != null) {
            tiplinear.setBgcolor(bgcolor);
        }
        return this;
    }

    /**
     * 设置背景
     *
     * @param bgId 背景颜色id
     * @return TipLinearUtil
     */
    public TipLinearUtil setBgResourceColor(int bgId) {
        if (tiplinear != null) {
            tiplinear.setBgResourceColor(bgId);
        }
        return this;
    }

    public TipLinearUtil setNullTop(boolean nullTop) {
        isNullTop = nullTop;
        return this;
    }

    @Override
    public String toString() {
        return super.toString();
    }


    /**
     * 创建提示
     *
     * @param activity 上下文
     * @return 提示工具类
     */
    public static TipLinearUtil create(@NonNull final Activity activity) {
        return create(activity, true);
    }

    /**
     * 创建提示
     *
     * @param activity  上下文
     * @param isNullTop 提示内容是否判断空值
     * @return 提示工具类
     */
    public static TipLinearUtil create(@NonNull final Activity activity, boolean isNullTop) {
        if (activity == null || activity.isFinishing()) {
            return new TipLinearUtil();
        }
        clearCurrent(activity);
        TipLinearUtil tipLinearUtil = new TipLinearUtil();
        tipLinearUtil.tiplinear = TipLinearLayout.getViewTop(activity);
        tipLinearUtil.setActivity(activity);
        tipLinearUtil.setNullTop(isNullTop);
        return tipLinearUtil;
    }

}
