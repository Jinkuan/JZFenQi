package com.juzifenqi.app.http;

import org.json.JSONObject;

public abstract class TRequestCallBack {

    public void onBefore() {//执行http之前
    }

    public abstract void callback(JSONObject jData, String msg, boolean success);

    public void onAfter() {//http返回之后
    }

    public static TRequestCallBack CALLBACK_DEFAULT = new TRequestCallBack() {

        @Override
        public void callback(JSONObject jData, String msg, boolean success) {

        }
    };
}