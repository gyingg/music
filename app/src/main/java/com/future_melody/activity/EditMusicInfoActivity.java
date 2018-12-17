package com.future_melody.activity;

import android.Manifest;
import android.arch.lifecycle.BuildConfig;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bwie.uploadpicture.ClipImageActivity;
import com.bwie.uploadpicture.utils.FileUtil;
import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.CommonConst;
import com.future_melody.common.SPconst;
import com.future_melody.net.CommonURL;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.api.FutrueApis;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.EditMusicInfo;
import com.future_melody.net.request.SetUserImage;
import com.future_melody.net.request.UploadMusicRequestBody;
import com.future_melody.net.respone.EditMusicInfoRespone;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.ThreadUtil;
import com.future_melody.utils.TipLinearUtil;
import com.future_melody.widget.UploadMusicDialog;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
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
 * Date：2018/5/10 12
 * Notes: 编辑音乐信息
 */
public class EditMusicInfoActivity extends BaseActivity {
    @BindView(R.id.copyright_statement)
    TextView copyrightStatement;
    private String TAG = "EditMusicInfoActivity";
    @BindView(R.id.music_imgage)
    ImageView musicImgage;
    @BindView(R.id.et_edit_music_name)
    EditText etEditMusicName;
    @BindView(R.id.et_edit_music_singer)
    EditText etEditMusicSinger;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.ph_title_right_img)
    ImageView phTitleRightImg;
    // 1: qq, 2: weixin
    private int type = 2;
    private Bitmap bitMap;
    //调用照相机返回图片文件
    private File tempFile;
    private String imgUrl;
    private String mufile;
    private String musicUrl;
    private String musicName;
    private String themeID;
    private String song;
    private String singer;
    private UploadMusicDialog dialog;
    private int Duration;
    //    private UploadMusicDialog dialog;

    public void sendMessage(int what, long current, long total, String msg) {
        Message message = Message.obtain();
        message.what = what;
        message.arg1 = (int) current;
        message.arg2 = (int) total;
        message.obj = msg;
        handler.sendMessage(message);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //上传失败
                    toast("上传失败");
                    dialog.dismiss();
                    break;
                case 2:
                    //上传成功
                    dialog.dismiss();
                    break;
                case 3:
                    //上传进度唉
//                    dialog = new UploadMusicDialog(mActivity);
                    int current = msg.arg1;
                    int total = msg.arg2;
                    dialog.setMsg(current + "%");
                    dialog.setProgressMax((int) total);
                    dialog.setProgress((int) current);
                    break;
            }
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_edit_music_info;
    }

    @Override
    protected void initView() {
        setBlackBackble();
        setTitle("编辑音乐详情");
        setTitleColor(R.color.color_333333);
        setBarColor(R.color.white, true);
        setTitleLayoutColor(mActivity, R.color.white);
        phTitleRightImg.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        musicUrl = intent.getStringExtra("url");
        song = intent.getStringExtra("song");
        singer = intent.getStringExtra("singer");
        Duration = intent.getIntExtra("Duration" ,-1);
        if (song.indexOf(".mp3") != -1) {
            song = song.replace(".mp3", "");
            etEditMusicName.setText(song + "");
        } else {
            etEditMusicName.setText(song + "");
        }
        etEditMusicSinger.setText(singer + "");
        etEditMusicName.setSelection(song.length());//将光标移至文字末尾
        etEditMusicSinger.setSelection(singer.length());//将光标移至文字末尾
        InputFilter[] emoji = {CommonUtils.enmoji(mActivity)};
        etEditMusicName.setFilters(emoji);
        etEditMusicSinger.setFilters(emoji);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case CommonConst.REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                    System.out.println("调用系统相机返回" + Uri.fromFile(tempFile));
                }
                break;
            case CommonConst.REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
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
                    musicImgage.setImageBitmap(bitMap);

                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......
                    if (userId() != null) {
                        uploadImg(new File(cropImagePath));
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, "失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    String result = response.body().string();
                    Gson gson = new Gson();
                    SetUserImage setUserImage = gson.fromJson(result, SetUserImage.class);
                    mufile = setUserImage.getMufile();
                    imgUrl = mufile;
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
        ;
        try {
            Glide.with(mActivity).load(mufile).into(musicImgage);
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //编辑音乐信息
    private void editMusicInfo(String userId, String music_path, String music_picture, String music_name, String singer_name, String biaoqian_id, String specialid ,int duration) {
        addSubscribe(apis.editmusic(new EditMusicInfo(userId, music_path, music_picture, music_name, singer_name, biaoqian_id, specialid ,duration))
                .compose(RxHttpUtil.<FutureHttpResponse<EditMusicInfoRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<EditMusicInfoRespone>handleResult())
                .subscribeWith(new HttpSubscriber<EditMusicInfoRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(EditMusicInfoRespone getUserInfoRespone) {
                        if (TextUtils.isEmpty(getUserInfoRespone.getSpecialid()) || getUserInfoRespone.getSpecialid() == null) {

                        } else {
                            SPUtils.getInstance().put(SPconst.THEMEID, getUserInfoRespone.getSpecialid());
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                        Intent intent = new Intent();
                        intent.putExtra(CommonConst.MUSICNAME, etEditMusicName.getText().toString());
                        setResult(CommonConst.MUSIC_CODE, intent);
                        finish();

                    }
                }));
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
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @OnClick({R.id.music_imgage, R.id.btn_send, R.id.copyright_statement})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.music_imgage:
                //相机，相册调用
                hideSoftInputView();
                uploadHeadImage();
                break;
            case R.id.btn_send:
                uploan(musicUrl);
                break;
            case R.id.copyright_statement:
                intent = new Intent(this, CopyrightStatementActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void isEmpty(String musicUrl) {
        if (TextUtils.isEmpty(etEditMusicName.getText().toString())) {
            TipLinearUtil.create(mActivity).showTipMessage("请输入歌曲名称");
        } else if (TextUtils.isEmpty(etEditMusicSinger.getText().toString())) {
            TipLinearUtil.create(mActivity).showTipMessage("请输入歌手名字");
        } else if (TextUtils.isEmpty(imgUrl)) {
            TipLinearUtil.create(mActivity).showTipMessage("请上传封面");
        } else {
            LogUtil.e("imgurl", imgUrl + "");
            themeID = SPUtils.getInstance().getString(SPconst.THEMEID);
            editMusicInfo(userId(), musicUrl, imgUrl, etEditMusicName.getText().toString().trim(), etEditMusicSinger.getText().toString().trim(), "", themeID ,Duration);
        }
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

    private void uploan(String pacht) {
        if (dialog == null) {
            dialog = new UploadMusicDialog(mActivity);
        }
        dialog.setCancelButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                return;
            }
        });
        final String url = FutrueApis.HOST + CommonURL.UPLOANMUSIC;
        File file = new File(pacht);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("mufile", file.getName(),
                RequestBody.create(MediaType.parse("mufile"), file));
        Request.Builder request = new Request.Builder().url(url)
                .post(new UploadMusicRequestBody(builder.build()) {
                    @Override
                    public void loading(long current, long total, boolean done) {
                        LogUtil.e("进度", "current:" + current + ";total:" + total);
                        if (!done) {
                            sendMessage(3, current, total, "");
                        }
                    }
                });

        final OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                //设置超时
                .connectTimeout(250, TimeUnit.SECONDS)
                .readTimeout(250, TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Log.e(TAG, "uploadMultiFile() e=" + e);
                try {
                    if (e.getCause().equals(SocketTimeoutException.class)) {
                        okHttpClient.newCall(call.request()).enqueue(this);
                    }
                    sendMessage(1, 0, 0, request.toString());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() || response == null || response.body() == null) {
                    sendMessage(1, 0, 0, "");
                } else {
                    //返回值处理
                    sendMessage(2, 0, 0, "");
                    try {
                        String result = response.body().string();
                        Gson gson = new Gson();
                        SetUserImage setUserImage = gson.fromJson(result, SetUserImage.class);
                        String url = setUserImage.getMufile();
                        isEmpty(url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
