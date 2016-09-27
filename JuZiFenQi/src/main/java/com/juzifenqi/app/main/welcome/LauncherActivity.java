package com.juzifenqi.app.main.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.juzifenqi.app.R;
import com.juzifenqi.app.main.MainActivity;
import com.juzifenqi.app.main.base.BaseActivity;
import com.juzifenqi.app.util.Utils;
import com.orhanobut.logger.Logger;

/**
 * Desc:   启动页
 * Time:   2016-09-23 15:27
 * Author: chende
 */
public class LauncherActivity extends BaseActivity implements Handler.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_launcher);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViewAfterOnCreate() {
        Logger.init("AAAA");
    }

    @Override
    public void initDataAfterOnCreate() {
        new Handler(this).sendEmptyMessageDelayed(0, 3000);
    }

    @Override
    public boolean handleMessage(Message message) {
        Utils.toFadeIn(mContext, new Intent(mContext, MainActivity.class), true);
        return false;
    }
}
