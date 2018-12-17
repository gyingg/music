package com.future_melody.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.net.CommonURL;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.api.FutrueApis;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.GetMusicLeaderReqest;
import com.future_melody.net.request.JoinStarRequest;
import com.future_melody.net.request.ReplyCommentRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.GetMusicLeaderRespone;
import com.future_melody.net.respone.JoinStarRespone;
import com.future_melody.net.respone.ReplyCommentResponse;
import com.future_melody.utils.LogUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestActivity extends BaseActivity {
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    @BindView(R.id.btn4)
    Button btn4;
    @BindView(R.id.btn5)
    Button btn5;
    @BindView(R.id.btn6)
    Button btn6;
    @BindView(R.id.but7)
    Button but7;
    @BindView(R.id.text)
    TextView text;
    private String userId;
    private String TAG = "TestActivity";
    String picPath = Environment.getExternalStorageDirectory() + "/feedback.log";
    // File file = new File(Environment.getExternalStorageDirectory() + "/360Download", "纸短情长.wma");


    @Override
    protected int getContentViewId() {
        return R.layout.test;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    private void uploan() {
        final String url = FutrueApis.HOST + CommonURL.UPLOAN;
        File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
        RequestBody fileBody = RequestBody.create(MediaType.parse("mufile"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("mufile", "test.jpg", fileBody)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();


        final OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                //设置超时
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "uploadMultiFile() e=" + e);
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "uploadMultiFile() response=" + response.body().string());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.but7, R.id.text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn:
                downFile("https://tfm.oss-cn-beijing.aliyuncs.com/future-sound/text/2018-06-08/3612e195-3b52-4e4e-a97a-ec8977000328.mp3");
                break;
            case R.id.btn2:
                musicleader(userId, 1, 20);
                break;
            case R.id.btn3:
//                download();
                break;
            //回复评论  parentId有值,一级评论 "";
            case R.id.btn4:
                replycomment("评论", "3f5e3d13f8c6428b9b609e0c314b4021 ", " beda2213edb5414fa415eda81da46111", userId);
                break;
            //歌曲点赞
            case R.id.btn5:
                //  dotmuisc(0, "asdasdasdsad", "忘情水", " http://www.abc.com", "beda2213edb5414fa415eda81da46111", userId);
                break;
            //评论点赞
            case R.id.btn6:
                //  dotcomment("3f5e3d13f8c6428b9b609e0c314b4021", 1, "beda2213edb5414fa415eda81da46111", userId);
                break;
            case R.id.but7:
                share();
                break;
            case R.id.text:
//                loadNewVersionProgress("https://tfm.oss-cn-beijing.aliyuncs.com/future-sound/text/2018-06-08/fb1a046b-29cf-4d34-8bb8-9e24ad880855.mp3", text);
                break;
        }
    }

    private void musicleader(final String userId, int pageNum, int pageSize) {
        addSubscribe(apis.musicleader(new GetMusicLeaderReqest(userId, pageNum, pageSize, 1))
                .compose(RxHttpUtil.<FutureHttpResponse<List<GetMusicLeaderRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<GetMusicLeaderRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<GetMusicLeaderRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(List<GetMusicLeaderRespone> GetMusicLeaderRespone) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }


    /**
     * 分享
     */
    private void share() {
        //分享的图片
        UMImage thumb = new UMImage(this, R.mipmap.page1);
        //分享链接
        UMWeb web = new UMWeb("http://www.baidu.com");
        web.setTitle("百度");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription("sdfsfsfdsff");//描述
        new ShareAction(this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        toast("分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        toast("分享失败");
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        toast("分享失败");
                    }
                }).open();
    }

    //不要忘记重写分享成功后的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 回复评论
     *
     * @param commentContent
     * @param parentId
     * @param spcialId
     * @param userId
     */
    private void replycomment(final String commentContent, String parentId, String spcialId, String userId) {
        addSubscribe(apis.replycomment(new ReplyCommentRequest(commentContent, parentId, spcialId, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<ReplyCommentResponse>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ReplyCommentResponse>handleResult())
                .subscribeWith(new HttpSubscriber<ReplyCommentResponse>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(ReplyCommentResponse ReplyCommentResponse) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    /**
     * 加入星球
     */
    private void joinstart(final String userId, String starrysky_id, String planet_name) {
        addSubscribe(apis.joinstart(new JoinStarRequest(userId, starrysky_id, planet_name, ""))
                .compose(RxHttpUtil.<FutureHttpResponse<JoinStarRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<JoinStarRespone>handleResult())
                .subscribeWith(new HttpSubscriber<JoinStarRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(JoinStarRespone JoinStarRespone) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public void downFile(final String url) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //下载失败
                Toast.makeText(mActivity, "更新失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
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
                                    text.setText(result + "%");
                                    LogUtil.e("进度", result + "%");
                                }
                            });

                        }

                    }
                    fos.flush();
                    // 下载完成
                    if (fos != null) {
                        fos.close();
                    }
//                    view.downSuccess();
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
