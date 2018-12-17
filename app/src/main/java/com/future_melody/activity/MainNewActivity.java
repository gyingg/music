package com.future_melody.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.future_melody.R;
import com.future_melody.adapter.ViewPagerAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.CommonConst;
import com.future_melody.common.SPconst;
import com.future_melody.fragment.FindFragment;
import com.future_melody.fragment.HomePageFragment;
import com.future_melody.fragment.MainActivityFragment;
import com.future_melody.fragment.MineFragment;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.VersionRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.VersionRespone;
import com.future_melody.receiver.HeadListenReceiver;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.SPUtils;
import com.future_melody.widget.NewUserDialog;
import com.future_melody.widget.NoScrollViewPager;
import com.future_melody.widget.UpdataVersionDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author WZL
 * Date：2018/9/1 25
 * Notes:
 */
public class MainNewActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.viewpager_main)
    NoScrollViewPager viewpagerMain;
    @BindView(R.id.img_first)
    ImageView imgFirst;
    @BindView(R.id.text_first)
    TextView textFirst;
    @BindView(R.id.btn_bottom_first)
    LinearLayout btnBottomFirst;
    @BindView(R.id.img_find)
    ImageView imgFind;
    @BindView(R.id.text_find)
    TextView textFind;
    @BindView(R.id.btn_bottom_find)
    LinearLayout btnBottomFind;
    @BindView(R.id.img_second)
    ImageView imgSecond;
    @BindView(R.id.text_second)
    TextView textSecond;
    @BindView(R.id.btn_bottom_second)
    LinearLayout btnBottomSecond;
    @BindView(R.id.img_third)
    ImageView imgThird;
    @BindView(R.id.text_third)
    TextView textThird;
    @BindView(R.id.btn_bottom_third)
    LinearLayout btnBottomThird;
    @BindView(R.id.layout_bottom)
    LinearLayout layoutBottom;
    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;
    private HomePageFragment homePageFragment;
    // private LikeFragment likeFragment;
    private MainActivityFragment mainActivityFragment;
    private FindFragment findFragment;
    private MineFragment mineFragment;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragments = new ArrayList<>();
    private HeadListenReceiver receiver;
    private UpdataVersionDialog dialog;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main_new;
    }

    @Override
    protected void initView() {
        requestPermission();
        LogUtil.e("isFireMain", SPUtils.getInstance().getBoolean(SPconst.isFireMain) + "");
        ImmersionBar.with(this).statusBarColor(R.color.colorAccent).fitsSystemWindows(true).init();
        if (!SPUtils.getInstance().getBoolean(SPconst.isFireMain)) {
            NewUserDialog dialog = new NewUserDialog(mActivity);
            dialog.setMsg("点击喜欢的星球，加入！更多惊喜等着你！");
            dialog.setCancelButton(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    viewpagerMain.setCurrentItem(1, false);
                    NewUserDialog musicdialog = new NewUserDialog(mActivity);
                    musicdialog.setMsg("每天听完星歌都能得分贝奖励！");
                    musicdialog.setCancelButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            musicdialog.dismiss();
                            viewpagerMain.setCurrentItem(3, false);
                            NewUserDialog minedialog = new NewUserDialog(mActivity);
                            minedialog.setMsg("点击登录/注册开启你的星球之旅");
                            minedialog.setCancelButton(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    minedialog.dismiss();
                                    SPUtils.getInstance().put(SPconst.isFireMain, true);
                                }
                            });
                        }
                    });
                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("Main", "主页");
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("Main", "onResume");
        updateVersion();
        String isMine = "";
        Intent intent = getIntent();
        isMine = intent.getStringExtra("isMine");
        if (isMine != null) {
            if (isMine.equals("1")) {
                imgThird.setImageResource(R.mipmap.icon_main_third);
                textThird.setTextColor(mActivity.getResources().getColor(R.color.color_00AFFD));
                viewpagerMain.setCurrentItem(3, false);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initData() {

        homePageFragment = new HomePageFragment();
        //likeFragment = new LikeFragment();
        mainActivityFragment = new MainActivityFragment();
        mineFragment = new MineFragment();
        findFragment = new FindFragment();
        fragments.add(homePageFragment);
        fragments.add(findFragment);
        fragments.add(mainActivityFragment);
        fragments.add(mineFragment);
        viewpagerMain.addOnPageChangeListener(this);
        viewpagerMain.setOffscreenPageLimit(3);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewpagerMain.setAdapter(adapter);
        if (SPUtils.getInstance().getBoolean(SPconst.ISSHOWHOMEFRAGMENG, false)) {
            imgFind.setImageResource(R.mipmap.icon_main_find);
            textFind.setTextColor(mActivity.getResources().getColor(R.color.color_00AFFD));
            viewpagerMain.setCurrentItem(1, false);
        } else {
            imgFirst.setImageResource(R.mipmap.icon_main_first);
            textFirst.setTextColor(mActivity.getResources().getColor(R.color.color_00AFFD));
            viewpagerMain.setCurrentItem(0, false);
            layoutBottom.getBackground().setAlpha(10);
        }
        initMusic();
        SPUtils.getInstance().put(SPconst.ISSHOWHOMEFRAGMENG, true);
    }

    private void initMusic() {
        receiver = new HeadListenReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState);
    }

    private void updateData(int position) {
        switch (position) {
            case 0:
                layoutBottom.getBackground().setAlpha(10);
                imgFirst.setImageResource(R.mipmap.icon_main_first);
                textFirst.setTextColor(mActivity.getResources().getColor(R.color.color_00AFFD));
                imgFind.setImageResource(R.mipmap.icon_main_unfirst);
                imgSecond.setImageResource(R.mipmap.icon_main_unsecond);
                imgThird.setImageResource(R.mipmap.icon_main_unthird);
                textSecond.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                textThird.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                textFind.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                break;
            case 1:
                layoutBottom.getBackground().setAlpha(255);
                imgFind.setImageResource(R.mipmap.icon_main_find);
                textFind.setTextColor(mActivity.getResources().getColor(R.color.color_00AFFD));
                imgFirst.setImageResource(R.mipmap.icon_main_fir);
                imgThird.setImageResource(R.mipmap.icon_main_unthird);
                imgSecond.setImageResource(R.mipmap.icon_main_unsecond);
                textFirst.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                textThird.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                textSecond.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                break;
            case 2:
                layoutBottom.getBackground().setAlpha(255);
                imgSecond.setImageResource(R.mipmap.icon_main_second);
                textSecond.setTextColor(mActivity.getResources().getColor(R.color.color_00AFFD));
                imgFirst.setImageResource(R.mipmap.icon_main_fir);
                imgThird.setImageResource(R.mipmap.icon_main_unthird);
                imgFind.setImageResource(R.mipmap.icon_main_unfirst);
                textFirst.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                textThird.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                textFind.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                break;
            case 3:
                layoutBottom.getBackground().setAlpha(255);
                imgThird.setImageResource(R.mipmap.icon_main_third);
                textThird.setTextColor(mActivity.getResources().getColor(R.color.color_00AFFD));
                imgFirst.setImageResource(R.mipmap.icon_main_fir);
                imgSecond.setImageResource(R.mipmap.icon_main_unsecond);
                imgFind.setImageResource(R.mipmap.icon_main_unfirst);
                textFirst.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                textSecond.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                textFind.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                break;
        }
    }

    @OnClick({R.id.btn_bottom_first, R.id.btn_bottom_find, R.id.btn_bottom_second, R.id.btn_bottom_third})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_bottom_first:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewpagerMain.setCurrentItem(0, false);
                    }
                });
                break;
            case R.id.btn_bottom_find:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewpagerMain.setCurrentItem(1, false);
                    }
                });
                break;
            case R.id.btn_bottom_second:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewpagerMain.setCurrentItem(2, false);
                    }
                });
                break;
            case R.id.btn_bottom_third:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewpagerMain.setCurrentItem(3, false);
                    }
                });
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        switch (position) {
            case 0:
                updateData(0);
                break;
            case 1:
                updateData(1);
                break;
            case 2:
                updateData(2);
                break;
            case 3:
                updateData(3);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //6.0以上权限申请
    private void requestPermission() {
        RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity instance
        //权限申请
        rxPermissions
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.SYSTEM_ALERT_WINDOW
                )
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted
                    } else {
                        // At least one permission is denied
                    }
                });
    }

    private void updateVersion() {
        addSubscribe(apis.version(new VersionRequest(1))
                .compose(RxHttpUtil.<FutureHttpResponse<VersionRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<VersionRespone>handleResult())
                .subscribeWith(new HttpSubscriber<VersionRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(VersionRespone versionRespone) {
                        LogUtil.e("当前版本：", CommonUtils.getVersionName(mActivity));
                        LogUtil.e("服务器版本版本：", versionRespone.version_code);
                        if (versionRespone.version_code.equals(CommonUtils.getVersionName(mActivity))) {
                        } else {
                            if (dialog != null) {
                                dialog.show();
                            } else {
                                dialog = new UpdataVersionDialog(mActivity);
                                dialog.show();
                            }
                            dialog.setListener(mActivity);
                            dialog.setTextContext(versionRespone.content);
                            dialog.setTextVersion("未来声音:" + versionRespone.version_code);
                            dialog.setDetermineButton(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        Toast.makeText(mActivity, "请开启存储权限", Toast.LENGTH_SHORT).show();
                                        //申请READ_EXTERNAL_STORAGE权限
                                        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                                CommonConst.READ_EXTERNAL_STORAGE_REQUEST_CODE);
                                    } else {
                                        downFile(versionRespone.apkurl);
                                    }
//                                    dialog.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public void downFile(final String url) {
        showLoadingDialog();
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dismissLoadingDialog();
                //下载失败
                Toast.makeText(mActivity, "更新失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dismissLoadingDialog();
                InputStream is = null;//输入流
                FileOutputStream fos = null;//输出流
                try {
                    is = response.body().byteStream();//获取输入流
                    long total = response.body().contentLength();//获取文件大小
                    LogUtil.e("大小", total + "");
//                    view.setMax(total);//为progressDialog设置大小
                    if (is != null) {
                        Log.d("SettingPresenter", "onResponse: 不为空");
                        File file = new File(Environment.getExternalStorageDirectory(), "future_melody.apk");// 设置路径
                        fos = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int process = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fos.write(buf, 0, ch);
                            process += ch;
//                            view.downLoading(process);       //这里就是关键的实时更新进度了！
                            int finalProcess = process;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    NumberFormat numberFormat = NumberFormat.getInstance();
                                    numberFormat.setMaximumFractionDigits(0);
                                    String result = numberFormat.format((float) finalProcess / (float) response.body().contentLength() * 100);
                                    if (dialog != null) {
                                        LogUtil.e("result", result);
                                        dialog.setTextNum(result + "%");
                                        LogUtil.e("进度", result + "%");
                                    }
                                }
                            });

                        }

                    }
                    fos.flush();
                    // 下载完成
                    if (fos != null) {
                        fos.close();
                    }
                    dialog.dismiss();
                    CommonUtils.install(mActivity);
                } catch (Exception e) {
//                    view.downFial();
                    Log.d("SettingPresenter", e.toString());
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }
}
