package com.future_melody.view;

import java.io.File;
import java.io.InputStream;

/**
 * Author WZL
 * Date：2018/6/8 01
 * Notes:
 */
public interface DownloadingListener {
    /**
     * 下载成功
     */
    void onDownloadSuccess(File lrcUri);

    /**
     * @param progress 下载进度
     */
    void onDownloading(int progress);

    /**
     * 下载失败
     */
    void onDownloadFailed();
}
