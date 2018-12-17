package com.future_melody.utils;

import android.os.Environment;

import com.future_melody.common.CommonConst;
import com.future_melody.view.DownloadingListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author WZL
 * Date：2018/6/8 54
 * Notes:
 */
public class DownloadUtils {
    //下载歌词
    public static void downloadLrc(String musicId, String url, DownloadingListener listener) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = null;
                FileOutputStream outputStream = null;
                try {
                    inputStream = response.body().byteStream();
                    // 下载完成
//                    lrc = CommonUtils.getStreamString(inputStream);
                    File file = new File(CommonConst.MUSIC_LRC, (musicId + ".lrc"));
                    outputStream = new FileOutputStream(file);
                    byte[] buf = new byte[1024];
                    int ch = -1;
                    while ((ch = inputStream.read(buf)) != -1) {
                        outputStream.write(buf, 0, ch);

                    }
                    outputStream.flush();
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    listener.onDownloadSuccess(file);
                } catch (Exception e) {
                } finally {
                    try {
                        if (inputStream != null)
                            inputStream.close();
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }
        });
    }
}
