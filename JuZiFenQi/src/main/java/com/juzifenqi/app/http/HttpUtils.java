package com.juzifenqi.app.http;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.juzifenqi.app.constant.Const;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

/**
 * Desc:   Http请求初始化工具类
 * Time:   2016-09-23 13:35
 * Author: chende
 */
public class HttpUtils {

    public static void initAsyncHttp(Context applicationContext) {//初始化okHttp并将cookies持久化到本地
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(applicationContext));
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(Const.HTTP_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(Const.HTTP_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor(null))
                .cookieJar(cookieJar)
                .hostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        AsyncRequest.initClient(okHttpClient);
    }
}
