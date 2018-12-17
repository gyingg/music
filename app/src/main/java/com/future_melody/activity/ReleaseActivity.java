package com.future_melody.activity;

import android.Manifest;
import android.arch.lifecycle.BuildConfig;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bwie.uploadpicture.ClipImageActivity;
import com.bwie.uploadpicture.utils.FileUtil;
import com.future_melody.R;
import com.future_melody.adapter.ReleaseMusicAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.CommonConst;
import com.future_melody.common.SPconst;
import com.future_melody.net.CommonURL;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.api.FutrueApis;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.SendTheme;
import com.future_melody.net.request.SetUserImage;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.SendThemeRespone;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.MyListView;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.ThreadUtil;
import com.future_melody.utils.TipLinearUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.bwie.uploadpicture.utils.FileUtil.getRealFilePathFromUri;

/**
 * Author WZL
 * Date：2018/5/9 00
 * Notes: 发布
 */
public class ReleaseActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.text_et_num)
    TextView textEtNum;
    @BindView(R.id.text_isadd_img)
    TextView textIsaddImg;
    @BindView(R.id.btn_layout_music)
    RelativeLayout btnLayoutMusic;
    @BindView(R.id.text_isadd_music)
    TextView textIsaddMusic;
    @BindView(R.id.btn_layout_Image)
    RelativeLayout btnLayoutImage;
    @BindView(R.id.btn_send_theme)
    Button btnSendTheme;
    @BindView(R.id.text_title_left)
    TextView textTitleLeft;
    @BindView(R.id.theme_img)
    ImageView themeImg;
    @BindView(R.id.text_noisadd_img)
    TextView textNoisaddImg;
    @BindView(R.id.localmusic_listview)
    MyListView localmusicListview;
    private CharSequence mCharSequence;
    private int ettStart;
    private int ettEnd;
    private int maxNum = 200;
    private File tempFile;
    // 1: qq, 2: weixin
    private int type = 1;
    private Bitmap bitMap;
    //调用照相机返回图片文件
    private String mufile;
    private ReleaseMusicAdapter adapter;
    private List<String> strings;
    private String themeId;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_release;
    }

    @Override
    protected void initView() {
        textIsaddMusic.setText("未添加");
        etContent.addTextChangedListener(this);
        themeImg.setVisibility(View.GONE);
        textNoisaddImg.setVisibility(View.GONE);
        textNoisaddImg.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        setBarColor(R.color.white, true);
        setTitle("发布");
        setTitleLeft("取消");
        setTitleColor(R.color.color_333333);
        setTitleLeftColor(R.color.color_666666);
        setTitleLayoutColor(mActivity, R.color.white);
        strings = new ArrayList<>();
        InputFilter[] emoji = {CommonUtils.enmoji(mActivity)};
        etTitle.setFilters(emoji);
        etContent.setFilters(emoji);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("onDestroy", "onDestroy");
        SPUtils.getInstance().remove(SPconst.THEMEID);
    }

    @OnClick(value = {R.id.btn_layout_Image, R.id.btn_layout_music, R.id.btn_send_theme, R.id.text_title_left})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.text_title_left:
                finish();
                break;
            case R.id.btn_layout_Image:
                //相机，相册调用
                uploadHeadImage();
                break;
            case R.id.btn_layout_music:
                if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            CommonConst.READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    intent = new Intent(mActivity, LocalMusicActivity.class);
                    startActivityForResult(intent, CommonConst.MUSIC_CODE);
                }
                break;
            case R.id.btn_send_theme:
                themeId = SPUtils.getInstance().getString(SPconst.THEMEID);
                if (TextUtils.isEmpty(etTitle.getText().toString())) {
                    TipLinearUtil.create(mActivity).showTipMessage("标题不能为空");
                } else if (TextUtils.isEmpty(etContent.getText().toString())) {
                    TipLinearUtil.create(mActivity).showTipMessage("内容不能为空");
                } else if (TextUtils.isEmpty(mufile)) {
                    TipLinearUtil.create(mActivity).showTipMessage("封面不能为空");
                } else if (TextUtils.isEmpty(themeId)) {
                    TipLinearUtil.create(mActivity).showTipMessage("请选择音乐");
                } else {
                    sendTheme(etTitle.getText().toString(), mufile, etContent.getText().toString(), userId(), themeId);
                }
                break;
        }
    }


    private void sendTheme(String special_title, String special_picture, String special_describe, String userid, String specialid) {
        addSubscribe(apis.sendTheme(new SendTheme(special_title, special_picture, special_describe, userid, specialid))
                .compose(RxHttpUtil.<FutureHttpResponse<SendThemeRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<SendThemeRespone>handleResult())
                .subscribeWith(new HttpSubscriber<SendThemeRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(SendThemeRespone sendThemeRespone) {
                    }

                    @Override
                    public void onComplete() {
                        TipLinearUtil.create(mActivity).showTipMessage("发布成功");
                        SPUtils.getInstance().remove(SPconst.THEMEID);
                        finish();
                    }
                }));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mCharSequence = charSequence;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        ettStart = etContent.getSelectionStart();
        ettEnd = etContent.getSelectionEnd();
        textEtNum.setText("您还可以输入" + (maxNum - mCharSequence.length()) + "个字");
        if (mCharSequence.length() > maxNum) {
            toast("最多可输出200个字");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        String s = intent.getStringExtra(CommonConst.MUSICNAME);
        LogUtil.e("requestCode:", requestCode + "");
        LogUtil.e("resultCode:", resultCode + "");
        switch (requestCode) {
            case CommonConst.REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
//                    gotoClipActivity(Uri.fromFile(tempFile));
                    System.out.println("调用系统相机返回" + Uri.fromFile(tempFile));
                    String cropImagePath = getRealFilePathFromUri(getApplicationContext(), Uri.fromFile(tempFile));
                    System.out.println("地址:" + cropImagePath);
                    bitMap = BitmapFactory.decodeFile(cropImagePath);

                    /**
                     * 头像的icon
                     */
                    if (bitMap != null) {
                        themeImg.setVisibility(View.VISIBLE);
                        themeImg.setImageBitmap(bitMap);
                        textNoisaddImg.setVisibility(View.VISIBLE);
                        textIsaddImg.setVisibility(View.GONE);
                    } else {
                        themeImg.setVisibility(View.GONE);
                        textNoisaddImg.setVisibility(View.GONE);
                        textIsaddImg.setVisibility(View.VISIBLE);
                    }

                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......
                    if (userId() != null) {
                        uploadImg(new File(cropImagePath));
                    }
                }
                break;
            case CommonConst.REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
//                    gotoClipActivity(uri);
                    String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    System.out.println("地址:" + cropImagePath);
                    bitMap = BitmapFactory.decodeFile(cropImagePath);

                    /**
                     * 头像的icon
                     */
                    if (bitMap != null) {
                        themeImg.setVisibility(View.VISIBLE);
                        themeImg.setImageBitmap(bitMap);
                        textNoisaddImg.setVisibility(View.VISIBLE);
                        textIsaddImg.setVisibility(View.GONE);
                    } else {
                        themeImg.setVisibility(View.GONE);
                        textNoisaddImg.setVisibility(View.GONE);
                        textIsaddImg.setVisibility(View.VISIBLE);
                    }

                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......
                    if (userId() != null) {
                        uploadImg(new File(cropImagePath));
                    }
                    System.out.println("调用系统相册返回" + uri);
                }
                break;
            case CommonConst.REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    System.out.println("剪切图片返回" + uri);
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    System.out.println("地址:" + cropImagePath);
                    bitMap = BitmapFactory.decodeFile(cropImagePath);

                    /**
                     * 头像的icon
                     */
                    if (bitMap != null) {
                        themeImg.setVisibility(View.VISIBLE);
                        themeImg.setImageBitmap(bitMap);
                        textNoisaddImg.setVisibility(View.VISIBLE);
                        textIsaddImg.setVisibility(View.GONE);
                    } else {
                        themeImg.setVisibility(View.GONE);
                        textNoisaddImg.setVisibility(View.GONE);
                        textIsaddImg.setVisibility(View.VISIBLE);
                    }

                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......
                    if (userId() != null) {
                        uploadImg(new File(cropImagePath));
                    }
                }
                break;
            //上传音乐
            case CommonConst.MUSIC_CODE:
                if (resultCode == 3) {
                    if (intent != null) {
                        strings.add(intent.getStringExtra(CommonConst.MUSICNAME));
                        if (localmusicListview != null) {
                            textIsaddMusic.setText("继续添加");
                        } else {
                            textIsaddMusic.setText("未添加");
                        }
                        adapter = new ReleaseMusicAdapter(mActivity, strings);
                        localmusicListview.setAdapter(adapter);
                    }
                }
                break;
        }
    }

    private void uploadImg(File file) {
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("mufile", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
        MultipartBody build = builder.build();
        /**
         *
         * 上传头像接口
         */
        final Request request = new Request.Builder().post(build).url(FutrueApis.HOST + CommonURL.SETUSERIMG).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Toast.makeText(mActivity, "失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    String result = response.body().string();
                    Gson gson = new Gson();
                    SetUserImage setUserImage = gson.fromJson(result, SetUserImage.class);
                    mufile = setUserImage.getMufile();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            setImg();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setImg() {
        ThreadUtil.assertBackgroundThread();
        try {
            Glide.with(mActivity).load(mufile).into(themeImg);
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开截图界面
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, CommonConst.REQUEST_CROP_PHOTO);
    }

    /**
     * 上传头像（需要在Manifest配置,导入Uploadpicture的Module） 权限3个 可读,可写,相机
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = view.findViewById(R.id.btn_camera);
        TextView btnPhoto = view.findViewById(R.id.btn_photo);
        TextView btnCancel = view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        //View parent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (CommonUtils.isCameraCanUse()) {
                    if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        //申请WRITE_EXTERNAL_STORAGE权限
                        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA},
                                CommonConst.REQUEST_CAPTURE);
                    } else {
                        gotoCamera();
                        popupWindow.dismiss();
                    }
                } else {
                    CommonUtils.getAppDetailSettingIntent(mActivity);
//                    toast("您未打开摄像头权限");
                }
                //跳转到调用系统相机
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    Toast.makeText(mActivity, "请开启存储权限", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            CommonConst.READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    gotoPhoto();
                    popupWindow.dismiss();
                }
                //跳转到相册
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 外部存储权限申请返回
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CommonConst.WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCamera();
            }
        } else if (requestCode == CommonConst.READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoPhoto();
            }
        }
    }

    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Log.d("evan", "*****************打开图库********************");
        //跳转到调用系统图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), CommonConst.REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCamera() {
        Log.d("evan", "*****************打开相机********************");
        //创建拍照存储的图片文件
        tempFile = new File(FileUtil.checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"), System.currentTimeMillis() + ".jpg");

        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //设置7.0中共享文件，分享路径定义在xml/file_paths.xml
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, CommonConst.REQUEST_CAPTURE);
    }

}
