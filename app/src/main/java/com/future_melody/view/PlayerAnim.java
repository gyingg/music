package com.future_melody.view;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.future_melody.utils.SPUtils;

/**
 * Author WZL
 * Date：2018/6/11 42
 * Notes:
 */
public class PlayerAnim {
    private ImageView view;
    private ObjectAnimator mCoverAnim;
    private long currentPlayTime = 0;

    public PlayerAnim(ImageView view) {
        this.view = view;
    }

    public static PlayerAnim getInstance(ImageView view) {
        PlayerAnim anim = new PlayerAnim(view);
        return anim;
    }

    /**
     * 转圈动画
     */
    public void initMusicCoverAnim() {
        mCoverAnim = ObjectAnimator.ofFloat(view, "rotation", 0, 359);
        mCoverAnim.setDuration(20000);
        mCoverAnim.setInterpolator(new LinearInterpolator());
        mCoverAnim.setRepeatCount(Integer.MAX_VALUE);
    }

    /**
     * 开始转圈
     */
    public void startCoverAnim() {
        mCoverAnim.start();
        mCoverAnim.setCurrentPlayTime(currentPlayTime);
    }

    /**
     * 停止转圈
     */
    public void pauseCoverAnim() {
        currentPlayTime = mCoverAnim.getCurrentPlayTime();
        mCoverAnim.cancel();
    }

    public void resetCoverAnim() {
        pauseCoverAnim();
        view.setRotation(0);
    }

    public void destroyAnim() {
        resetCoverAnim();
        mCoverAnim = null;
    }
}
