package com.future_melody.common;

import android.os.Environment;

import com.future_melody.utils.SPUtils;
import com.lzx.musiclibrary.cache.CacheUtils;

import java.io.File;

/**
 * Author WZL
 * Date：2018/5/17 32
 * Notes:
 */
public class CommonConst {


    //请求相机
    public static final int REQUEST_CAPTURE = 100;
    //请求相册
    public static final int REQUEST_PICK = 101;
    //请求截图
    public static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    public static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;

    //调用照相机返回图片文件
    public File tempFile;
    //本地音乐文件
    public static final String FILE_MUSIC = "music";
    //上传音乐
    public static final int MUSIC_CODE = 3;
    public static final String MUSICNAME = "name";
    //转音乐
    public static final String SONGINFO = "songInfo";
    //登录
    public static final String ISFINISH = "isFinish";
    //是否设置资金密码
    public static final String ISPSW = "isPassword";
    public static final String MINE_THEME = "mineTheme";
    public static final String FIND_THEME = "findTheme";
    public static final String START_THEME = "startTheme";
    public static final String FOLLOWS_THEME = "followsTheme";
    public static final String USER_THEME = "userTheme";
    public static final String ISXING_MUSIC = "1";
    public static final String XIAOWEIMACSN = "XIAOWEIMACSN";
    public static final String XIAOWEI_IS_NET = "XIAOWEI_IS_NET";
    public static final String MUSIC_LRC = Environment.getExternalStorageDirectory() + "/Future/Music/";

    public static final String userId() {
        return SPUtils.getInstance().getString(SPconst.USER_ID);
    }
}
