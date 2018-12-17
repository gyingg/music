package com.future_melody.net;

import com.future_melody.net.exception.ApiException;
import com.future_melody.net.respone.FutureHttpResponse;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Rx网络统一处理封装类
 */
public class RxHttpUtil {
    private static final String HTTP_REQUEST_SUCCESS_CODE = "200";

    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 统一返回结果处理
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<FutureHttpResponse<T>, T> handleResult() {   //compose判断结果
        return new FlowableTransformer<FutureHttpResponse<T>, T>() {
            @Override
            public Flowable<T> apply(Flowable<FutureHttpResponse<T>> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(new Function<FutureHttpResponse<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(@NonNull FutureHttpResponse<T> PHHttpResponse) throws Exception {
                        if (PHHttpResponse.getCode().equals(HTTP_REQUEST_SUCCESS_CODE)) {
                            return createData(PHHttpResponse.getData());
                        } else {
                            return Flowable.error(new ApiException(new Exception(PHHttpResponse.getMsg()), PHHttpResponse.getCode()));
                        }
                    }
                });
            }
        };
    }

    /**
     * 生成Flowable
     *
     * @param <T>
     * @return
     */
    public static <T> Flowable<T> createData(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                try {
                    if (t != null) {
                        emitter.onNext(t);
                    }
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                    e.printStackTrace();
                }
            }
        }, BackpressureStrategy.BUFFER);
    }
}
