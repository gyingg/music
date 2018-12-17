package com.future_melody.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.future_melody.R;
import com.future_melody.common.SPconst;
import com.future_melody.net.HttpUtil;
import com.future_melody.net.api.FutrueApis;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.UmengUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.lzx.musiclibrary.utils.LogUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Audhor: WZL
 * Time:  2018/1/18  17:42
 * Notes:
 */

public abstract class BaseFragment extends Fragment {
    protected BaseActivity mActivity;
    public FutrueApis apis = HttpUtil.getPHApis();
    protected CompositeDisposable mCompositeDisposable;
    private View view;
    protected ImmersionBar mImmersionBar;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        view = LayoutInflater.from(mActivity).inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view, savedInstanceState);
        initData();
//        ImmersionBar.with(this).statusBarColor(R.color.white).fitsSystemWindows(true).init();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.e("onDestroyView", "Base这里");
        unbinder.unbind();
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar(int color, boolean isBlack) {
        mImmersionBar = ImmersionBar.with(this);
        if (isBlack) {
            mImmersionBar.statusBarDarkFont(true);
            mImmersionBar.statusBarColor(color).fitsSystemWindows(true).init();
        } else {
            mImmersionBar.statusBarColor(color).fitsSystemWindows(true).init();
        }
    }

    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    public String userId() {
        return SPUtils.getInstance().getString(SPconst.USER_ID);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mImmersionBar != null)
            mImmersionBar.init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View view, Bundle savedInstanceState);

    protected abstract void initData();

    public void toast(String s) {
        Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
    }

    public void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    public void setTitle(String title) {
        TextView titleView = (TextView) view.findViewById(R.id.ph_title_name);
        if (titleView != null) {
            titleView.setText(title);
        }
    }

    public void setTitleLayoutColor(Activity activity, int color) {
        RelativeLayout layout_title = view.findViewById(R.id.layout_title);
        if (layout_title != null) {
            layout_title.setBackgroundColor(activity.getResources().getColor(color));
        }
    }

    public void setTitleColor(int color) {
        TextView titleView = (TextView) view.findViewById(R.id.ph_title_name);
        if (titleView != null) {
            titleView.setTextColor(getResources().getColor(color));
        }
    }

    //状态栏设置颜色
    public void setBarColor(int color, int navigationBarColor) {
        ImmersionBar.with(this)
                .navigationBarColor(navigationBarColor)
                .statusBarColor(color).fitsSystemWindows(true).init();
    }

    public void setBarDarkFont() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    public boolean isLogin() {
        return SPUtils.getInstance().getBoolean(SPconst.ISlogin, false);
    }

    private AlertDialog alertDialog;

    public void showLoadingDialog() {
        alertDialog = new AlertDialog.Builder(getActivity()).create();
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

}
