package com.future_melody.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {

    public static String getFileName(String pathandname) {

        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1) {
            if (end != -1 && start < end) {
                return pathandname.substring(start + 1, end);
            } else {
                return pathandname.substring(start + 1);
            }
        } else {
            if (end != -1) {
                return pathandname.substring(0, end);
            } else {
                return pathandname;
            }
        }

    }

    public static String getCompleteFileName(String pathandname) {

        int start = pathandname.lastIndexOf("/");
        if (start != -1) {
            return pathandname.substring(start + 1);
        } else {
            return pathandname;
        }

    }

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int start = filename.lastIndexOf("/");
            int end = filename.lastIndexOf(".");
            if ((end > -1) && end > start && (end < (filename.length() - 1))) {
                return filename.substring(end + 1);
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String FormetFileSize(long fileSize) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }


    /**
     * 判断是否是整数
     *
     * @param num
     * @return
     */
    public static boolean isWholeNumber(String num) {
        if (TextUtils.isEmpty(num)) {
            return false;
        }
        String regExp = "^[1-9]\\d*$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(num);
        return m.matches();
    }

}
