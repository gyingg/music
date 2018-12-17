package com.future_melody.activity.zxing;


import com.google.zxing.Result;


public interface OnScannerListener {
    void onSuccess(String type, Result result);

    void onFail(String type, String message);
}
