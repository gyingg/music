package com.future_melody.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.future_melody.R;

/**
 * ================================================
 * 描    述：用于提示的LinearLayout
 * 创建日期：16/6/17
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class TipLinearLayout extends LinearLayout {

    //显示图标
    private ImageView imageView;
    //显示消息
    private TextView textView;
    //显示动画
    private BaseAnimatorSet.AnimatorListener animatorSetListener;
    //隐藏动画
    private BaseAnimatorSet.AnimatorListener dismissSetListener;
    //回调接口
    private ShowHideListner listner;
    //显示时间
    private long showTimer = 3000;//显示毫秒数
    //当前显示的activity
    private Activity activity;

    public TipLinearLayout(Context context) {
        super(context);
        initChildren();
    }

    public TipLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initChildren();
    }

    public TipLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChildren();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TipLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initChildren();
    }

    private void initChildren() {
        setGravity(Gravity.CENTER);
        setPadding(getResources().getDimensionPixelOffset(R.dimen.dp_12), 0, getResources().getDimensionPixelOffset(R.dimen.dp_14), 0);
        setOrientation(LinearLayout.HORIZONTAL);
        int px15 = getResources().getDimensionPixelOffset(R.dimen.dp_14);
        //添加图标
        imageView = new ImageView(getContext());
        LayoutParams params = new LayoutParams(px15, px15);
        params.gravity = Gravity.CENTER;
        imageView.setImageResource(R.mipmap.cd_ic_checked_failure);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        //添加提示
        textView = new TextView(getContext());
        LayoutParams paramst = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramst.leftMargin = getResources().getDimensionPixelOffset(R.dimen.dp_6);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.sp_14));
//        textView.setSingleLine(true);
        textView.setMaxLines(3);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        setBackgroundColor(Color.parseColor("#FFe93f4c"));
//        if (isStatusBar) {
//            int px10 = PhoneUtil.dip2px(getContext(), 10);
//            params.topMargin = px10;
//            paramst.topMargin = px10;
//        }
        imageView.setLayoutParams(params);
        textView.setLayoutParams(paramst);
        addView(imageView);
        addView(textView);

        animatorSetListener = new BaseAnimatorSet.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {
//                isShow = true;//设置状态为显示
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                onShow();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }
        };
        dismissSetListener = new BaseAnimatorSet.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                onHide();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }
        };
        //拦截单击事件
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 设置提示内容
     *
     * @param text 内容
     * @return
     */
    public TipLinearLayout setMessage(String text) {
        this.textView.setText(text);
        return this;
    }

    /**
     * 设置提示的图标
     *
     * @param resources 图标
     * @return
     */
    public TipLinearLayout setImage(int resources) {
        if (resources != -1) {
            this.imageView.setImageResource(resources);
        }
        return this;
    }

    /**
     * 设置提示的图标
     *
     * @param image 图标
     * @return
     */
    public TipLinearLayout setImage(Bitmap image) {
        if (image != null) {
            this.imageView.setImageBitmap(image);
        }
        return this;
    }

    /**
     * 设置提示时间
     *
     * @param showTimer 时间
     * @return
     */
    public TipLinearLayout setShowTimer(long showTimer) {
        if (showTimer < 0) {
            showTimer = 3000;
        }
        this.showTimer = showTimer;
        return this;
    }

    /**
     * 设置背景
     *
     * @param bgcolor 背景颜色
     * @return
     */
    public TipLinearLayout setBgcolor(int bgcolor) {
        if (bgcolor != -1) {
            setBackgroundColor(bgcolor);
        }
        return this;
    }

    /**
     * 设置背景
     *
     * @param bgId 背景颜色id
     * @return
     */
    public TipLinearLayout setBgResourceColor(int bgId) {
        if (bgId != -1) {
            setBackgroundResource(bgId);
        }
        return this;
    }

    /**
     * 布局完全显示完成调用方法
     */
    private void onShow() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getHideAnimation().playOn(TipLinearLayout.this);
            }
        }, showTimer);
        if (listner != null) {
            listner.onShow();
        }
    }

    /**
     * 一次完整动画执行完成调用方法
     */
    private void onHide() {
        if (listner != null) {
            listner.onHide();
        }
    }

    private BaseAnimatorSet getShowAnimation() {
        BaseAnimatorSet animatorSet = new BaseAnimatorSet() {
            @Override
            public void setAnimation(View view) {
                DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
                animatorSet.playTogether(ObjectAnimator.ofFloat(view, "translationY", -250 * dm.density, 0));
            }
        };
        animatorSet.listener(animatorSetListener);
        return animatorSet;
    }

    private BaseAnimatorSet getHideAnimation() {
        BaseAnimatorSet dismissSet = new BaseAnimatorSet() {
            @Override
            public void setAnimation(View view) {
                DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
                animatorSet.playTogether(ObjectAnimator.ofFloat(view, "translationY", 0, -250 * dm.density));
            }
        };
        dismissSet.listener(dismissSetListener);
        return dismissSet;
    }

    /**
     * 设置回调接口
     *
     * @param listner 接口
     */
    public void setListner(ShowHideListner listner) {
        this.listner = listner;
    }

    /**
     * 显示提示
     */
    public void showTipMessage() {
//        isStop = false;
        if (getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
        }
        //启动提示
        getShowAnimation().playOn(this);
    }

    public interface ShowHideListner {
        void onShow();

        void onHide();
    }

    /**
     * 将TipLinearLayout添加到Activity顶部
     *
     * @param activity 上下文
     * @return TipLinearLayout
     */
    public static TipLinearLayout getViewTop(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return null;
        }
        TipLinearLayout layout = new TipLinearLayout(activity);
        layout.setId(R.id.ll_tip);
        layout.activity = activity;
        return layout;
    }

}
