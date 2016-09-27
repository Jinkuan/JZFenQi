package com.juzifenqi.app.main;

import android.os.Bundle;

import com.juzifenqi.app.R;
import com.juzifenqi.app.constant.Const;
import com.juzifenqi.app.http.TRequestCallBack;
import com.juzifenqi.app.main.base.BaseActivity;

import org.json.JSONObject;

/**
 * Desc:   主界面
 * Time:   2016-09-23 12:04
 * Author: chende
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViewAfterOnCreate() {
    }

    @Override
    public void initDataAfterOnCreate() {
        params.put("userName", "rufeng");
        requestData(Const.URL.LOGOUT_URL, "", userInfoTRequestCallBack);

        requestFirstData(Const.URL.LOGOUT_URL,userInfoTRequestCallBack);
        requestMoreData(Const.URL.LOGOUT_URL,userInfoTRequestCallBack);
    }

    private TRequestCallBack userInfoTRequestCallBack = new TRequestCallBack() {

        @Override
        public void callback(JSONObject jData, String msg, boolean success) {
            if (success) {

            } else {
                toShow(msg);
            }
        }
    };
}
