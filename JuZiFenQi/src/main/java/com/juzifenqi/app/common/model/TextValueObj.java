package com.juzifenqi.app.common.model;


import com.juzifenqi.app.constant.Const;

import org.json.JSONObject;

import java.util.Locale;

/**
 * Desc:   通用键值对model
 * Time:   2016-09-23 12:54
 * Author: chende
 */
public class TextValueObj {

    public String text;
    public String url;
    public int value;

    public TextValueObj() {
        super();
    }

    public TextValueObj(String text, int value) {
        super();
        this.text = text;
        this.value = value;
    }

    public TextValueObj(String text, String url) {// 认证信息
        super();
        this.text = text;
        this.url = url;
    }

    public static TextValueObj parse(JSONObject jData) {
        TextValueObj model = new TextValueObj();
        model.text = jData.optString("authType2");
        model.url = jData.optString("picUrl");
        if (!model.url.toLowerCase(Locale.CHINESE).startsWith("http://")) {
            model.url = Const.BASE_URL + model.url;
        }
        return model;
    }
}
