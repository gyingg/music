package com.future_melody.net;

import com.future_melody.net.exception.ApiException;
import com.future_melody.net.exception.ExceptionHandle;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by WZL
 * on 2018/1/24.
 * 16:47
 * By Android Studio 3.0.1
 */
public abstract class HttpSubscriber<T> extends ResourceSubscriber<T> {

    private ErrorListener mErrorListener;

    public HttpSubscriber(ErrorListener mErrorListener) {
        this.mErrorListener = mErrorListener;
    }

    @Override
    public void onError(Throwable t) {
        ApiException apiException = ExceptionHandle.handleException(t);
        if (mErrorListener != null) {
            mErrorListener.onError(apiException);
        }
    }

    /**
     * 如果返回结果T为null的话，不会调用这个方法，会调用onComplete方法，后续操作需要放在onComplete方法中
     */
    @Override
    public abstract void onNext(T t);

    @Override
    public void onComplete() {

    }

    public interface ErrorListener {
        void onError(ApiException exception);
    }
}
