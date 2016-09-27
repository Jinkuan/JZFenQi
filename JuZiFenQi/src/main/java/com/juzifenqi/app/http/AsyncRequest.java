package com.juzifenqi.app.http;

import android.text.TextUtils;

import com.juzifenqi.app.http.builder.GetBuilder;
import com.juzifenqi.app.http.builder.PostFormBuilder;
import com.juzifenqi.app.http.request.RequestCall;
import com.juzifenqi.app.http.utils.Platform;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Desc:   异步Http请求类
 * Time:   2016-09-23 12:04
 * Author: chende
 */
public class AsyncRequest {

    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private volatile static AsyncRequest mInstance;
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform;

    public AsyncRequest(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }

        mPlatform = Platform.get();
    }


    public static AsyncRequest initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (AsyncRequest.class) {
                if (mInstance == null) {
                    mInstance = new AsyncRequest(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static AsyncRequest getInstance() {
        return initClient(null);
    }


    public Executor getDelivery() {
        return mPlatform.defaultCallbackExecutor();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public void execute(final RequestCall requestCall, TRequestCallBack callback) {
        if (callback == null)
            callback = TRequestCallBack.CALLBACK_DEFAULT;
        final TRequestCallBack finalCallback = callback;
        requestCall.getCall().enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, final IOException e) {
                sendFailResultCallback(call, e, finalCallback);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                try {
                    if (call.isCanceled()) {
                        sendFailResultCallback(call, new IOException("Canceled!"), finalCallback);
                        return;
                    }
                    if (!response.isSuccessful()) {//请求失败
                        finalCallback.callback(new JSONObject(), response.message(), false);
                        return;
                    }
                    sendSuccessResultCallback(response.body().string(), finalCallback);
                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalCallback);
                } finally {
                    if (response.body() != null)
                        response.body().close();
                }

            }
        });
    }


    public void sendFailResultCallback(final Call call, final Exception e, final TRequestCallBack callback) {
        if (callback == null) return;

        mPlatform.execute(new Runnable() {

            @Override
            public void run() {
                callback.callback(new JSONObject(), e.getMessage(), false);
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final String text, final TRequestCallBack callback) {
        if (callback == null) return;
        mPlatform.execute(new Runnable() {

            @Override
            public void run() {
                if (!TextUtils.isEmpty(text)) {//返回的数据不为空
                    if (text.contains("{")) {
                        int code = 0;
                        try {
                            JSONObject jData = new JSONObject(text);
                            String msg = jData.optString("msg");
                            String jsonStr = jData.optString("data", "");
                            code = jData.optInt("code", 0);
                            switch (code) {
                                case 1://Http请求成功
                                    if (jsonStr.contains("{")) {
                                        callback.callback(new JSONObject(jsonStr), msg, false);
                                    } else {
                                        callback.callback(new JSONObject(), "无效的json格式", false);
                                    }
                                    break;
                                case 0://Http请求失败
                                    callback.callback(new JSONObject(), msg, false);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {//返回无效的果断格式
                        callback.callback(new JSONObject(), "无效的json格式", false);
                    }
                }
                callback.onAfter();
            }
        });
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }


}

