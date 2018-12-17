package com.future_melody.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.future_melody.R;
import com.future_melody.common.SPconst;
import com.future_melody.interfaces.ActivityCollector;
import com.future_melody.net.HttpUtil;
import com.future_melody.net.api.FutrueApis;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.KeyBoardUtil;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.UmengUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.lzx.musiclibrary.constans.PlayMode;
import com.lzx.musiclibrary.manager.MusicManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Audhor: WZL
 * Time:  2018/1/18  11:47
 * Notes:
 */

public abstract class BaseActivity extends AppCompatActivity {
    public BaseActivity mActivity;
    protected CompositeDisposable mCompositeDisposable;
    public FutrueApis apis = HttpUtil.getPHApis();
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(this.getContentViewId());
        ImmersionBar.with(this).init();
        ButterKnife.bind(mActivity);
//        Aria.download(this).register();
        initView();
        initData();
        LogUtil.e("BaseActivity", "BaseActivity+1");
        ActivityCollector.addActivity(this);
        playerType();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        ActivityCollector.removeActivity(this);
        UMShareAPI.get(this).release();
        LogUtil.e("走了啊", "BaseActivity+1");
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;

    }

    protected abstract int getContentViewId();

    protected abstract void initView();

    protected abstract void initData();

    public void toast(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    public void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    public void setTitle(String title) {
        TextView titleView = (TextView) findViewById(R.id.ph_title_name);
        if (titleView != null) {
            titleView.setText(title);
        }
    }

    public void setTitleLeft(String title) {
        TextView titleView = (TextView) findViewById(R.id.text_title_left);
        if (titleView != null) {
            titleView.setVisibility(View.VISIBLE);
            titleView.setText(title);
        }
    }

    public void setBackble(boolean backable) {
        ImageView btn_back = (ImageView) findViewById(R.id.ph_title_back);
        if (btn_back != null && backable) {
            btn_back.setVisibility(View.VISIBLE);
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KeyBoardUtil.hideKeyBoard(mActivity);
                    finish();
                }
            });
        }
    }

    public void setBlackBackble() {
        ImageView btn_back = (ImageView) findViewById(R.id.ph_title_back);
        btn_back.setImageResource(R.mipmap.back_back);
        btn_back.setVisibility(View.VISIBLE);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtil.hideKeyBoard(mActivity);
                finish();
            }
        });
    }

    public void setTitleLayoutColor(Activity activity, int color) {
        RelativeLayout layout_title = findViewById(R.id.layout_title);
        if (layout_title != null) {
            layout_title.setBackgroundColor(activity.getResources().getColor(color));
        }
    }

    public void setTitleColor(int color) {
        TextView titleView = (TextView) findViewById(R.id.ph_title_name);
        if (titleView != null) {
            titleView.setTextColor(getResources().getColor(color));
        }
    }

    public void setTitleLeftColor(int color) {
        TextView titleView = (TextView) findViewById(R.id.text_title_left);
        if (titleView != null) {
            titleView.setTextColor(getResources().getColor(color));
        }
    }


    public boolean isLogin() {
        return SPUtils.getInstance().getBoolean(SPconst.ISlogin, false);
    }

    public String userId() {
        return SPUtils.getInstance().getString(SPconst.USER_ID);
    }

    /**
     * QQ与新浪分享
     *
     * @param requestCode
     * @param resultCode
     * @param data        注意onActivityResult不可在fragment中实现，如果在fragment中调用登录或分享，需要在fragment依赖的Activity中实现
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public void setBarColor(int color, boolean isBlack) {
        if (isBlack) {
            ImmersionBar.with(this)
                    .statusBarDarkFont(true)
                    .statusBarColor(color).fitsSystemWindows(true).init();
        } else {
            ImmersionBar.with(this)
                    .statusBarColor(color).fitsSystemWindows(true).init();
        }
    }

    public void setBarColor() {
        ImmersionBar
                .with(this)
                .init();
    }

    private AlertDialog alertDialog;

    public void showLoadingDialog() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        alertDialog.setCancelable(false);
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK)
                    return true;
                return false;
            }
        });
        alertDialog.show();
        alertDialog.setContentView(R.layout.loading_alert);
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public void dismissLoadingDialog() {
        if (null != alertDialog && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            if (CommonUtils.isFastDoubleClick()) {
//                return true;
//            }
//        }
        return super.dispatchTouchEvent(ev);
    }

    private void playerType() {
        int type = SPUtils.getInstance().getInt(SPconst.PLAYER_TYPE);
        switch (type) {
            case 1:
                //单曲
                MusicManager.get().setPlayMode(PlayMode.PLAY_IN_SINGLE_LOOP);
                break;
            case 2:
                //随机
                MusicManager.get().setPlayMode(PlayMode.PLAY_IN_RANDOM);
                break;
            case 3:
                //顺序
                MusicManager.get().setPlayMode(PlayMode.PLAY_IN_LIST_LOOP);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengUtils.onResumeToActivity(mActivity);
        LogUtil.e("Activity名字：", mActivity.getClass().getSimpleName() + "");
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtils.onPauseToActivity(mActivity);
    }


}
