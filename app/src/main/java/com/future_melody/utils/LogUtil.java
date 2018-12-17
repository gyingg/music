package com.future_melody.utils;

import android.util.Log;

/**
 * Log工具类
 */
public final class LogUtil {
	
	private static final boolean isLog = true;
	
	public static void e(String TAG, String msg){
		if (isLog) {
			Log.e(TAG, msg);
		}
	}
	
	public static void i(String TAG, String msg){
		if (isLog) {
			Log.i(TAG, msg);
		}
	}
	
	public static void w(String TAG, String msg){
		if (isLog) {
			Log.w(TAG, msg);
		}
	}
	
	public static void d(String TAG, String msg){
		if (isLog) {
			Log.d(TAG, msg);
		}
	}
	
	public static void v(String TAG, String msg){
		if (isLog) {
			Log.v(TAG, msg);
		}
	}
}
