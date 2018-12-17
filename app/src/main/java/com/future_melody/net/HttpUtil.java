package com.future_melody.net;


import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.MyCookieStore;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.future_melody.base.FutureApplication;
import com.future_melody.net.api.FutrueApis;
import com.future_melody.utils.LogUtil;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtil {
    private static final String TAG = "HttpUtil";

    private static final long DEFAULT_TIMEOUT = 60;

    private static FutrueApis phApis;
    private static SharedPrefsCookiePersistor persistor = new SharedPrefsCookiePersistor(FutureApplication.getContext());

    private static SetCookieCache cache = new SetCookieCache();
    private static MyCookieStore cookieStore = new MyCookieStore(FutureApplication.getContext());

    public static FutrueApis getPHApis() {
        if (phApis == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(getOkHttpClient())
                    .baseUrl(FutrueApis.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            phApis = retrofit.create(FutrueApis.class);
        }
        return phApis;
    }

    public static String getCookie() {
        SharedPrefsCookiePersistor persistor = new SharedPrefsCookiePersistor(FutureApplication.getContext());
        List<Cookie> cookies = persistor.loadAll();
        StringBuilder strCookie = new StringBuilder();
        for (int index = 0; index < cookies.size(); index++) {
            Cookie cookie = cookies.get(index);
            strCookie.append(cookie.name()).append(":").append(cookie.value());
            if (index < cookies.size() - 1) {
                strCookie.append(";");
            }
        }
        LogUtil.i(TAG, "cookie:" + strCookie.toString());
        return strCookie.toString();
    }

    private static OkHttpClient getOkHttpClient() {
        ClearableCookieJar cookieJar = new PersistentCookieJar(cache, persistor);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder builder = chain.request().newBuilder();
                        builder.addHeader("Content-Type", "application/json;charset=UTF-8");
                        builder.addHeader("device_type", "ANDROID");
                        return chain.proceed(builder.build());
                    }
                })
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                .build();
        return httpClient;
    }

}
