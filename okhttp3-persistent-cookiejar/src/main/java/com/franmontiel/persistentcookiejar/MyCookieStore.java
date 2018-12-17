package com.franmontiel.persistentcookiejar;

import android.content.Context;

import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

/**
 * <br/>
 * Created by SL
 * on 2017/7/25.
 * 10:08
 * By AndroidStudio 2.3.2
 */
public class MyCookieStore implements CookieStore {
    SharedPrefsCookiePersistor persistor;

    public MyCookieStore(Context context) {
        persistor = new SharedPrefsCookiePersistor(context.getApplicationContext());
    }

    @Override
    public void addCookie(Cookie cookie) {
        persistor.addCookie(new okhttp3.Cookie.Builder().name(cookie.getName()).domain(cookie.getDomain()).path(cookie.getPath()).value(cookie.getValue()).secure().expiresAt(Long.MAX_VALUE).build());
    }

    @Override
    public List<Cookie> getCookies() {
        List<okhttp3.Cookie> cookies = persistor.loadAll();
        LinkedList<Cookie> cookieLinkedList = new LinkedList<>();
        for (okhttp3.Cookie cookie : cookies) {
            cookieLinkedList.add(new BasicClientCookie(cookie.name(), cookie.value()));
        }
        return cookieLinkedList;
    }

    @Override
    public boolean clearExpired(Date date) {
        return false;
    }

    @Override
    public void clear() {

    }
}
