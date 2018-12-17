package com.future_melody.activity;

import android.Manifest;
import android.arch.lifecycle.BuildConfig;
import android.content.Intent;
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
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bwie.uploadpicture.ClipImageActivity;
import com.bwie.uploadpicture.utils.FileUtil;
import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.CommonConst;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.CommonURL;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.api.FutrueApis;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.SetUserImage;
import com.future_melody.net.request.ShowUserRequest;
import com.future_melody.net.request.UpdataUserRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.ShowUserRespone;
import com.future_melody.net.respone.UpdataUserResponse;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.InputManager;
import com.google.gson.Gson;
import com.lzx.musiclibrary.manager.MusicManager;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
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
 * 个人资料
 */
public class PersonaalDataActivity extends BaseActivity implements View.OnClickListener {


    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;

    //调用照相机返回图片文件
    private File tempFile;
    // 1: qq, 2: weixin
    private int type = 1;
    private Bitmap bitMap;
    private CircleImageView icon_circle;
    private TextView tv_into;
    private ImageView ph_title_right_img;
    private TextView updata_nickname;
    private EditText et_nickname;
    private Button but_baocun;
    private String mufile;
    private String isShowStar;
    private RadioButton man;
    private RadioButton woman;
    private RadioGroup redio_g;
    private TextView xiao_starrysky;
    private int mSex;
    private String isShow;
    private String starrysky_id;
    private ImageView back;
    private Animation animation;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_personaal_data;
    }

    @Override
    protected void initView() {
        icon_circle = findViewById(R.id.icon_circle);
        tv_into = findViewById(R.id.tv_into);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        updata_nickname = findViewById(R.id.updata_nickname);
        et_nickname = findViewById(R.id.et_nickname);
        but_baocun = findViewById(R.id.but_baocun);
        man = findViewById(R.id.man);
        woman = findViewById(R.id.woman);
        redio_g = findViewById(R.id.redio_g);
        xiao_starrysky = findViewById(R.id.xiao_starrysky);
        back = findViewById(R.id.back);
    }

    @Override
    protected void initData() {
        back.setOnClickListener(this);
        icon_circle.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
        updata_nickname.setOnClickListener(this);
        but_baocun.setOnClickListener(this);
        tv_into.setOnClickListener(this);
        showuser(userId() + "");
        redio_g.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.man:
                        mSex = 1;
                        break;
                    case R.id.woman:
                        mSex = 2;
                        break;
                }
            }
        });
        initAnim();
        InputFilter[] emoji = {CommonUtils.enmoji(mActivity)};
        et_nickname.setFilters(emoji);
    }


    private void showuser(final String userid) {
        addSubscribe(apis.showuser(new ShowUserRequest(userid))
                .compose(RxHttpUtil.<FutureHttpResponse<ShowUserRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ShowUserRespone>handleResult())
                .subscribeWith(new HttpSubscriber<ShowUserRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(ShowUserRespone ShowUserRespone) {
                        isShow = ShowUserRespone.getPlanet_name();
                        starrysky_id = ShowUserRespone.getStarrysky_id();
                        if (TextUtils.isEmpty(isShow) || isShow == null) {
                            tv_into.setText("立即加入");
                        } else {
                            tv_into.setText(ShowUserRespone.getPlanet_name());
                        }
                        RequestOptions RequestOption = new RequestOptions();
                        RequestOption.placeholder(R.mipmap.moren);
                        Glide.with(mActivity)
                                .load(ShowUserRespone.getHead_portrait())
                                .apply(RequestOption)
                                .into(icon_circle);
                        et_nickname.setText(ShowUserRespone.getNickname());
                        if (TextUtils.isEmpty(ShowUserRespone.getStarrysky_name()) || ShowUserRespone.getStarrysky_name() == null) {
                            xiao_starrysky.setText(ShowUserRespone.getAsteroid_name());
                        } else {
                            xiao_starrysky.setText("—");
                        }
                        if (ShowUserRespone.getSex() == 1) {
                            man.setChecked(true);
                            woman.setChecked(false);
                        } else if (ShowUserRespone.getSex() == 2) {
                            man.setChecked(false);
                            woman.setChecked(true);
                        } else {
                            man.setChecked(false);
                            woman.setChecked(false);
                        }

//                        if (TextUtils.isEmpty(ShowUserRespone.getPlanet_name()) || ShowUserRespone.getPlanet_name() == null) {
//                            tv_into.setText("立即加入");
//                            tv_into.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            });
//                        } else {
//                            tv_into.setText(ShowUserRespone.getPlanet_name());
//                        }


                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.icon_circle:
                //相机，相册调用
                uploadHeadImage();
                break;
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
            case R.id.updata_nickname:
                InputManager.getInstances(mActivity).totleShowSoftInput();
                et_nickname.setEnabled(true);
                et_nickname.setText("");
                et_nickname.setFocusable(true);
                et_nickname.setCursorVisible(true);
                et_nickname.setFocusableInTouchMode(true);
                et_nickname.setFilters(new InputFilter.LengthFilter[]{new InputFilter.LengthFilter(7)});
                et_nickname.requestFocus();
                break;
            case R.id.but_baocun:
                String nickname = et_nickname.getText().toString().trim();
                String starrysky = xiao_starrysky.getText().toString().trim();
                baocun(nickname, mufile, starrysky, mSex, userId() + "");
                break;
            case R.id.tv_into:
                if (TextUtils.isEmpty(isShow) || isShow == null) {
                    intent = new Intent(mActivity, MainNewActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(mActivity, StarDetailsActivity.class);
                    intent.putExtra("planetId", starrysky_id);
                    startActivity(intent);
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void baocun(final String nickname, String head_portrait, String asteroid_name, int sex, String userid) {
        addSubscribe(apis.updatauser(new UpdataUserRequest(nickname, head_portrait, asteroid_name, sex, userid))
                .compose(RxHttpUtil.<FutureHttpResponse<UpdataUserResponse>>rxSchedulerHelper())
                .compose(RxHttpUtil.<UpdataUserResponse>handleResult())
                .subscribeWith(new HttpSubscriber<UpdataUserResponse>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(UpdataUserResponse UpdataUserResponse) {

                    }

                    @Override
                    public void onComplete() {
                        finish();
                    }
                }));
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
                    Toast.makeText(mActivity, "请开启存储权限", Toast.LENGTH_SHORT).show();
                    //申请READ_EXTERNAL_STORAGE权限
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
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCamera();
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
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
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
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
        startActivityForResult(intent, REQUEST_CAPTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                    System.out.println("调用系统相机返回" + Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                    System.out.println("调用系统相册返回" + uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
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
                    icon_circle.setImageBitmap(bitMap);

                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......
                    if (userId() != null) {
                        uploadImg(new File(cropImagePath));
                    }
                }
                break;
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
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
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
                try {
                    Toast.makeText(mActivity, "失败", Toast.LENGTH_SHORT).show();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    String result = response.body().string();
                    Gson gson = new Gson();
                    SetUserImage setUserImage = gson.fromJson(result, SetUserImage.class);
                    mufile = setUserImage.getMufile();
                    Glide.with(mActivity).load(mufile).into(icon_circle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAnmi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MusicManager.isPlaying()) {
            startAnmi();
        } else {
            stoptAnmi();
        }
    }

    private void initAnim() {
        animation = AnimationUtils.loadAnimation(this, R.anim.player);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
    }

    private void startAnmi() {
        ph_title_right_img.startAnimation(animation);
    }

    private void stoptAnmi() {
        ph_title_right_img.clearAnimation();
    }

    private void destroyAnmi() {
        animation.cancel();
        animation = null;
    }
}
